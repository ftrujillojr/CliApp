package org.nve.cliapp;

// junit.framework.Test package is the legacy namespace used with Junit v3 and older versions of Java that do not support annotations.
// org.junit.Test is the new namespace used by JUnit v4 and requires Java v1.5 or later for its annotations.

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;


/**
 * <pre>
 * http://www.javaworld.com/article/2076265/testing-debugging/junit-best-practices.html
 * http://www.kyleblaney.com/junit-best-practices/
 *
 * ==============================================================================
 * Don't assume the order in which tests within a test case run
 *
 * Put assertion parameters in the proper order
 *
 * The parameters to JUnit's assertions are: 1. expected 2. actual
 *
 * For example, use assertEquals(expected, actual) rather than
 * assertEquals(actual, expected).
 *
 * Ordering the parameters correctly ensures that JUnit's messages are accurate.
 *
 * ==============================================================================
 *
 * Tutorial http://www.tutorialspoint.com/junit/junit_quick_guide.htm
 *
 * JUnit v4.10
 *
 * assertArrayEquals
 * assertEquals                (does NOT have assertNotEqual() until JUnit v4.11
 * assertTrue
 * assertFalse
 * assertNotNull
 * assertNotSame
 * assertNull
 * assertSame
 * assertThat
 * fail()
 * fail(message)
 *
 * This is the fix for assertNotEqual() in v4.10 and older.
 * import static org.junit.Assert.*;
 * import static org.hamcrest.CoreMatchers.*;
 *
 * assertThat(objectUnderTest, is(not(someOtherObject)));
 * assertThat(objectUnderTest, not(someOtherObject));
 * assertThat(objectUnderTest, not(equalTo(someOtherObject)));
 *
 * </pre>
 */
public class MainTest {

    private static String strToMatch;

    /**
     * Do not use the test-case constructor to set up a test case. See
     * setupClass() method below. The reason is the stack if FirstTest fails to
     * instantiate.
     */
    public MainTest() {
    }

    /**
     * <pre>
     * Fixtures is a fixed state of a set of objects used as a baseline for
     * running tests. The purpose of a test fixture is to ensure that there
     * is a well known and fixed environment in which tests are run so that
     * results are repeatable.
     *
     * Sometimes several tests need to share computationally expensive setup
     * (like logging into a database).
     * If you need to do an initialization once before testing all methods.
     * </pre>
     */
    @BeforeClass
    public static void setUpClass() {
        strToMatch = "JUnit is working fine";
    }

    /**
     * <pre>
     * If you allocate expensive external resources in a BeforeClass method you
     * need to release them after all the tests in the class have run.
     * </pre>
     */
    @AfterClass
    public static void tearDownClass() {
        strToMatch = null;
    }

    /**
     * This test shows assertEquals(expected, actual)
     */
    @Test
    public void hello() {
        assertEquals("JUnit is working fine", strToMatch);
    }

    /**
     * How to skip a test. Use the @Ignore
     *
     * Best Practices say not to skip tests. Use for short term weeding out.
     */
    @Test
    @Ignore
    public void aSkippedTest() {
        // How to force fail a test for Test Driven Development.
        fail("You should not see this if @Ignore is in effect");
    }

    /**
     * If your method is supposed to throw Exception, then use this annotation.
     *
     * Sometimes your test will want to throw Exception to ensure that is does
     * throw the Exception.
     */
    @Test(expected = NullPointerException.class)
    public void testingExceptionThrown() {
        this.someFunctionThatThrowsException();
    }

    /**
     * Testing if code took too long to execute. Use timeout in @TEST
     * annotation.
     *
     * JUnit automatically catches exceptions. It considers uncaught exceptions
     * to be errors. Do NOT put try/catch in JUnit tests.
     *
     *
     * @throws java.lang.InterruptedException
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

    // This is just an example to show a method throwing an Exception.
    // This method would normally be in your source code, but shown here for 
    // learning.
    private boolean someFunctionThatThrowsException() {
        boolean result = false;

        if (true) { // always throw this exception as an example.
            throw new NullPointerException("This is just an example");
        }
        return (result);
    }

}
