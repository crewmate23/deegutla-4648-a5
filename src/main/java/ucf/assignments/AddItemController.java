/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Sathwika Deegutla
 */
package ucf.assignments;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class AddItemController {

    //global variable for inventory and scene manager
    private Inventory inventory;
    private SceneManager sceneManager;

    //textfields
    @FXML
    private TextField nameField;
    @FXML
    private TextField serialNumberField;
    @FXML
    private TextField valueField;

    //constructor
    public AddItemController(Inventory inventory, SceneManager sceneManager){
        //initialize inventory and sceneManager
        this.inventory = inventory;
        this.sceneManager = sceneManager;
    }

    //add item method
    public void addItemBtnClicked(ActionEvent actionEvent){
        //get info from textfields
        String name = nameField.getText();
        String serialNumber = serialNumberField.getText();
        BigDecimal value = null;

        //validate input
        boolean validInput = true;

        //if any blank fields, alert
        if(nameField.getText() == "" || serialNumberField.getText() == "" || valueField.getText() == ""){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Blank Field");
            errorAlert.setContentText("Please do not leave any field blank!");
            errorAlert.showAndWait();
        }

        //validate name field, between 2 and 256 length
        //if not, alert
        if(name.length() < 2 && name.length() > 256){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid Name");
            errorAlert.setContentText("Item's name must be between 2 and 256 characters.");
            errorAlert.showAndWait();

            //clear fields
            clearField(nameField);

            //set validInput to false
            validInput = false;
        }


        //validate value field
        //textfield is number/decimal test with try/catch
        //if not, alert, clear fields, and set boolean to false
        try{
            value = new BigDecimal(valueField.getText());
        }catch (NumberFormatException ex){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid Value");
            errorAlert.setContentText("Item's value price must be number and format 0.00.");
            errorAlert.showAndWait();

            clearField(valueField);

            validInput = false;
        }

        //if value not null and decimal places is more than 2,
        //alert, clear fields, and boolean to false
        if(value != null && value.scale() > 2){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid Value");
            errorAlert.setContentText("Please enter valid value price in format of 0.00.");
            errorAlert.showAndWait();

            clearField(valueField);

            validInput = false;
        }


        //validate serial number field
        //check if serial number is a letter or a digit only
        //if not, alert, clear fields, and boolean to false
        for(int i = 0; i < serialNumber.length(); i++){
            if(!Character.isLetterOrDigit(serialNumber.charAt(i))){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Invalid Serial Number");
                errorAlert.setContentText("Item's serial number must be a letter or a digit.");
                errorAlert.showAndWait();

                clearField(serialNumberField);

                validInput = false;
            }
        }

        //if serial number length is not 10, alert
        //clear fields, and boolean to false
        if(serialNumber.length() != 10){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid Serial Number");
            errorAlert.setContentText("Item's serial number must be 10 characters XXXXXXXXXX.");
            errorAlert.showAndWait();

            clearField(serialNumberField);

            validInput = false;
        }

        //check if serial number exists already
        //use inventory's checkSerialNumber method
        //if exists, alert, clear fields, and boolean to false
        if(inventory.checkSerialNumber(serialNumber)) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Serial Number Exists");
            errorAlert.setContentText("This serial number exists already in the inventory.");
            errorAlert.showAndWait();

            clearField(serialNumberField);

            validInput = false;
        }

        //if none of errors occur, then add item
        if(validInput){
            //add new item with values from textfields
            inventory.addItem(new Item(name, serialNumber, value));

            //reset the text fields
            nameField.setPromptText("Name: ");
            nameField.clear();

            serialNumberField.setPromptText("Serial Number: XXXXXXXXXX");
            serialNumberField.clear();

            valueField.setPromptText("Value: $0.00");
            valueField.clear();

            //close the window
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();
        }


    }

    //cancel btn
    public void cancelBtnClicked(ActionEvent actionEvent){
        //get the stage and close the window
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void clearField(TextField textField){
       //get textfield and clear it
        textField.clear();
    }
}
