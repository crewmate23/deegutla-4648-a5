/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Sathwika Deegutla
 */

package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
}
