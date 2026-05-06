package com.android.sakhisanchay.member;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.sakhisanchay.R;
import com.android.sakhisanchay.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class MemberDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome, tvTotalSavings, tvLoanBalance;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_dashboard);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize Views
        tvWelcome = findViewById(R.id.tvWelcomeMember);
        tvTotalSavings = findViewById(R.id.tvTotalSavings);
        tvLoanBalance = findViewById(R.id.tvLoanBalance);

        loadUserData();

        // Logout Setup
        findViewById(R.id.btnLogoutMember).setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // History Setup - FIXED: Added intent to HistoryActivity
        findViewById(R.id.btnViewHistory).setOnClickListener(v -> {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    private void loadUserData() {
        if (mAuth.getCurrentUser() == null) return;
        String uid = mAuth.getCurrentUser().getUid();

        // 1. Get Profile Name
        db.collection("Users").document(uid).get().addOnSuccessListener(document -> {
            if (document.exists()) {
                String name = document.getString("fullName");
                tvWelcome.setText("Welcome, " + name);

                // 2. Fetch Financials
                fetchPersonalSavings(uid);
                fetchPersonalLoanStatus(uid);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Profile load failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchPersonalSavings(String uid) {
        db.collection("Collections")
                .whereEqualTo("memberId", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    double total = 0;
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Double amount = doc.getDouble("amount");
                        if (amount != null) total += amount;
                    }
                    tvTotalSavings.setText(String.format(Locale.getDefault(), "₹ %.2f", total));
                });
    }

    private void fetchPersonalLoanStatus(String uid) {
        db.collection("Loans")
                .whereEqualTo("memberId", uid)
                .whereEqualTo("status", "ACTIVE")
                .get()
                .addOnSuccessListener(loans -> {
                    if (!loans.isEmpty()) {
                        // Assuming one active loan at a time
                        DocumentSnapshot loanDoc = loans.getDocuments().get(0);
                        Double amount = loanDoc.getDouble("loanAmount");
                        double originalLoanAmount = (amount != null) ? amount : 0;
                        String loanId = loanDoc.getId();

                        // Subtract repayments made
                        calculateRemainingBalance(loanId, originalLoanAmount);
                    } else {
                        tvLoanBalance.setText("₹ 0.00");
                    }
                });
    }

    private void calculateRemainingBalance(String loanId, double originalAmount) {
        db.collection("Repayments")
                .whereEqualTo("loanId", loanId)
                .get()
                .addOnSuccessListener(repays -> {
                    double totalPrincipalPaid = 0;
                    for (DocumentSnapshot r : repays) {
                        Double paid = r.getDouble("principalPaid");
                        if (paid != null) totalPrincipalPaid += paid;
                    }
                    double netBalance = originalAmount - totalPrincipalPaid;
                    tvLoanBalance.setText(String.format(Locale.getDefault(), "₹ %.2f", netBalance));
                });
    }
}