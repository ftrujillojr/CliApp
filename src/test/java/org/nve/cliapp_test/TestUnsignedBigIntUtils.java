package org.nve.cliapp_test;

// junit.framework.Test package is the legacy namespace used with Junit v3 and older versions of Java that do not support annotations.
// org.junit.Test is the new namespace used by JUnit v4 and requires Java v1.5 or later for its annotations.
import java.math.BigInteger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nve.cliapp.UnsignedBigIntUtils;
import org.nve.cliapp.UnsignedBigIntUtilsException;
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
public class TestUnsignedBigIntUtils {

    /**
     * Do not use the constructor to set up a test case. Use setupClass() method
     * below.
     *
     * The reason is the stack crashes if first test fails to instantiate.
     */
    public TestUnsignedBigIntUtils() {
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

    /**
     * Give your test methods meaningful names.
     */
    @Test
    //@Ignore
    public void testHex2BI() throws UnsignedBigIntUtilsException {
        // ARRANGE
        String data = "0xdead | beef | ffff | 0000 | ffff | 0000 | dead | beef";

        // ACT
        String expectMasked1 = "0xdead0000";
        BigInteger biResult = UnsignedBigIntUtils.maskBitRange(UnsignedBigIntUtils.toBI(data), 31, 16);
        String actualMasked1 = UnsignedBigIntUtils.toHexString(biResult);

        // ASSERT
        if (expectMasked1.equals(actualMasked1) == false) {
            System.out.println("expectMasked1 " + expectMasked1);
            System.out.println("actualMasked1 " + actualMasked1);
        }
        Assert.assertEquals(expectMasked1, actualMasked1);

        // ACT
        String expectPadded1 = "0x00000000dead0000";
        String actualPadded1 = UnsignedBigIntUtils.toPaddedHexString(biResult, 64);
        // ASSERT
        if (expectPadded1.equals(actualPadded1) == false) {
            System.out.println("expectPadded1 => " + expectPadded1);
            System.out.println("actualPadded1 => " + actualPadded1);
        }
        Assert.assertEquals(expectPadded1, actualPadded1);
        
        // ACT
        String expectFormatted1 = "0x0000 | 0000 | dead | 0000";
        String actualFormatted1 = UnsignedBigIntUtils.toFormattedHexString(biResult, 64);
        // ASSERT
        if (expectFormatted1.equals(actualFormatted1) == false) {
            System.out.println("expectFormatted1 => " + expectFormatted1);
            System.out.println("actualFormatted1 => " + actualFormatted1);
        }
        Assert.assertEquals(expectFormatted1, actualFormatted1);

    }

}
