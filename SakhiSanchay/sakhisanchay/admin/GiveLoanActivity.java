package com.android.sakhisanchay.admin;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.sakhisanchay.R;
import com.android.sakhisanchay.models.LoanModel;
import com.android.sakhisanchay.models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class GiveLoanActivity extends AppCompatActivity {
    private Spinner spinner;
    private EditText etAmount, etInterest;
    private FirebaseFirestore db;
    private String groupId;
    private List<User> memberList = new ArrayList<>();
    private List<String> memberNames = new ArrayList<>();
    private TextView tvAvailableFunds;
    private double currentAvailableCash = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_loan);

        // 1. Initialize Firebase and Data FIRST
        db = FirebaseFirestore.getInstance();
        groupId = getIntent().getStringExtra("GROUP_ID");

        // 2. Initialize Views
        tvAvailableFunds = findViewById(R.id.tvAvailableInLoanScreen);
        spinner = findViewById(R.id.spinnerLoanMembers);
        etAmount = findViewById(R.id.etLoanAmount);
        etInterest = findViewById(R.id.etInterestRate);
        Button btnSubmit = findViewById(R.id.btnSubmitLoan);

        // 3. Now it is safe to call database methods
        fetchAvailableBalance();
        loadMembers();

        btnSubmit.setOnClickListener(v -> checkBalanceAndIssueLoan());
    }

    private void fetchAvailableBalance() {
        if (groupId == null) return;

        db.collection("Collections").whereEqualTo("groupId", groupId).get()
                .addOnSuccessListener(savingsDocs -> {
                    double totalSavings = 0;
                    for (DocumentSnapshot d : savingsDocs) {
                        Double amt = d.getDouble("amount");
                        if (amt != null) totalSavings += amt;
                    }

                    double finalSavings = totalSavings;
                    db.collection("Loans").whereEqualTo("groupId", groupId).get()
                            .addOnSuccessListener(loanDocs -> {
                                double totalLoans = 0;
                                for (DocumentSnapshot d : loanDocs) {
                                    Double amt = d.getDouble("loanAmount");
                                    if (amt != null) totalLoans += amt;
                                }

                                currentAvailableCash = finalSavings - totalLoans;
                                tvAvailableFunds.setText("Available Funds: ₹ " + String.format("%.2f", currentAvailableCash));

                                if (currentAvailableCash <= 0) {
                                    tvAvailableFunds.setTextColor(android.graphics.Color.RED);
                                } else {
                                    tvAvailableFunds.setTextColor(android.graphics.Color.parseColor("#388E3C")); // Green
                                }
                            });
                });
    }

    private void loadMembers() {
        db.collection("Users")
                .whereEqualTo("groupId", groupId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    memberList.clear();
                    memberNames.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        User user = doc.toObject(User.class);
                        if (user != null) {
                            memberList.add(user);
                            memberNames.add(user.fullName);
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, memberNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                });
    }

    private void checkBalanceAndIssueLoan() {
        String amountStr = etAmount.getText().toString().trim();
        String interestStr = etInterest.getText().toString().trim();
        int selectedPos = spinner.getSelectedItemPosition();

        if (amountStr.isEmpty() || interestStr.isEmpty() || selectedPos == -1) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double requestedAmount = Double.parseDouble(amountStr);
        double interestRate = Double.parseDouble(interestStr);
        User borrower = memberList.get(selectedPos);

        // Safety Check using the cached currentAvailableCash
        if (requestedAmount > currentAvailableCash) {
            Toast.makeText(this, "Insufficient Funds! Available: ₹" + currentAvailableCash, Toast.LENGTH_LONG).show();
        } else {
            saveLoanToFirestore(borrower, requestedAmount, interestRate);
        }
    }

    private void saveLoanToFirestore(User borrower, double amount, double interest) {
        String loanId = UUID.randomUUID().toString();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        LoanModel loan = new LoanModel(
                loanId,
                borrower.uid,
                borrower.fullName,
                groupId,
                amount,
                interest,
                date
        );

        db.collection("Loans").document(loanId).set(loan)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Loan of ₹" + amount + " disbursed to " + borrower.fullName, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to disburse: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}