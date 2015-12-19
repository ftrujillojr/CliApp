package org.nve.cliapp;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

/**
 * http://www.javacreed.com/gson-deserialiser-example/
 *
 * http://www.leveluplunch.com/java/examples/convert-json-array-to-arraylist-gson/
 *
 * 1) Create class that => implements JsonDeserializer<T>
 * 2) Implement abstract method deserialize().
 *    a. Get jsonObject from jsonElement.
 *    b. Stuff tmp vars from jsonObject.
 *       1.  DEFER the deserialization of other objects as needed.
 *    c. Populate object.
 *    d. Return object.
 */

public class PersonDeserializer implements JsonDeserializer<Person> {

    public PersonDeserializer() {
    }

    @Override
    public Person deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        
        // Stuff tmp vars from jsonObject.
        String firstName = jsonObject.get("firstName").getAsString();
        String lastName = jsonObject.get("lastName").getAsString();
        Integer age = jsonObject.get("age").getAsInt();
        Double salary = jsonObject.get("salary").getAsDouble();
        Boolean isStudent = jsonObject.get("isStudent").getAsBoolean();
        
        // DEFER the deserialization of other objects as needed.
        //
        // ProjectCategory projectCategory = context.deserialize(
        //         jsonObject.get("projectCategory"),
        //         ProjectCategory.class);
        
        // Populate object.
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAge(age);
        person.setSalary(salary);
        person.setIsStudent(isStudent);

        // Return object
        return(person);
    }
}
