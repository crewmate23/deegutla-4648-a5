/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Sathwika Deegutla
 */
package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    private Inventory inventory = new Inventory();
    private ObservableList<Item> items = FXCollections.observableArrayList();

    private List<Item> dummyData = new ArrayList<>();

    public void addDummyData(){
        dummyData.add(new Item("MacBook Air", "983AR93JMB", new BigDecimal(1199.00)));
        dummyData.add(new Item("MacBook Pro", "983PR203MB", new BigDecimal(1599.00)));
        dummyData.add(new Item("iPad Air", "342AR894IP", new BigDecimal(699.00)));
        dummyData.add(new Item("iPad Pro", "342PR932IP", new BigDecimal(899.00)));
    }

    @Test
    void addItem_Test(){
        addDummyData();

        inventory.addItem(dummyData.get(0));
        inventory.addItem(dummyData.get(1));
        inventory.addItem(dummyData.get(2));
        inventory.addItem(dummyData.get(3));

        //expected dummyData, actual inventory
        assertArrayEquals(dummyData.toArray(), inventory.getItems().toArray());
    }

    @Test
    void removeItems_Test() {
        //add dummy data to inventory
        addDummyData();
        inventory.addItem(dummyData.get(0));
        inventory.addItem(dummyData.get(1));
        inventory.addItem(dummyData.get(2));
        inventory.addItem(dummyData.get(3));

        //remove 'Pro'
        items.add(dummyData.get(1));
        items.add(dummyData.get(3));


        inventory.removeItems(items);

        dummyData.removeAll(items);

        assertArrayEquals(dummyData.toArray(), inventory.getItems().toArray());
    }

    public void inventoryRefresh(){
        inventory.getItems().removeAll();

        addDummyData();
        inventory.addItem(dummyData.get(0));
        inventory.addItem(dummyData.get(1));
        inventory.addItem(dummyData.get(2));
        inventory.addItem(dummyData.get(3));
    }


    @Test
    void editName_Test() {
        inventoryRefresh();

        //edit "MacBook Pro" to "MacBook Pro Max"
        inventory.editName(dummyData.get(1), "MacBook Pro Max");

        assertEquals("MacBook Pro Max", inventory.getItems().get(1).getName());
    }

    @Test
    void editValue_Test() {
        inventoryRefresh();

        //edit MacBook Air's price "1199.00" to "1299.00"
        inventory.editValue(dummyData.get(0), new BigDecimal(1299.00));

        assertEquals(new BigDecimal(1299.00), inventory.getItems().get(0).getValue());
    }

    @Test
    void editSerialNumber_Test() {
        inventoryRefresh();

        //edit iPad Air's serial number "342AR894IP" to "342AR498IP"
        inventory.editSerialNumber(dummyData.get(2), "342AR498IP");

        assertEquals("342AR498IP", inventory.getItems().get(2).getSerialNumber());
    }

    @Test
    void checkSerialNumber_true_Test() {
        inventoryRefresh();

        //check existing serial number "342PR932IP"
        assertTrue(inventory.checkSerialNumber("342PR932IP"));
    }

    @Test
    void checkSerialNumber_false_Test() {
        inventoryRefresh();

        //check non-existing serial number "342PR932IP"
        assertFalse(inventory.checkSerialNumber("9124232IKN"));
    }

    @Test
    void findItemBySerial_Test() {
        inventoryRefresh();

        //find iPad Pro by "342PR932IP"
        Item item = inventory.findItemBySerial("342PR932IP");

        assertEquals("342PR932IP", item.getSerialNumber());
    }

    @Test
    void findItemByName_Test() {
        inventoryRefresh();

        //find iPad Pro by "iPad Pro"
        Item item = inventory.findItemByName("iPad Pro");

        assertEquals("iPad Pro", item.getName());
    }

    @Test
    void sortByName_Test(){
        inventoryRefresh();

        boolean validate = true;

        //add to expected items list in accordance with name
        items.add(dummyData.get(2));
        items.add(dummyData.get(3));
        items.add(dummyData.get(0));
        items.add(dummyData.get(1));

        ObservableList<Item> inventoryItems = inventory.sortByName();

        for(int i = 0; i < inventoryItems.size(); i++){
            if(!inventoryItems.get(i).getName().equals(items.get(i).getName()))
                validate = false;
        }

        assertTrue(validate);

    }

    @Test
    void sortBySerialNumber_Test(){
        inventoryRefresh();

        boolean validate = true;

        //add to expected items list in accordance with name
        items.add(dummyData.get(2));
        items.add(dummyData.get(3));
        items.add(dummyData.get(0));
        items.add(dummyData.get(1));

        ObservableList<Item> inventoryItems = inventory.sortBySerialNumber();

        for(int i = 0; i < inventoryItems.size(); i++){
            if(!inventoryItems.get(i).getSerialNumber().equals(items.get(i).getSerialNumber()))
                validate = false;
        }

        assertTrue(validate);
    }

    @Test
    void sortByValue_Test(){
        inventoryRefresh();

        boolean validate = true;

        //add to expected items list in accordance with name
        items.add(dummyData.get(2));
        items.add(dummyData.get(3));
        items.add(dummyData.get(0));
        items.add(dummyData.get(1));

        ObservableList<Item> inventoryItems = inventory.sortByValue();

        for(int i = 0; i < inventoryItems.size(); i++){
            if(!inventoryItems.get(i).getValue().equals(items.get(i).getValue()))
                validate = false;
        }

        assertTrue(validate);

    }

}