package com.example.expensearo.JavaClasses;

public class User
{
    String name,email,password,profile_picture,gender;
    public User(){}
    public User(String name, String email, String password, String profile_picture, String gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profile_picture=profile_picture;
        this.gender=gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
