package com.project.changzhzfinalproject;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class creditCardApp extends Application {
    /**
     * Connect to fxml file and load the scene to the stage
     *
     * @param stage
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(creditCardApp.class.getResource("creditCard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login & Payment Information");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launch data to show stage
     *
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }
}
