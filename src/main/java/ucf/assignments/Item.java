/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Sathwika Deegutla
 */

package ucf.assignments;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;

public class Item {

    //data values
    //SimpleStringProperty for name and serialNumber
    //BigDecimal for value
    private SimpleStringProperty name;
    private SimpleStringProperty serialNumber;
    private BigDecimal value;

    //constructor
    public Item(String name, String serialNumber, BigDecimal value){
        //initialize instance variables
        this.name = new SimpleStringProperty(name);
        this.serialNumber = new SimpleStringProperty(serialNumber);
        this.value = value;
    }

    //getters
    public String getName() {
        //return name
        return name.get();
    }

    public String getSerialNumber() {
        //return serial number
        return serialNumber.get();
    }

    public BigDecimal getValue() {
        //return value
        return this.value;
    }

    //setter
    public void setName(String name) {
        //set name to parameter
        this.name.set(name);
    }

    public void setSerialNumber(String serialNumber) {
        //set serial number to parameter
        this.serialNumber.set(serialNumber);
    }

    public void setValue(BigDecimal value) {
        //set value to parameter
        this.value = value;
    }
}
