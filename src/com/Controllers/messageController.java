package com.Controllers;

import com.Interface.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by h4ck3r on 1/15/16.
 * As by name In this file all the headache
 * for messages interface and sending messages
 * is handled.
 */
public class messageController implements Runnable {

    //
    //Variables to handle interface
    //
    Main obj = new Main();
    GnuPG gpg = new GnuPG();
    Thread thread;
    //
    //Variables to respond to FXML
    //
    @FXML
    private Text message;
    @FXML
    private TextField messageField;
    @FXML
    private Text user;
    static String username = null;
    public static String keyid;
    private String pass;
    int count = 0;


    //to handle if message if received or send
    private boolean flag = true;
    //
    // Server socket variables
    //
    public static String server;
    public static int port = 1024;
    private static Socket socket;
    private static DataInputStream din;
    private static DataOutputStream dout;

    /**
     * this is initializer for FXML equivalent
     * to onLoad in JS equivalent to
     * construct in POJOs
     */
    @FXML
    protected void initialize() throws IOException {
        user.setText("'" + username + "'" + " chatting with " + keyid);
        // Start a background thread for receiving messages
        thread = new Thread(this);
        thread.start();

    }

    public static boolean Connect() {
        boolean status = false;
        try {
            // Initiate the connection
            socket = new Socket(server, port);
            // We got a connection! Tell the world
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());

            //set username on server
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println(username);
            printWriter.flush();
            status = true;
        } catch (IOException ie) {
            JOptionPane.showMessageDialog(null, "Error cannot connect to server");
            status = false;
        }
        return status;
    }


    @FXML
    protected void send(ActionEvent event) {
        this.setFlag(true);
        if (messageField.getText().isEmpty()) {
            /*
            using swing OptionPane 
            because Javafx doesn't have
            its fully functional OptionPane yet
             */
            int answer = JOptionPane.showConfirmDialog(null,
                    "GPG doesn't allow empty messages. Want to " +
                            "send a QuestionMark(?) Instead?",
                    "Really", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                messageField.setText("?");
            } else {
                return;
            }

        }
        message.setText(message.getText() + "You: " + messageField.getText() + "\n");

        //encrypt message
        gpg.Encrypt(keyid, messageField.getText());

        //send message
        try {
            dout.writeUTF(gpg.getEncryptedMessage());


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error sending message");
        }

        messageField.setText("");
    }

    @FXML
    public void logout(ActionEvent actionEvent) {
        //get user confirmation
        int ans = JOptionPane.showConfirmDialog(null,
                "Do you really want to logout?", "Logout?",
                JOptionPane.YES_NO_OPTION);
        if (ans == JOptionPane.YES_OPTION) {
            try {
                //close the threads
                thread.interrupt();
                //close the connection
                socket.close();
                //set form to login form
                Node source = (Node) actionEvent.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
                obj.setForm("login.fxml");
            } catch (IOException ex) {
                Logger.getLogger(messageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public void run() {
        try {
            while (true) {
                //stay in listening mode and listen to messages
                String text = din.readUTF();
                //if message received than call this to decrypt it
                this.messageReceived(text);
            }
        } catch (IOException e) {
            thread.interrupt();
        }

    }

    /**
     * called from @method run()
     * when new message received.
     * Its duty is to decrypt and add message to
     * message field.
     *
     * @param text is the message received from other client in
     *             encrypted form.
     */
    private void messageReceived(String text) {
        //If user send the message then do not try to decrypt it.
        if (this.isFlag()) {
            this.setFlag(false);
        }
        //decrypt message
        else {
            //ask for password but only once
            if (count == 0) {
                JPasswordField pf = new JPasswordField();
                int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (okCxl == JOptionPane.OK_OPTION) {
                    pass = new String(pf.getPassword());
                }
                count++;
            }
            gpg.Decrypt(text, pass);
            //set on screen
            message.setText(message.getText() + "Friend: " + gpg.getDecryptedMessage() + "\n");
        }
    }

    /**
     * @return flag which is used in @method messageReceived
     */
    public boolean isFlag() {
        return this.flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
