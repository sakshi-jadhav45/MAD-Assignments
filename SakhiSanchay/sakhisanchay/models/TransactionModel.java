package com.android.sakhisanchay.models;

public class TransactionModel {
    public String title;
    public String date;
    public String amount;
    public int color; // To show Green for savings, Red for loans

    public TransactionModel(String title, String date, String amount, int color) {
        this.title = title;
        this.date = date;
        this.amount = amount;
        this.color = color;
    }
}