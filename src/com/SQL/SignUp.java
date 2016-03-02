package com.SQL;

import javax.swing.*;
import java.sql.*;

/**
 * Created by h4ck3r on 2/19/16.
 */
public class SignUp {
    private static String url = "jdbc:derby:mydb;user=app;password=mine;create=true;";
    private static String tableName = "Users";
    private static Connection conn = null;
    private static Statement stmt = null;

    public static void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            //Get a connection
            conn = DriverManager.getConnection(url);
        } catch (Exception except) {
            except.printStackTrace();
        }
    }

    public static boolean createUser(String name, String password, String keyid) {
        try {
            stmt = conn.createStatement();
            //First of all check if database table exist
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getTables(null, null, "USERS", null);
            if (rs.next()) {
                //means table exist
            } else {
                String text = "CREATE TABLE Users" +
                        "(" +
                        "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                        "username varchar(255) NOT NULL UNIQUE ," +
                        "password varchar(32) NOT NULL," +
                        "keyid VARCHAR(32) UNIQUE" +

                        ")";
                stmt.execute(text);

            }
            //insert username and password
            stmt.execute("INSERT INTO Users(username ,password,keyid) VALUES ('" + name + "','" + password + "','" + keyid + "')");
            conn.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Users already exist");

        }
        return true;


    }

    public static void test() {
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from USERS");
            if (rs.next()) {
                while (rs.next()) {
                    System.out.println(rs.getInt(1) + rs.getString(2));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkRun() {
        DatabaseMetaData metaData = null;
        try {
            stmt = conn.createStatement();
            metaData = conn.getMetaData();
            ResultSet rs = metaData.getTables(null, null, "USERS", null);
            if (rs.next()) {
                //means table exist
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;

    }
}
