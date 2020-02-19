package com.example.cerver.Validations;

import java.util.HashMap;
import java.util.Map;

public class UpdateDetailsValidations {

    public Map validate(String userName, String userAge, String userWeight, String userHeight) {
        Map<String, String> errors = new HashMap<>();

        if(userWeight.isEmpty()) {
            errors.put("name", "The name is required");
        }
        if(userHeight.isEmpty()) {
            errors.put("height", "The height is required");
        }
        if(userAge.isEmpty()) {
            errors.put("age", "The age is required");
        }
        if(userName.isEmpty()) {
            errors.put("name", "The name is required");
        }
        return errors;
    }
}
