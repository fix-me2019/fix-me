package com.kondiewtc.router;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class IOHandler {

    private Attachment attachment;
    private int port, id;

    public IOHandler(Attachment attachment)
    {
        this.port = attachment.getPost();
        this.id = attachment.getId();
        this.attachment = attachment;

        if (port == 5000){
            handleInput(attachment.getClient());
        }
        else{
            handleOutput(attachment.getClient());
        }
    }

    private void handleInput(AsynchronousSocketChannel socket)
    {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        socket.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                String msg = new String(attachment.array()).trim();
                if (port == 5000) {
                    Router.setBrokerMsg(msg);
                    Logger.log("[Broker]: ID=" + id + "|MSG=" + msg.split(":")[0] + "|CKSUM=" + msg.split(":")[1]);
                }else{
                    Router.setMarketMsg(msg);
                    Logger.log("[Market]: ID=" + id + "|MSG=" + msg.split(":")[0] + "|CKSUM=" + msg.split(":")[1]);
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                Logger.log("Failed to read");
            }
        });
//        String msg;
//        if (port == 5000){
//            msg = Router.getBrokerMsg();
//        }else{
//            msg = Router.getMarketMsg();
//        }
//        if (msg.split(":").length == 2) {
//            if (CheckSum.isIntact(msg.split(":")[0], Integer.valueOf(msg.split(":")[1]))) {
//                if (attachment.getPost() == 5000 && Router.getMarket() != null) {
//                    handleOutput(Router.getMarket());
//                }else if (Router.getBroker() != null){
//                    handleOutput(Router.getBroker());
//                }
//            } else {
//                Logger.log("This sent message was not intact");
//            }
//        }
//        else{
//            Logger.log(msg + "====" + msg.split(":").length + "__" + port);
//        }
        buffer.clear();
    }

    private void handleOutput(AsynchronousSocketChannel socket)
    {
        String str;
        if (port == 5000) {
            str = Router.getMarketMsg();
        }else{
            str = Router.getBrokerMsg();
        }

        Logger.log("=-=-=-=-=-=-===-=-=");
        socket.write(ByteBuffer.wrap(str.getBytes()), str, new CompletionHandler<Integer, String>() {
            @Override
            public void completed(Integer result, String attachment) {
//                if (str != "") {
                    Logger.log("Server: " + socket);
                    if (port == 5001 && Router.getBroker() != null){
                        handleOutput(Router.getBroker());
                        Router.setBroker(null);
                    }
//                }
            }

            @Override
            public void failed(Throwable exc, String attachment) {
                Logger.log("Failed to write");
            }
        });
    }
}
