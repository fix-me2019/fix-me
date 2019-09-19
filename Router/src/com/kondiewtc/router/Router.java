package com.kondiewtc.router;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

public class Router implements Runnable {

    private int port;
    private static String brokerMsg = "";
    private static String marketMsg = "";
    private static AsynchronousSocketChannel broker;
    private static AsynchronousSocketChannel market;
    private AsynchronousSocketChannel brokerClient = null, marketClient = null;

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

    public void setMarketClient(AsynchronousSocketChannel marketClient) {
        this.marketClient = marketClient;
    }

    public void setBrokerClient(AsynchronousSocketChannel brokerClient) {
        this.brokerClient = brokerClient;
    }

    public AsynchronousSocketChannel getMarketClient() {
        return marketClient;
    }

    public AsynchronousSocketChannel getBrokerClient() {
        return brokerClient;
    }

    public static AsynchronousSocketChannel getBroker() {
        return broker;
    }

    public static AsynchronousSocketChannel getMarket() {
        return market;
    }

    public static void setBroker(AsynchronousSocketChannel broker) {
        Router.broker = broker;
    }

    public static void setMarket(AsynchronousSocketChannel market) {
        Router.market = market;
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
