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

    private Map<String, Scene> scenes = new HashMap<>();

    public void loadScenes(){
        Inventory inventory = new Inventory();

        InventoryController inventoryController = new InventoryController(inventory, this);
        AddItemController addItemController = new AddItemController(inventory, this);

        Parent root;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("InventoryItems.fxml"));
        loader.setController(inventoryController);

        try{
            root = loader.load();
            scenes.put("InventoryScene", new Scene(root));
        }catch (IOException e){
            e.printStackTrace();
        }

        loader = new FXMLLoader(getClass().getResource("AddItem.fxml"));
        loader.setController(addItemController);

        try {
            root = loader.load();
            scenes.put("AddItemScene", new Scene(root));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Scene getScene(String sceneName){
        return scenes.get(sceneName);
    }


}
