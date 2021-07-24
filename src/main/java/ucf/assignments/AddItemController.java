/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Sathwika Deegutla
 */
package ucf.assignments;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.math.BigDecimal;

public class AddItemController {

    private Inventory inventory;
    private SceneManager sceneManager;

    @FXML
    private TextField nameField;
    @FXML
    private TextField serialNumberField;
    @FXML
    private TextField valueField;

    public AddItemController(Inventory inventory, SceneManager sceneManager){
        this.inventory = inventory;
        this.sceneManager = sceneManager;
    }

    public void addItemBtnClicked(ActionEvent actionEvent){
        String name = nameField.getText();
        String serialNumber = serialNumberField.getText();
        BigDecimal value = null;

        boolean validInput = true;

        if(nameField.getText() == "" || serialNumberField.getText() == "" || valueField.getText() == ""){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Blank Field");
            errorAlert.setContentText("Please do not leave any field blank!");
            errorAlert.showAndWait();
        }

        //validate name field
        if(name.length() < 2 && name.length() > 256){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid Name");
            errorAlert.setContentText("Item's name must be between 2 and 256 characters.");
            errorAlert.showAndWait();

            clearField(nameField);

            validInput = false;
        }


        //validate value field
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

        if(value != null && value.scale() > 2){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid Value");
            errorAlert.setContentText("Please enter valid value price in format of 0.00.");
            errorAlert.showAndWait();

            clearField(valueField);

            validInput = false;
        }


        //validate serial number field
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

        if(serialNumber.length() < 10){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid Serial Number");
            errorAlert.setContentText("Item's serial number must be 10 characters XXXXXXXXXX.");
            errorAlert.showAndWait();

            clearField(serialNumberField);

            validInput = false;
        }

        for(Item i: inventory.getItems()){
            if(serialNumber.equals(i.getSerialNumber())){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Serial Number Exists");
                errorAlert.setContentText("This serial number exists already in the inventory.");
                errorAlert.showAndWait();

                clearField(serialNumberField);

                validInput = false;
            }
        }

        if(validInput){
            inventory.getItems().add(new Item(name, serialNumber, value));

            nameField.setPromptText("Name: ");
            nameField.clear();

            serialNumberField.setPromptText("Serial Number: XXXXXXXXXX");
            serialNumberField.clear();

            valueField.setPromptText("Value: $0.00");
            valueField.clear();

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();
        }


    }

    public void cancelBtnClicked(ActionEvent actionEvent){
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void clearField(TextField textField){
        textField.clear();
    }
}
