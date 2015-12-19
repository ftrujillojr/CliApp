package org.nve.cliapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        firstName = null;
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

    // http://javarevisited.blogspot.sg/2011/10/override-hashcode-in-java-example.html
    // http://martinaharris.com/2009/10/testing-java-equals-and-hashcode-methods-essential/
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.firstName);
        hash = 31 * hash + Objects.hashCode(this.lastName);
        hash = 31 * hash + this.age;
        hash = 31 * hash + (int) (Double.doubleToLongBits(this.salary) ^ (Double.doubleToLongBits(this.salary) >>> 32));
        hash = 31 * hash + (this.isStudent ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (this.age != other.age) {
            return false;
        }
        if (Double.doubleToLongBits(this.salary) != Double.doubleToLongBits(other.salary)) {
            return false;
        }
        if (this.isStudent != other.isStudent) {
            return false;
        }
        return true;
    }

    
    
    public static List<Person> fromJson(String jsonResults) throws JsonUtilsException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();

        gsonBuilder.registerTypeAdapter(
                Person.class,
                new PersonDeserializer());

        // more Deserializers here if Person contains other objects.
        Gson gson = gsonBuilder.create();
        Type collectionType = new TypeToken<ArrayList<Person>>() {
        }.getType();

        List<Person> projectList = null;

        try {
            projectList = gson.fromJson(jsonResults, collectionType);
        } catch (JsonParseException ex) {
            String msg = ex.getMessage();
            msg += "\n" + jsonResults;
            throw new JsonUtilsException(msg);
        }
        return projectList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%25s => %s\n", "firstName", this.firstName));
        sb.append(String.format("%25s => %s\n", "lastName", this.lastName));
        sb.append(String.format("%25s => %d\n", "age", this.age));
        sb.append(String.format("%25s => %f\n", "salary", this.salary));
        sb.append(String.format("%25s => %s\n", "isStudent", this.isStudent));

        return (sb.toString());
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
