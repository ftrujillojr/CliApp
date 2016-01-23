package org.nve.cliapp_test;

// junit.framework.Test package is the legacy namespace used with Junit v3 and older versions of Java that do not support annotations.
// org.junit.Test is the new namespace used by JUnit v4 and requires Java v1.5 or later for its annotations.
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nve.cliapp.RSA;
import org.nve.cliapp.exceptions.RSAException;
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
public class TestRSA {

    private static String someString;   // If needed, declare here and use setUpClass() to initialize.

    /**
     * Do not use the constructor to set up a test case. Use setupClass() method
     * below.
     *
     * The reason is the stack crashes if first test fails to instantiate.
     */
    public TestRSA() {
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

    /**
     * Test RSA algorythm
     *
     * @throws org.nve.cliapp.exceptions.SysUtilsException
     * @throws org.nve.cliapp.exceptions.RSAException
     */
    @Test
    public void testRSA() throws SysUtilsException, RSAException {
        SysUtils.setVerbose(true);

//        RSA rsa = new RSA(Paths.get(SysUtils.getTmpDir(), "testRSA").toString(), "CliApp", 1024);
        RSA rsa = new RSA();
        
        String encryptedStr = rsa.encryptBase64("Hello");
        System.out.println("encryptedStr => " + encryptedStr);
        String decryptedStr = rsa.decryptBase64(encryptedStr);
        System.out.println("decryptedStr => " + decryptedStr);
        
        SysUtils.setVerbose(false);

    }

    private void exampleByteCopyBuffer() {
        byte[] bytes = {(byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07,
            (byte) 0xDE, (byte) 0xAD, (byte) 0xBE, (byte) 0xEF,
            (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03,
            (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD,
            (byte) 0xEE, (byte) 0xFF, (byte) 0xA5, (byte) 0x5A,
            (byte) 0x80, (byte) 0x11, (byte) 0x22};
        
        System.out.println("length " + bytes.length);
        SysUtils.displayHexDump(bytes);

        int blockSize = 3;
        int start = 0;

        // copy, act on buffer[blockSize] from bytes[]
        for (int ii = 0; ii < (bytes.length / blockSize); ii++) {
            int stop = start + blockSize - 1;
            byte[] buffer = java.util.Arrays.copyOfRange(bytes, start, stop + 1);
            SysUtils.displayHexDump(buffer);
            start = stop + 1;
        }

        // any remaining bytes
        if ((bytes.length % blockSize) > 0) {
            int stop = start + (bytes.length % blockSize) - 1;
            byte[] buffer = java.util.Arrays.copyOfRange(bytes, start, stop + 1);
            System.out.println("length => " + buffer.length);
            SysUtils.displayHexDump(buffer);
        }
    }
}
