/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Sathwika Deegutla
 */
package ucf.assignments;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.math.BigDecimal;

public class FileManager {

    //store file and inventory globally
    File file;
    Inventory inventory;

    //save method
    public void save(File file, String fileType, Inventory inventory){
        //get file, file type, and inventory
        this.file = file;
        this.inventory = inventory;

        //check the file type and call appropriate saveAsFileType
        if(fileType.equals(".txt")){
            saveAsTSV();
        }else if(fileType.equals(".html")){
            saveAsHTML();
        }else if(fileType.equals(".json")){
            saveAsJSON();
        }
    }

    //save as .txt with tab-separated values
    private void saveAsTSV(){
        //use bufferedwriter to write
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            //use "\t" to separate values
            bw.write("Name\tSerial Number\tValue\t\n");

            //loop through inventory items and write them
            for(Item item: inventory.getItems()){
                bw.write(item.getName() + "\t");
                bw.write(item.getSerialNumber() + "\t");
                bw.write(item.getValue() + "\t\n");
            }

            //close the writer
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //save as .html with tabular format
    private void saveAsHTML(){
        try {
            //use buffered writer
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            //write the heading and tags for html
            bw.write("<!DOCTYPE html>\n");
            bw.write("<html>\n");
            bw.write("<body>\n");
            //tabular format
            bw.write("<table>\n");
            bw.write("<tr>\n");
            //headers for the table
            bw.write("<th>Name</th>\n");
            bw.write("<th>Serial Number</th>\n");
            bw.write("<th>Value</th>\n");
            bw.write("</tr>\n");

            //loop through inventory items and add using html tags
            for(Item item: inventory.getItems()){
                bw.write("<tr>\n");
                bw.write("<td>" + item.getName() + "</td>\n");
                bw.write("<td>" + item.getSerialNumber() + "</td>\n");
                bw.write("<td>" + item.getValue() + "</td>\n");
                bw.write("</tr>\n");
            }

            //close the table tags and other tags
            bw.write("</table>\n");
            bw.write("</body>\n");
            bw.write("</html>\n");

            //close the writer
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //save as .json with objects format
    private void saveAsJSON(){

        //use gson's jsonwriter
        try {
            JsonWriter writer = new JsonWriter(new FileWriter(file));

            //data structure - array of inventory items with each item object
            writer.beginObject().name("inventory").beginArray();

            //loop through inventory items and add as object
            for(Item item : inventory.getItems()){
                writer.beginObject().name("name").value(item.getName());
                writer.name("serial number").value(item.getSerialNumber());
                writer.name("value").value(String.valueOf(item.getValue())).endObject();
            }

            //end the array and object
            writer.endArray().endObject().flush();

            //close the writer
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //load method
    public ObservableList<Item> load(File file, String fileType){
        //get file and file type parameters
        this.file = file;

        //create new inventory object
        this.inventory = new Inventory();

        //check file type and load values accordingly
        if(fileType.equals(".txt")){
            return loadTSV();
        }else if(fileType.equals(".html")){
            return loadHTML();
        }else if(fileType.equals(".json")){
            return loadJSON();
        }

        //if not valid return type, return null
        return null;
    }

    //load tab-separated values from .txt file
    private ObservableList<Item> loadTSV(){
        //create new observable list to add data from file
        ObservableList<Item> fileItems = FXCollections.observableArrayList();

        try {
            //use a bufferedreader for reading the .txt file
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = "";
            line = br.readLine(); //ignores the headings
            while((line = br.readLine()) != null){
                String[] values = line.split("\t"); //to separate values

                //add into fileItems
                fileItems.add(new Item(values[0], values[1], new BigDecimal(values[2])));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //return the observable fileItems
        return fileItems;

    }

    //load html tags and values from .html file
    private ObservableList<Item> loadHTML(){
        //create new observable list to add data from file
        ObservableList<Item> fileItems = FXCollections.observableArrayList();

        try {
            //use a bufferedreader for reading the .html file
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = ""; //read each line
            while((line = br.readLine()) != null){
                if(line.startsWith("<td>")) { //if line starts with <td>, column in a row in a table
                    //use substring to divide values between the html tags
                    String name = line.substring(4, line.indexOf("</td>"));
                    line = br.readLine(); //read next data
                    String serialNumber = line.substring(4, line.indexOf("</td>"));
                    line = br.readLine(); //read next data
                    BigDecimal value = new BigDecimal(line.substring(4, line.indexOf("</td>")));

                    //add into fileItems
                    fileItems.add(new Item(name, serialNumber, value));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //return the observable fileItems
        return fileItems;
    }

    //load json objects and values from .json file
    private ObservableList<Item> loadJSON(){
        //create new observable list to add data from file
        ObservableList<Item> fileItems = FXCollections.observableArrayList();

        try {
            //use jsonelement and jsonobject for parse reading
            JsonElement readElement = JsonParser.parseReader(new FileReader(file));
            JsonObject readObject = readElement.getAsJsonObject();

            JsonArray jsonInventory = readObject.get("inventory").getAsJsonArray();

            //loop through every element and parse read
            for(JsonElement itemElement : jsonInventory){
                JsonObject itemJsonObject = itemElement.getAsJsonObject();

                String name = itemJsonObject.get("name").getAsString();
                String serialNumber = itemJsonObject.get("serial number").getAsString();
                BigDecimal value = itemJsonObject.get("value").getAsBigDecimal();

                //add the data from parse reading to observable list
                fileItems.add(new Item(name, serialNumber, value));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //return the observable fileItems
        return fileItems;
    }
}
