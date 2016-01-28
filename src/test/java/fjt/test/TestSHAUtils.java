package fjt.test;

// junit.framework.Test package is the legacy namespace used with Junit v3 and older versions of Java that do not support annotations.
// org.junit.Test is the new namespace used by JUnit v4 and requires Java v1.5 or later for its annotations.
import java.io.IOException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import fjt.utils.SHAUtils;
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

    private static String someShortPassword;
    private static String someLongPassword;
    private static String filename;

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
        someShortPassword = "Hello123World";
        someLongPassword = "Hello World This is a very long password with 1204-F6^% Mary had a little lamb whose fleece was as white as snow.  Everywhere Mary went the lamb was sure to go.";
        filename = Paths.get("src", "main", "java", "org", "nve", "cliapp", "utils", "SHAUtils.java").toString();
    }

    /**
     * If you have to clear up resources like closing database, then do it here.
     */
    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void testGenerateSalt() {
        String salt1 = SHAUtils.generateSalt();
        String salt2 = SHAUtils.generateSalt();

        //System.out.println("SALT1 => " + salt1);
        //System.out.println("SALT2 => " + salt2 + "\n");
        assertNotEquals(salt1, salt2);

    }

    @Test
    public void testGenerateSHA256HashForFile() throws IOException {
        String sha256Hash = SHAUtils.generateSHA256HashForFile(filename);
        //System.out.println("SHA256 Hash for File => " + sha256Hash + "   File: " + filename);

        boolean isValid = SHAUtils.validateSHA256ForFile(filename, sha256Hash);
        if (isValid == false) {
            System.out.println("NO MATCH - this should have matched.");
        }

        assertTrue(isValid);
    }

    @Test
    public void testGenerateSHA512HashForFile() throws IOException {
        String sha512Hash = SHAUtils.generateSHA512HashForFile(filename);
        //System.out.println("SHA512 Hash for File => " + sha512Hash + "   File: " + filename);

        boolean isValid = SHAUtils.validateSHA512ForFile(filename, sha512Hash);
        if (isValid == false) {
            System.out.println("NO MATCH - this should have matched.");
        }

        assertTrue(isValid);

        //System.out.println("HEX => " + SHAUtils.hashToHex(sha512Hash));
    }

    @Test
    public void testGenerateSHA256() throws IOException {
        String sha256Hash = SHAUtils.generateSHA256Hash(someShortPassword);
        //System.out.println("SHA256 " + sha256Hash.length()+ " => " + sha256Hash);

        boolean isValid = SHAUtils.validateSHA256(someShortPassword, sha256Hash);
        if (isValid == false) {
            System.out.println("NO MATCH - this should have matched.");
        }

        assertTrue(isValid);
        
        sha256Hash = SHAUtils.generateSHA256Hash(someLongPassword);
        //System.out.println("SHA256 " + sha256Hash.length()+ " => " + sha256Hash);

        isValid = SHAUtils.validateSHA256(someLongPassword, sha256Hash);
        if (isValid == false) {
            System.out.println("NO MATCH - this should have matched.");
        }

        assertTrue(isValid);
        
    }

    @Test
    public void testGenerateSHA512() throws IOException {
        String sha512Hash = SHAUtils.generateSHA512Hash(someShortPassword);
        //System.out.println("SHA512 " + sha512Hash.length() + " => " + sha512Hash);

        boolean isValid = SHAUtils.validateSHA512(someShortPassword, sha512Hash);
        if (isValid == false) {
            System.out.println("NO MATCH - this should have matched.");
        }

        assertTrue(isValid);
        
        sha512Hash = SHAUtils.generateSHA512Hash(someLongPassword);
        //System.out.println("SHA512 " + sha512Hash.length() + " => " + sha512Hash);

        isValid = SHAUtils.validateSHA512(someLongPassword, sha512Hash);
        if (isValid == false) {
            System.out.println("NO MATCH - this should have matched.");
        }
        
        assertTrue(isValid);
    }

    @Test
    public void testGeneratePBKDF2Hash() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String pbkdf2Hash = SHAUtils.generatePBKDF2Hash(someShortPassword);
        //System.out.println("PBKDF2 "+ pbkdf2Hash.length() + " => " + pbkdf2Hash);

        boolean isValid = SHAUtils.validatePBKDF2(someShortPassword, pbkdf2Hash);
        if (isValid == false) {
            System.out.println("NO MATCH - this should have matched.");
        }

        assertTrue(isValid);

        // I just changed the first character to lowercase.
        isValid = SHAUtils.validatePBKDF2("hello World This is a very long password with 1204-F6^%", pbkdf2Hash);

        if (isValid == true) {
            System.out.println("MATCH - this one should be invalid.");
        }
        assertFalse(isValid);
    }

}
