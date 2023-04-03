package com.example.medicinetrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ReminderEditorActivity extends AppCompatActivity {

    Button addReminderButton;
    Button deleteReminderButton;

    EditText editMedNameEditText;

    // CREATE, UPDATE, DELETE
    public final static String ACTION_KEY = "ACTION_KEY";

    public final static String MED_NAME_KEY = "MED_NAME_KEY";
    public final static String POSITION_KEY = "POSITION_KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_editor);
        editMedNameEditText = findViewById(R.id.edit_medName_editText);
        addReminderButton = findViewById(R.id.add_reminder_button);
        deleteReminderButton = findViewById(R.id.delete_reminder_button);

        Intent intent = getIntent();
        String action = intent.getStringExtra(ACTION_KEY);
        int position = intent.getIntExtra(POSITION_KEY,-1);

        String medName = intent.getStringExtra(MED_NAME_KEY);

        // TODO SET DATA TO NOTE DATA
        if (action.equals("UPDATE")) {
            editMedNameEditText.setText(medName);
        }



        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String medName = editMedNameEditText.getText().toString();
                Toast.makeText(getApplicationContext(), medName, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ReminderEditorActivity.this, MainActivity.class);

                if (action.equals("CREATE") ) {
                    intent.putExtra(ACTION_KEY, "CREATE");


                }

                if (action.equals("UPDATE") ) {intent.putExtra(ACTION_KEY, "UPDATE");}




                intent.putExtra(POSITION_KEY,position);

                intent.putExtra(MED_NAME_KEY, medName);

                startActivity(intent);
            }});

        deleteReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReminderEditorActivity.this,MainActivity.class);
                intent.putExtra(ACTION_KEY,"DELETE");
                intent.putExtra(POSITION_KEY,position);

                startActivity(intent);
            }
        });
    }
}