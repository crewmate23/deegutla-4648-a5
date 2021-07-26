/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Sathwika Deegutla
 */

package ucf.assignments;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application {

    public static void main(String[] args){
        //launch the application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        //set the primary stage to InventoryItems.fxml
        SceneManager sceneManager = new SceneManager();
        sceneManager.loadScenes();

        Scene scene = sceneManager.getScene("InventoryScene");
        //scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Inventory Items");
        primaryStage.show();
    }
}
