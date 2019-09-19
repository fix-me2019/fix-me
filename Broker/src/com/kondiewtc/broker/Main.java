package com.kondiewtc.broker;

public class Main {

    public static void main(String[] args)
    {
        if (args.length >= 3) {
            Broker broker = new Broker(5000);
            broker.startBroker(args[0], args[1], args[2]);
        }
        else{
            Logger.log("Please specify the query(\"buy\" OR \"sell\"), item ID and Quantity");
        }
    }
}
