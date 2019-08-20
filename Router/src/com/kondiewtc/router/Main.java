package com.kondiewtc.router;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args)
    {
        Router router1 = new Router(5000);
        Executor executor1 = new RouterExec();
        executor1.execute(router1);

        Router router2 = new Router(5001);
        Executor executor2 = new RouterExec();
        executor2.execute(router2);

        //maybe i'll use it later
//        ExecutorService thread = Executors.newCachedThreadPool();
//        thread.submit(router1);
//        thread.submit(router2);
//        thread.shutdown();
    }
}
