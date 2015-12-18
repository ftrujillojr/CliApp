package org.nve.cliapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class JsonUtils {

    public JsonUtils() {
    }
    
    public static <T> String toJsonPretty(T obj) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(obj);
        return (json);
    }
    
    public static <T> String toJsonCompact(T obj) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(obj);
        return (json);
    }

    public static String readJsonFromFile(String fileName) throws JsonUtilsException {
        String jsonStr = null;
        try {
            File file = new File(fileName);

            if (file.exists() == false) {
                String msg = "File does NOT exist! => " + fileName;
                throw new JsonUtilsException(msg);
            }

            System.err.println("Reading JSON file => " + file.getCanonicalPath() + "\n");
            jsonStr = JsonUtils.toCompactFormat(new String(Files.readAllBytes(Paths.get(fileName))));
            
        } catch (IOException ex) {
            String msg = "IOExeption from fileName => " + fileName + " for class => " + JsonUtils.class.getName();
            msg += ex.getMessage();
            throw new JsonUtilsException(msg);
        }
        return (jsonStr);
    }

    
    public static void writeJsonToFile(String jsonStr, String fileName) throws JsonUtilsException {
        // write JSON String to file
        File file = new File(fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()))) {
                bw.write(jsonStr);
            }
            System.err.println("Wrote JSON to file => " + file.getCanonicalPath() + "\n");
        } catch (IOException ex) {
            String msg = "IOExeption from fileName => " + fileName + " for class => " + JsonUtils.class.getName();
            msg += ex.getMessage();
            throw new JsonUtilsException(msg);
        }
    }

    public static String toPrettyFormat(String jsonString) throws JsonUtilsException {
        String prettyJson = "";

        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gsonBuilder.serializeNulls();
            Gson gson = gsonBuilder.create();

            JsonParser parser = new JsonParser();
            JsonElement rootJsonElement = parser.parse(jsonString);

            if (rootJsonElement.isJsonObject()) {
                // java.lang.reflect.Type
                Type type = new TypeToken<JsonObject>() {
                }.getType();
                JsonObject jsonObject = gson.fromJson(jsonString, type);
                prettyJson = gson.toJson(jsonObject);
            } else {
                // java.lang.reflect.Type
                Type type = new TypeToken<ArrayList<JsonObject>>() {
                }.getType();
                ArrayList<JsonObject> jsonListObjects = gson.fromJson(jsonString, type);
                prettyJson = gson.toJson(jsonListObjects);
            }
        } catch (JsonSyntaxException ex) {
            String msg = "ERROR: Invalid JSON.\n\n";
            msg += jsonString;
            throw new JsonUtilsException(msg);
        }

        return prettyJson;
    }

    public static String toCompactFormat(String jsonString) throws JsonUtilsException {
        String compactJson;

        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls();
            Gson gson = gsonBuilder.create();

            JsonParser parser = new JsonParser();
            JsonElement rootJsonElement = parser.parse(jsonString);

            if (rootJsonElement.isJsonObject()) {
                // java.lang.reflect.Type
                Type type = new TypeToken<JsonObject>() {
                }.getType();
                JsonObject jsonObject = gson.fromJson(jsonString, type);
                compactJson = gson.toJson(jsonObject);
            } else {
                // java.lang.reflect.Type
                Type type = new TypeToken<ArrayList<JsonObject>>() {
                }.getType();
                ArrayList<JsonObject> jsonListObjects = gson.fromJson(jsonString, type);
                compactJson = gson.toJson(jsonListObjects);
            }
        } catch (JsonSyntaxException ex) {
            String msg = "ERROR: Invalid JSON.\n\n";
            msg += jsonString;
            throw new JsonUtilsException(msg);
        }
        return compactJson;
    }
}
