package com.kondiewtc.market;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.sql.ResultSet;
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
                    String query = new String(attachment.array()).trim();
//                    Logger.log(query);
                    if (query.split("~").length >= 2 && query.split("~")[1].split(":").length == 3 && CheckSum.isIntact(query.split("~")[1].split(":")[1], query.split("~")[1].split(":")[2])) {
                        Logger.log("Server: " + query.split("~")[0]);
                        Logger.log("Broker: " + query.split("~")[1].split(":")[0]);

                        String str = getReply(query);
                        str += ":" + CheckSum.generateChecksum(str);
                        socket.write(ByteBuffer.wrap(str.getBytes()), str, new CompletionHandler<Integer, String>() {
                            @Override
                            public void completed(Integer result, String attachment) {
                                Logger.log("Market: " + attachment.split(":")[0]);
                                System.exit(0);
                            }

                            @Override
                            public void failed(Throwable exc, String attachment) {
                                Logger.log("Failed to write");
                            }
                        });
                    }
                    else{
                        Logger.log("Error");
                        System.exit(0);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    Logger.log("Failed to read");
                }
            });
            buffer.clear();

            Thread.currentThread().join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String getReply(String query){

        String[] splitArr = query.split("~")[1].split(":")[1].split(" ");
        int id = Integer.valueOf(splitArr[1]);
        int quantity = Integer.valueOf(splitArr[2]);
        String queryType = splitArr[0];

        try {
            ResultSet rs = Main.conn.getItem(id);
            if (rs.next()) {
                if (queryType.equalsIgnoreCase("buy") && (rs.getInt("quantity") - quantity) > 0) {
                    Main.conn.updateItem(rs.getInt("quantity") - quantity, id);
                    Main.showItems(Main.conn);
                    return "Executed. Buy request accepted";
                } else if (queryType.equalsIgnoreCase("sell")) {
                    Main.conn.updateItem(rs.getInt("quantity") + quantity, id);
                    Main.showItems(Main.conn);
                    return "Executed. Sell request accepted";
                } else {
                    Main.showItems(Main.conn);
                    return "Rejected. Request denied";
                }
            }
            else{
                return "Rejected. Request denied, make sure the selected id exists";
            }
        }catch (Exception e){
            return "Rejected. Request denied because of some error";
        }
    }
}
