package com.kondiewtc.router;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

public class Attachment {
    private AsynchronousServerSocketChannel server;
    private int post;
    private AsynchronousSocketChannel client;
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public void setServer(AsynchronousServerSocketChannel server) {
        this.server = server;
    }

    public AsynchronousServerSocketChannel getServer() {
        return server;
    }

    public int getPost() {
        return post;
    }

    public void setClient(AsynchronousSocketChannel client) {
        this.client = client;
    }

    public AsynchronousSocketChannel getClient() {
        return client;
    }
}
