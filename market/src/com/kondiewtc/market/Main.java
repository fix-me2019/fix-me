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
    }
}
