package org.nve.cliapp;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import static java.lang.Integer.MAX_VALUE;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private static PrintStream savedStdout = System.out; // This is what stdout is originally
    private static PrintStream savedStderr = System.err; // This is what stderr is originally
    private static boolean verbose = false;

    private static int binaryBufferSize = 16 * 1024; // use in mulitples of 1024 please.

    public SysUtils() {
    }

    public static void setVerbose(boolean val) {
        SysUtils.verbose = val;
    }

    /**
     * This is for my jUnit tests, but I could it elsewhere. (I did not write a
     * unit test for this method)
     *
     * @return
     */
    public static String getTmpDir() {
        if (SysUtils.isWindows()) {
            SysUtils.mkdir_p("c:\\tmp");
            return ("c:\\tmp");
        } else {
            return ("/tmp");
        }
    }

    /**
     * Returns the env value for a given key.
     *
     * If none, then returns empty String. (I did not write a unit test for this
     * method)
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
     * (I did not write a unit test for this method)
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
     * (I did not write a unit test for this method)
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
     * (I did not write a unit test for this method)
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
     * (I did not write a unit test for this method)
     *
     * @param filename A string
     * @throws org.nve.cliapp.SysUtilsException
     */
    public static void rmFile(String filename) throws SysUtilsException {
        try {
            Files.deleteIfExists(Paths.get(filename));
        } catch (IOException ex) {
            String msg = "ERROR: IOException (permissions) => " + filename + "\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        }
    }

    /**
     * Remove dir if it exists.
     *
     * (I did not write a unit test for this method)
     *
     * @param dirname
     * @throws org.nve.cliapp.SysUtilsException
     */
    public static void rmDir(String dirname) throws SysUtilsException {
        try {
            Files.deleteIfExists(Paths.get(dirname));
        } catch (NoSuchFileException ex) {
            String msg = "ERROR: NoSuchFileException => " + dirname + "\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        } catch (DirectoryNotEmptyException ex) {
            String msg = "ERROR: DirectoryNotEmptyException => " + dirname + "\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        } catch (IOException ex) {
            String msg = "ERROR: IOException (permissions) => " + dirname + "\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        }
    }

    /**
     * Removes the ENTIRE tree denoted by dirname input.
     *
     * (tested in test_mkdir_p())
     *
     * @param dirname
     * @throws org.nve.cliapp.SysUtilsException
     */
    public static void rmDirTree(String dirname) throws SysUtilsException {
        File fileObj = new File(dirname);

        if (fileObj.exists()) {
            for (File sub : fileObj.listFiles()) {
                if (Files.isSymbolicLink(Paths.get(sub.getAbsolutePath()))) {
                    if (SysUtils.verbose) {
                        System.out.println("DELETE SymLink => " + sub.getAbsolutePath());
                    }
                    SysUtils.rmFile(sub.getAbsolutePath());
                } else if (sub.isFile()) {
                    if (SysUtils.verbose) {
                        System.out.println("DELETE File => " + sub.getAbsolutePath());
                    }
                    SysUtils.rmFile(sub.getAbsolutePath());
                } else if (sub.isDirectory()) {
                    SysUtils.rmDirTree(sub.getAbsolutePath());
                    if (SysUtils.verbose) {
                        System.out.println("DELETE Directory => " + sub.getAbsolutePath());
                    }
                    SysUtils.rmDir(sub.getAbsolutePath());
                }
            }
            if (SysUtils.verbose) {
                System.out.println("DELETE TOP Directory => " + dirname);
            }
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
        boolean status;

        File fileObj = new File(dirname);
        if (fileObj.exists() == false) {
            status = fileObj.mkdirs();
        } else {
            status = true;
        }

        return (status);
    }

    /**
     * This method will allow a system level call to a shell and return results
     * in a List&lt;String&gt;
     *
     * Currently, method only works for Linux (CSH) and Windows (CMD) shells.
     *
     * @param command
     * @return List&lt;String&gt; There are no new lines in output.
     * @throws SysUtilsException
     */
    public static List<String> system(String command) throws SysUtilsException {
        String[] linuxCmd = {"/bin/csh", "-c", command};
        String[] windowsCmd = {"cmd", "/c", command};

        ArrayList<String> results = new ArrayList<>();
        String line;
        try {
            Process proc;

            if (SysUtils.isLinux()) {
                proc = Runtime.getRuntime().exec(linuxCmd);
            } else if (SysUtils.isWindows()) {
                proc = Runtime.getRuntime().exec(windowsCmd, null, null);
            } else {
                String msg = "ERROR: SysUtils.system() is not setup to run on your platform.";
                throw new SysUtilsException(msg);
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
                // We must call this so that if you call redirectStdout more than once prior 
                // to calling restoreStdout, then files will be flushed and closed.
                SysUtils.restoreStdout();
                // Ensure the directory is created for filename.
                String dirname = SysUtils.getDirName(filename);
                SysUtils.mkdir_p(dirname);
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
     * Since this is NOT intuitive, I tested this extensively. This method a
     * used in ffind() to walk the tree;
     *
     * We are skipping over repository [git, svn, cvs, mecurial, sccs]
     * directories, NetApp snapshots, SSH directory, Maven repo, Perl CPAN,
     * Windows AppData.
     *
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
                if (RegExp.isMatch(".*[/\\\\].git$|.*[/\\\\].svn$|.*[/\\\\]CVS$|.*[/\\\\].snapshot$|.*[/\\\\].ssh$|.*[/\\\\].m2$|.*[/\\\\]CPAN$|.*[/\\\\].hg|.*[/\\\\]SCCS$|.*[/\\\\]AppData$", dir.toString())) {
                    if (SysUtils.verbose) {
                        System.out.println("SKIP: dir " + dir.toString());
                    }
                    return FileVisitResult.SKIP_SUBTREE;
                }
                if (SysUtils.verbose) {
                    System.out.format("DIR: %s %n", dir);
                }
                SysUtils.dirMap.put(dir.toString(), attrs);  // this is cleared prior to walking tree.
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (SysUtils.verbose) {
                    System.out.format("FILE: %s%n", file);
                }
                String basename = SysUtils.getBaseName(file.toString());
                if (attrs.isRegularFile() && RegExp.isMatch(fileNameRegExp, basename)) {
                    SysUtils.fileMap.put(file.toString(), attrs); // this is cleared prior to walking tree.
                }
                return FileVisitResult.CONTINUE;
            }

            /**
             * Permission problems on files or directories can be handled here.
             *
             * http://www.technojeeves.com/index.php/9-freebies/82-accessdeniedexception-with-filevisitor-in-java
             *
             * @param file
             * @param e
             * @return
             * @throws IOException
             */
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException e)
                    throws IOException {
                if (SysUtils.verbose) {
                    System.err.printf("WARNING: Directory or file access is restricted for => %s\n", file);
                }
                return FileVisitResult.CONTINUE;
            }
        }
        FileVisitor<Path> visitor = new DirVisitor<>();
        return visitor;
    }

    /**
     * Recursively find all files in startPath that match regexp fileRegEx.
     *
     * @param startPath
     * @param fileRegEx
     * @return
     * @throws IOException
     */
    public static Map<String, BasicFileAttributes> ffind(Path startPath, String fileRegEx) throws IOException {
        return (SysUtils.ffind(startPath, fileRegEx, MAX_VALUE));
    }

    /**
     * Recursively find all files in startPath that match regexp fileRegEx with
     * max depth.
     *
     * @param startPath
     * @param fileRegEx
     * @param maxDepth
     * @return
     * @throws IOException
     */
    public static Map<String, BasicFileAttributes> ffind(Path startPath, String fileRegEx, int maxDepth) throws IOException {
        // These two maps are just temporary collections while I walk the tree.
        SysUtils.dirMap.clear();
        SysUtils.fileMap.clear();

        Set<FileVisitOption> fileVisitOptions = new TreeSet<>();
        fileVisitOptions.add(FileVisitOption.FOLLOW_LINKS); // this is the only option right now. Might be more someday.

        FileVisitor<Path> visitor = SysUtils.getFileVisitor();

        if (fileRegEx != null && !fileRegEx.isEmpty()) {
            SysUtils.fileNameRegExp = fileRegEx;
        }

        Path walkFileTree = Files.walkFileTree(
                startPath,
                fileVisitOptions,
                maxDepth,
                visitor);

        // I want to return a NEW instance on each call, just in case I make several calls to the ffind() method.
        // see test_ffind() for example
        Map<String, BasicFileAttributes> resultFileMap = new LinkedHashMap<>(SysUtils.fileMap);
        return (resultFileMap);
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
     * Display a Set&lt;T&gt; to stdout.
     *
     * @param <T>
     * @param set
     */
    public static <T> void displaySet(Set<T> set) {
        Iterator<T> itr = set.iterator();
        while (itr.hasNext()) {
            T t = itr.next();
            System.out.println(t.toString());
        }
    }

    public static void displayFileMap(Map<String, BasicFileAttributes> map) {
        Iterator<String> itr = map.keySet().iterator();
        while (itr.hasNext()) {
            String filename = itr.next();
            BasicFileAttributes attrs = map.get(filename);
            System.out.println(filename);

//            attrs.creationTime();
//            attrs.lastAccessTime();
//            attrs.lastModifiedTime();
//            attrs.isDirectory();
//            attrs.isRegularFile();
//            attrs.isSymbolicLink();
//            attrs.size();
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

        if (RegExp.isMatch(".*LINUX.*", osName.toUpperCase())) {
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
        if (RegExp.isMatch(".*WINDOWS.*", osName.toUpperCase())) {
            isWindows = true;
        }
        return isWindows;
    }

    /**
     * Method for CHARACTER IO, not binary. (see the encoding param??)
     *
     * Get a BufferedReader instance for the following encoding types.
     *
     * ISO-8859-1 US-ASCII UTF-16 UTF-16BE UTF-16LE UTF-8
     *
     * @param filename
     * @param encoding
     * @return
     * @throws org.nve.cliapp.SysUtilsException
     */
    public static BufferedReader getBufferedReaderInstance(String filename, String encoding) throws SysUtilsException {
        BufferedReader br = null;

        try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, encoding);
            br = new BufferedReader(isr);
        } catch (FileNotFoundException ex) {
            String msg = "ERROR: FileNotFoundException " + SysUtils.class.getName() + "(" + filename + ", " + encoding + ")\n";
            msg += "\n" + ex.getMessage() + "\n";
            throw new SysUtilsException(msg);
        } catch (UnsupportedEncodingException ex) {
            String msg = "ERROR: UnsupportedEncodingException " + SysUtils.class.getName() + "(" + filename + ", " + encoding + ")\n";
            msg += "\n" + ex.getMessage() + "\n";
            throw new SysUtilsException(msg);
        }

        return (br);
    }

    /**
     * Method for CHARACTER IO, not binary. (see the encoding param??)
     *
     * Get a BufferedWriter instance for the following encoding types. This
     * method can either APPEND or OVERWRITE file depending on 3rd param.
     *
     * ISO-8859-1 US-ASCII UTF-16 UTF-16BE UTF-16LE UTF-8
     *
     * @param filename
     * @param encoding
     * @param appendToFile
     * @return
     * @throws org.nve.cliapp.SysUtilsException
     */
    public static BufferedWriter getBufferedWriterInstance(String filename, String encoding, boolean appendToFile) throws SysUtilsException {
        BufferedWriter bw = null;

        try {
            File file = new File(filename);
            FileOutputStream fos = new FileOutputStream(file, appendToFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos, encoding);
            bw = new BufferedWriter(osw);
        } catch (FileNotFoundException ex) {
            String msg = "ERROR: FileNotFoundException " + SysUtils.class.getName() + "(" + filename + ", " + encoding + ")\n";
            msg += "\n" + ex.getMessage() + "\n";
            throw new SysUtilsException(msg);
        } catch (UnsupportedEncodingException ex) {
            String msg = "ERROR: UnsupportedEncodingException " + SysUtils.class.getName() + "(" + filename + ", " + encoding + ")\n";
            msg += "\n" + ex.getMessage() + "\n";
            throw new SysUtilsException(msg);
        }

        return (bw);
    }

    /**
     * This is a convenience method to help with the stream resource nightmare AND
     * using a BufferedInputStream to maximize reads from a file.
     * 
     * @param filename
     * @return
     * @throws SysUtilsException 
     */
    public static byte[] readBinaryFile(String filename) throws SysUtilsException {
        // I am using StringBuilder to store up all bytes and then return byte[].
        StringBuilder sb = new StringBuilder();

        // try(with resources)    will close DataInputStream and all of its derived streams.
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(filename)), SysUtils.binaryBufferSize))) {
            String strFileContents;
            byte[] contents = new byte[SysUtils.binaryBufferSize];
            int bytesRead;

            while ((bytesRead = dis.read(contents)) != -1) {
                strFileContents = new String(contents, 0, bytesRead);
                sb.append(strFileContents);
            }
        } catch (IOException ex) {
            String msg = "ERROR: IOException " + SysUtils.class.getName() + "(" + filename + ")\n";
            msg += "\n" + ex.getMessage() + "\n";
            throw new SysUtilsException(msg);
        }

        return sb.toString().getBytes();
    }

    /**
     * Write byte[] to filename with option to append data.
     * 
     * @param filename
     * @param byteData
     * @param append
     * @throws SysUtilsException 
     */
    public static void writeBinaryFile(String filename, byte[] byteData, boolean append) throws SysUtilsException {
        // Ensure the directory is created for filename.
        String dirname = SysUtils.getDirName(filename);
        SysUtils.mkdir_p(dirname);

        // try(with resources)      will close DataOutputStream and all of its derived streams.
        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(filename), append), SysUtils.binaryBufferSize))) {
            dos.write(byteData);
            dos.flush();
        } catch (IOException ex) {
            String msg = "ERROR: " + SysUtils.class.getName() + "(" + filename + ")\n";
            msg += "\n" + ex.getMessage() + "\n";
            throw new SysUtilsException(msg);
        }
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
     * @throws org.nve.cliapp.SysUtilsException
     */
    public static List<String> readTextFile(String filename, String encoding) throws SysUtilsException {
        List<String> records = new ArrayList<>();
        String line;

        // try with resources will close BufferedReader instance.
        try (BufferedReader breader = SysUtils.getBufferedReaderInstance(filename, encoding)) {
            while ((line = breader.readLine()) != null) {
                records.add(line);
            }
        } catch (SysUtilsException | IOException ex) {
            String msg = "ERROR: readTextFile(" + filename + ", " + encoding + ")\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        }

        return records;
    }

    /**
     * Convenience overloaded method.
     *
     * @param filename
     * @param listStrings
     * @param encoding
     * @throws SysUtilsException
     */
    public static void writeTextFile(String filename, List<String> listStrings, String encoding) throws SysUtilsException {
        SysUtils.writeTextFile(filename, listStrings, encoding, false);
    }

    /**
     * Convenience overloaded method.
     *
     * @param filename
     * @param listStrings
     * @param encoding
     * @throws SysUtilsException
     */
    public static void appendTextFile(String filename, List<String> listStrings, String encoding) throws SysUtilsException {
        SysUtils.writeTextFile(filename, listStrings, encoding, true);
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
     * @throws org.nve.cliapp.SysUtilsException
     */
    public static void writeTextFile(String filename, List<String> listStrings, String encoding, boolean appendToFile) throws SysUtilsException {
        // Ensure the directory is created for filename.
        String dirname = SysUtils.getDirName(filename);
        SysUtils.mkdir_p(dirname);
        
        // Try with resources will close the Buffered Writer in all events.  Java 1.7+
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filename), appendToFile), encoding))) {
            for (int ii = 0; ii < listStrings.size(); ii++) {
                writer.write(listStrings.get(ii));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException ex) {
            String msg = "ERROR: IOException writeTextFile(" + filename + ", " + encoding + ")\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        }
    }

}
