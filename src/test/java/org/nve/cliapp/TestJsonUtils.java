package org.nve.cliapp;

import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

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
    
    private static List<Person> personList =  new ArrayList<>();
    
    /**
     * Do not use the constructor to set up a test case. 
     * Use setupClass() method below. 
     * 
     * The reason is the stack crashes if first test fails to instantiate.
     */    
    public TestJsonUtils() {
        // Best practice => Do not put anything here.
    }

    /**
     * Sometimes several tests need to share computationally expensive setup
     * like logging into a database.
     * If you need to do an initialization once before testing all methods.
     */
    @BeforeClass
    public static void setUpClass() {
        personList.add(new Person("Wiley", "Coyote", 80, 1.10, false));
        personList.add(new Person("Road", "Runner", 81, 100.23, false));
        personList.add(new Person("Bugs", "Bunny", 100, 5436.34, true));
    }

    /**
     * If you have to clear up resources like closing database, then do it here.
     */
    @AfterClass
    public static void tearDownClass() {
    }


    @Test
    public void testToJson() throws JsonUtilsException {
        String actual = JsonUtils.toJsonCompact(personList);
        String expect = "[{\"firstName\":\"Wiley\",\"lastName\":\"Coyote\",\"age\":80,\"salary\":1.1,\"isStudent\":false},{\"firstName\":\"Road\",\"lastName\":\"Runner\",\"age\":81,\"salary\":100.23,\"isStudent\":false},{\"firstName\":\"Bugs\",\"lastName\":\"Bunny\",\"age\":100,\"salary\":5436.34,\"isStudent\":true}]";
        assertEquals(expect, actual);
        
        String pretty = JsonUtils.toPrettyFormat(actual);
        System.err.println(pretty);
        
        JsonUtils.writeJsonToFile(actual, "/tmp/compact.json");
        JsonUtils.writeJsonToFile(pretty, "/tmp/pretty.json");
        
    }
    
    
    
    
    
    
}
