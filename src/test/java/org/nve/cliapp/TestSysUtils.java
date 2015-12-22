package org.nve.cliapp;

// junit.framework.Test package is the legacy namespace used with Junit v3 and older versions of Java that do not support annotations.
// org.junit.Test is the new namespace used by JUnit v4 and requires Java v1.5 or later for its annotations.
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
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
    
    private static String someString;   // If needed, declare here and use setUpClass() to initialize.
    
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
        someString = "Hello World";
    }

    /**
     * If you have to clear up resources like closing database, then do it here.
     */
    @AfterClass
    public static void tearDownClass() {
    }


    /**
     * Give your test methods meaningful names.
     */
    @Test
    public void testListDir() throws IOException {
        
        List<String> dirList2 = SysUtils.ffind(Paths.get(SysUtils.getEnv("HOME"), "bin"), "");
        System.out.println("========================================");
        SysUtils.displayList(dirList2);
        
    }
    
    
}
