package org.nve.cliapp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import static java.lang.Integer.MAX_VALUE;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Just a few methods to help with System level stuff by utilizing standard Java
 * 1.8 out of the box.
 *
 * https://docs.oracle.com/javase/8/docs/api/
 *
 *
 * https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html
 *
 */
@SuppressWarnings("FieldMayBeFinal")
public final class SysUtils {

    public enum Perms {

        READ(4), WRITE(2), EXECUTE(1);

        private final int value;

        private Perms(final int v) {
            value = v;
        }

        public int getValue() {
            return value;
        }
    }

    private static Map<String, String> env = System.getenv();
    private static String fileNameRegExp = ".*";
    private static Map<String, BasicFileAttributes> fileMap = new LinkedHashMap<>();
    private static Map<String, BasicFileAttributes> dirMap = new LinkedHashMap<>();
    private static int lastSystemStatus = 0;
    private static PrintStream savedStdout = System.out;
    private static PrintStream savedStderr = System.err;

    public SysUtils() {
    }

    public static String getTmpDir() {
        if (SysUtils.isLinux()) {
            return ("/tmp");
        } else {
            return ("/tmp");
        }
    }

    /**
     * Returns the env value for a given key.
     *
     * If none, then returns empty String.
     *
     * @param key Env index
     * @return String
     */
    public static String getEnv(String key) {
        String val = "";
        if (env.containsKey(key)) {
            val = env.get(key);
        }
        return (val);
    }

    /**
     * Return a Set of SysUtils.Perms for a given file or dir name string.
     *
     * @param fileOrDirName Just a filename or dirname.
     * @return Set of SysUtils.Perms
     */
    public static Set<Perms> getPerms(String fileOrDirName) {
        Set<Perms> perms = EnumSet.noneOf(Perms.class);

        File f = new File(fileOrDirName);
        if (f.exists()) {
            if (f.canWrite()) {
                perms.add(Perms.WRITE);
            }
            if (f.canRead()) {
                perms.add(Perms.READ);
            }
            if (f.canExecute()) {
                perms.add(Perms.EXECUTE);
            }
        }
        return perms;
    }

    /**
     * Input => /home/ftrujillo/filename.txt Output => filename.ext
     *
     * @param fullPath See input
     * @return See output
     */
    public static String getBaseName(String fullPath) {
        File fileObj = new File(fullPath);
        String baseName = fileObj.getName(); // Java has built-in functions to get the basename and dirname for a given file path, but the function names are not so self-apparent
        return (baseName);
    }

    /**
     * Input => /home/ftrujillo/filename.txt Output => /home/ftrujillo
     *
     * @param fullPath see input
     * @return see output
     */
    public static String getDirName(String fullPath) {
        File fileObj = new File(fullPath);
        String dirName = fileObj.getParent(); // Java has built-in functions to get the basename and dirname for a given file path, but the function names are not so self-apparent
        return (dirName);
    }

    /**
     * Remove file if it exists.
     *
     * @param filename A string
     */
    public static void rmFile(String filename) {
        File fileObj = new File(filename);

        if (fileObj.exists() && fileObj.isFile()) {
            fileObj.delete();
        }
    }

    /**
     * Similar to Linux /bin/mkdir -p. It will create recursively all
     * directories needed to the new dirname
     *
     * @param dirname //
     * @return boolean //
     */
    public static boolean mkdir_p(String dirname) {
        boolean status = false;

        File fileObj = new File(dirname);
        if (fileObj.exists() == false) {
            status = fileObj.mkdirs();
        }

        return (status);
    }

