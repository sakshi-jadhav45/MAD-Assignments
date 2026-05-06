package com.android.sakhisanchay.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.sakhisanchay.admin.AdminDashboardActivity;
import com.android.sakhisanchay.member.MemberDashboardActivity;
import com.android.sakhisanchay.R;
import com.android.sakhisanchay.models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPassword;
    private RadioGroup rgRole;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Account...");

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        rgRole = findViewById(R.id.rgRole);
        android.widget.Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> handleRegistration());
    }

    private void handleRegistration() {
        String name = Objects.requireNonNull(etName.getText()).toString().trim();
        String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(etPassword.getText()).toString().trim();

        int selectedRoleId = rgRole.getCheckedRadioButtonId();
        if (selectedRoleId == -1) {
            Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show();
            return;
        }

        final String role = selectedRoleId == R.id.rbAdmin ? "Admin" : "Member";

        if (name.isEmpty() || email.isEmpty() || password.length() < 6) {
            Toast.makeText(this, "Check inputs (Password min 6 chars)", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                User user = new User(uid, name, email, role);

                db.collection("Users").document(uid).set(user).addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, role.equals("Admin") ? AdminDashboardActivity.class : MemberDashboardActivity.class));
                    finish();
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
                progressDialog.dismiss();
                String error = task.getException() != null ? task.getException().getMessage() : "Unknown Error";
                Toast.makeText(this, "Failed: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}

