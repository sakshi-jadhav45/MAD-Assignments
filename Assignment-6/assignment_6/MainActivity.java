package com.example.assignment_6;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etName = findViewById(R.id.etName);
        RadioGroup rgGender = findViewById(R.id.rgGender);
        ToggleButton toggleNotify = findViewById(R.id.toggleNotify);
        CheckBox cbTerms = findViewById(R.id.cbTerms);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            // Validate Checkbox
            if (!cbTerms.isChecked()) {
                Toast.makeText(this, "Please accept terms", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get Name
            String name = etName.getText().toString();

            // Get Selected Radio Button
            int selectedId = rgGender.getCheckedRadioButtonId();
            String gender = "Not Specified";
            if (selectedId != -1) {
                RadioButton rb = findViewById(selectedId);
                gender = rb.getText().toString();
            }

            // Get Toggle Status
            String notifyStatus = toggleNotify.isChecked() ? "Enabled" : "Disabled";

            // Final Result
            String message = "Registered: " + name + "\nGender: " + gender + "\nAlerts: " + notifyStatus;
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
    }
}