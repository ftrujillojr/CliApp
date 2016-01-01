package org.nve.cliapp_test;

import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.nve.cliapp.JsonUtils;
import org.nve.cliapp.JsonUtilsException;
import org.nve.cliapp.RegExp;
import org.nve.cliapp.SysUtils;

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
public class TestJsonUtils {

    private static List<Person> personList = new ArrayList<>();

    /**
     * Do not use the constructor to set up a test case. Use setupClass() method
     * below.
     *
     * The reason is the stack crashes if first test fails to instantiate.
     */
    public TestJsonUtils() {
        // Best practice => Do not put anything here.
    }

    /**
     * Sometimes several tests need to share computationally expensive setup
     * like logging into a database. If you need to do an initialization once
     * before testing all methods.
     */
    @BeforeClass
    public static void setUpClass() {
        personList.add(new Person("Wiley", "Coyote", 80, 1.10, false));
        personList.add(new Person("Road", "Runner", 81, 100.23, false));
        personList.add(new Person("Bugs", "Bunny", 100, 5436.34, true));

        //JsonUtils.setVerbose(true);  // Show write and read files.
    }

    /**
     * If you have to clear up resources like closing database, then do it here.
     */
    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void testToJson() throws JsonUtilsException {
        String actual = JsonUtils.objectToJsonCompact(personList);
        String expect = "[{\"firstName\":\"Wiley\",\"lastName\":\"Coyote\",\"age\":80,\"salary\":1.1,\"isStudent\":false},{\"firstName\":\"Road\",\"lastName\":\"Runner\",\"age\":81,\"salary\":100.23,\"isStudent\":false},{\"firstName\":\"Bugs\",\"lastName\":\"Bunny\",\"age\":100,\"salary\":5436.34,\"isStudent\":true}]";
        assertEquals(expect, actual);

        String tmpDir = SysUtils.getTmpDir();
        String pretty = JsonUtils.toPrettyFormat(actual);
        JsonUtils.writeJsonToFile(actual, tmpDir + "/compact.json");
        JsonUtils.writeJsonToFile(pretty, tmpDir + "/pretty.json");

        String readCompact = JsonUtils.readJsonFromFile(tmpDir + "/compact.json");
        String readCompactExpect = JsonUtils.readJsonFromFile("./src/test/resources/compact.json");
        assertEquals(readCompactExpect, readCompact);

        String readPretty = JsonUtils.readJsonFromFile(tmpDir + "/pretty.json");
        String readPrettyExpect = JsonUtils.readJsonFromFile("./src/test/resources/pretty.json");
        assertEquals(readPrettyExpect, readPretty);

        String compact = JsonUtils.toCompactFormat(pretty);
        assertEquals(expect, compact);
    }

    @Test
    public void testFromJson() throws JsonUtilsException {
        // ARRANGE
        String json = "[{\"firstName\":\"Elmer\",\"lastName\":\"Fudd\",\"age\":90,\"salary\":16.16,\"isStudent\":false},{\"firstName\":\"Daffy\",\"lastName\":\"Duck\",\"age\":91,\"salary\":0.25,\"isStudent\":true},{\"firstName\":\"Foghorn\",\"lastName\":\"Leghorn\",\"age\":20,\"salary\":6.00,\"isStudent\":false}]";

        List<Person> expectList = new ArrayList<>();
        
        expectList.add(new Person("Elmer", "Fudd", 90, 16.16, false));
        expectList.add(new Person("Daffy", "Duck", 91, 0.25, true));
        expectList.add(new Person("Foghorn", "Leghorn", 20, 6.0, false));

        // ACT
        List<Person> actualList = Person.fromJson(json);

        // ASSERT - Person.java has override on equals() and hashCode()
        // http://javarevisited.blogspot.com/2011/02/how-to-write-equals-method-in-java.html
        assertTrue(expectList.equals(actualList));
    }

}
