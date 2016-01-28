package fjt.test;

// junit.framework.Test package is the legacy namespace used with Junit v3 and older versions of Java that do not support annotations.
// org.junit.Test is the new namespace used by JUnit v4 and requires Java v1.5 or later for its annotations.
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import fjt.support.Latch;
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
public class TemplateTest {
    
    private static String someString;   // If needed, declare here and use setUpClass() to initialize.
    
    /**
     * Do not use the constructor to set up a test case. 
     * Use setupClass() method below. 
     * 
     * The reason is the stack crashes if first test fails to instantiate.
     */    
    public TemplateTest() {
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
    //@Ignore
    public void ensureStaticStringInitialized() {
        assertEquals("Hello World", someString);
    }
    
    /**
     * Sometimes your test will want to throw Exception to ensure that is does
     * throw the Exception.
     */
    @Test(expected = NullPointerException.class)
    public void testingExceptionThrown() {
        this.someMethodThatThrowsExceptionAlways();
    }
    
    /**
     * If a test MUST complete in a certain amount of time, then add timeout.
     * 
     * NOTE: I am using the Latch.class that I defined in src.main.java.....
     *       Why?  multi-thread testing and sleep do not mix.  
     *       Latch solves this by allowing us to effectively "sleep"
     * 
     * @throws InterruptedException 
     */
    
    @Test(timeout = 2500)
    public void testTransactTimeout() throws InterruptedException {
        Latch l = new Latch(2, 1000); // 2 iterations @ 1000 milliseconds each.
        l.startLatchCountdown();

        // You can do more stuff here.  Your Test Thread is still running.
        // I set a timeout of 2.5 seconds and the Latch to 2 seconds.
        // Change timeout to 1500 and it will fail.
        l.waitForLatchToComplete();

        // Do some more stuff.
    }    
    
    /**
     * This is NOT a test.  
     * Just an example method that throws an exception ALWAYS.
     */
    private void someMethodThatThrowsExceptionAlways() {
        throw new NullPointerException("This is just an example");
    }
    
}
