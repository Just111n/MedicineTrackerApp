package com.example.medicinetrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    public final static String CREATE = "CREATE";
    public final static String UPDATE = "UPDATE";
    public final static String DELETE = "DELETE";
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
        if (action.equals(UPDATE)) {
            editMedNameEditText.setText(medName);
            addReminderButton.setText("Update Reminder");
        }



        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String medName = editMedNameEditText.getText().toString();
                if (action.equals(CREATE)) {
                    Toast.makeText(getApplicationContext(), medName  +" added successfully", Toast.LENGTH_LONG).show();
                }
                if (action.equals(UPDATE)) {
                    Toast.makeText(getApplicationContext(), medName  +" updated successfully", Toast.LENGTH_LONG).show();
                }


                Intent intent = new Intent(ReminderEditorActivity.this, MainActivity.class);


                intent.putExtra(ACTION_KEY, action);

                intent.putExtra(POSITION_KEY,position);
                intent.putExtra(MED_NAME_KEY, medName);

                startActivity(intent);
            }});

        deleteReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(ReminderEditorActivity.this)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this reminder?")
                        .setIcon(R.drawable.ic_app)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(ReminderEditorActivity.this,MainActivity.class);
                                intent.putExtra(ACTION_KEY,DELETE);
                                intent.putExtra(POSITION_KEY,position);
                                startActivity(intent);

                            }
                        }).setNegativeButton("No",null).show();

            }
        });
    }
}