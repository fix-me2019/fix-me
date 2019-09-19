package com.kondiewtc.market;

import com.kondiewtc.market.db.DbConn;

import java.sql.ResultSet;

public class Main {

    public static void main(String[] args)
    {
        DbConn conn = new DbConn();

        Market market = new Market(5001);
        market.startMarket();
    }
}
