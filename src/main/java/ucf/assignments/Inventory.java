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

    //declare observable list
    private ObservableList<Item> items;

    //constructor
    public Inventory(){
        //initialize observable list
        items = FXCollections.observableArrayList();
    }

    //set items
    public void setItems(ObservableList<Item> items){
        //get observable list of items and set to this.items
        this.items = items;
    }

    //get items
    public ObservableList<Item> getItems(){
        //return this.items
        return this.items;
    }

    //remove items
    public void removeItems(ObservableList<Item> removeItems){
        //get list of remove items
        //remove them from this.items
        items.removeAll(removeItems);
    }

    //add item
    public void addItem(Item item){
        //get item to add
        //add it into this.items
        items.add(item);
    }

    //edit item's name
    public void editName(Item selectedItem, String newName){
        //get item to edit, and new name
        //use item's setName() method
        selectedItem.setName(newName);
    }

    //edit item's serial number
    public void editSerialNumber(Item selectedItem, String newSerialNumber){
        //get item being editted and new serial number
        //use item's setSerialNumber() method
        selectedItem.setSerialNumber(newSerialNumber);
    }

    //edit item's value
    public void editValue(Item selectedItem, BigDecimal newValue){
        //get item being editted and new value
        //use item's setValue() method
        selectedItem.setValue(newValue);
    }

    //check if serial number exists
    public boolean checkSerialNumber(String serialNumber){
        //loop through items list and see if it exists
        for(Item item : items){
            if(serialNumber.equals(item.getSerialNumber()))
                return true; //if so return boolean true
        }

        return false; //else not found so false
    }

    //find item by serial
    public Item findItemBySerial(String serialNumber){
        //loop through items list and see if serial number matches
        for(Item item : items){
            if(serialNumber.equalsIgnoreCase(item.getSerialNumber()))
                return item; //if so return the item
        }

        return null; //else return null since no item found
    }

    //find item by name
    public Item findItemByName(String name){
        //loop through items list and see if name matches
        for(Item item : items){
            if(name.equalsIgnoreCase(item.getName()))
                return item; //if so return the item
        }

        return null; //else return null since no item found
    }

    //sort list by name
    public ObservableList<Item> sortByName(){
        //create new observable list for sortedNames and copy this.items data
        ObservableList<Item> sortedNames = items;

        //use comparator sort to sort the list by name
        FXCollections.sort(sortedNames, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });

        //return the sorted list
        return sortedNames;
    }

    //sort list by serial number
    public ObservableList<Item> sortBySerialNumber(){
        //create new observable list for sortedNames and copy this.items data
        ObservableList<Item> sortedNames = items;

        //use comparator sort to sort the list by serial number
        FXCollections.sort(sortedNames, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getSerialNumber().toLowerCase().compareTo(o2.getSerialNumber().toLowerCase());
            }
        });

        //return the sorted list
        return sortedNames;
    }

    //sort list by value
    public ObservableList<Item> sortByValue(){
        //create new observable list for sortedNames and copy this.items data
        ObservableList<Item> sortedValues = items;

        //use comparator sort to sort the list by value
        FXCollections.sort(sortedValues, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        //return the sorted list
        return sortedValues;
    }

}
