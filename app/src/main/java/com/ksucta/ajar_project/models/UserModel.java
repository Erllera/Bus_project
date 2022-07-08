package com.ksucta.ajar_project.models;

public class UserModel {
    private String email, name, city, number;

    public UserModel(String name, String city, String number) {
        this.name = name;
        this.city = city;
        this.number = number;
    }

    public UserModel(String name, String city, String number, String email) {
        this.name = name;
        this.city = city;
        this.number = number;
        this.email = email;
    }

    public UserModel() {

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


}
