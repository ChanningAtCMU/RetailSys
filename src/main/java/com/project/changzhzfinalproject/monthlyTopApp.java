package com.project.changzhzfinalproject;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class monthlyTopApp extends Application {

    /**
     * Connect to fxml file and load the scene to the stage
     *
     * @param stage
     */
    @Override
    public void start(Stage stage){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(monthlyTopApp.class.getResource("monthlyTop.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Monthly Top Report");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
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
