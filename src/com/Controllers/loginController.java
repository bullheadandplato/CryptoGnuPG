package com.Controllers;

import com.Interface.Main;
import com.SQL.Login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class loginController {

    //
    //Varables to handle interface
    //
    Main obj = new Main();
    //
    //Variables to respond to FXML
    //
    @FXML
    private Text error;
    @FXML
    private TextField username;
    @FXML
    private PasswordField passme;


    //Action listener for login button
    @FXML
    protected void actionHandler(ActionEvent event) {
        //check the user
        Login.createConnection();
        if (Login.select(username.getText(), passme.getText())) {
            //set username on message form
            messageController.username = username.getText();
            //change scene in fx main window
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            try {
                //set message form as current scene
                obj.setForm("SelectFriend.fxml");

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            error.setText("Failed to authenticate");
        }
    }
}
