package com.android.sakhisanchay.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.sakhisanchay.R;
import com.android.sakhisanchay.models.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.UUID;

public class CreateGroupActivity extends AppCompatActivity {

    private EditText etGroupName, etAmount;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        db = FirebaseFirestore.getInstance();
        etGroupName = findViewById(R.id.etGroupName);
        etAmount = findViewById(R.id.etMonthlyAmount);
        Button btnSave = findViewById(R.id.btnSaveGroup);

        btnSave.setOnClickListener(v -> {
            String name = etGroupName.getText().toString();
            String amountStr = etAmount.getText().toString();

            if (!name.isEmpty() && !amountStr.isEmpty()) {
                String groupId = UUID.randomUUID().toString(); // Generate unique ID
                String adminUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                double amount = Double.parseDouble(amountStr);

                Group newGroup = new Group(groupId, name, adminUid, amount);

                db.collection("Groups").document(groupId).set(newGroup)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Group Created!", Toast.LENGTH_SHORT).show();
                            finish(); // Go back to Dashboard
                        });
            }
        });
    }
}