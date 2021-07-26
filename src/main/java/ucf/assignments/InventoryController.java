/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Sathwika Deegutla
 */
package ucf.assignments;

import javafx.collections.ObservableList;
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
import java.util.ResourceBundle;

public class InventoryController implements Initializable {

    //private instance of model and managers
    private Inventory inventory;
    private SceneManager sceneManager;
    private FileManager fileManager;

    //file chooser
    private FileChooser fileChooser = new FileChooser();

    //global converter for big decimal
    BigDecimalStringConverter converter = new BigDecimalStringConverter();

    //private declaration of buttons, menuitems, and textfields
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
    private MenuItem quitBtn;
    @FXML
    private TextField searchField;

    @FXML
    private MenuItem sortByName;
    @FXML
    private MenuItem sortBySerialNumber;
    @FXML
    private MenuItem sortByValue;

    //configure table and columns
    @FXML
    private TableView<Item> tableView;
    //table columns
    @FXML
    private TableColumn<Item, String> nameColumn;
    @FXML
    private TableColumn<Item, String> serialNumberColumn;
    @FXML
    private TableColumn<Item, BigDecimal> valueColumn;

    //constructor
    public InventoryController(Inventory inventory, SceneManager sceneManager){
        //initialize inventory and managers
        this.inventory = inventory;
        this.sceneManager = sceneManager;
        this.fileManager = new FileManager();
    }

    //initialize method for javafx
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //setting up table and columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        serialNumberColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("serialNumber"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<Item, BigDecimal>("value"));

        //allowing table coumns to be editable
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        serialNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        //catch when converting string to big decimal
        try{
            valueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        } catch (NumberFormatException ex){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid Value");
            errorAlert.setContentText("Item's value price must be number and format 0.00.");
            errorAlert.showAndWait();
        }

        //sortable for table
        nameColumn.setSortable(true);
        serialNumberColumn.setSortable(true);
        valueColumn.setSortable(true);

        //manually set on action event handlers for buttons and menuitems
        //since manually set controller
        addNewItemBtn.setOnAction(e -> addNewItemBtnClicked());
        removeItemBtn.setOnAction(e -> removeItemBtnClicked());
        searchBtn.setOnAction(e -> searchBtnClicked());
        saveBtn.setOnAction(e -> saveBtnClicked());
        loadBtn.setOnAction(e -> loadBtnClicked());
        newBtn.setOnAction(e -> newBtnClicked());
        quitBtn.setOnAction(e -> quitBtnClicked());
        searchField.setOnMouseExited(e -> clearSearch());

        //sort menuitems set on action
        sortByName.setOnAction(e -> sort("name"));
        sortBySerialNumber.setOnAction(e -> sort("serialNumber"));
        sortByValue.setOnAction(e -> sort("value"));

        //show items on table
        tableView.setItems(inventory.getItems());

        //allow to select multiple on table
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //allow table to be editable
        tableView.setEditable(true);

        //create new directory for inventory and store all files there
        String newFolder = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Inventory_Deegutla";
        File newDirectory = new File(newFolder);
        newDirectory.mkdirs(); //make directory

