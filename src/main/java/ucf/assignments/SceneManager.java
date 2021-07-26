/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Sathwika Deegutla
 */

package ucf.assignments;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {

    //data of hash map to store all scenes with string key
    private Map<String, Scene> scenes = new HashMap<>();

    //load all scenes
    public void loadScenes(){
        //declare and initialize inventory model,
        //inventoryController, addItemController

        Inventory inventory = new Inventory();

        InventoryController inventoryController = new InventoryController(inventory, this);
        AddItemController addItemController = new AddItemController(inventory, this);

        //get the fxml show
        Parent root;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("InventoryItems.fxml")); //main app screen
        loader.setController(inventoryController); //manually setting controller

        try{
            root = loader.load();
            scenes.put("InventoryScene", new Scene(root)); //add the scene to hash map
        }catch (IOException e){
            e.printStackTrace();
        }

        loader = new FXMLLoader(getClass().getResource("AddItem.fxml")); //add item screen
        loader.setController(addItemController); //manually setting controller

        try {
            root = loader.load();
            scenes.put("AddItemScene", new Scene(root)); //add the scene to hash map
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //get scene
    public Scene getScene(String sceneName){
        //return scene using string, or scenename, key
        return scenes.get(sceneName);
    }


}
