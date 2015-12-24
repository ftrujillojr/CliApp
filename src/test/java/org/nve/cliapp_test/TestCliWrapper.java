package org.nve.cliapp_test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.cli.Option;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nve.cliapp.CliWrapper;
import org.nve.cliapp.CliWrapperException;
//import org.junit.Ignore;

/* To run tests:
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
 * http://junit.org/javadoc/latest/index.html     (JUnit must be >= 4.11)
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
 *        assertThat(String reason, T actual, Match<T> matcher)
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
public class TestCliWrapper {

    protected static CliWrapper cliWrapperValidOptions = null;
    protected static CliWrapper cliWrapperNoOptions = null;
    protected static CliWrapper cliWrapperDuplicateShortOptions = null;
    protected static CliWrapper cliWrapperDuplicateLongOptions = null;

    public TestCliWrapper() {
        // Best practice => Do not put anything here.
    }

    /**
     * Sometimes several tests need to share computationally expensive setup
     * like logging into a database. If you need to do an initialization once
     * before testing all methods.
     */
    @BeforeClass
    public static void setUpClass() {
        List<Option> noOptionsList = new ArrayList<>();
        cliWrapperNoOptions = new CliWrapper(noOptionsList, "n/a", "n/a", "n/a");
        
        List<Option> validOptionsList = new ArrayList<>();

        validOptionsList.add(Option.builder("f")
                .longOpt("filename")
                .required(true)
                .hasArg(true)
                .argName("FILENAME")
                .desc("filename to use as input...")
                .type(String.class)
                .build());

        validOptionsList.add(Option.builder()
                .longOpt("debug")
                .required(false)
                .hasArg(true)
                .argName("LEVEL")
                .desc("Debug  1,2,3,...")
                .type(Integer.class)
                .build());

        validOptionsList.add(Option.builder("y")
                .longOpt("yield")
                .required(false)
                .hasArg(true)
                .argName("YIELD")
                .desc("percentage passing")
                .type(Double.class)
                .build());

        validOptionsList.add(Option.builder("h")
                .longOpt("help")
                .required(false)
                .hasArg(false)
                .desc("This help message")
                .type(Boolean.class)
                .build());

        cliWrapperValidOptions = new CliWrapper(validOptionsList, "n/a", "n/a", "n/a");

        List<Option> duplicateShortOptionsList = new ArrayList<>();
        duplicateShortOptionsList.add(Option.builder("d") // duplicate short option
                .longOpt("debug")
                .required(false)
                .hasArg(true)
                .argName("LEVEL")
                .desc("Debug  1,2,3,...")
                .type(Integer.class)
                .build());

        duplicateShortOptionsList.add(Option.builder("d") // duplicate sort option
                .longOpt("dump")
                .required(false)
                .hasArg(false)
                .desc("dump database.")
                .type(Double.class)
                .build());

        duplicateShortOptionsList.add(Option.builder("h")
                .longOpt("help")
                .required(false)
                .hasArg(false)
                .desc("This help message")
                .type(Boolean.class)
                .build());

        cliWrapperDuplicateShortOptions = new CliWrapper(duplicateShortOptionsList, "n/a", "n/a", "n/a");

        
        List<Option> duplicateLongOptionsList = new ArrayList<>();
        duplicateLongOptionsList.add(Option.builder("a")
                .longOpt("debug")  // duplicate LONG option
                .required(false)
                .hasArg(true)
                .argName("LEVEL")
                .desc("Debug  1,2,3,...")
                .type(Integer.class)
                .build());

        duplicateLongOptionsList.add(Option.builder("b")
                .longOpt("debug")  // duplicate LONG option
                .required(false)
                .hasArg(false)
                .desc("dump database.")
                .type(Double.class)
                .build());

        duplicateLongOptionsList.add(Option.builder("h")
                .longOpt("help")
                .required(false)
                .hasArg(false)
                .desc("This help message")
                .type(Boolean.class)
                .build());

        cliWrapperDuplicateLongOptions = new CliWrapper(duplicateLongOptionsList, "n/a", "n/a", "n/a");
        
    }

    /**
     * If you have to clear up resources like closing database, then do it here.
     */
    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void testValidOptionsValidArgs() throws CliWrapperException {
        // ARRANGE - setup expect data and stimulus (String[] validArgs1)
        Map<String, Object> expectMap1 = new TreeMap<>();
        expectMap1.put("filename", "/tmp/readme.txt");
        expectMap1.put("yield", 95.6);
        expectMap1.put("debug", 2);
        expectMap1.put("help", false);

        // the command line input would generate this input.
        String[] validArgs1 = {
            "--filename",
            "/tmp/readme.txt",
            "--debug",
            "2",
            "--yield",
            "95.6"
        };

        // ACT - test the method.
        Map<String, Object> actualMap1 = cliWrapperValidOptions.parseArgsToMap(validArgs1);

        // ASSERT - see if expect and actual are in harmony.
        assertEquals("Maps from parseArgsToMap were not equal.", expectMap1, actualMap1);
    }


    @Test(expected = CliWrapperException.class)
    public void testNoOptions() throws CliWrapperException {
        // ARRANGE - setup expect data and stimulus (String[] validArgs1)
        Map<String, Object> expectMap1 = new TreeMap<>();

        // the command line input would generate this input.
        String[] validArgs1 = {
            "--filename",
            "/tmp/readme.txt",
            "--debug",
            "2",
            "--yield",
            "95.6"
        };

        // ACT - test the method and it should throw exception.
        Map<String, Object> actualMap1 = cliWrapperNoOptions.parseArgsToMap(validArgs1);
        
    }
    
    
    
    @Test(expected = CliWrapperException.class)
    public void testNoOptionsValidArgsWithEXTRA() throws CliWrapperException {
        // ARRANGE - setup expect data and stimulus (String[] validArgs1)
        Map<String, Object> expectMap1 = new TreeMap<>();
        expectMap1.put("filename", "/tmp/readme.txt");
        expectMap1.put("yield", 95.6);
        expectMap1.put("debug", 2);
        expectMap1.put("help", false);

        // the command line input would generate this input.
        String[] validArgs1 = {
            "--filename",
            "/tmp/readme.txt",
            "--debug",
            "2",
            "--somebogusswitch",  // User put something on command line that was NOT part of option list.
            "--yield",
            "95.6"
        };

        // ACT - test the method.  Should throw exception.
        Map<String, Object> actualMap1 = cliWrapperValidOptions.parseArgsToMap(validArgs1);
    }
    
    @Test(expected = CliWrapperException.class)
    public void testRequiredSwitchesThrowExceptionIfMissing() throws CliWrapperException {
        // ARRANGE - dont care about expect data;  only stimulus (String[] validArgs1)
        // the command line input would generate this input.
        // took out required arg --filename and the filename /tmp/readme.txt
        String[] validArgs1 = {
            "--debug",
            "2",
            "--yield",
            "95.6"
        };

        // ACT - test the method. This should throw CliWrapperException
        Map<String, Object> actualMap1 = cliWrapperValidOptions.parseArgsToMap(validArgs1);
    }

    @Test(expected = CliWrapperException.class)
    public void testDuplicateShortOptions() throws CliWrapperException {
        // ARRANGE - does not matter what input is, the short options for -d should
        // throw exception on init.
        String[] dont_care = {};
        
        // ACT - test the method. This should throw CliWrapperException
        Map<String, Object> actualMap1 = cliWrapperDuplicateShortOptions.parseArgsToMap(dont_care);
    }
    
    @Test(expected = CliWrapperException.class)
    public void testDuplicateLongOptions() throws CliWrapperException {
        // ARRANGE - does not matter what input is, the short options for --debug should
        // throw exception on init.
        String[] dont_care = {};
        
        // ACT - test the method. This should throw CliWrapperException
        Map<String, Object> actualMap1 = cliWrapperDuplicateLongOptions.parseArgsToMap(dont_care);
        
    }
    
}
