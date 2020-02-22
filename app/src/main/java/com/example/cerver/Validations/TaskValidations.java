package com.example.cerver.Validations;

import java.util.HashMap;
import java.util.Map;

public class TaskValidations {
    public Map validate(String title, String description, String difficulty, String experience) {
        Map<String, String> errors = new HashMap<>();

        if(title.isEmpty()) {
            errors.put("title", "The title is required");
        }
        if(description.isEmpty()) {
            errors.put("description", "The description is required");
        }
        if(difficulty.isEmpty()) {
            errors.put("difficulty", "The difficulty is required");
        }
        if(experience.isEmpty()) {
            errors.put("experience", "The experience is required");
        }
        return errors;
    }
}
