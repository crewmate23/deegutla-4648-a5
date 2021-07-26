/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Sathwika Deegutla
 */
package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    private FileManager fileManager = new FileManager();

    private Inventory inventory = new Inventory();
    private ObservableList<Item> items = FXCollections.observableArrayList();

    private List<Item> dummyData = new ArrayList<>();

    public void addDummyData(){
        dummyData.add(new Item("MacBook Air", "983AR93JMB", new BigDecimal(1199.00)));
        dummyData.add(new Item("iPad Air", "342AR894IP", new BigDecimal(699.00)));
    }

    public void inventoryRefresh(){
        inventory.getItems().removeAll();

        addDummyData();
        inventory.addItem(dummyData.get(0));
        inventory.addItem(dummyData.get(1));
    }

    @Test
    void save_TSV_Test() {
        //first initialize
        inventoryRefresh();

        //hardcode location
        File file = new File("/Users/sathwika/Desktop/Inventory_Deegutla/Testing.txt");
        String fileType = ".txt";

        fileManager.save(file, fileType, inventory);

        String actual ="";

        try{
            //read the file
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = "";
            while((line = br.readLine()) != null){
                actual += line + "\n"; //add the file values into the actual string
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create an expected string with dummy item's value
        String expected = """
                Name\tSerial Number\tValue\t
                MacBook Air\t983AR93JMB\t1199\t
                iPad Air\t342AR894IP\t699\t
                """;


        assertEquals(expected , actual);
    }

    @Test
    void save_HTML_Test() {
        //first initialize
        inventoryRefresh();

        //hardcode location
        File file = new File("/Users/sathwika/Desktop/Inventory_Deegutla/Testing.html");
        String fileType = ".html";

        fileManager.save(file, fileType, inventory);

        String actual ="";

        try{
            //read the file
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = "";
            while((line = br.readLine()) != null){
                actual += line + "\n"; //add the file values into the actual string
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create an expected string with dummy item's value
        String expected = """
                <!DOCTYPE html>
                <html>
                <body>
                <table>
                <tr>
                <th>Name</th>
                <th>Serial Number</th>
                <th>Value</th>
                </tr>
                <tr>
                <td>MacBook Air</td>
                <td>983AR93JMB</td>
                <td>1199</td>
                </tr>
                <tr>
                <td>iPad Air</td>
                <td>342AR894IP</td>
                <td>699</td>
                </tr>
                </table>
                </body>
                </html>
                """;

        assertEquals(expected , actual);
    }

    @Test
    void save_JSON_Test(){
        //first initialize
        inventoryRefresh();

        //hardcode location
        File file = new File("/Users/sathwika/Desktop/Inventory_Deegutla/Testing.json");
        String fileType = ".json";

        fileManager.save(file, fileType, inventory);

        String actual ="";

        try{
            //read the file
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = "";
            while((line = br.readLine()) != null){
                actual += line + "\n"; //add the file values into the actual string
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create an expected string with dummy item's value
        String expected = """
                {"inventory":[{"name":"MacBook Air","serial number":"983AR93JMB","value":"1199"},{"name":"iPad Air","serial number":"342AR894IP","value":"699"}]}
                """;

        assertEquals(expected , actual);
    }

    @Test
    void load_TSV_Test() {
        inventoryRefresh();

        //hardcode location
        File file = new File("/Users/sathwika/Desktop/Inventory_Deegutla/Testing.txt");
        String fileType = ".txt";

        fileManager.save(file, fileType, inventory);

        ObservableList<Item> loadItems = fileManager.load(file, fileType);

        boolean validate = true;
        for(int i = 0; i < inventory.getItems().size(); i++){
            if(!loadItems.get(i).getName().equals(inventory.getItems().get(i).getName()))
                validate = false;
        }

        assertTrue(validate);
    }

    @Test
    void load_HTML_Test() {
        inventoryRefresh();

        //hardcode location
        File file = new File("/Users/sathwika/Desktop/Inventory_Deegutla/Testing.html");
        String fileType = ".html";

        fileManager.save(file, fileType, inventory);

        ObservableList<Item> loadItems = fileManager.load(file, fileType);

        boolean validate = true;
        for(int i = 0; i < inventory.getItems().size(); i++){
            if(!loadItems.get(i).getName().equals(inventory.getItems().get(i).getName()))
                validate = false;
        }

        assertTrue(validate);
    }

    @Test
    void load_JSON_Test() {
        inventoryRefresh();

        //hardcode location
        File file = new File("/Users/sathwika/Desktop/Inventory_Deegutla/Testing.json");
        String fileType = ".json";

        fileManager.save(file, fileType, inventory);

        ObservableList<Item> loadItems = fileManager.load(file, fileType);

        boolean validate = true;
        for(int i = 0; i < inventory.getItems().size(); i++){
            if(!loadItems.get(i).getName().equals(inventory.getItems().get(i).getName()))
                validate = false;
        }

        assertTrue(validate);
    }
}