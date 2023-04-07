package com.example.medicinetrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DrugView extends AppCompatActivity {

    private TextView drugName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_view);

        drugName = findViewById(R.id.drug_name);
        String name = getIntent().getStringExtra(ReminderEditorActivity.MED_NAME_KEY);  // getting med name
        drugName.setText(name);
    }
}