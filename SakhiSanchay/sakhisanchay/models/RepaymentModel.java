package com.android.sakhisanchay.models;

public class RepaymentModel {
    public String repaymentId;
    public String loanId;
    public String memberId;
    public String memberName;
    public double interestPaid;
    public double principalPaid; // Usually 0 unless they repay the whole loan
    public String monthYear;     // e.g., "May 2026"
    public String date;

    public RepaymentModel() {} // Required for Firestore

    public RepaymentModel(String repaymentId, String loanId, String memberId, String memberName,
                          double interestPaid, double principalPaid, String monthYear, String date) {
        this.repaymentId = repaymentId;
        this.loanId = loanId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.interestPaid = interestPaid;
        this.principalPaid = principalPaid;
        this.monthYear = monthYear;
        this.date = date;
    }
}