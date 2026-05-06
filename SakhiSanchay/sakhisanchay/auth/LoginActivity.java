package com.android.sakhisanchay.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.sakhisanchay.admin.AdminDashboardActivity;
import com.android.sakhisanchay.member.MemberDashboardActivity;
import com.android.sakhisanchay.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();
        // AUTO LOGIN: If user is already signed in, go straight to dashboard
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            checkUserRole(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        MaterialButton btnGoToRegister = findViewById(R.id.btnGoToRegister);

        btnLogin.setOnClickListener(v -> {
            String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
            String password = Objects.requireNonNull(etPassword.getText()).toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            checkUserRole(mAuth.getCurrentUser().getUid());
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(this, "Login Failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btnGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void checkUserRole(String uid) {
        db.collection("Users").document(uid).get().addOnSuccessListener(doc -> {
            progressDialog.dismiss();
            if (doc.exists()) {
                String role = doc.getString("role");
                Intent intent;
                if ("Admin".equals(role)) {
                    intent = new Intent(this, AdminDashboardActivity.class);
                } else {
                    intent = new Intent(this, MemberDashboardActivity.class);
                }
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
            }
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show();
        });
    }
}