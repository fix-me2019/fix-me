package com.kondiewtc.router;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;

public class Router implements Runnable {

    private int port;
    private static List<String> brokerMsg;
    private static List<String> marketMsg;
    private static List<AsynchronousSocketChannel> broker;
    private static List<AsynchronousSocketChannel> market;

    public Router(int port)
    {
        this.port = port;
        broker = new ArrayList<>();
        market = new ArrayList<>();
        brokerMsg = new ArrayList<>();
        marketMsg = new ArrayList<>();
    }

    public static void setMarketMsg(String marketMsg) {
        Router.marketMsg.add(marketMsg);
    }

    public static void setBrokerMsg(String brokerMsg) {
        Router.brokerMsg.add(brokerMsg);
    }

    public static String getMarketMsg() {
        if (marketMsg.size() >= 1) {
            String msg = marketMsg.get(0);
            marketMsg.remove(0);
            return msg;
        }
        else{
            return "";
        }
    }

    public static String getBrokerMsg() {
        if (brokerMsg.size() >= 1) {
            String msg = brokerMsg.get(0);
            brokerMsg.remove(0);
            return msg;
        }
        else{
            return "";
        }
    }

    public static AsynchronousSocketChannel getBroker() {
        if (broker.size() >= 1) {
            AsynchronousSocketChannel client = broker.get(0);
            broker.remove(0);
            return client;
        }
        else{
            return null;
        }
    }

    public static AsynchronousSocketChannel getMarket() {
        if (market.size() >= 1) {
            AsynchronousSocketChannel client = market.get(0);
            market.remove(0);
            return client;
        }
        else{
            return null;
        }
    }

    public static void setBroker(AsynchronousSocketChannel broker) {
        Router.broker.add(broker);
    }

    public static void setMarket(AsynchronousSocketChannel market) {
        Router.market.add(market);
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
