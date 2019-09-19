package com.kondiewtc.broker;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Future;

public class Broker {

    private int port;

    public Broker(int port)
    {
        this.port = port;
    }

    public void startBroker(String query, String id, String quantity)
    {
        try (AsynchronousSocketChannel socket = AsynchronousSocketChannel.open())
        {
            Future<Void> result = socket.connect(new InetSocketAddress("127.0.0.1", port));
            result.get();

            Logger.log("Connected to server");
            String str = String.format("%s %s %s", query, id, quantity);
            str += ":" + CheckSum.generateChecksum(str);
            socket.write(ByteBuffer.wrap(str.getBytes()), str, new CompletionHandler<Integer, String>() {
                @Override
                public void completed(Integer result, String attachment) {
                    Logger.log("Client: " + attachment);
                }

                @Override
                public void failed(Throwable exc, String attachment) {
                    Logger.log("Fail...");
                }
            });

            handleRead(socket);
            Thread.currentThread().join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void handleRead(AsynchronousSocketChannel socket){

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                socket.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result, ByteBuffer attachment) {
                        Logger.log("Server: " + new String(attachment.array()).trim());
                        handleRead(socket);
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        exc.printStackTrace();
                        Logger.log("Fail..-.");
                    }
                });
            }
        }, 2000);
        buffer.clear();
    }
}
