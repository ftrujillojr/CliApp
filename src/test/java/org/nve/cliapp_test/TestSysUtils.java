package org.nve.cliapp_test;

// junit.framework.Test package is the legacy namespace used with Junit v3 and older versions of Java that do not support annotations.
// org.junit.Test is the new namespace used by JUnit v4 and requires Java v1.5 or later for its annotations.
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.junit.AfterClass;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nve.cliapp.utils.RegExp;
import org.nve.cliapp.utils.SysUtils;
import org.nve.cliapp.exceptions.SysUtilsException;

//import org.junit.Ignore;

/*   ARRANGE    ACT    ASSERT

 To run tests:
 ============================
 mvn test
 mvn -Dtest=TestCircle test
 (in Netbeans, Alt+F6)

 Your pom.xml should have this plugin.

 <plugin>
 <groupId>org.apache.maven.plugins</groupId>
 <artifactId>maven-surefire-plugin</artifactId>
 <version>2.12.4</version>
 <configuration>
 <skipTests>false</skipTests>
 </configuration>
 </plugin>
 */
/**
 * <pre>
 * http://junit.org/javadoc/latest/index.html     (JUnit must be &gt;= 4.11)
 *
 * assertArrayEquals(String msg,    **[] expect,   **[] actual)
 *      assertEquals(String msg,    ** expect,     ** actual)
 *   assertNotEquals(String msg,    ** unexpected, ** actual)
 *        assertTrue(String msg,    boolean condition)
 *       assertFalse(String msg,    boolean condition)
 *     assertNotNull(String msg,    Object object)
 *     assertNotSame(String msg,    Object unexpected, Object actual)
 *        assertNull(String msg,    Object object)
 *        assertSame(String msg,    Object expected, Object actual)
 *        assertThat(String reason, T actual, Match&lt;T&gt; matcher)
 *              fail()
 *              fail(String msg)
 *
 *
 * Matchers are used in assertThat() calls.
 *
 * http://junit.org/javadoc/latest/org/hamcrest/core/package-frame.html
 *
 *   assertThat("myValue",                          allOf(startsWith("my"), containsString("Val")))
 *   assertThat("myValue",                          anyOf(startsWith("foo"), containsString("Val")))
 *   assertThat("fab",                              both(containsString("a")).and(containsString("b")))
 *   assertThat("fab",                              both(containsString("a")).or(containsString("b")))
 *   assertThat("fab",                              either(containsString("a")).and(containsString("b")))
 *   assertThat(Arrays.asList("bar", "baz"),        everyItem(startsWith("ba")))
 *   assertThat(cheese,                             is(equalTo(smelly)))
 *   assertThat(Arrays.asList("foo", "bar"),        hasItem(startsWith("ba")))
 *   assertThat(Arrays.asList("foo", "bar", "baz"), hasItems(endsWith("z"), endsWith("o")))
 *   assertThat("foo",                              equalTo("foo"));
 *   assertThat(new Canoe(),                        instanceOf(Paddlable.class));
 *   assertThat(cheese,                             is(not(equalTo(smelly))))
 *   assertThat("myStringOfNote",                   containsString("ring"))
 *   assertThat("myStringOfNote",                   endsWith("Note"))
 *   assertThat("myStringOfNote",                   startsWith("my"))
 *
 * </pre>
 */
public class TestSysUtils {

    /**
     * Do not use the constructor to set up a test case. Use setupClass() method
     * below.
     *
     * The reason is the stack crashes if first test fails to instantiate.
     */
    public TestSysUtils() {
        // Best practice => Do not put anything here.
    }

    /**
     * Sometimes several tests need to share computationally expensive setup
     * like logging into a database. If you need to do an initialization once
     * before testing all methods.
     */
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     * If you have to clear up resources like closing database, then do it here.
     */
    @AfterClass
    public static void tearDownClass() {
    }

    // THIS IS NOT A TEST.
    private static void createEmptyFileForTesting(String filename) throws SysUtilsException, IOException {
        String dirname = SysUtils.getDirName(filename);
        SysUtils.mkdir_p(dirname);
        Files.deleteIfExists(Paths.get(filename));
        try (BufferedWriter bw = SysUtils.getBufferedWriterInstance(filename, "UTF-8", false)) {
            bw.write("Just a file for testing.");
            bw.flush();
        }
    }

