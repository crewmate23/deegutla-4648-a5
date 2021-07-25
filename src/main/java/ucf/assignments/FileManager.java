/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Sathwika Deegutla
 */
package ucf.assignments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    File file;
    Inventory inventory;

    public void save(File file, String fileType, Inventory inventory){
        this.file = file;
        this.inventory = inventory;

        if(fileType.equals("txt")){
            saveAsTSV();
        }else if(fileType.equals("html")){
            saveAsHTML();
        }else if(fileType.equals("json")){
            saveAsJSON();
        }
    }

    public void saveAsTSV(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            bw.write("Name\t");
            bw.write("Serial Number\t");
            bw.write("Value\t\n");

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

    public void saveAsHTML(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

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

            bw.write("</table>");
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAsJSON(){

    }
}
