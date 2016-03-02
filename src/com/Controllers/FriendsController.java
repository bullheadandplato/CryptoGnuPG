package com.Controllers;

import com.Interface.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

/**
 * Created by h4ck3r on 2/19/16.
 */
public class FriendsController {
    GnuPG gpg = new GnuPG();
    Main obj = new Main();
    @FXML
    ComboBox<String> friends;
    @FXML
    TextField host;

    @FXML
    protected void initialize() {
        List<String> abc = gpg.friends();
        ObservableList<String> temp = FXCollections.observableArrayList(abc);
        friends.setItems(temp);

    }

    public void chat(ActionEvent actionEvent) {
        try {
            String email = friends.getValue();
            String keyid = email.substring(email.indexOf('<') + 1, email.length() - 1);
            messageController.keyid = keyid;
            messageController.server = host.getText();
            if (messageController.Connect()) {
                //change scene in fx main window
                Node source = (Node) actionEvent.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
                //set message form as current scene
                obj.setForm("messages.fxml");
            }

        } catch (NullPointerException | IOException e) {
            JOptionPane.showMessageDialog(null, "Please select one friend");
        }

    }
}
