package com.kondiewtc.market;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;

public class Market {
    private int port;

    public Market(int port)
    {
        this.port = port;
    }

    public void startMarket()
    {
        try (AsynchronousSocketChannel socket = AsynchronousSocketChannel.open())
        {
            Future<Void> result = socket.connect(new InetSocketAddress("127.0.0.1", port));
            result.get();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            socket.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    Logger.log("Server: " + new String(attachment.array()).trim());
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    Logger.log("Failed to read");
                }
            });
            buffer.clear();


            String str = "This is a message from market";
            str += ":" + CheckSum.generateChecksum(str);
            socket.write(ByteBuffer.wrap(str.getBytes()), str, new CompletionHandler<Integer, String>() {
                @Override
                public void completed(Integer result, String attachment) {
                    Logger.log("Client: " + attachment);
                }

                @Override
                public void failed(Throwable exc, String attachment) {
                    Logger.log("Failed to write");
                }
            });


            Thread.currentThread().join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
