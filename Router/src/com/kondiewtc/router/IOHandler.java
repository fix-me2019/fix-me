package com.kondiewtc.router;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class IOHandler {

    private AsynchronousSocketChannel socket;
    private int port;

    public IOHandler(Attachment attachment)
    {
        this.socket = attachment.getClient();
        this.port = attachment.getPost();
        handleInput();
    }

    private void handleInput()
    {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        socket.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                if (port == 5000) {
                    Router.setBrokerMsg(new String(attachment.array()).trim());
                }else{
                    Router.setMarketMsg(new String(attachment.array()).trim());
                }
                Logger.log("Client: " + new String(attachment.array()).trim());
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                Logger.log("Fail__");
            }
        });
        handleOutput();
        buffer.clear();
    }

    private void handleOutput()
    {
        String str;
        if (port == 5000) {
            str = Router.getMarketMsg();
        }else{
            str = Router.getBrokerMsg();
        }
        socket.write(ByteBuffer.wrap(str.getBytes()), str, new CompletionHandler<Integer, String>() {
            @Override
            public void completed(Integer result, String attachment) {

                Logger.log("Server: " + attachment);
            }

            @Override
            public void failed(Throwable exc, String attachment) {
                Logger.log("FFFFF");
            }
        });
    }
}
