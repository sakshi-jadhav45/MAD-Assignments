package com.android.sakhisanchay.member;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.sakhisanchay.R;
import com.android.sakhisanchay.TransactionAdapter;
import com.android.sakhisanchay.models.TransactionModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<TransactionModel> historyList = new ArrayList<>();
    private TransactionAdapter adapter;
    private FirebaseFirestore db;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        db = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getUid();

        recyclerView = findViewById(R.id.rvHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionAdapter(historyList);
        recyclerView.setAdapter(adapter);

        loadCombinedHistory();
    }

    private void loadCombinedHistory() {
        // 1. Fetch Savings
        db.collection("Collections").whereEqualTo("memberId", uid).get()
                .addOnSuccessListener(savings -> {
                    for (DocumentSnapshot d : savings) {
                        historyList.add(new TransactionModel(
                                "Monthly Saving (" + d.getString("monthYear") + ")",
                                d.getString("date"),
                                "+ ₹ " + d.getDouble("amount"),
                                Color.parseColor("#2E7D32") // Green
                        ));
                    }

                    // 2. Fetch Loans Taken
                    db.collection("Loans").whereEqualTo("memberId", uid).get()
                            .addOnSuccessListener(loans -> {
                                for (DocumentSnapshot d : loans) {
                                    historyList.add(new TransactionModel(
                                            "Loan Disbursed",
                                            d.getString("date"),
                                            "- ₹ " + d.getDouble("loanAmount"),
                                            Color.parseColor("#C62828") // Red
                                    ));
                                }

                                // Sort by date logic would go here
                                adapter.notifyDataSetChanged();
                            });
                });
    }
}