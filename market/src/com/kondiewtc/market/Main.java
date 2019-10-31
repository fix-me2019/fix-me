package com.kondiewtc.market;

import com.kondiewtc.market.db.DbConn;

import java.sql.ResultSet;

public class Main {

    public static DbConn conn;

    public static void main(String[] args)
    {
        conn = new DbConn();

        Market market = new Market(5001);
        market.startMarket();

        conn.clearAll();
//        insertIntoDb(conn);
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
