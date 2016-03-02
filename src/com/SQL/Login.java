package com.SQL;

import javax.swing.*;
import java.sql.*;

/**
 * Created by h4ck3r on 2/19/16.
 */
public class Login {

    private static String url = "jdbc:derby:mydb;user=app;password=mine;"; //specify the connection url
    private static String tableName = "Users";
    private static Connection conn = null;
    private static PreparedStatement stmt = null;


    /**
     * Open the database connection.
     */
    public static void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");      //Driver class name
            //Get a connection
            conn = DriverManager.getConnection(url);               //try to open connection
        } catch (Exception except) {
            except.printStackTrace();
        }
    }

    /**
     * select() check the user if yes login else
     * have a life
     *
     * @param username is the name of user for auth
     * @param password is the password of user for auth
     */
    public static boolean select(String username, String password) {
        try {
            stmt = conn.prepareStatement("select * from " + tableName + " where " +
                    "username= ? AND password= ?");     //this is a parameterized query
            stmt.setString(1, username);                 //setting the first parameter
            stmt.setString(2, password);               //setting the second parameter
            ResultSet results = stmt.executeQuery();       //execute query and save result in results

        /*
        Process of user authentication start here
        if the result is empty for that the else part will be executed
        if the 'if' part execute then check the username and password
        here is dual verification
         */
            if (results.next()) {
                while (results.next()) {
                    String User = results.getString(2);
                    String pass = results.getString(3);
                    //test user
                    return username.equalsIgnoreCase(User) && password.equals(pass);
                }
            } else {
                return false;
            }
            //close the every open thing
            results.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
        return true;

    }

}
