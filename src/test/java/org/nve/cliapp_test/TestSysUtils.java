package org.nve.cliapp_test;

// junit.framework.Test package is the legacy namespace used with Junit v3 and older versions of Java that do not support annotations.
// org.junit.Test is the new namespace used by JUnit v4 and requires Java v1.5 or later for its annotations.
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nve.cliapp.RegExp;
import org.nve.cliapp.SysUtils;
import org.nve.cliapp.SysUtilsException;
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
     * Do not use the constructor to set up a test case. 
     * Use setupClass() method below. 
     * 
     * The reason is the stack crashes if first test fails to instantiate.
     */    
    public TestSysUtils() {
        // Best practice => Do not put anything here.
    }

    /**
     * Sometimes several tests need to share computationally expensive setup
     * like logging into a database.
     * If you need to do an initialization once before testing all methods.
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
    private static void createEmptyFileForTesting(String filename) throws IOException {
        String dirname = SysUtils.getDirName(filename);
        SysUtils.mkdir_p(dirname);
        Files.deleteIfExists(Paths.get(filename));
        try(BufferedWriter bw = SysUtils.getBufferedWriterInstance(filename, "UTF-8", false)) {
            bw.write("Just a file for testing.");
            bw.flush();
        }
    }

    /**
     * This test will create 7 files with different extensions.
     * The first assertion should return a sub set of the files. (4)
     * The second assertion should return all 7.
     * @throws IOException
     * @throws SysUtilsException 
     */
    @Test
    public void test_ffind() throws IOException, SysUtilsException {
        // ARRANGE
        Path tmpPath = Paths.get(SysUtils.getTmpDir(), "testListDir");
        List<String> fileList = new ArrayList<>();
        fileList.add(Paths.get(tmpPath.toString(), "top.txt").toString());
        fileList.add(Paths.get(tmpPath.toString(), "SHOULD_NOT_SEE.doc").toString());
        fileList.add(Paths.get(tmpPath.toString(), "sub1", "sub1.txt").toString());
        fileList.add(Paths.get(tmpPath.toString(), "sub1", "SHOULD_NOT_SEE.ppt").toString());
        fileList.add(Paths.get(tmpPath.toString(), "sub1", "sub2", "sub2.pdf").toString());
        fileList.add(Paths.get(tmpPath.toString(), "sub1", "sub2", "mickey_mouse.png").toString());
        fileList.add(Paths.get(tmpPath.toString(), "sub1", "sub2", "SHOULD_NOT_SEE.xls").toString());

        int expectedCount = 0;
        Iterator<String> itr = fileList.iterator();
        while(itr.hasNext()) {
            String fileName = itr.next();
            TestSysUtils.createEmptyFileForTesting(fileName);
            if(! RegExp.isMatch(".*SHOULD_NOT_SEE.*", fileName)) {
                expectedCount++;
            }
        }

        // ACT
        Map<String, BasicFileAttributes> fileMap = SysUtils.ffind(tmpPath, ".*\\.png$|.*\\.pdf$|.*\\.txt$");
        Map<String, BasicFileAttributes> fileMapAll = SysUtils.ffind(tmpPath, ".*");
        Map<String, BasicFileAttributes> fileMapTop = SysUtils.ffind(tmpPath, ".*", 1);
        
        // ASSERT
        if(fileMap.keySet().size() != expectedCount) {
            SysUtils.displayFileMap(fileMap);
        }
        assertEquals(expectedCount, fileMap.keySet().size());
        
        if(fileMapAll.keySet().size() != fileList.size()) {
            SysUtils.displayFileMap(fileMapAll);
        }
        assertEquals(fileList.size(), fileMapAll.keySet().size());
        
        if(fileMapTop.keySet().size() != 2) {
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
        SysUtils.rmDirTree("/tmp/test");
        
        // ACT
        boolean dirCreated = SysUtils.mkdir_p("/tmp/test/sysutils");
        
        // ASSERT
        assertTrue(dirCreated);
        
        // Cleanup
        SysUtils.rmDirTree("/tmp/test");
        File fileObj = new File("/tmp/test");
        boolean dirShouldNotExist = fileObj.exists();
        assertFalse(dirShouldNotExist);
    }
    
    @Test
    public void testSystem() throws IOException, SysUtilsException {
        // ARRANGE - /tmp/testSystemDir/file*.txt
        Path tmpPath = Paths.get(SysUtils.getTmpDir(), "testSystemDir");
        Set<String> expectFileSet = new TreeSet<>();
        expectFileSet.add("file1.txt");
        expectFileSet.add("file2.txt");
        
        Iterator<String> itr = expectFileSet.iterator();
        while(itr.hasNext()) {
            String fileName = itr.next();
            TestSysUtils.createEmptyFileForTesting(Paths.get(tmpPath.toString(), fileName).toString());
        }
        
        // ACT - showing two different commands that do similar function on different OS.
        List<String> results;
        if(SysUtils.isLinux()) {
            results = SysUtils.system("/bin/ls -al " + tmpPath.toString());
        } else {
            results = SysUtils.system("dir " + tmpPath.toString());
        }

        Set<String> actualFileSet = new TreeSet<>();
        Iterator<String> itr2 = results.iterator();
        while(itr2.hasNext()) {
            String line = itr2.next();
            if(RegExp.isMatch(".*(file[0-9]+\\.txt).*", line)) {
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
    
    
}
