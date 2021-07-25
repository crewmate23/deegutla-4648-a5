/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Sathwika Deegutla
 */

package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;

public class Inventory {

    private ObservableList<Item> items;

    public Inventory(){
        items = FXCollections.observableArrayList();
    }

    public void setItems(ObservableList<Item> items){
        this.items = items;
    }

    public ObservableList<Item> getItems(){
        return this.items;
    }

    public void removeItems(ObservableList<Item> removeItems){
        items.removeAll(removeItems);
    }

    public void addItem(Item item){
        items.add(item);
    }

    public void editName(Item selectedItem, String newName){
        selectedItem.setName(newName);
    }

    public void editSerialNumber(Item selectedItem, String newSerialNumber){
        selectedItem.setSerialNumber(newSerialNumber);
    }

    public boolean checkSerialNumber(String serialNumber){
        for(Item item : items){
            if(serialNumber.equals(item.getSerialNumber()))
                return false;
        }

        return true;
    }

    public Item findItemBySerial(String serialNumber){
        for(Item item : items){
            if(serialNumber.equals(item.getSerialNumber()))
                return item;
        }

        return null;
    }

    public Item findItemByName(String name){
        for(Item item : items){
            if(name.equals(item.getName()))
                return item;
        }

        return null;
    }

    public void editValue(Item selectedItem, BigDecimal newValue){
        selectedItem.setValue(newValue);
    }
}
