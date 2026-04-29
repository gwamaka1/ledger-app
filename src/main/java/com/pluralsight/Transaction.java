package com.pluralsight;

public class Transaction {
    private String date;
    private String time;
    private String description;
    private String vendor;
    private double transaction;

    public Transaction(String date, String time, String description, String vendor, double transaction) {
        this.time = time;
        this.date = date;
        this.description = description;
        this.vendor = vendor;
        this.transaction = transaction;
    }

    public double getTransaction() {
        return transaction;
    }

    public void setTransaction(double transaction) {
        this.transaction = transaction;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String companyName) {
        this.vendor = vendor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
