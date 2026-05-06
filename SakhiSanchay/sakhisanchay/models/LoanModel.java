package com.android.sakhisanchay.models;

public class LoanModel {
    public String loanId, memberId, memberName, groupId, date, status;
    public double loanAmount, interestRate, remainingAmount;

    public LoanModel() {}

    public LoanModel(String loanId, String memberId, String memberName, String groupId,
                     double loanAmount, double interestRate, String date) {
        this.loanId = loanId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.groupId = groupId;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.remainingAmount = loanAmount; // Initially, full amount is pending
        this.date = date;
        this.status = "ACTIVE"; // Status can be ACTIVE or PAID
    }
}