        //initialize filechooser
        fileChooser.setInitialDirectory(newDirectory);

    }

    //sort method
    public void sort(String type){
        //get the type of sort requested

        //if name, call sortByName() and set items on table
        if(type.equals("name")){
            tableView.setItems(inventory.sortByName());
            tableView.refresh();
        }

        //if serial number, call sortBySerialNumber() and set items on table
        if(type.equals("serialNumber")){
            tableView.setItems(inventory.sortBySerialNumber());
            tableView.refresh();
        }

        //if value, call sortByValue() and set items on table
        if(type.equals("value")){
            tableView.setItems(inventory.sortByValue());
            tableView.refresh();
        }
    }

    //quit menu item
    public void quitBtnClicked(){
        //get current scene
        Stage stage = (Stage) searchField.getScene().getWindow();
        //close it
        stage.close();
    }

    //new menu item
    public void newBtnClicked(){
        //clear all items from inventory and tableview
        inventory.getItems().removeAll();
        tableView.getItems().clear();
    }

    //add item btn
    public void addNewItemBtnClicked(){
        //show new scene using sceneManager
        Stage stage = new Stage();
        stage.setTitle("Add New Item");
        stage.setScene(sceneManager.getScene("AddItemScene"));
        stage.show();
    }

    //remove item btn
    public void removeItemBtnClicked(){
        //get selected items
        ObservableList<Item> selectedItems;

        selectedItems = tableView.getSelectionModel().getSelectedItems();

        //remove them from inventory and table
        inventory.removeItems(selectedItems);
        tableView.refresh();
    }

    //save menu item
    public void saveBtnClicked(){
        //get current scene
        Stage stage = (Stage) searchField.getScene().getWindow();

        //date format for adding to file name at end, in order to be unique filename
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH.mm.ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);

        //set title and initial file name for the save dialog
        fileChooser.setTitle("Save Dialog");
        fileChooser.setInitialFileName("Inventory_" + date);

        //allowed extensions are .txt, .html, .json
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TSV file", "*.txt"),
                new FileChooser.ExtensionFilter("HTML file", "*.html"),
                new FileChooser.ExtensionFilter("JSON file", "*.json"));

        try {
            //get file chose from fileChooser
            File file = fileChooser.showSaveDialog(stage);
            //sets a directory for future reference
            fileChooser.setInitialDirectory(file.getParentFile());

            //find file type by substring on file name
            String fileName = file.getName();
            String fileType = fileName.substring(fileName.lastIndexOf("."));

            //if file is not null, call the fileManager's save method
            if(file != null){
                fileManager.save(file, fileType, inventory);
            }
        }catch (Exception ex){
            System.out.println("An error occurred.");
        }
    }

    //load menu item
    public void loadBtnClicked(){
        //get current stage
        Stage stage = (Stage) searchField.getScene().getWindow();

        //set title for load dialog
        fileChooser.setTitle("Load Dialog");

        //only accept .txt, .html, .json files
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TSV file", "*.txt"),
                new FileChooser.ExtensionFilter("HTML file", "*.html"),
                new FileChooser.ExtensionFilter("JSON file", "*.json"));

        try {
            //shows open dialog
            File file = fileChooser.showOpenDialog(stage);
            //sets a directory for future reference
            fileChooser.setInitialDirectory(file.getParentFile());

            //find file type using substring on file name
            String fileName = file.getName();
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            System.out.println(fileType);

            //if file not null, use fileManager's load method
            //then inventory's and tableview setItems to loaded items
            if(file != null){
                inventory.setItems(fileManager.load(file, fileType));
                tableView.setItems(inventory.getItems());
            }
        }catch (Exception ex){
            System.out.println("An error occurred.");
        }
    }

    //search btn
    public void searchBtnClicked(){
        //get text from search field
        String search = searchField.getText();

        //declare temp item for found
        Item foundItem;

        //call findItemByName() to see if item's name matches
        //when item found, highlight it and scroll to it
        if((foundItem = inventory.findItemByName(search)) != null){
            tableView.getSelectionModel().select(foundItem);
            tableView.scrollTo(foundItem);
        }else if((foundItem = inventory.findItemBySerial(search)) != null){ //else call findItemBySerial() to see if item's serial number matches
            tableView.getSelectionModel().select(foundItem); //highlight found item
            tableView.scrollTo(foundItem); //scroll to found item
        }else{ //if item not found at all, alert
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Item not found");
            errorAlert.setContentText("No matches.");
            errorAlert.showAndWait();
        }
    }

    //clear the search
    public void clearSearch(){
        //clear the selection of highlight
        tableView.getSelectionModel().clearSelection();
    }

    //change name cell
    public void changeNameCellEvent(TableColumn.CellEditEvent edittedCell){
        //get selected item
        Item selectedItem = tableView.getSelectionModel().getSelectedItem();
        //store old data
        String oldName = selectedItem.getName();
        //get new data
        String newName = edittedCell.getNewValue().toString();

        //validate the field
        if(newName.length() < 2 && newName.length() > 256){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid Name");
            errorAlert.setContentText("Item's name must be between 2 and 256 characters.");
            errorAlert.showAndWait();

            //if not right, keep the old data
            newName = oldName;
        }

        //use editName() to set new or old data
        inventory.editName(selectedItem, newName);
    }

    //change serial number cell
    public void changeSerialNumberCellEvent(TableColumn.CellEditEvent edittedCell){
        //get selected item
        Item selectedItem = tableView.getSelectionModel().getSelectedItem();
        //store old data
        String oldSerialNumber = selectedItem.getSerialNumber();
        //get new data
        String newSerialNumber = edittedCell.getNewValue().toString();

        //validate the field
        //if serial number exists already with a different item, alret
        if(inventory.checkSerialNumber(newSerialNumber) && !(inventory.findItemBySerial(newSerialNumber).equals(selectedItem))) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Serial Number Exists");
            errorAlert.setContentText("This serial number already exists in the inventory.");
            errorAlert.showAndWait();

            //if not right, keep the old data
            newSerialNumber = oldSerialNumber;
        }

        //use editSerialNumber() to set new or old data
        inventory.editSerialNumber(selectedItem, newSerialNumber);
        tableView.refresh();

    }

    //change value cell
    public void changeValueCellEvent (TableColumn.CellEditEvent edittedCell){
        //get selected item
        Item selectedItem = tableView.getSelectionModel().getSelectedItem();
        //store old data
        BigDecimal oldValue = selectedItem.getValue();
        //get new data
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

            //if not right, keep the old data
            newValue = oldValue;
        }

        //use editValue() to set new or old data
        inventory.editValue(selectedItem, newValue);
    }
}