    public static List<String> system(String command) throws SysUtilsException {
        String[] linuxCmd = {"/bin/csh", "-c", command};

        ArrayList<String> results = new ArrayList<>();
        String line;
        try {
            Process proc;

            if (SysUtils.isLinux()) {
                proc = Runtime.getRuntime().exec(linuxCmd);
            } else {
                proc = Runtime.getRuntime().exec(command, null, null);
            }

            try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                while ((line = stdInput.readLine()) != null) {
                    results.add(line);
                }
            }

            try (BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
                while ((line = stdError.readLine()) != null) {
                    results.add(line);
                }
            }

            proc.waitFor();  // waits until process terminates.
            int retStatus = proc.exitValue();
            lastSystemStatus = retStatus;

        } catch (IOException | InterruptedException ex) {
            String msg = "ERROR: tried to run system() on => " + command;
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        }
        return results;
    }

    public static void redirectStdout(String filename, Boolean append) {
        if (filename.isEmpty() == false) {
            try {
                System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(filename, append)), true));
            } catch (FileNotFoundException ex) {
                System.err.println("WARNING: Unable to redirectStdout to filename => " + filename);
                System.setOut(savedStdout);
            }
        }
    }

    public static void restoreStdout() {
        if (System.out != savedStdout) {
            // We were writing to a file.
            System.out.flush();
            System.out.close();
        }
        System.setOut(savedStdout);
    }

    public static String formatDoubleToString(Double value) {
        DecimalFormat df = new DecimalFormat("#.0000");
        String result = df.format(value);
        return result;
    }

    public static String getEpochAsString() {
        return formatDoubleToString(SysUtils.getEpochAsDouble());
    }

    public static Double getEpochAsDouble() {
        Date mydate = new Date();
        long epoch = mydate.getTime(); // returns milliseconds, need to / 1000
        Double result = (epoch / 1000.0);
        return result;
    }

    public static String epochToAsciiDate(String strEpoch) {
        Double dbEpoch = Double.parseDouble(strEpoch);
        Date myDate = new Date((dbEpoch.longValue() * 1000));
        return myDate.toString();
    }

    public static String epochToAsciiDate(double epoch) {
        @SuppressWarnings("UnnecessaryBoxing")
        Double dbEpoch = new Double(epoch);
        Date myDate = new Date((dbEpoch.longValue() * 1000));
        return myDate.toString();
    }

    public static Double atof(String mydouble) {
        Double result = Double.parseDouble(mydouble);
        return result;
    }

    public static Integer atoi(String value) {
        Integer result = Integer.parseInt(value);
        return (result);
    }

    public String itoa(int value) {
        String result = Integer.toString(value);
        return (result);
    }

    public static Path getCwd() {
        String workingDir = System.getProperty("user.dir");
        Path path = Paths.get(workingDir);
        return (path);
    }

    public Path getHome() {
        String home;

        if (SysUtils.isWindows()) {
            if (SysUtils.env.containsKey("USERPROFILE")) {
                Path tmpPath = Paths.get(SysUtils.getEnv("USERPROFILE"), "Documents");
                home = tmpPath.toString();
            } else if (SysUtils.env.containsKey("HOME")) {
                home = SysUtils.getEnv("HOME");
            } else if (SysUtils.env.containsKey("HOMEDRIVE") && SysUtils.env.containsKey("HOMEPATH")) {
                home = String.format("%s%s", SysUtils.getEnv("HOMEDRIVE"), SysUtils.getEnv("HOMEPATH"));
            } else {
                home = "C:\\TEMP";
            }
        } else if (SysUtils.isLinux()) {
            home = SysUtils.getEnv("HOME");
        } else {
            home = System.getProperty("user.home");  // This is where your application starts up.
        }

        Path path = Paths.get(home);
        return (path);
    }

    
    /**
     * http://www.javaworld.com/article/2928805/core-java/nio-2-cookbook-part-3.html
     *
     * @return FileVisitor&lt&Path&gt;
     */
    private static FileVisitor<Path> getFileVisitor() {
        class DirVisitor<Path> extends SimpleFileVisitor<Path> {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                // Do not walk down repository or NetApp snapshots.
                // [/\\\\]  matches BOTH Unix and Windows separators.
                if (RegExp.isMatch(".*[/\\\\].git$|.*[/\\\\].svn$|.*[/\\\\]CVS$|.*[/\\\\].snapshot$|.*[/\\\\].ssh$|.*[/\\\\].m2$|.*[/\\\\]CPAN$|.*[/\\\\].hg|.*[/\\\\]SCCS$$", dir.toString())) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
//                System.out.format("%s [Directory]%n", dir);
                SysUtils.dirMap.put(dir.toString(), attrs);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
//                  System.out.format("%s [File,  Size: %s  bytes]%n", file, attrs.size());
                String basename = getBaseName(file.toString());
                if (RegExp.isMatch(fileNameRegExp, basename)) {
                    SysUtils.fileMap.put(file.toString(), attrs);
                }
                return FileVisitResult.CONTINUE;
            }

            // http://www.technojeeves.com/index.php/9-freebies/82-accessdeniedexception-with-filevisitor-in-java
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException e)
                    throws IOException {
                System.err.printf("WARNING: Directory or file access is restricted for => %s\n", file);
                return FileVisitResult.SKIP_SUBTREE;
            }
        }
        FileVisitor<Path> visitor = new DirVisitor<>();
        return visitor;
    }

    public static Map<String, BasicFileAttributes> ffind(Path startPath, String fileRegEx) throws IOException {
        SysUtils.dirMap.clear();
        SysUtils.fileMap.clear();
        Set<FileVisitOption> fileVisitOptions = new TreeSet<>();
        fileVisitOptions.add(FileVisitOption.FOLLOW_LINKS); // this is the only option right now. Might be more someday.
        int maxDepth = MAX_VALUE;  // MAX_VALUE is all directories,  0 is top only.
        FileVisitor<Path> visitor = getFileVisitor();

        if (fileRegEx != null && !fileRegEx.isEmpty()) {
            SysUtils.fileNameRegExp = fileRegEx;
        }

        Path walkFileTree = Files.walkFileTree(
                startPath,
                fileVisitOptions,
                maxDepth,
                visitor);

        return (SysUtils.fileMap);
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

    public static void displayFileMap(Map<String, BasicFileAttributes> map) {
        Iterator<String> itr = map.keySet().iterator();
        while (itr.hasNext()) {
            String filename = itr.next();
            System.out.println(filename);
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
     * Write List of Strings to an encoded text file.
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
    public static void writeTextFile(String filename, List<String> listStrings, String encoding, boolean appendToFile) throws FileNotFoundException, UnsupportedEncodingException, IOException {
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
