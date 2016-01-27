package org.nve.cliapp_test;

// junit.framework.Test package is the legacy namespace used with Junit v3 and older versions of Java that do not support annotations.
// org.junit.Test is the new namespace used by JUnit v4 and requires Java v1.5 or later for its annotations.
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.nve.cliapp.RSA;
import org.nve.cliapp.exceptions.RSAException;
import org.nve.cliapp.utils.SHAUtils;
import org.nve.cliapp.utils.SysUtils;
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
public class TestSHAUtils {

    /**
     * Do not use the constructor to set up a test case. Use setupClass() method
     * below.
     *
     * The reason is the stack crashes if first test fails to instantiate.
     */
    public TestSHAUtils() {
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
     * Only testing the String versions. The FILE versions are the same, but are
     * hard to put in a test because when the file is changed, then so follows
     * the SHA-256.
     *
     * http://hash.online-convert.com/sha256-generator
     *
     * @throws java.io.IOException
     */
    @Test
    public void testGetSHA256ForFile() throws IOException {
//        String salt = SHAUtils.generateSalt();
        String salt = "4wFKHJ6YHpcFtyPjVqxt1CZ/ml5xA22AVI9PqYKjGYM=";
        String myString = "Hello World This is a very long password with 1204-F6^%";
        String sha256Str = SHAUtils.getSHA256HashWithSalt(myString, salt);
        String expectData = "224a2d453be6c107a6b0e8619ee1faee1e0bc2a697aab01aa99065b22438740b";
        

        if (expectData.equals(sha256Str) == false) {
            System.out.println("SALT   => " + salt);
            System.out.println("Expect => " + expectData);
            System.out.println("Actual => " + sha256Str);
            System.out.println("===============");
            
        }

        assertEquals(expectData, sha256Str);
    }
    
    @Test
    public void testGenerateStrongPasswordHash() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String myString = "Hello World This is a very long password with 1204-F6^%";
        String strongHash = SHAUtils.generateStrongPasswordHash(myString);
        System.out.println("Strong hash => " + strongHash);
        
        boolean isValid = SHAUtils.validatePassword("Hello World This is a very long password with 1204-F6^%", strongHash);
        if(isValid == false) {
            System.out.println("NO MATCH");
        }
        
        assertTrue(isValid);

        // I just changed the first character to lowercase.
        isValid = SHAUtils.validatePassword("hello World This is a very long password with 1204-F6^%", strongHash);
        
        if(isValid == true) {  // This one should be invalid
            System.out.println("MATCH");
        } 
        assertFalse(isValid);
    }

}
