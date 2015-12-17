package org.nve.cliapp;

import java.util.List;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
public class TestRegExp {

    private static String someString;   // If needed, declare here and use setUpClass() to initialize.

    /**
     * Do not use the constructor to set up a test case. Use setupClass() method
     * below.
     *
     * The reason is the stack crashes if first test fails to instantiate.
     */
    public TestRegExp() {
        // Best practice => Do not put anything here.
    }

    /**
     * Sometimes several tests need to share computationally expensive setup
     * like logging into a database. If you need to do an initialization once
     * before testing all methods.
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

    @Test
    public void testReplaceFirst() {
        // ARRANGE
        String myString = "one two three one two three";
        String firstExpect = "one 2 three one two three";
        
        // ACT
        String firstActual = RegExp.replaceFirst("two", myString, "2");
        
        // ASSERT
        assertEquals(firstExpect, firstActual);
    }
    
    @Test
    public void testReplaceLast() {
        // ARRANGE
        String myString = "one two three one two three";
        String firstExpect = "one two three one 2 three";
        
        // ACT
        String firstActual = RegExp.replaceLast("two", myString, "2");
        
        // ASSERT
        assertEquals(firstExpect, firstActual);
    }
    
    @Test
    public void testReplaceAll() {
        // ARRANGE
        String myString = "one two three one two three";
        String allExpect = "one 2 three one 2 three";
        
        // ACT
        String allActual = RegExp.replaceAll("two", myString, "2");
        
        // ASSERT
        assertEquals(allExpect, allActual);
    }

    /**
     * I know this is testing java RegEx.
     * But, if you add stuff to RegExp class, then I want to ensure functionality
     * is the same.
     */
    @Test
    //@Ignore
    public void testIsMatch() {
        // ARRANGE
        String stringToTest = "Mary had 1 little lamb";
        List<String> subExps = null;
        
        String re = ".*(little lamb).*";
        String reBeginning = "^Mary.*";
        String reNumber = ".*([0-9]+).*";
        String reBad = ".*George.*";
        String reCaseSensitive = "mary.*";
        String reCaseInsensitive = "(?i:mary.*)"; // clunky, but it works
        
        String expect1 = "little lamb";
        String expect2 = "1";
        
        // ACT + ASSERT
        assertFalse("Failed to match sub string => " + reBad, RegExp.isMatch(reBad, stringToTest));
        assertTrue("Failed to match sub string => " + reBeginning, RegExp.isMatch(reBeginning, stringToTest));
        
        // group1
        assertTrue("Failed to match sub string => " + re, RegExp.isMatch(re, stringToTest));
        subExps = RegExp.getSubExps();
        assertTrue("Expected 2 subExps.", (subExps.size() == 2));
        assertEquals("Failed to return grouping from regexp.", expect1, subExps.get(1));
        
        // group2
        assertTrue("Failed to match sub string => " + reNumber, RegExp.isMatch(reNumber, stringToTest));
        subExps = RegExp.getSubExps();
        assertTrue("Expected 2 subExps.", (subExps.size() == 2));
        assertEquals("Failed to return grouping from regexp.", expect2, subExps.get(1));
        
        // group3
        assertFalse("Should have FAILED CASE SENSISTIVE  regex => " + reCaseSensitive, RegExp.isMatch(reCaseSensitive, stringToTest));
        assertTrue("Should have  PASSED CASE INSENSITIVE regex => " + reCaseInsensitive, RegExp.isMatch(reCaseInsensitive, stringToTest));
        assertTrue("Should have  PASSED CASE SENSISTIVE  regex => " + reCaseSensitive, RegExp.isMatch(reCaseSensitive, stringToTest, CASE_INSENSITIVE));
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
     * Why? multi-thread testing and sleep do not mix. Latch solves this by
     * allowing us to effectively "sleep"
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
     * This is NOT a test. Just an example method that throws an exception
     * ALWAYS.
     */
    private void someMethodThatThrowsExceptionAlways() {
        throw new NullPointerException("This is just an example");
    }

}
