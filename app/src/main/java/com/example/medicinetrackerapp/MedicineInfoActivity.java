package com.example.medicinetrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MedicineInfoActivity extends AppCompatActivity {

    private TextView tvDrugName;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_info);

        mAuth = FirebaseAuth.getInstance();


        tvDrugName = findViewById(R.id.tv_drug_name);
        String name = getIntent().getStringExtra(ReminderEditorActivity.MED_NAME_KEY);  // getting med name
        tvDrugName.setText(name);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MedicineInfoActivity.this, LoginActivity.class));
        }
    }
}