/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Sathwika Deegutla
 */
package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.BigDecimalStringConverter;


import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {

    private Inventory inventory;
    private SceneManager sceneManager;
    private FileManager fileManager;

    @FXML
    private Button addNewItemBtn;
    @FXML
    private Button removeItemBtn;
    @FXML
    private Button searchBtn;
    @FXML
    private TextField searchField;

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

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        serialNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

        addNewItemBtn.setOnAction(e -> addNewItemBtnClicked());
        /*addNewItemBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addNewItemBtnClicked(event);
            }
        });*/

        removeItemBtn.setOnAction(e -> removeItemBtnClicked());

        searchBtn.setOnAction(e -> searchBtnClicked());
        /*removeItemBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                removeItemBtnClicked(event);
            }
        });*/

        //nameColumn.setOnEditCommit(e -> changeNameCellEvent());

        tableView.setItems(inventory.getItems());

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setEditable(true);


        /*FilteredList<Item> itemFilteredList = new FilteredList<>(inventory.getItems(), b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            itemFilteredList.setPredicate(item -> {

                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String lowerCaseSearch = newValue.toLowerCase();

                if(item.getName().toLowerCase().indexOf(lowerCaseSearch) != -1){
                    return true;
                }else if(item.getSerialNumber().toLowerCase().indexOf(lowerCaseSearch) != -1){
                    return true;
                }else{
                    return false;
                }
            });
        });

        *//*SortedList<Item> itemSortedList = new SortedList<>(itemFilteredList);

        itemSortedList.comparatorProperty().bind(tableView.comparatorProperty());*//*
        //ObservableList<Item> searchItems = FXCollections.observableArrayList();

        FXCollections.copy(inventory.getItems(), itemFilteredList);
        tableView.refresh();*/

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

    public void searchBtnClicked(){
        String search = searchField.getText();

        ObservableList<Item> searchItems = FXCollections.observableArrayList();

        List<String> searchWordsArray = Arrays.asList(search.trim().split(" "));

        for(String searchWord : searchWordsArray){
            if(findItem(searchWord) != null){
                FXCollections.copy(searchItems, findItem(searchWord));
            }
        }

        tableView.setItems(searchItems);
    }

    public ObservableList<Item> findItem(String searchWord){
        ObservableList<Item> foundItems = FXCollections.observableArrayList();

        for(Item item : inventory.getItems()){
            if(item.getName().contains(searchWord.toLowerCase())){
                foundItems.add(item);
            }else if(item.getSerialNumber().contains(searchWord.toLowerCase())){
                foundItems.add(item);
            }
        }

        return foundItems;
    }

    public void changeNameCellEvent(TableColumn.CellEditEvent edittedCell){
        Item selectedItem = tableView.getSelectionModel().getSelectedItem();
        selectedItem.setName(edittedCell.getNewValue().toString());
    }

    public void changeSerialNumberCellEvent(TableColumn.CellEditEvent edittedCell){
        Item selectedItem = tableView.getSelectionModel().getSelectedItem();
        String oldSerialNumber = selectedItem.getSerialNumber();
        String newSerialNumber = edittedCell.getNewValue().toString();

        for(Item item : inventory.getItems()){
            if(newSerialNumber.equals(item.getSerialNumber()) && !(item.equals(selectedItem))){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Serial Number Exists");
                errorAlert.setContentText("This serial number already exists in the inventory.");
                errorAlert.showAndWait();

                newSerialNumber = oldSerialNumber;
                //serialNumberColumn.setEditable(false);
            }
        }

        selectedItem.setSerialNumber(newSerialNumber);
        tableView.refresh();

    }

    public void changeValueCellEvent (TableColumn.CellEditEvent edittedCell){
        Item selectedItem = tableView.getSelectionModel().getSelectedItem();
        BigDecimal oldValue = selectedItem.getValue();
        BigDecimal newValue;

        //validate value field
        try{
            newValue = new BigDecimal(edittedCell.getNewValue().toString());
        } catch (NumberFormatException ex){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid Value");
            errorAlert.setContentText("Item's value price must be number and format 0.00.");
            errorAlert.showAndWait();

            newValue = oldValue;
        }

        /*String regex = "\\d+";
        if(!edittedCell.getNewValue().toString().matches(regex)){
            selectedItem.setValue(oldValue);
        }else{
            selectedItem.setValue(new BigDecimal(edittedCell.getNewValue().toString()));
        }*/

        selectedItem.setValue(newValue);
    }
}
