package com.ksucta.ajar_project.models;

public class TicketSample {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TicketSample(String id, String from, String to, String time, String number, String driverInfo, long date) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.time = time;
        this.number = number;
        this.driverInfo = driverInfo;
        this.date = date;
    }

    private String from;
    private String to;
    private String time;
    private String number;
    private String driverInfo;
    private long date;

    public TicketSample() {
    }

    public TicketSample(String from, String to, String time, String number, String driverInfo, long date) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.number = number;
        this.driverInfo = driverInfo;
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(String driverInfo) {
        this.driverInfo = driverInfo;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
