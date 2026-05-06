package com.android.sakhisanchay.models;

public class CollectionModel {
    public String collectionId, memberId, memberName, groupId, date, monthYear;
    public double amount;

    public CollectionModel() {}

    public CollectionModel(String collectionId, String memberId, String memberName, String groupId, double amount, String date, String monthYear) {
        this.collectionId = collectionId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.groupId = groupId;
        this.amount = amount;
        this.date = date;
        this.monthYear = monthYear; // New Field
    }
}