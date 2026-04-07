package com.example.ise1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginbtn = findViewById(R.id.button);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("user") && pass.equals("user@123")) {

                    Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    startActivity(intent);

                } else {

                    Toast.makeText(MainActivity.this,"Credentials are incorrect",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}