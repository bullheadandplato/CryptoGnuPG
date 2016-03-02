package com.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by h4ck3r on 2/19/16.
 */
public class RequiredInfo {
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
}
