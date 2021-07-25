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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.BigDecimalStringConverter;


import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {

    private Inventory inventory;
    private SceneManager sceneManager;
    private FileManager fileManager;

    FileChooser fileChooser = new FileChooser();

    BigDecimalStringConverter converter = new BigDecimalStringConverter();

    @FXML
    private Button addNewItemBtn;
    @FXML
    private Button removeItemBtn;
    @FXML
    private Button searchBtn;
    @FXML
    private MenuItem saveBtn;
    @FXML
    private MenuItem loadBtn;
    @FXML
    private MenuItem newBtn;
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
        this.fileManager = new FileManager();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        serialNumberColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("serialNumber"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<Item, BigDecimal>("value"));

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        serialNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        /*try{
            valueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        }catch (NumberFormatException ex){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid Value");
            errorAlert.setContentText("Item's value price must be number and format 0.00.");
            errorAlert.showAndWait();
        }*/
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

        saveBtn.setOnAction(e -> saveBtnClicked());

        loadBtn.setOnAction(e -> loadBtnClicked());

        newBtn.setOnAction(e -> newBtnClicked());

        searchField.setOnMouseExited(e -> clearSearch());
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

        String newFolder = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Inventory_Deegutla";
        File newDirectory = new File(newFolder);
        newDirectory.mkdirs();

        fileChooser.setInitialDirectory(newDirectory);

    }


    public void newBtnClicked(){
        inventory.getItems().removeAll();
        tableView.getItems().clear();
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

    public void saveBtnClicked(){
        Stage stage = (Stage) searchField.getScene().getWindow();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH.mm.ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);

        fileChooser.setTitle("Save Dialog");
        fileChooser.setInitialFileName("Inventory_" + date);


        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TSV file", "*.txt"),
                new FileChooser.ExtensionFilter("HTML file", "*.html"),
                new FileChooser.ExtensionFilter("JSON file", "*.json"));

        try {
            File file = fileChooser.showSaveDialog(stage);
            //sets a directory for future reference
            fileChooser.setInitialDirectory(file.getParentFile());

            String fileName = file.getName();
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            System.out.println(fileType);

            if(file != null){
                fileManager.save(file, fileType, inventory);
                //calls list's save method to write into that file
            }
        }catch (Exception ex){
            System.out.println("An error occurred.");
        }
    }

    public void loadBtnClicked(){
        Stage stage = (Stage) searchField.getScene().getWindow();

        fileChooser.setTitle("Load Dialog");

        //only accept csv files
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TSV file", "*.txt"),
                new FileChooser.ExtensionFilter("HTML file", "*.html"),
                new FileChooser.ExtensionFilter("JSON file", "*.json"));

        try {
            File file = fileChooser.showOpenDialog(stage);
            //sets a directory for future reference
            fileChooser.setInitialDirectory(file.getParentFile());

            String fileName = file.getName();
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            System.out.println(fileType);

            if(file != null){
                //call list's load to get arraylist of items
                //convert to observable list and sort by date and display in the table
                //this.inventory = fileManager.load(file, fileType);
                //inventory.getItems().removeAll(inventory.getItems());
                //FXCollections.copy(inventory.getItems(), fileManager.load(file, fileType));
                inventory.setItems(fileManager.load(file, fileType));
                tableView.setItems(inventory.getItems());
            }
        }catch (Exception ex){
            System.out.println("An error occurred.");
        }
    }

    public void searchBtnClicked(){
        String search = searchField.getText();

        ObservableList<Item> searchItems = FXCollections.observableArrayList();

        //List<String> searchWordsArray = Arrays.asList(search.trim().split(" "));

        tableView.getItems().stream()
                .filter(item -> item.getName().contains(search))
                .findAny()
                .ifPresent(item -> {
                    tableView.getSelectionModel().select(item);
                    tableView.scrollTo(item);
                });

        tableView.getItems().stream()
                .filter(item -> item.getSerialNumber().contains(search))
                .findAny()
                .ifPresent(item -> {
                    tableView.getSelectionModel().select(item);
                    tableView.scrollTo(item);
                });
        //tableView.setItems(searchItems);
    }

    public void clearSearch(){
        tableView.getSelectionModel().clearSelection();
    }

    public void changeNameCellEvent(TableColumn.CellEditEvent edittedCell){
        Item selectedItem = tableView.getSelectionModel().getSelectedItem();
        String oldName = selectedItem.getName();
        String newName = edittedCell.getNewValue().toString();

        if(newName.length() < 2 && newName.length() > 256){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid Name");
            errorAlert.setContentText("Item's name must be between 2 and 256 characters.");
            errorAlert.showAndWait();

            newName = oldName;
        }

        selectedItem.setName(newName);
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
        BigDecimal newValue = converter.fromString(edittedCell.getNewValue().toString());

        //validate value field
        try{
            if(edittedCell.getNewValue().toString() != null)
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
