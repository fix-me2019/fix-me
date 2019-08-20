package com.kondiewtc.router;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

public class Router implements Runnable {

    private int port;
    private static String brokerMsg = "";
    private static String marketMsg = "";

    public Router(int port)
    {
        this.port = port;
    }

    public static void setMarketMsg(String marketMsg) {
        Router.marketMsg = marketMsg;
    }

    public static void setBrokerMsg(String brokerMsg) {
        Router.brokerMsg = brokerMsg;
    }

    public static String getMarketMsg() {
        return marketMsg;
    }

    public static String getBrokerMsg() {
        return brokerMsg;
    }

    @Override
    public void run() {

        try (AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open())
        {
            server.bind(new InetSocketAddress("127.0.0.1", port));
            Logger.log("Server started");

            if (port == 5000){
                Logger.log("Waiting for connection from broker");
            }
            else{
                Logger.log("Waiting for connection from market");
            }
            Attachment attachment = new Attachment();
            attachment.setPost(port);
            attachment.setServer(server);
            server.accept(attachment, new CompHandler());
            Thread.currentThread().join();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
