package com.android.sakhisanchay.models;

public class User {
    public String uid;
    public String fullName;
    public String email;
    public String role;
    public String groupId; // Default can be "none"

    // 1. Empty Constructor (CRITICAL: Firebase needs this to convert data into this object)
    public User() {
    }

    // 2. Constructor for creating a new user during registration
    public User(String uid, String fullName, String email, String role) {
        this.uid = uid;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.groupId = "none"; // Initially, users might not belong to a group
    }

    public User(String uid, String fullName, String email, String role, String groupId) {
        this.uid = uid;
        this.fullName = fullName; // Use the parameter 'fullName'
        this.email = email;       // Use the parameter 'email'
        this.role = role;         // Use the parameter 'role'
        this.groupId = groupId;   // Use the parameter 'groupId'
    }
}
