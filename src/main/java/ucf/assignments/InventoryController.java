/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Sathwika Deegutla
 */
package ucf.assignments;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {

    private Inventory inventory;
    private SceneManager sceneManager;
    private FileManager fileManager;

    @FXML
    private Button addNewItemBtn;
    @FXML
    private Button removeItemBtn;

    //configure table and columns
    @FXML
    private TableView<Item> tableView;

    @FXML
    private TableColumn<Item, String> nameColumn;
    @FXML
    private TableColumn<Item, String> serialNumberColumn;
    @FXML
    private TableColumn<Item, BigDecimal> valueColumn;


    public InventoryController(Inventory inventory, SceneManager sceneManager){
        this.inventory = inventory;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        serialNumberColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("serialNumber"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<Item, BigDecimal>("value"));

        addNewItemBtn.setOnAction(e -> addNewItemBtnClicked());
        /*addNewItemBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addNewItemBtnClicked(event);
            }
        });*/

        removeItemBtn.setOnAction(e -> removeItemBtnClicked());
        /*removeItemBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                removeItemBtnClicked(event);
            }
        });*/

        tableView.setItems(inventory.getItems());

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    public void addNewItemBtnClicked(){
        Stage stage = new Stage();
        stage.setTitle("Add New Item");
        stage.setScene(sceneManager.getScene("AddItemScene"));
        stage.show();
    }

    public void removeItemBtnClicked(){
        ObservableList<Item> selectedItems, allItems;

        allItems = tableView.getItems();
        selectedItems = tableView.getSelectionModel().getSelectedItems();

        allItems.removeAll(selectedItems);
    }
}
