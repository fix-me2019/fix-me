package com.kondiewtc.router;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class CompHandler implements CompletionHandler<AsynchronousSocketChannel, Attachment> {

    private static int id = 100000;

    @Override
    public void completed(AsynchronousSocketChannel client, Attachment attachment) {
        try {
            if (client != null && client.isOpen()) {
                attachment.getServer().accept(attachment, this);
                attachment.setClient(client);
                attachment.setId(id++);
                String str = "Connected to router. ID: " + attachment.getId();

                SocketAddress sa = client.getRemoteAddress();
                Logger.log("Accepted a connection from " + sa.toString());

                client.write(ByteBuffer.wrap(str.getBytes()), attachment, new CompletionHandler<Integer, Attachment>() {
                    @Override
                    public void completed(Integer result, Attachment attachment) {
                        if (attachment.getPost() == 5000) {
                            Logger.log("Response sent back to broker");
                        } else {
                            Logger.log("Response sent back to market");
                        }
                    }

                    @Override
                    public void failed(Throwable exc, Attachment attachment) {
                        if (attachment.getPost() == 5000) {
                            Logger.log("Failed to send back response to broker");
                        } else {
                            Logger.log("Failed to send back response to market");
                        }
                    }
                });
                new IOHandler(attachment);
            } else {
                Logger.log("Something went wrong");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, Attachment attachment) {
        exc.printStackTrace();
    }
}
