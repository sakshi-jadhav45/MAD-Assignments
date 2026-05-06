package com.android.sakhisanchay.models;

public class Group {
    public String groupId, groupName, adminUid;
    public double monthlyContribution;

    public Group() {} // Required for Firestore

    public Group(String groupId, String groupName, String adminUid, double monthlyContribution) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.adminUid = adminUid;
        this.monthlyContribution = monthlyContribution;
    }
}