    /**
     * This test will create 7 files with different extensions. The first
     * assertion should return a sub set of the files. (4) The second assertion
     * should return all 7.
     *
     * @throws IOException
     * @throws SysUtilsException
     */
    @Test
    public void test_ffind() throws IOException, SysUtilsException {
        // ARRANGE
        Path tmpPath = Paths.get(SysUtils.getTmpDir(), "test_ffind");
        List<String> fileList = new ArrayList<>();
        fileList.add(Paths.get(tmpPath.toString(), "top.txt").toString());
        fileList.add(Paths.get(tmpPath.toString(), "SHOULD_NOT_SEE.doc").toString());
        fileList.add(Paths.get(tmpPath.toString(), "sub1", "sub1.txt").toString());
        fileList.add(Paths.get(tmpPath.toString(), "sub1", "SHOULD_NOT_SEE.ppt").toString());
        fileList.add(Paths.get(tmpPath.toString(), "sub1", "sub2", "sub2.pdf").toString());
        fileList.add(Paths.get(tmpPath.toString(), "sub1", "sub2", "mickey_mouse.png").toString());
        fileList.add(Paths.get(tmpPath.toString(), "sub1", "sub2", "SHOULD_NOT_SEE.xls").toString());

        // create a whole bunch of files in different known directories.
        int expectedCount = 0;
        Iterator<String> itr = fileList.iterator();
        while (itr.hasNext()) {
            String fileName = itr.next();
            TestSysUtils.createEmptyFileForTesting(fileName);
            if (!RegExp.isMatch(".*SHOULD_NOT_SEE.*", fileName)) {
                expectedCount++;
            }
        }

        // ACT
        Map<String, BasicFileAttributes> fileMap = SysUtils.ffind(tmpPath, ".*\\.png$|.*\\.pdf$|.*\\.txt$");
        Map<String, BasicFileAttributes> fileMapAll = SysUtils.ffind(tmpPath, ".*");
        Map<String, BasicFileAttributes> fileMapTop = SysUtils.ffind(tmpPath, ".*", 1);

        // ASSERT
        if (fileMap.keySet().size() != expectedCount) {
            SysUtils.displayFileMap(fileMap);
        }
        assertEquals(expectedCount, fileMap.keySet().size());

        if (fileMapAll.keySet().size() != fileList.size()) {
            SysUtils.displayFileMap(fileMapAll);
        }
        assertEquals(fileList.size(), fileMapAll.keySet().size());

        if (fileMapTop.keySet().size() != 2) {
            SysUtils.displayFileMap(fileMapTop);
        }
        assertEquals(2, fileMapTop.keySet().size());

        // Cleanup
        // Remove the tmp file tree.
        SysUtils.rmDirTree(tmpPath.toString());
    }

    @Test
    public void test_mkdir_p() throws SysUtilsException {
        // ARRANGE
        Path tmpPath = Paths.get(SysUtils.getTmpDir(), "test_mkdir_p");
        SysUtils.rmDirTree(tmpPath.toString());
        
        // check to see if directory got removed.
        File fileObj = new File(tmpPath.toString());
        boolean dirShouldNotExist = fileObj.exists();
        assertFalse(dirShouldNotExist);
        
        String newDir = Paths.get(SysUtils.getTmpDir(), "test_mkdir_p", "sysutils").toString();

        // ACT
        boolean dirCreated = SysUtils.mkdir_p(newDir);

        // ASSERT
        assertTrue(dirCreated);

        // Cleanup
        SysUtils.rmDirTree(tmpPath.toString());
    }

    @Test
    public void testSystem() throws IOException, SysUtilsException {
        // ARRANGE - /tmp/testSystemDir/file*.txt
        Path tmpPath = Paths.get(SysUtils.getTmpDir(), "testSystemDir");
        Set<String> expectFileSet = new TreeSet<>();
        expectFileSet.add("file1.txt");
        expectFileSet.add("file2.txt");

        Iterator<String> itr = expectFileSet.iterator();
        while (itr.hasNext()) {
            String fileName = itr.next();
            TestSysUtils.createEmptyFileForTesting(Paths.get(tmpPath.toString(), fileName).toString());
        }

        // ACT - showing two different commands that do similar function on different OS.
        List<String> results;
        if (SysUtils.isLinux()) {
            results = SysUtils.system("/bin/ls -al " + tmpPath.toString());
        } else {
            results = SysUtils.system("dir " + tmpPath.toString());
        }

        Set<String> actualFileSet = new TreeSet<>();
        Iterator<String> itr2 = results.iterator();
        while (itr2.hasNext()) {
            String line = itr2.next();
            if (RegExp.isMatch(".*(file[0-9]+\\.txt).*", line)) {
                List<String> subExps = RegExp.getSubExps();
                actualFileSet.add(subExps.get(1));
            }
        }

        // ASSERT
        assertEquals(expectFileSet, actualFileSet);

        // Cleanup
        // Remove the tmp file tree.
        SysUtils.rmDirTree(tmpPath.toString());
    }

