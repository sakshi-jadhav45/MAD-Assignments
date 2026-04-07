package com.example.ise1;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LabDetailActivity extends AppCompatActivity {

    ImageView labImage;
    TextView labDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_detail);

        labImage = findViewById(R.id.labImage);
        labDescription = findViewById(R.id.labDescription);

        int image = getIntent().getIntExtra("image", 0);
        String description = getIntent().getStringExtra("description");

        labImage.setImageResource(image);
        labDescription.setText(description);
    }
}