package com.arcfit.murasaki;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlanActivity extends AppCompatActivity {
    private Button btnPlanInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plan);

        btnPlanInput = findViewById(R.id.addExercise_btn);

        if (LoginActivity.loggedAccount == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        btnPlanInput.setOnClickListener(v -> {
            Intent intent = new Intent(PlanActivity.this, PlanInputActivity.class);
            startActivity(intent);
        });
    }
}