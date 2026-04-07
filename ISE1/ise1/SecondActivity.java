package com.example.ise1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second1);

        // 1. Java Programming Lab
        setupLab(R.id.lab1Layout, R.drawable.lab1,
                "Java Programming Lab\n\nFocuses on Object-Oriented Programming (OOP) concepts. " +
                        "Key topics include Inheritance, Polymorphism, Interfaces, and Exception Handling using Java.");

        // 2. Advanced Database System Lab
        setupLab(R.id.lab2Layout, R.drawable.lab2,
                "Advanced Database System Lab\n\nCovers complex database topics like Query Optimization, " +
                        "Transaction Management, Concurrency Control, and Distributed Databases.");

        // 3. Database Engineering Lab
        setupLab(R.id.lab3Layout, R.drawable.lab3,
                "Database Engineering Lab\n\nPractical application of SQL, ER-Modeling, and Normalization. " +
                        "Learn to design robust database schemas and write complex stored procedures.");

        // 4. R Programming Lab
        setupLab(R.id.lab4Layout, R.drawable.lab4,
                "R Programming Lab\n\nDedicated to Statistical Computing and Graphics. " +
                        "Learn data manipulation, calculation, and graphical display using the R language.");

        // 5. Project Lab
        setupLab(R.id.lab5Layout, R.drawable.lab5,
                "Project Lab\n\nA collaborative workspace for building Capstone projects. " +
                        "Includes System Design, Agile methodology, and full-stack development cycles.");

        // 6. Research Lab
        setupLab(R.id.lab6Layout, R.drawable.lab6,
                "Research Lab\n\nFocuses on advanced experimentation in AI, Machine Learning, " +
                        "and Cyber Security. Aimed at innovation and technical paper publications.");

        // 7. Web Technology Lab
        setupLab(R.id.lab7Layout, R.drawable.lab7,
                "Web Technology Lab\n\nLearn to build modern websites using HTML5, CSS3, " +
                        "JavaScript, and frameworks like React or Node.js for full-stack capabilities.");

        // 8. Python Programming Lab
        setupLab(R.id.lab8Layout, R.drawable.lab8,
                "Python Programming Lab\n\nMaster Python for automation, data science, and scripting. " +
                        "Covers libraries like NumPy, Pandas, and Matplotlib.");

        // 9. C Programming Lab
        setupLab(R.id.lab9Layout, R.drawable.lab9,
                "C Programming Lab\n\nThe foundation of computer science. Focuses on Memory Management, " +
                        "Pointers, Structures, and low-level system programming.");
    }

    private void setupLab(int layoutId, final int imageRes, final String desc) {
        View layout = findViewById(layoutId);
        if (layout != null) {
            layout.setOnClickListener(v -> {
                Intent intent = new Intent(SecondActivity.this, LabDetailActivity.class);
                intent.putExtra("image", imageRes);
                intent.putExtra("description", desc);
                startActivity(intent);
            });
        }
    }
}