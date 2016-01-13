package org.nve.cliapp_test;

import java.util.Comparator;

/**
 * Comparator interface is used when you want 
 *     1) many different ways to sort.  (need a class implementing Comparator for each case)
 *     2) or you do not have control over source code of Object.  e.g. Person in this example
 * 
 * Usage:   
 *       List&lt;Person&gt; personList = ArrayList&lt;&gt;();
 *       Collections.sort(personList, new PersonSortByAgeSalaryComparator());
 */
public class PersonSortByAgeSalaryComparator implements Comparator<Person> {

    public PersonSortByAgeSalaryComparator() {
    }

    @Override
    public int compare(Person o1, Person o2) {
        int comp = Integer.compare(o1.getAge(), o2.getAge());

        // for String(s) use compareTo to return -1,0,1
        //comp = this.lastName.compareTo(obj.lastName);
        
        if(comp == 0) {
            comp = Double.compare(o1.getSalary(), o2.getSalary());
        }
        return comp;
    }

}
