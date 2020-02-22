package com.example.cerver.Classes;

public class Task {
    private String Title;
    private String Description;
    private String Difficulty;
    private String Experience;

    public Task (String title, String description, String difficulty, String experience) {
        Title = title;
        Description = description;
        Difficulty = difficulty;
        Experience = experience;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDifficulty() {
        return Difficulty;
    }

    public void setDifficulty(String difficulty) {
        Difficulty = difficulty;
    }

    public String getExperience() {
        return Experience;
    }

    public void setExperience(String experience) {
        Experience = experience;
    }
}
