package com.kondiewtc.market.db;

import lombok.Getter;

import java.sql.*;

@Getter
public class DbConn {

    private static Connection conn = null;
    static PreparedStatement stat = null;
    public static ResultSet rs;

    public DbConn()
    {
        makeJDBCConnection();
//        dropDb();
    }

    public static void makeJDBCConnection() {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            return;
        }

        try {
            // DriverManager: The basic service for managing a set of JDBC drivers.
            conn = DriverManager.getConnection("jdbc:sqlite:swingy.db");
        } catch (SQLException e) {
            return;
        }

        String query = "CREATE TABLE MarketItems (name VARCHAR(50), quantity INT, price INT, id INT, max INT AUTO_INCREMENT NOT NULL PRIMARY KEY)";
        try {
            stat = conn.prepareStatement(query);
            stat.executeUpdate();
        } catch (SQLException e) {
//            log("create databjashdoaslhdas: " + e.toString());
        }
    }

    public void addItem(String name, int quantity, int price, int id) {

        try {
            String insertQueryStatement = "INSERT  INTO  MarketItems  VALUES  (?, ?, ?, ?)";

            stat = conn.prepareStatement(insertQueryStatement);
            stat.setString(1, name);
            stat.setInt(2, quantity);
            stat.setInt(3, price);
            stat.setInt(4, id);
//            stat.setInt(5, max);

            // execute insert SQL statement
            stat.executeUpdate();
        } catch (SQLException e) {
//            log("insertion sjfbsi: " + e.toString());
        }
    }

    private void dropDb(){
        try {
            String dropQueryStatement = "DROP DATABASE MarketItems;";
            stat = conn.prepareStatement(dropQueryStatement);
            stat.executeUpdate();
        }catch (Exception e){
            log("insertion sjfbsi: " + e.toString());
        }
    }

    public void updateItem(int quantity, int id) {

        try {
            String insertQueryStatement = "UPDATE MarketItems SET quantity = ? WHERE id = ?; COMMIT;";

            stat = conn.prepareStatement(insertQueryStatement);
            stat.setInt(1, quantity);
            stat.setInt(2, id);

            // execute insert SQL statement
            stat.executeUpdate();
        } catch (SQLException e) {
//            log("insertion sjfbsi: " + e.toString());
        }
    }

    public ResultSet getItems() {

        try {
            String getQueryStatement = "SELECT * FROM MarketItems";

            stat = conn.prepareStatement(getQueryStatement);

            rs = stat.executeQuery();


        } catch (SQLException e) {
//            log("getting : " + e.toString());
        }

        return rs;
    }

    public ResultSet getItem(int id) {

        try {
            String getQueryStatement = "SELECT * FROM MarketItems WHERE id = ?";

            stat = conn.prepareStatement(getQueryStatement);
            stat.setInt(1, id);

            rs = stat.executeQuery();


        } catch (SQLException e) {
//            log("getting : " + e.toString());
        }

        return rs;
    }

    public void clearAll()
    {
        try {
            String getQueryStatement = "DELETE FROM MarketItems";

            stat = conn.prepareStatement(getQueryStatement);

            stat.executeUpdate();

        } catch (SQLException e) {
//            log("del : " + e.toString());
        }
    }

    // Simple log utility
    private static void log(String string) {
        System.out.println(string);

    }
}
