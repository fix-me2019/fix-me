package com.kondiewtc.broker;

public class Main {

    public static void main(String[] args)
    {
        Broker broker = new Broker(5000);
        broker.startBroker();
    }
}
