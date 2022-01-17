package com.company;

import org.json.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;
import org.apache.commons.io.*;
import com.google.gson.*;

/**
 * @author Jeffrey Tang
 */

public class Main {
    public static void main(String[] args) throws IOException {
        File dataFile = new File("dataFile.json");
        JSONObject database;
        Scanner keyboard = new Scanner(System.in);
        String entryName = "";

        //Checks if the dataJson file already exists.
        //If it does, read from it. If it doesn't make a new one.
        if(dataFile.exists()){
            InputStream jsonStream = new FileInputStream("dataFile.json");
            String jsonText = IOUtils.toString(jsonStream, StandardCharsets.UTF_8);
            database = new JSONObject(jsonText);
        } else{
            database = new JSONObject();
            PrintWriter jsonWriter = new PrintWriter("dataFile.json");
            jsonWriter.write(database.toString());
            jsonWriter.flush();
            jsonWriter.close();
        }

        System.out.println("Command inputs are all in parentheses!");
        //Goto labels like this might be bad practice.
        label:
        while(true){
            System.out.println("Do you want to (c)reate an entry, (e)dit one, (d)elete one, or (v)iew a list of them?");
            System.out.println("Alternatively, you can (s)ave, (exit), or (clear) all databases.");
            String userChoice = keyboard.nextLine();
            userChoice = userChoice.toLowerCase();
            //This could be an if statement.
            //Switch is used for readability.
            switch (userChoice) {
                case "c":
                case "create":
                case "e":
                case "edit":
                    //Create a new entry with an attribute.
                    Map jsonInput = new LinkedHashMap(2);
                    System.out.println("Entry Name:");
                    entryName = keyboard.nextLine();
                    System.out.println("How many attributes will you be modifying? (Default 1)");
                    int attributeNumber;
                    try {
                        //You have to parseInt because nextInt doesn't move the cursor to the next line.
                        //You could also use another nextLine to eat the blank line.
                        attributeNumber = Integer.parseInt(keyboard.nextLine());
                    } catch (Exception e) {
                        attributeNumber = 1;
                    }
                    for (int i = 0; i < attributeNumber; i++) {
                        System.out.println("Attribute Name:");
                        String jsonName = keyboard.nextLine();
                        System.out.println("Attribute Value:");
                        String jsonValue = keyboard.nextLine();
                        jsonInput.put(jsonName, jsonValue);
                        try{
                            if(database.getJSONObject(entryName).isEmpty()){
                                database.put(entryName, jsonInput);
                            } else{
                                database.getJSONObject(entryName).put(jsonName, jsonValue);
                            }
                        } catch (Exception e){
                            database.put(entryName, jsonInput);
                        }
                    }
                    System.out.println("New Entry created: " + entryName);
                    break;
                case "d":
                case "delete":
                    //Check if the user wants to delete just an entry or a value of an entry.
                    //They use different commands, so you need an if statement.
                    System.out.println("Are you deleting an (e)ntry or just a (v)alue?");
                    userChoice = keyboard.nextLine();
                    userChoice = userChoice.toLowerCase();
                    if (userChoice.equals("e") || userChoice.equals("entry")) {
                        System.out.println("Entry Name:");
                        entryName = keyboard.nextLine();
                        database.remove(entryName);
                    } else if (userChoice.equals("v") || userChoice.equals("value")) {
                        System.out.println("Entry Name:");
                        entryName = keyboard.nextLine();
                        System.out.println("Attribute Name:");
                        String jsonName = keyboard.nextLine();
                        database.getJSONObject(entryName).remove(jsonName);
                    }
                    System.out.println(entryName + " Deleted!");
                    break;
                case "v":
                case "view":
                    //Printing the Json database nicely using Gson.
                    Gson prettyJson = new GsonBuilder().setPrettyPrinting().create();
                    InputStream jsonStream = new FileInputStream("dataFile.json");
                    JsonElement prettyElement = JsonParser.parseString(IOUtils.toString(jsonStream, StandardCharsets.UTF_8));
                    String prettyJsonString = prettyJson.toJson(prettyElement);
                    System.out.println(prettyJsonString);
                    break;
                case "s":
                case "save": {
                    //Finish creating and saving the dataFile.
                    PrintWriter jsonWriter = new PrintWriter("dataFile.json");
                    jsonWriter.write(database.toString());
                    jsonWriter.flush();
                    jsonWriter.close();
                    System.out.println("Saved!");
                    break;
                }
                case "exit":
                    //Uses break to leave the infinite while loop.
                    //This command and the "clear" command don't use letters
                    // to prevent users from doing big things by accident.
                    System.out.println("Exiting...");
                    break label;
                case "clear": {
                    dataFile.delete();
                    database = new JSONObject();
                    PrintWriter jsonWriter = new PrintWriter("dataFile.json");
                    jsonWriter.write(database.toString());
                    jsonWriter.flush();
                    jsonWriter.close();
                    break;
                }
                default:
                    System.out.println("Unknown input, please try again.");
                    System.out.println("Command inputs are all in parentheses!");
                    break;
            }
        }
    }
}