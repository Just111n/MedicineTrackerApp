package com.example.medicinetrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DrugViewActivity extends AppCompatActivity {

    private TextView drugName;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_view);

        drugName = findViewById(R.id.drug_name);
        String name = getIntent().getStringExtra(ReminderEditorActivity.MED_NAME_KEY);  // getting med name
        drugName.setText(name);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(DrugViewActivity.this, LoginActivity.class));
        }
    }
}