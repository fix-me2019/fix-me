package com.kondiewtc.router;

import com.kondiewtc.router.db.DbConn;

import java.sql.ResultSet;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static DbConn conn;

    public static void main(String[] args)
    {
        Router router1 = new Router(5000);
        Executor executor1 = new RouterExec();
        executor1.execute(router1);

        Router router2 = new Router(5001);
        Executor executor2 = new RouterExec();
        executor2.execute(router2);

        conn = new DbConn();
        conn.clearAll();
        insertIntoDb(conn);
        showItems(conn);
        conn.closeDB();

        //maybe i'll use it later
//        ExecutorService thread = Executors.newCachedThreadPool();
//        thread.submit(router1);
//        thread.submit(router2);
//        thread.shutdown();
    }

    private static void insertIntoDb(DbConn conn){

        conn.addItem("wethinkcode_shirt", 20, 140, 1);
        conn.addItem("wethinkcode_water_bottle", 27, 240, 2);
        conn.addItem("phone_cover", 10, 40, 3);
        conn.addItem("something", 260, 10, 4);
    }

    public static void showItems(DbConn conn){

        try {
            ResultSet results = conn.getItems();

            while (results.next()) {
                System.out.printf("\nId: %-3d Name: %-40s Price: R%-10d Quantity: %d\n", results.getInt("id"), results.getString("name"), results.getInt("price"), results.getInt("quantity"));
            }
            Logger.log("");
        }catch (Exception e){
            System.out.println("Couldn't show items.");
        }
    }
}
