/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Sathwika Deegutla
 */

package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.util.Comparator;

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
                return true;
        }

        return false;
    }

    public Item findItemBySerial(String serialNumber){
        for(Item item : items){
            if(serialNumber.equalsIgnoreCase(item.getSerialNumber()))
                return item;
        }

        return null;
    }

    public Item findItemByName(String name){
        for(Item item : items){
            if(name.equalsIgnoreCase(item.getName()))
                return item;
        }

        return null;
    }

    public void editValue(Item selectedItem, BigDecimal newValue){
        selectedItem.setValue(newValue);
    }

    public ObservableList<Item> sortByName(){
        ObservableList<Item> sortedNames = FXCollections.observableArrayList();
        sortedNames = items;

        FXCollections.sort(sortedNames, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });

        return sortedNames;
    }

    public ObservableList<Item> sortBySerialNumber(){
        ObservableList<Item> sortedNames = FXCollections.observableArrayList();
        sortedNames = items;

        FXCollections.sort(sortedNames, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getSerialNumber().toLowerCase().compareTo(o2.getSerialNumber().toLowerCase());
            }
        });

        return sortedNames;
    }

    public ObservableList<Item> sortByValue(){
        ObservableList<Item> sortedValues = FXCollections.observableArrayList();
        sortedValues = items;

        FXCollections.sort(sortedValues, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        return sortedValues;
    }

}
