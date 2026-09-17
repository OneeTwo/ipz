package com.ipz.bills;

public class Payment {

    private String type;
    private String details;
    private double amount;

    public Payment(String type, String details, double amount) {
        this.type = type;
        this.details = details;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}