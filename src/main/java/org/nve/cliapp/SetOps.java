package org.nve.cliapp;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Set Operations for Arrays, Lists, and Sets of like types.
 * 
 * union()                         All elements in A or B.
 * intersect()                     All elements in A and B.
 * diff()                          Elements in A not in B.
 * relativeCompliment()            Elements in B not in A.
 * symmetricDiff()                 Elements in A or B, but not in intersect.
 * 
 * @param <T> Generic Type
 */
public class SetOps<T> {

    private Set<T> setA;
    private Set<T> setB;

    public SetOps(T[] arrayA, T[] arrayB) {
        this.setA = new TreeSet<>(Arrays.asList(arrayA));
        this.setB = new TreeSet<>(Arrays.asList(arrayB));
    }

    public SetOps(List<T> listA, List<T> listB) {
        this.setA = new TreeSet<>(listA);
        this.setB = new TreeSet<>(listB);
    }

    public SetOps(Set<T> setA, Set<T> setB) {
        this.setA = new TreeSet<>(setA);
        this.setB = new TreeSet<>(setB);
    }
    
    /**
     * <pre>
     * Union of the sets A and B is the set of all objects
     * that are a member of A, or B, or both.
     *
     *                  A             B                   Result
     * The union of {1, 2, 3} and {2, 3, 4} is the set {1, 2, 3, 4} .
     * </pre>
     *
     * @return Set&lt;T&gt;
     */
    public Set<T> union() {
        Set<T> resultList = new TreeSet<>(this.setA);
        resultList.addAll(this.setB);
        return (resultList);
    }

    /**
     * <pre>
     * Intersection of the sets A and B  is the set of all
     * objects that are members of both A and B.
     *
     *                         A             B                Result
     * The intersection of {1, 2, 3} and {2, 3, 4} is the set {2, 3}
     * </pre>
     *
     * @return Set&lt;T&gt; //
     */
    public Set<T> intersect() {
        Set<T> resultList = new TreeSet<>(this.setA);
        resultList.retainAll(this.setB);
        return (resultList);
    }
    
    /**
     * <pre>
     * Difference is NOT commutative !!!! A-B != B-A
     *
     * Set A =&gt; 0, 2, 4, 6, 8, 10
     *
     * Set B =&gt; 5, 6, 7, 8, 9, 10
     *
     * DIFF (elements of A not in B) =&gt; 0, 2, 4
     * </pre>
     *
     * @return Set&lt;T&gt;  //
     */
    public Set<T> diff() {
        Set<T> resultList = new TreeSet<>(this.setA);
        resultList.removeAll(this.setB);
        return (resultList);
    }
    
     /**
     * <pre>
     * If A and B are sets, then the relative complement of A in B is the set
     * of elements in B, but not in A.
     *
     * Set A =&gt; 0, 2, 4, 6, 8, 10
     *
     * Set B =&gt; 5, 6, 7, 8, 9, 10
     *
     * RELATIVE COMPLIMENT (elements of B not in A) =&gt; 5, 7, 9
     * </pre>
     *
     * @return Set&lt;T&gt;  //
     */
    public Set<T> relativeCompliment() {
        Set<T> resultList = new TreeSet<>(this.setB);
        resultList.removeAll(this.setA);
        return (resultList);
    }

    /**
     * <pre>
     * The symmetric difference of two sets is the set of elements which are
     * in either of the sets and not in their intersection.
     *
     * For example, the symmetric difference of the sets
     * {1,2,3} and {3,4} is {1,2,4}.
     * </pre>
     *
     * @return Set&lt;T&gt;   //
     */
    public Set<T> symmetricDiff() {
        Set<T> leftHandSide = new TreeSet<>(this.setA);
        leftHandSide.removeAll(this.setB);

        Set<T> rightHandSide = new TreeSet<>(this.setB);
        rightHandSide.removeAll(this.setA);

        // Combine the two diffs.
        Set<T> resultList = new TreeSet<>(leftHandSide);
        resultList.addAll(rightHandSide);

        return (resultList);
    }  

}
