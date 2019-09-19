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

        int id = Integer.valueOf(query.split("~")[1].split(":")[0].split(" ")[1]);
        int quantity = Integer.valueOf(query.split("~")[1].split(":")[0].split(" ")[2]);
        String queryType = query.split("~")[1].split(":")[0].split(" ")[0];

        try {
            ResultSet rs = Main.conn.getItem(id);
            if (rs.next()) {
                if (queryType.equalsIgnoreCase("buy") && (rs.getInt("quantity") - quantity) > 0) {
                    Main.conn.updateItem(rs.getInt("quantity") - quantity, id);
                    return "Request accepted";
                } else if (queryType.equalsIgnoreCase("sell")) {
                    Main.conn.updateItem(rs.getInt("quantity") + quantity, id);
                    return "Request accepted";
                } else {
                    return "Request denied";
                }
            }
            else{
                return "Request denied, make use the selected id exists";
            }
        }catch (Exception e){
            return "Request denied because of some error";
        }
    }
}
