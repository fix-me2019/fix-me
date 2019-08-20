package com.kondiewtc.router;

import java.util.concurrent.Executor;

public class RouterExec implements Executor {
    @Override
    public void execute(Runnable task) {
        new Thread(task).start();
    }
}
