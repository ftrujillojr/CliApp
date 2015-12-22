package org.nve.cliapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * SVN information
 * $Revision:$
 * $Author:$
 * $Date:$
 * $HeadURL:$
 *
 */
public class SysUtils {

    private static Map<String, String> env = System.getenv();

    public SysUtils() {
    }

    /**
     * Returns the env value for a given key.
     *
     * If none, then returns empty String.
     *
     * @param key //
     * @return String //
     */
    public static String getEnv(String key) {
        String val = "";
        if (env.containsKey(key)) {
            val = env.get(key);
        }
        return (val);
    }

    public static List<String> ffind(Path start, String matchString) throws IOException {
        ArrayList<String> results = new ArrayList<>();

        int maxDepth = 5;
        try (Stream<Path> stream = Files.walk(start, maxDepth)) {
            String joined = stream
                    .map(String::valueOf)
//                    .filter(path -> path.endsWith(".js"))
                    .sorted()
                    .collect(Collectors.joining("\n"));
            System.out.println("walk(): " + joined);
        }
        

        return (results);
    }

    /**
     * Display a List&lt;T&gt; to stdout.
     *
     * @param <T>
     * @param list
     */
    public static <T> void displayList(List<T> list) {
        Iterator<T> itr = list.iterator();
        while (itr.hasNext()) {
            T t = itr.next();
            System.out.println(t.toString());
        }
    }

    /**
     * Remove file if it exists.
     *
     * @param filename
     */
    public static void rmFile(String filename) {
        File fileObj = new File(filename);

        if (fileObj.exists() && fileObj.isFile()) {
            fileObj.delete();
        }
    }

    /**
     * Uses System property os.name to determine if running on Linux.
     *
     * @return true/false
     */
    public static Boolean isLinux() {
        Boolean isLinux = false;
        String osName = System.getProperty("os.name");

        if (RegExp.isMatch(osName.toUpperCase(), "LINUX")) {
            isLinux = true;
        }
        return isLinux;
    }

    /**
     * Uses System property os.name to determine if running on Windows.
     *
     * @return true/false
     */
    public static Boolean isWindows() {
        Boolean isWindows = false;
        String osName = System.getProperty("os.name");

        if (RegExp.isMatch(osName.toUpperCase(), "WINDOWS")) {
            isWindows = true;
        }
        return isWindows;
    }

    /**
     * Get a BufferedReader instance for the following encoding types.
     *
     * ISO-8859-1 US-ASCII UTF-16 UTF-16BE UTF-16LE UTF-8
     *
     * @param filename
     * @param encoding
     * @return
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static BufferedReader getBufferedReaderInstance(String filename, String encoding) throws FileNotFoundException, UnsupportedEncodingException {
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, encoding);
        return (new BufferedReader(isr));
    }

    /**
     * Get a BufferedWriter instance for the following encoding types. This
     * method can either APPEND or OVERWRITE file depending on 3rd param.
     *
     * ISO-8859-1 US-ASCII UTF-16 UTF-16BE UTF-16LE UTF-8
     *
     * @param filename
     * @param encoding
     * @param appendToFile
     * @return
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static BufferedWriter getBufferedWriterInstance(String filename, String encoding, boolean appendToFile) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        File file = new File(filename);
        FileOutputStream fos = new FileOutputStream(file, appendToFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos, encoding);
        return (new BufferedWriter(osw));
    }

    /**
     * Open and read a text file using specified encoding, and return the lines
     * in the file as a list of Strings.
     *
     * encoding can be => ISO-8859-1 US-ASCII UTF-16 UTF-16BE UTF-16LE UTF-8
     *
     * @param filename
     * @param encoding
     * @return List&lt;String&gt;
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     * @throws java.io.IOException
     */
    public static List<String> readTextFile(String filename, String encoding) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        List<String> records = new ArrayList<>();
        String line;

        try (BufferedReader reader = SysUtils.getBufferedReaderInstance(filename, encoding)) {
            while ((line = reader.readLine()) != null) {
                records.add(line);
            }
        } catch (Exception ex) {
            throw ex;
        }

        return records;
    }

    /**
     * Write List of Strings to a UTF8 encoded text file.
     *
     * encoding can be => ISO-8859-1 US-ASCII UTF-16 UTF-16BE UTF-16LE UTF-8
     *
     * @param filename
     * @param listStrings
     * @param encoding
     * @param appendToFile
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     * @throws java.io.IOException
     */
    public static void writeTextFileUTF8(String filename, List<String> listStrings, String encoding, boolean appendToFile) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        // Try with resources will close the Buffered Writer in all events.  Java 1.7+
        try (BufferedWriter writer = new BufferedWriter(getBufferedWriterInstance(filename, encoding, appendToFile))) {
            for (int ii = 0; ii < listStrings.size(); ii++) {
                writer.write(listStrings.get(ii));
                writer.newLine();
            }
            writer.flush();
        } catch (Exception ex) {
            throw ex;
        }
    }

}
