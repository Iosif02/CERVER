package com.example.cerver.Validations;

import java.util.HashMap;
import java.util.Map;

public class SignUpValidations {
    public Map validate(String email, String pwd, String userName, String userAge, String userWeight, String userHeight, String userSex) {
        Map<String, String> errors = new HashMap<>();

        if(email.isEmpty()) {
            errors.put("email", "The email is required");
        }
        if(pwd.isEmpty()) {
            errors.put("password", "The password is required");
        }
        if(userSex.isEmpty() || userSex.equals("Sex")) {
            errors.put("sex", "The sex is required");
        }
        if(userWeight.isEmpty()) {
            errors.put("weight", "The weight is required");
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
