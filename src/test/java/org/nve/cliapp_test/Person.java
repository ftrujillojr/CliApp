package org.nve.cliapp_test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.nve.cliapp.JsonUtilsException;

// Serializability of a class is enabled by the class implementing the java.io.Serializable interface. 
        
public class Person implements Serializable {
    // serialVersionUID, which is used during deserialization to verify that the sender 
    // and receiver of a serialized object have loaded classes for that object that are 
    // compatible with respect to serialization
    private static final long serialVersionUID = 01L;  
    
    private String firstName;
    private String lastName;
    private int age;
    private double salary;
    private boolean isStudent;

    public Person() { // A no-arg constructor is needed if class is serializable.
        super();
        this.firstName = null;
        this.lastName = "";
        this.age = -1;
        this.salary = 0.0;
        this.isStudent = false;
    }

    public Person(String firstName, String lastName, int age, double salary, boolean isStudent) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.salary = salary;
        this.isStudent = isStudent;
    }

    // http://javarevisited.blogspot.sg/2011/10/override-hashcode-in-java-example.html
    // http://martinaharris.com/2009/10/testing-java-equals-and-hashcode-methods-essential/
    //
    // 1) Write class, implement Serializable
    // 2) Add attributes and serialVersionUID
    // 3) ALT+INSERT  Generate Setters/Getters
    // 4) ALT+INSERT  Generate hashCode/equals
    
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
    
    /**
     * Showing how to deserialize fromJson.   see PersonDeserializer.java
     * 
     * @param jsonResults
     * @return
     * @throws JsonUtilsException 
     */
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
