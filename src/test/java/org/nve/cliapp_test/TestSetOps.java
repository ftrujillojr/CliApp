package org.nve.cliapp_test;

// junit.framework.Test package is the legacy namespace used with Junit v3 and older versions of Java that do not support annotations.
// org.junit.Test is the new namespace used by JUnit v4 and requires Java v1.5 or later for its annotations.
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nve.cliapp.SetOps;
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
public class TestSetOps {

    // DATA
    private static Integer[] intArrayA = {0, 2, 4, 6, 8, 10};
    private static Integer[] intArrayB = {5, 6, 7, 8, 9, 10};

    private static String[] strArrayA = {"dog", "cat", "house", "pig"};
    private static String[] strArrayB = {"horse", "dog", "cow", "chicken", "pig"};

    private static List<Integer> intListA;
    private static List<Integer> intListB;
    
    private static List<String> strListA;
    private static List<String> strListB;

    private static Set<Integer> intSetA;
    private static Set<Integer> intSetB;

    private static Set<String> strSetA;
    private static Set<String> strSetB;

    // EXPECT
    private static Set<Integer> unionIntExpect;
    private static Set<Integer> intersectIntExpect;
    private static Set<Integer> diffIntExpect;
    private static Set<Integer> relativeComplimentIntExpect;
    private static Set<Integer> symmetricDiffIntExpect;

    private static Set<String> unionStrExpect;
    private static Set<String> intersectStrExpect;
    private static Set<String> diffStrExpect;
    private static Set<String> relativeComplimentStrExpect;
    private static Set<String> symmetricDiffStrExpect;

    /**
     * Do not use the constructor to set up a test case. Use setupClass() method
     * below.
     *
     * The reason is the stack crashes if first test fails to instantiate.
     */
    public TestSetOps() {
        // Best practice => Do not put anything here.
    }

    /**
     * Sometimes several tests need to share computationally expensive setup
     * like logging into a database. If you need to do an initialization once
     * before testing all methods.
     */
    @BeforeClass
    public static void setUpClass() {
        intListA = new ArrayList<>(Arrays.asList(intArrayA));
        intListB = new ArrayList<>(Arrays.asList(intArrayB));
        strListA = new ArrayList<>(Arrays.asList(strArrayA));
        strListB = new ArrayList<>(Arrays.asList(strArrayB));
        intSetA = new TreeSet<>(Arrays.asList(intArrayA));
        intSetB = new TreeSet<>(Arrays.asList(intArrayB));
        strSetA = new TreeSet<>(Arrays.asList(strArrayA));
        strSetB = new TreeSet<>(Arrays.asList(strArrayB));

        Integer[] intArrayUnion = {0, 2, 4, 5, 6, 7, 8, 9, 10};
        Integer[] intArrayIntersect = {6, 8, 10};
        Integer[] intArrayDiff = {0, 2, 4};
        Integer[] intArrayRelativeCompliment = {5, 7, 9};
        Integer[] intArraySymmetricDiff = {0, 2, 4, 5, 7, 9};

        unionIntExpect = new TreeSet<>(Arrays.asList(intArrayUnion));
        intersectIntExpect = new TreeSet<>(Arrays.asList(intArrayIntersect));
        diffIntExpect = new TreeSet<>(Arrays.asList(intArrayDiff));
        relativeComplimentIntExpect = new TreeSet<>(Arrays.asList(intArrayRelativeCompliment));
        symmetricDiffIntExpect = new TreeSet<>(Arrays.asList(intArraySymmetricDiff));

        String[] strArrayUnion = {"cat", "chicken", "cow", "dog", "horse", "house", "pig"};
        String[] strArrayIntersect = {"dog", "pig"};
        String[] strArrayDiff = {"cat", "house"};
        String[] strArrayRelativeCompliment = {"chicken", "cow", "horse"};
        String[] strArraySymmetricDiff = {"cat", "chicken", "cow", "horse", "house"};

        unionStrExpect = new TreeSet<>(Arrays.asList(strArrayUnion));
        intersectStrExpect = new TreeSet<>(Arrays.asList(strArrayIntersect));
        diffStrExpect = new TreeSet<>(Arrays.asList(strArrayDiff));
        relativeComplimentStrExpect = new TreeSet<>(Arrays.asList(strArrayRelativeCompliment));
        symmetricDiffStrExpect = new TreeSet<>(Arrays.asList(strArraySymmetricDiff));
    }

