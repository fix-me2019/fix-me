package com.kondiewtc.router;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.sql.ResultSet;
import java.util.Timer;
import java.util.TimerTask;

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
            handleOutput(attachment.getClient(), true);
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
                    int price = 0;
                    try {
                        ResultSet rs = Main.conn.getItem(Integer.valueOf(msg.split(":")[0].split(" ")[1]));
                        if (rs.next()) {
                            price = rs.getInt("price");
                            Logger.log("[Broker]: ID=" + id + "|MARKET: WTC" + "|INSTRUCTION=" + msg.split(":")[0].split(" ")[0] + "|PRICE=" + price + "|ITEM_ID=" + msg.split(":")[0].split(" ")[1] + "|QUANTITY=" + msg.split(":")[0].split(" ")[2] + "|CKSUM=" + msg.split(":")[1]);
                        }
                        else{
                            Logger.log("[Broker]: ID=" + id + "|MARKET: WTC" + "|INSTRUCTION=" + msg.split(":")[0].split(" ")[0] + "|PRICE=" + price + "|ITEM_ID=" + msg.split(":")[0].split(" ")[1] + "|QUANTITY=" + msg.split(":")[0].split(" ")[2] + "|CKSUM=" + msg.split(":")[1]);
                        }
                    }catch (Exception e){
                        Logger.log(e.toString());
                    }
                }else{
                    Router.setMarketMsg(msg);
                    Logger.log("[Market]: ID=" + id + "|MARKET: WTC" + "|MSG=" + msg.split(":")[0] + "|CKSUM=" + msg.split(":")[1]);
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

    private void handleOutput(AsynchronousSocketChannel socket, boolean isReady)
    {
        final String[] str = new String[1];
        int timerTime = 0;
        if (port == 5000) {
            str[0] = Router.getMarketMsg();
        }else{
            if (!isReady) {
                handleInput(Router.getMarket());
                timerTime = 6000;
            }
            else {
                str[0] = Router.getBrokerMsg();
            }
        }
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isReady) {
                    str[0] = Router.getMarketMsg();
                }
                socket.write(ByteBuffer.wrap(str[0].getBytes()), str[0], new CompletionHandler<Integer, String>() {
                    @Override
                    public void completed(Integer result, String attachment) {
                        if (port == 5001 && Router.getBroker() != null && isReady){
                            handleOutput(Router.getBroker(), false);
                        }
                        else if (!isReady){
                            Main.showItems(Main.conn);
                        }
                    }

                    @Override
                    public void failed(Throwable exc, String attachment) {
                        Logger.log("Failed to write");
                    }
                });
            }
        }, timerTime);
    }
}
