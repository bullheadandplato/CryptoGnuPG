package com.Controllers;

import com.SQL.SignUp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.swing.*;

/**
 * Created by h4ck3r on 2/19/16.
 */
public class FirstRunController {
    @FXML
    TextField username;
    @FXML
    TextField keyid;
    @FXML
    PasswordField password;
    @FXML
    Text info;

    @FXML
    public void submit(ActionEvent e) {
        if (keyid.getText().equals("") || username.getText().equals("") || password.getText().equals("")) {
            info.setText("One or more required feilds is/are empty");
        } else {
            SignUp.createConnection();
            if (SignUp.createUser(username.getText(), password.getText(), keyid.getText())) {
                JOptionPane.showMessageDialog(null, "Success, Login now");
            } else {
                info.setText("Error please fill correctly");
            }
        }

    }

    public void help(ActionEvent actionEvent) {
        JOptionPane.showMessageDialog(null, "Please consider reading this. \nIF you don't know what is your key id" +
                " just run this command in your terminal\n 'gpg --list-keys' key will look like this ---/12345678" +
                " text next to /------- is your key id \n like here it is '12345678'");
    }
}
