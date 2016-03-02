package com.Interface;

import com.SQL.SignUp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/**
 * Created by h4ck3r on 2/19/16.
 */
public class Main extends Application {
    static String form = "Form.fxml";
    Stage stage;
    Parent root;
    Scene scene;

    public Main() {
        stage = new Stage();
    }
    //Test whether this is first time run or not

    public static void main(String[] args) {
        SignUp.createConnection();
        if (SignUp.checkRun()) {
            form = "FirstRun.fxml";
        } else
            form = "login.fxml";
        launch(args);
    }

    @Override
    public void start(Stage stag) throws Exception {
        stage.setTitle("LOGIN in CRYPTO | MESSENGER");
        stage.setResizable(false);
        root = FXMLLoader.load(getClass().getResource(form));
        scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();


    }

    public void setForm(String x) throws IOException {
        //set correct title according to form
        if (x.equals("messages.fxml"))
            stage.setTitle("User Messages");
        else if (x.equals("SelectFriend.fxml")) {
            stage.setTitle("We need Information to continue");
        } else if (x.equals("login.fxml"))
            stage.setTitle("LOGIN in CRYPTO | MESSENGER");

        stage.setResizable(false);
        root = FXMLLoader.load(getClass().getResource(x));
        scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
    }
}
