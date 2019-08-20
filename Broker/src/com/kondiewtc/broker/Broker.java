package com.kondiewtc.broker;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;

public class Broker {

    private int port;

    public Broker(int port)
    {
        this.port = port;
    }

    public void startBroker()
    {
        try (AsynchronousSocketChannel socket = AsynchronousSocketChannel.open())
        {
            Future<Void> result = socket.connect(new InetSocketAddress("127.0.0.1", port));
            result.get();

            Logger.log("Connected to server");
            String str = "This is a message from broker";
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

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            socket.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    Logger.log("Server: " + new String(attachment.array()).trim());
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                    Logger.log("Fail..-.");
                }
            });
            buffer.clear();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
