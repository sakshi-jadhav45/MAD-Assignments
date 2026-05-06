package com.android.sakhisanchay.admin;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.sakhisanchay.R;
import com.android.sakhisanchay.models.CollectionModel;
import com.android.sakhisanchay.models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CollectSavingsActivity extends AppCompatActivity {

    private Spinner spinnerMembers;
    private EditText etAmount;
    private FirebaseFirestore db;
    private String groupId;
    private List<User> memberList = new ArrayList<>();
    private List<String> memberNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_savings);

        db = FirebaseFirestore.getInstance();
        groupId = getIntent().getStringExtra("GROUP_ID");

        spinnerMembers = findViewById(R.id.spinnerMembers);
        etAmount = findViewById(R.id.etSavingsAmount);
        Button btnSubmit = findViewById(R.id.btnSubmitSavings);

        loadMembers();

        btnSubmit.setOnClickListener(v -> validateAndCheck());
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
                    spinnerMembers.setAdapter(adapter);
                });
    }

    private void validateAndCheck() {
        String amountStr = etAmount.getText().toString();
        int selectedPos = spinnerMembers.getSelectedItemPosition();

        if (selectedPos == -1) {
            Toast.makeText(this, "Please select a member", Toast.LENGTH_SHORT).show();
            return;
        }
        if (amountStr.isEmpty()) {
            etAmount.setError("Enter amount");
            return;
        }

        User selectedMember = memberList.get(selectedPos);
        String currentMonthYear = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(new Date());

        // Step 1: Check if this member already has a record for this month
        db.collection("Collections")
                .whereEqualTo("memberId", selectedMember.uid)
                .whereEqualTo("monthYear", currentMonthYear)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Member already paid!
                        Toast.makeText(this, selectedMember.fullName + " has already paid for " + currentMonthYear, Toast.LENGTH_LONG).show();
                    } else {
                        // Safe to save
                        performSave(selectedMember, Double.parseDouble(amountStr), currentMonthYear);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void performSave(User member, double amount, String monthYear) {
        String collectionId = UUID.randomUUID().toString();
        String fullDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        CollectionModel collection = new CollectionModel(
                collectionId,
                member.uid,
                member.fullName,
                groupId,
                amount,
                fullDate,
                monthYear
        );

        db.collection("Collections").document(collectionId).set(collection)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Savings recorded for " + monthYear, Toast.LENGTH_SHORT).show();
                    finish(); // Go back to dashboard
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show());
    }
}