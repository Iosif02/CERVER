package com.example.cerver.Classes;

import android.util.Log;

public class UserDetails {
    private String Name;
    private String Age;
    private String Weight;
    private String Height;
    private String Sex;

    public UserDetails(String name, String age, String weight, String height, String sex) {
        Name = name;
        Age = age;
        Weight = weight;
        Height = height;
        Sex = sex;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }
}
