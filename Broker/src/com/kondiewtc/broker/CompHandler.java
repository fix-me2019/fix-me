package com.kondiewtc.broker;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class CompHandler implements CompletionHandler<AsynchronousSocketChannel, ByteBuffer> {

    @Override
    public void completed(AsynchronousSocketChannel result, ByteBuffer attachment) {
        Logger.log("Done....");
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        Logger.log("Fail...");
    }
}
