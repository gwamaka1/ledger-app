package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;

    @Override
    public String toString() {
        return String.format("%-12s | %-10s | %-35s | %-25s | %10.2f",
                date.format(DATE_FMT),
                time.format(TIME_FMT),
                description,
                vendor,
                amount);


    }

    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.time = time;
        this.date = date;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public double getTransaction() {
        return amount;
    }

    public void setTransaction(double transaction) {
        this.amount = amount;
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

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}