    @Test
    public void testRedirect() throws SysUtilsException {
        // ARRANGE
        String filename = Paths.get(SysUtils.getTmpDir(), "testRedirect", "stdout.txt").toString();
        List<String> expectedResults = new ArrayList<>();
        expectedResults.add("This should go to a file.");
        expectedResults.add("This line also.");

        // ACT1 - redirect stdout to a file, then read it back
        SysUtils.redirectStdout(filename, false);
        System.out.println("This should go to a file.");
        System.out.println("This line also.");
        SysUtils.restoreStdout();

        // This should not be in filename.  The restoreStdout() has already been called.
        //System.out.println("Done with REDIRECT stdout test. Read back filename => " + filename);
        // ACT2 - read results into List<String>.
        List<String> actualResults = SysUtils.readTextFile(filename, "UTF-8");

        // ASSERT
        assertEquals(expectedResults, actualResults);

        // Cleanup
        // Remove the tmp file tree.
        SysUtils.rmDirTree(SysUtils.getDirName(filename));
    }

    @Test
    public void testWriteReadTextFile() throws SysUtilsException {
        // ARRANGE
        String filename = Paths.get(SysUtils.getTmpDir(), "testWriteReadTextFile", "file.txt").toString();
        List<String> expectData = new ArrayList<>();
        expectData.add("This is a line to be written to a file.");
        expectData.add("=======================================");
        expectData.add(":-)");

        String[] encodings = "ISO-8859-1 US-ASCII UTF-16 UTF-16BE UTF-16LE UTF-8".split("[ \\t\\n]+");

        for (int ii = 0; ii < encodings.length; ii++) {
            // ACT - Write then Read for each encoding
            SysUtils.writeTextFile(filename, expectData, encodings[ii]);
            List<String> actualData = SysUtils.readTextFile(filename, encodings[ii]);

            // ASSERT
            if (!expectData.equals(actualData)) {
                System.out.println("FAILED test for encoding: testWriteReadTextFile() => " + encodings[ii]);
            }
            assertEquals(expectData, actualData);
        }

        // Cleanup
        // Remove the tmp file tree.
        SysUtils.rmDirTree(SysUtils.getDirName(filename));
    }

