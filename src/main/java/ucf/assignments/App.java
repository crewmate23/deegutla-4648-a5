/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Sathwika Deegutla
 */

package ucf.assignments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        //Parent root = FXMLLoader.load(getClass().getResource("InventoryItems.fxml"));
        SceneManager sceneManager = new SceneManager();
        sceneManager.loadScenes();

        Scene scene = sceneManager.getScene("InventoryScene");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Inventory Items");
        primaryStage.show();
    }
}
