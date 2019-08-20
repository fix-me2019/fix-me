package com.kondiewtc.router;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class IOHandler {

    private AsynchronousSocketChannel socket;
    private int port, id;

    public IOHandler(Attachment attachment)
    {
        this.socket = attachment.getClient();
        this.port = attachment.getPost();
        this.id = attachment.getId();
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
                    Logger.log("[Broker]: ID=" + id + "|MSG=" + new String(attachment.array()).trim() + "|CKSUM=" + CheckSum.generateChecksum(new String(attachment.array()).trim()));
                }else{
                    Router.setMarketMsg(new String(attachment.array()).trim());
                    Logger.log("[Market]: ID=" + id + "|MSG=" + new String(attachment.array()).trim() + "|CKSUM=" + CheckSum.generateChecksum(new String(attachment.array()).trim()));
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                Logger.log("Failed to read");
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
//                if (str != "") {
//                    Logger.log("Server: " + attachment);
//                }
            }

            @Override
            public void failed(Throwable exc, String attachment) {
                Logger.log("Failed to write");
            }
        });
    }
}
