package org.nve.cliapp;

/*
 * SVN information
 * $Revision:$
 * $Author:$
 * $Date:$
 * $HeadURL:$
 *
 */
public class Person {

    private String firstName;
    private String lastName;
    private int age;
    private double salary;
    private boolean isStudent;

    public Person() {
        firstName = "";
        lastName = "";
        age = -1;
        salary = 0.0;
        isStudent = false;
    }

    public Person(String firstName, String lastName, int age, double salary, boolean isStudent) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.salary = salary;
        this.isStudent = isStudent;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public boolean isIsStudent() {
        return isStudent;
    }

    public void setIsStudent(boolean isStudent) {
        this.isStudent = isStudent;
    }
}
