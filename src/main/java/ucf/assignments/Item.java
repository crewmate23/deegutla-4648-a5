/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Sathwika Deegutla
 */

package ucf.assignments;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Item {

    private SimpleStringProperty name;
    private SimpleStringProperty serialNumber;
    private SimpleDoubleProperty value;

    public Item(String name, String serialNumber, Double value){
        this.name = new SimpleStringProperty(name);
        this.serialNumber = new SimpleStringProperty(serialNumber);
        this.value = new SimpleDoubleProperty(value);
    }

    public String getName() {
        return name.get();
    }

    public String getSerialNumber() {
        return serialNumber.get();
    }

    public Double getValue() {
        return value.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber.set(serialNumber);
    }

    public void setValue(Double value) {
        this.value.set(value);
    }
}