    /**
     * This is an example of 3 different types of binary data.
     *
     * *** You have to convert using String.getBytes(encodingString) *** *
     * ============= UTF-8 =====================
     *
     * 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f
     * --------------------------------------------------------------------------
     * 00000000 61 62 63 31 32 33 61 62 63 31 32 33 41 42 43 30 00000010 39 38
     * 2d 3d 20 20 25 5e 20 2d 56 21 40 28 29 09 00000020 0a 0d 61 62 63 *
     * ============= UTF-16LE ==================
     *
     * 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f
     * --------------------------------------------------------------------------
     * 00000000 61 00 62 00 63 00 31 00 32 00 33 00 61 00 62 00 00000010 63 00
     * 31 00 32 00 33 00 41 00 42 00 43 00 30 00 00000020 39 00 38 00 2d 00 3d
     * 00 20 00 20 00 25 00 5e 00 00000030 20 00 2d 00 56 00 21 00 40 00 28 00
     * 29 00 09 00 00000040 0a 00 0d 00 61 00 62 00 63 00 * =============
     * UTF-16BE ==================
     *
     * 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f
     * --------------------------------------------------------------------------
     * 00000000 00 61 00 62 00 63 00 31 00 32 00 33 00 61 00 62 00000010 00 63
     * 00 31 00 32 00 33 00 41 00 42 00 43 00 30 00000020 00 39 00 38 00 2d 00
     * 3d 00 20 00 20 00 25 00 5e 00000030 00 20 00 2d 00 56 00 21 00 40 00 28
     * 00 29 00 09 00000040 00 0a 00 0d 00 61 00 62 00 63
     *
     *
     * @throws SysUtilsException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testWriteReadBinaryFile() throws SysUtilsException, UnsupportedEncodingException {
        // ARRANGE 
        String filename = Paths.get(SysUtils.getTmpDir(), "testWriteReadBinaryFile", "file.bin").toString();
        String filename2 = Paths.get(SysUtils.getTmpDir(), "testWriteReadBinaryFile", "file2.bin").toString();
        String filename3 = Paths.get(SysUtils.getTmpDir(), "testWriteReadBinaryFile", "file3.bin").toString();
        String data = "abc123abc123ABC098-=  %^ -V!@()\t\n\rabc";
        byte[] expectData = data.getBytes("UTF-8"); // defaults to UTF-8 if no argument.
        byte[] expectData2 = data.getBytes("UTF-16LE");
        byte[] expectData3 = data.getBytes("UTF-16BE");

        // ACT - write/read => test overloaded write methods.
        SysUtils.writeBinaryFile(filename, expectData, false);
        SysUtils.writeBinaryFile(filename2, expectData2, false);
        SysUtils.writeBinaryFile(filename3, expectData3, false);

        byte[] actualData = SysUtils.readBinaryFile(filename);
        byte[] actualData2 = SysUtils.readBinaryFile(filename2);
        byte[] actualData3 = SysUtils.readBinaryFile(filename3);

        // ASSERT
        if (!Arrays.equals(expectData, actualData)) {
            System.out.println("EXPECT");
            SysUtils.displayHexDump(expectData);
            System.out.println("ACTUAL");
            SysUtils.displayHexDump(actualData);
        }
        assertArrayEquals(expectData, actualData);

        if (!Arrays.equals(expectData2, actualData2)) {
            System.out.println("EXPECT");
            SysUtils.displayHexDump(expectData2);
            System.out.println("ACTUAL");
            SysUtils.displayHexDump(actualData2);
        }
        assertArrayEquals(expectData2, actualData2);

        if (!Arrays.equals(expectData3, actualData3)) {
            System.out.println("EXPECT");
            SysUtils.displayHexDump(expectData3);
            System.out.println("ACTUAL");
            SysUtils.displayHexDump(actualData3);
        }
        assertArrayEquals(expectData3, actualData3);

        if (false) {
            System.out.println("============= UTF-8 =====================");
            SysUtils.displayHexDump(expectData);
            System.out.println("============= UTF-16LE ==================");
            SysUtils.displayHexDump(expectData2);
            System.out.println("============= UTF-16BE ==================");
            SysUtils.displayHexDump(expectData3);
        }
        // Cleanup
        // Remove the tmp file tree.
        SysUtils.rmDirTree(SysUtils.getDirName(filename));
    }

    @Test
    public void testRepeatString() {
        String expect = "HelloHelloHello";
        String actual = SysUtils.repeatString("Hello", 3);
        assertEquals(expect, actual);
    }

    @Test
    public void testObjectToByteArray() throws IOException, ClassNotFoundException, SysUtilsException {
        // ARRANGE
        String filename = Paths.get(SysUtils.getTmpDir(), "testObjectToByteArray", "file.bin").toString();
        Person person = new Person("Francis", "Trujillo", 51, 8.00, false);

        // ACT - object to byte[] and back to object
        byte[] expectData = SysUtils.objectToByteArray(person);
        Person restoredPerson = (Person)SysUtils.byteArrayToObject(expectData);

        if (person.equals(restoredPerson) == false) {
            System.out.println(person.toString());
            System.out.println(restoredPerson.toString());
        }

        // ASSERT 
        assertEquals(person, restoredPerson);

        // ACT - write/read to and from file
        SysUtils.writeBinaryFile(filename, expectData, false);
        byte[] actualData = SysUtils.readBinaryFile(filename);

        // ASSERT
        if (Arrays.equals(expectData, actualData) == false) {
            SysUtils.displayHexDump(expectData);
            SysUtils.displayHexDump(actualData);
        }
        assertArrayEquals(expectData, actualData);

        // Cleanup
        // Remove the tmp file tree.
        SysUtils.rmDirTree(SysUtils.getDirName(filename));
    }

    @Test
    public void testSimpleBinaryWriteRead() throws SysUtilsException {
        // ARRANGE
        String filename = Paths.get(SysUtils.getTmpDir(), "testSimpleBinaryWriteRead", "file.bin").toString();
        byte[] expectData = {(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x9a, (byte) 0xbc, (byte) 0xde, (byte) 0xf0};

        // ACT
        SysUtils.writeBinaryFile(filename, expectData, false);
        byte[] actualData = SysUtils.readBinaryFile(filename);

        // ASSERT
        if (Arrays.equals(expectData, actualData) == false) {
            SysUtils.displayHexDump(expectData);
            SysUtils.displayHexDump(actualData);
        }
        assertArrayEquals(expectData, actualData);

        // Cleanup
        // Remove the tmp file tree.
        SysUtils.rmDirTree(SysUtils.getDirName(filename));
    }

}
