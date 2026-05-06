package com.android.sakhisanchay.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.sakhisanchay.MemberAdapter;
import com.android.sakhisanchay.R;
import com.android.sakhisanchay.models.User;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GroupDetailsActivity extends AppCompatActivity {

    private String groupId, groupName;
    private TextView tvName, tvTotalSavings, tvGroupBalance; // Added tvGroupBalance
    private FirebaseFirestore db; // The Firebase Instance

    private RecyclerView rvPreview;
    private MemberAdapter adapter;
    private List<User> previewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        // 1. Initialize Firestore Instance
        db = FirebaseFirestore.getInstance();

        // 2. Get Data from Intent
        groupId = getIntent().getStringExtra("GROUP_ID");
        groupName = getIntent().getStringExtra("GROUP_NAME");

        // 3. Initialize UI Components
        tvName = findViewById(R.id.tvDetailGroupName);
        tvTotalSavings = findViewById(R.id.tvTotalMonthlySavings);
        tvGroupBalance = findViewById(R.id.tvGroupBalance); // Connect to XML ID

        tvName.setText(groupName);

        // 4. Setup Member Preview List
        rvPreview = findViewById(R.id.rvMemberPreview);
        rvPreview.setLayoutManager(new LinearLayoutManager(this));
        previewList = new ArrayList<>();
        adapter = new MemberAdapter(previewList);
        rvPreview.setAdapter(adapter);

        // 5. Initialize Action Cards
        MaterialCardView cardAddMember = findViewById(R.id.cardAddMember);
        MaterialCardView cardCollectSavings = findViewById(R.id.cardCollectSavings);
        MaterialCardView cardGiveLoan = findViewById(R.id.cardGiveLoan);

        // 6. Click Listeners
        cardAddMember.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddMemberActivity.class);
            intent.putExtra("GROUP_ID", groupId);
            startActivity(intent);
        });

        cardCollectSavings.setOnClickListener(v -> {
            Intent intent = new Intent(this, CollectSavingsActivity.class);
            intent.putExtra("GROUP_ID", groupId);
            startActivity(intent);
        });

        cardGiveLoan.setOnClickListener(v -> {
            Intent intent = new Intent(this, GiveLoanActivity.class);
            intent.putExtra("GROUP_ID", groupId);
            startActivity(intent);
        });

        findViewById(R.id.btnViewMembersList).setOnClickListener(v -> {
            Intent intent = new Intent(this, MembersListActivity.class);
            intent.putExtra("GROUP_ID", groupId);
            startActivity(intent);
        });

        // Run initial data fetch
        refreshDashboardData();
    }

    // This method groups all data fetching logic together
    private void refreshDashboardData() {
        fetchMonthlyTotal();
        calculateAvailableBalance();
        loadMemberPreview();
    }

    private void calculateAvailableBalance() {
        // First, get ALL savings
        db.collection("Collections")
                .whereEqualTo("groupId", groupId)
                .get()
                .addOnSuccessListener(savingsDocs -> {
                    double totalSavings = 0;
                    for (DocumentSnapshot d : savingsDocs) {
                        Double amt = d.getDouble("amount");
                        if (amt != null) totalSavings += amt;
                    }

                    // Second, get ALL loans and subtract
                    double finalSavings = totalSavings;
                    db.collection("Loans")
                            .whereEqualTo("groupId", groupId)
                            .get()
                            .addOnSuccessListener(loanDocs -> {
                                double totalLoans = 0;
                                for (DocumentSnapshot d : loanDocs) {
                                    Double amt = d.getDouble("loanAmount");
                                    if (amt != null) totalLoans += amt;
                                }

                                double netBalance = finalSavings - totalLoans;
                                tvGroupBalance.setText("Available Balance: ₹ " + String.format("%.2f", netBalance));

                                // Visual Warning for low balance
                                if (netBalance < 500) {
                                    tvGroupBalance.setTextColor(android.graphics.Color.RED);
                                } else {
                                    tvGroupBalance.setTextColor(android.graphics.Color.parseColor("#555555"));
                                }
                            });
                });
    }

    private void fetchMonthlyTotal() {
        String currentMonthYear = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(new Date());
        db.collection("Collections")
                .whereEqualTo("groupId", groupId)
                .whereEqualTo("monthYear", currentMonthYear)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    double total = 0;
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Double amount = doc.getDouble("amount");
                        if (amount != null) total += amount;
                    }
                    tvTotalSavings.setText("₹ " + String.format("%.2f", total));
                });
    }

    private void loadMemberPreview() {
        db.collection("Users").whereEqualTo("groupId", groupId).limit(5).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    previewList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        User user = doc.toObject(User.class);
                        if (user != null) previewList.add(user);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // This ensures the balance "drops" immediately after giving a loan
        refreshDashboardData();
    }
}