package com.ksucta.ajar_project.models;

public class InfoModel {
    String date, driver, number;

    public InfoModel() {
    }

    public InfoModel(String date, String driver, String number) {
        this.date = date;
        this.driver = driver;
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
