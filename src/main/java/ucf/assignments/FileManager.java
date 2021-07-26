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

    File file;
    Inventory inventory;

    public void save(File file, String fileType, Inventory inventory){
        this.file = file;
        this.inventory = inventory;

        if(fileType.equals(".txt")){
            saveAsTSV();
        }else if(fileType.equals(".html")){
            saveAsHTML();
        }else if(fileType.equals(".json")){
            saveAsJSON();
        }
    }

    private void saveAsTSV(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            bw.write("Name\tSerial Number\tValue\t\n");

            for(Item item: inventory.getItems()){
                bw.write(item.getName() + "\t");
                bw.write(item.getSerialNumber() + "\t");
                bw.write(item.getValue() + "\t\n");
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveAsHTML(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));


            bw.write("<!DOCTYPE html>\n");
            bw.write("<html>\n");
            bw.write("<body>\n");
            bw.write("<table>\n");
            bw.write("<tr>\n");
            bw.write("<th>Name</th>\n");
            bw.write("<th>Serial Number</th>\n");
            bw.write("<th>Value</th>\n");
            bw.write("</tr>\n");

            for(Item item: inventory.getItems()){
                bw.write("<tr>\n");
                bw.write("<td>" + item.getName() + "</td>\n");
                bw.write("<td>" + item.getSerialNumber() + "</td>\n");
                bw.write("<td>" + item.getValue() + "</td>\n");
                bw.write("</tr>\n");
            }

            bw.write("</table>\n");
            bw.write("</body>\n");
            bw.write("</html>\n");
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveAsJSON(){

        Gson gson = new Gson();

        try {
            JsonWriter writer = new JsonWriter(new FileWriter(file));

            writer.beginObject().name("inventory").beginArray();

            for(Item item : inventory.getItems()){
                writer.beginObject().name("name").value(item.getName());
                writer.name("serial number").value(item.getSerialNumber());
                writer.name("value").value(String.valueOf(item.getValue())).endObject();
            }

            writer.endArray().endObject().flush();

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ObservableList<Item> load(File file, String fileType){
        this.file = file;
        this.inventory = new Inventory();

        if(fileType.equals(".txt")){
            return loadTSV();
        }else if(fileType.equals(".html")){
            return loadHTML();
        }else if(fileType.equals(".json")){
            return loadJSON();
        }

        return null;
    }

    private ObservableList<Item> loadTSV(){
        ObservableList<Item> fileItems = FXCollections.observableArrayList();

        try {
            //use a bufferedreader for reading the csv file
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = "";
            line = br.readLine(); //ignores the headings
            while((line = br.readLine()) != null){
                String[] values = line.split("\t"); //to separate values

                System.out.println(values); //console message


                //add into fileItems
                fileItems.add(new Item(values[0], values[1], new BigDecimal(values[2])));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileItems;

    }

    private ObservableList<Item> loadHTML(){
        ObservableList<Item> fileItems = FXCollections.observableArrayList();

        try {
            //use a bufferedreader for reading the csv file
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = "";
            while((line = br.readLine()) != null){
                if(line.startsWith("<td>")) {
                    String name = line.substring(4, line.indexOf("</td>"));
                    System.out.println(name);
                    line = br.readLine();
                    String serialNumber = line.substring(4, line.indexOf("</td>"));
                    System.out.println(serialNumber);
                    line = br.readLine();
                    BigDecimal value = new BigDecimal(line.substring(4, line.indexOf("</td>")));
                    System.out.println(value);

                    //add into fileItems
                    fileItems.add(new Item(name, serialNumber, value));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileItems;
    }

    private ObservableList<Item> loadJSON(){
        ObservableList<Item> fileItems = FXCollections.observableArrayList();

        try {
            JsonElement readElement = JsonParser.parseReader(new FileReader(file));
            JsonObject readObject = readElement.getAsJsonObject();

            JsonArray jsonInventory = readObject.get("inventory").getAsJsonArray();

            for(JsonElement itemElement : jsonInventory){
                JsonObject itemJsonObject = itemElement.getAsJsonObject();

                String name = itemJsonObject.get("name").getAsString();
                String serialNumber = itemJsonObject.get("serial number").getAsString();
                BigDecimal value = itemJsonObject.get("value").getAsBigDecimal();

                fileItems.add(new Item(name, serialNumber, value));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileItems;
    }
}
