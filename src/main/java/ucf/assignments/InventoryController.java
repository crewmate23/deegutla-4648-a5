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

    private Inventory inventory;
    private SceneManager sceneManager;
    private FileManager fileManager;

    private FileChooser fileChooser = new FileChooser();

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
        try{
            valueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        } catch (NumberFormatException ex){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid Value");
            errorAlert.setContentText("Item's value price must be number and format 0.00.");
            errorAlert.showAndWait();
        }
        //valueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

        nameColumn.setSortable(true);
        serialNumberColumn.setSortable(true);
        valueColumn.setSortable(true);

        addNewItemBtn.setOnAction(e -> addNewItemBtnClicked());
        removeItemBtn.setOnAction(e -> removeItemBtnClicked());
        searchBtn.setOnAction(e -> searchBtnClicked());
        saveBtn.setOnAction(e -> saveBtnClicked());
        loadBtn.setOnAction(e -> loadBtnClicked());
        newBtn.setOnAction(e -> newBtnClicked());
        quitBtn.setOnAction(e -> quitBtnClicked());
        searchField.setOnMouseExited(e -> clearSearch());

        sortByName.setOnAction(e -> sort("name"));
        sortBySerialNumber.setOnAction(e -> sort("serialNumber"));
        sortByValue.setOnAction(e -> sort("value"));

        tableView.setItems(inventory.getItems());

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setEditable(true);

        String newFolder = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Inventory_Deegutla";
        File newDirectory = new File(newFolder);
        newDirectory.mkdirs();

        fileChooser.setInitialDirectory(newDirectory);

    }

    public void sort(String type){

        if(type.equals("name")){
            tableView.setItems(inventory.sortByName());
            tableView.refresh();
        }

        if(type.equals("serialNumber")){
            tableView.setItems(inventory.sortBySerialNumber());
            tableView.refresh();
        }

        if(type.equals("value")){
            tableView.setItems(inventory.sortByValue());
            tableView.refresh();
        }
    }

    public void quitBtnClicked(){
        Stage stage = (Stage) searchField.getScene().getWindow();
        stage.close();
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
        ObservableList<Item> selectedItems;

        selectedItems = tableView.getSelectionModel().getSelectedItems();

        inventory.removeItems(selectedItems);
        tableView.refresh();
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
                inventory.setItems(fileManager.load(file, fileType));
                tableView.setItems(inventory.getItems());
            }
        }catch (Exception ex){
            System.out.println("An error occurred.");
        }
    }

    public void searchBtnClicked(){
        String search = searchField.getText();

        Item foundItem;

        if((foundItem = inventory.findItemByName(search)) != null){
            tableView.getSelectionModel().select(foundItem);
            tableView.scrollTo(foundItem);
        }else if((foundItem = inventory.findItemBySerial(search)) != null){
            tableView.getSelectionModel().select(foundItem);
            tableView.scrollTo(foundItem);
        }else{
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Item not found");
            errorAlert.setContentText("No matches.");
            errorAlert.showAndWait();
        }
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

        inventory.editName(selectedItem, newName);
    }

    public void changeSerialNumberCellEvent(TableColumn.CellEditEvent edittedCell){
        Item selectedItem = tableView.getSelectionModel().getSelectedItem();
        String oldSerialNumber = selectedItem.getSerialNumber();
        String newSerialNumber = edittedCell.getNewValue().toString();

        if(inventory.checkSerialNumber(newSerialNumber) && !(inventory.findItemBySerial(newSerialNumber).equals(selectedItem))) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Serial Number Exists");
            errorAlert.setContentText("This serial number already exists in the inventory.");
            errorAlert.showAndWait();

            newSerialNumber = oldSerialNumber;
            //serialNumberColumn.setEditable(false);
        }


        inventory.editSerialNumber(selectedItem, newSerialNumber);
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

        inventory.editValue(selectedItem, newValue);
    }
}