    /**
     * If you have to clear up resources like closing database, then do it here.
     */
    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void test_intArray() {
        // ARRANGE
        SetOps<Integer> setOps = new SetOps<>(intArrayA, intArrayB);

        // ACT
        Set<Integer> unionResult = setOps.union();
        Set<Integer> intersectResult = setOps.intersect();
        Set<Integer> diffResult = setOps.diff();
        Set<Integer> relativeComplimentResult = setOps.relativeCompliment();
        Set<Integer> symmetricDiffResult = setOps.symmetricDiff();

        // ASSERT
        assertEquals(unionIntExpect, unionResult);
        assertEquals(intersectIntExpect, intersectResult);
        assertEquals(diffIntExpect, diffResult);
        assertEquals(relativeComplimentIntExpect, relativeComplimentResult);
        assertEquals(symmetricDiffIntExpect, symmetricDiffResult);
    }
    
    @Test
    public void test_intList() {
        // ARRANGE
        SetOps<Integer> setOps = new SetOps<>(intListA, intListB);

        // ACT
        Set<Integer> unionResult = setOps.union();
        Set<Integer> intersectResult = setOps.intersect();
        Set<Integer> diffResult = setOps.diff();
        Set<Integer> relativeComplimentResult = setOps.relativeCompliment();
        Set<Integer> symmetricDiffResult = setOps.symmetricDiff();

        // ASSERT
        assertEquals(unionIntExpect, unionResult);
        assertEquals(intersectIntExpect, intersectResult);
        assertEquals(diffIntExpect, diffResult);
        assertEquals(relativeComplimentIntExpect, relativeComplimentResult);
        assertEquals(symmetricDiffIntExpect, symmetricDiffResult);
    }
    
    @Test
    public void test_intSet() {
        // ARRANGE
        SetOps<Integer> setOps = new SetOps<>(intSetA, intSetB);

        // ACT
        Set<Integer> unionResult = setOps.union();
        Set<Integer> intersectResult = setOps.intersect();
        Set<Integer> diffResult = setOps.diff();
        Set<Integer> relativeComplimentResult = setOps.relativeCompliment();
        Set<Integer> symmetricDiffResult = setOps.symmetricDiff();

        // ASSERT
        assertEquals(unionIntExpect, unionResult);
        assertEquals(intersectIntExpect, intersectResult);
        assertEquals(diffIntExpect, diffResult);
        assertEquals(relativeComplimentIntExpect, relativeComplimentResult);
        assertEquals(symmetricDiffIntExpect, symmetricDiffResult);
    }
    
    @Test
    public void test_strArray() {
        // ARRANGE
        SetOps<String> setOps = new SetOps<>(strArrayA, strArrayB);

        // ACT
        Set<String> unionResult = setOps.union();
        Set<String> intersectResult = setOps.intersect();
        Set<String> diffResult = setOps.diff();
        Set<String> relativeComplimentResult = setOps.relativeCompliment();
        Set<String> symmetricDiffResult = setOps.symmetricDiff();

        // ASSERT
        assertEquals(unionStrExpect, unionResult);
        assertEquals(intersectStrExpect, intersectResult);
        assertEquals(diffStrExpect, diffResult);
        assertEquals(relativeComplimentStrExpect, relativeComplimentResult);
        assertEquals(symmetricDiffStrExpect, symmetricDiffResult);
    }

    @Test
    public void test_strList() {
        // ARRANGE
        SetOps<String> setOps = new SetOps<>(strListA, strListB);

        // ACT
        Set<String> unionResult = setOps.union();
        Set<String> intersectResult = setOps.intersect();
        Set<String> diffResult = setOps.diff();
        Set<String> relativeComplimentResult = setOps.relativeCompliment();
        Set<String> symmetricDiffResult = setOps.symmetricDiff();

        // ASSERT
        assertEquals(unionStrExpect, unionResult);
        assertEquals(intersectStrExpect, intersectResult);
        assertEquals(diffStrExpect, diffResult);
        assertEquals(relativeComplimentStrExpect, relativeComplimentResult);
        assertEquals(symmetricDiffStrExpect, symmetricDiffResult);
    }
    
    @Test
    public void test_strSet() {
        // ARRANGE
        SetOps<String> setOps = new SetOps<>(strSetA, strSetB);

        // ACT
        Set<String> unionResult = setOps.union();
        Set<String> intersectResult = setOps.intersect();
        Set<String> diffResult = setOps.diff();
        Set<String> relativeComplimentResult = setOps.relativeCompliment();
        Set<String> symmetricDiffResult = setOps.symmetricDiff();

        // ASSERT
        assertEquals(unionStrExpect, unionResult);
        assertEquals(intersectStrExpect, intersectResult);
        assertEquals(diffStrExpect, diffResult);
        assertEquals(relativeComplimentStrExpect, relativeComplimentResult);
        assertEquals(symmetricDiffStrExpect, symmetricDiffResult);
    }
}
