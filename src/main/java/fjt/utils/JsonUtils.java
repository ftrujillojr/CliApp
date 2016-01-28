package fjt.utils;

import fjt.utils.SysUtils;
import fjt.exceptions.JsonUtilsException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class JsonUtils {

    private static boolean verbose = false;

    public JsonUtils() {
    }

    /**
     * Set this if you want to see more info.
     *
     * @param verbose boolean
     */
    public static void setVerbose(boolean verbose) {
        JsonUtils.verbose = verbose;
    }

    /**
     * Convert a simple Object to JSON in human readable form
     *
     * @param <T> Object type
     * @param obj object instance
     * @return String
     */
    public static <T> String objectToJsonPretty(T obj) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(obj);
        return (json);
    }

    /**
     * Do not serialize NULL fields.
     * 
     * @param <T> Object type
     * @param obj object instance
     * @return  JSON String
     */
    public static <T> String objectToJsonPrettyNoNulls(T obj) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(obj);
        return (json);
    }

    /**
     * Convert a simple Object to JSON in compact form.
     *
     * @param <T> Generic type
     * @param obj object instance
     * @return String
     */
    public static <T> String objectToJsonCompact(T obj) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(obj);
        return (json);
    }

    /**
     * Do not serialize NULL fields.
     * 
     * @param <T> Object type
     * @param obj object instance
     * @return  JSON String
     */
    public static <T> String objectToJsonCompactNoNulls(T obj) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(obj);
        return (json);
    }

    /**
     * Quick check if JSON string is valid.
     *
     * @param jsonString A valid JSON string
     * @return boolean
     */
    public static boolean isValidJson(String jsonString) {
        try {
            new JsonParser().parse(jsonString);
            return true;
        } catch (JsonSyntaxException jse) {
            return false;
        }
    }

    /**
     * This method will read JSON from a file, removing \r\n if on Windows and
     * replacing with \n only.
     *
     * @param fileName The file should only contain ONE json object or array of objects.
     * @return String
     * @throws JsonUtilsException  Captures IO Exception or FileNotExistException.
     */
    public static String readJsonFromFile(String fileName) throws JsonUtilsException {
        String jsonStr = null;
        try {
            File file = new File(fileName);

            if (file.exists() == false) {
                String msg = "File does NOT exist! => " + fileName;
                throw new JsonUtilsException(msg);
            }

            if (verbose) {
                System.out.println("Reading JSON file => " + file.getCanonicalPath() + "\n");
            }
            String line;
            StringBuilder sb = new StringBuilder();
            // BufferedReader removes \r\n and \n with readLine().
            try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()))) {
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n"); // here I replace the newline with Unix style.
                }
                jsonStr = sb.toString();  // set the return string
                jsonStr = jsonStr.replaceAll("[ \t\n]+$", "");  // remove whitespace at end always.
            }
        } catch (IOException ex) {
            String msg = "IOExeption from fileName => " + fileName + " for class => " + JsonUtils.class.getName();
            msg += ex.getMessage();
            throw new JsonUtilsException(msg);
        }
        return (jsonStr);
    }

    /**
     * Write JSON string verbatim to a file.
     *
     * @param jsonStr A valid JSON string
     * @param fileName Filename to write to
     * @throws JsonUtilsException Captures IO Exception or FileNotExistException.
     */
    public static void writeJsonToFile(String jsonStr, String fileName) throws JsonUtilsException {
        // Ensure the directory is created for fileName.
        String dirname = SysUtils.getDirName(fileName);
        SysUtils.mkdir_p(dirname);        
        
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

            if (verbose) {
                System.out.println("Wrote JSON to file => " + file.getCanonicalPath() + "\n");
            }
        } catch (IOException ex) {
            String msg = "IOExeption from fileName => " + fileName + " for class => " + JsonUtils.class.getName();
            msg += ex.getMessage();
            throw new JsonUtilsException(msg);
        }
    }

    /**
     * Convert a JSON string to human readable format.
     *
     * @param jsonString A valid JSON string
     * @return String
     * @throws JsonUtilsException  Captures JsonSyntaxException
     */
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

    /**
     * Convert JSON string by removing all newlines and white space not quoted.
     *
     * @param jsonString A valid JSON string
     * @return String
     * @throws JsonUtilsException Captures JsonSyntaxException
     */
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
