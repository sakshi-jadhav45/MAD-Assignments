package com.android.sakhisanchay.admin;



import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.sakhisanchay.R;
import com.android.sakhisanchay.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddMemberActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPass;
    private FirebaseFirestore db;
    private String currentGroupId; // We get this from the previous screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        db = FirebaseFirestore.getInstance();

        // Receive the Group ID from the Admin Dashboard
        currentGroupId = getIntent().getStringExtra("GROUP_ID");

        etName = findViewById(R.id.etMemberName);
        etEmail = findViewById(R.id.etMemberEmail);
        etPass = findViewById(R.id.etMemberPassword);
        Button btnAdd = findViewById(R.id.btnAddMemberSubmit);

        btnAdd.setOnClickListener(v -> saveMember());
    }

    private void saveMember() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();

        if(name.isEmpty() || email.isEmpty() || pass.length() < 6) {
            Toast.makeText(this, "Check details!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Logic: We create a user in Auth, then save their profile with the GroupID
        if(name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = task.getResult().getUser().getUid();

                        // CRITICAL: Ensure 'name' and 'role' are passed here!
                        User newMember = new User(uid, name, email, "Member", currentGroupId);

                        db.collection("Users").document(uid).set(newMember)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Member Added Successfully!", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error saving to DB: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "Auth Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    }

