package com.example.medicinetrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class ReminderEditorActivity extends AppCompatActivity {

    Button medNotificationTimeButton;
    Button medNotificationTimeButton2;
    Button addReminderButton;
    Button deleteReminderButton;
    EditText editMedNameEditText;

    String notificationTime;



    // CREATE, UPDATE, DELETE
    public final static String ACTION_KEY = "ACTION_KEY";
    public final static String CREATE = "CREATE";
    public final static String UPDATE = "UPDATE";
    public final static String DELETE = "DELETE";
    public final static String MED_NAME_KEY = "MED_NAME_KEY";
    public final static String POSITION_KEY = "POSITION_KEY";

    public final static String MED_NOTIFICATION_TIMES_KEY = "NOTIFICATION_TIME_KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_editor);


        editMedNameEditText = findViewById(R.id.edit_medName_editText);
        medNotificationTimeButton = findViewById(R.id.med_notification_time_button);
        medNotificationTimeButton2 = findViewById(R.id.med_notification_time_button2);
        addReminderButton = findViewById(R.id.add_reminder_button);
        deleteReminderButton = findViewById(R.id.delete_reminder_button);

        Intent intent = getIntent();
        String action = intent.getStringExtra(ACTION_KEY);
        int position = intent.getIntExtra(POSITION_KEY,-1);
        ArrayList<String> medNotificationTimes = intent.getStringArrayListExtra(MED_NOTIFICATION_TIMES_KEY);

        String medName = intent.getStringExtra(MED_NAME_KEY);

        // TODO SET DATA TO NOTE DATA
        if (action.equals(UPDATE)) {
            editMedNameEditText.setText(medName);

            medNotificationTimeButton.setText(medNotificationTimes.get(0));
            medNotificationTimeButton2.setText(medNotificationTimes.get(1));


            addReminderButton.setText("Update Reminder");

        }




        medNotificationTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime(medNotificationTimeButton);
            }
        });
        medNotificationTimeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime(medNotificationTimeButton2);
            }
        });



        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String medName = editMedNameEditText.getText().toString();
                String medNotificationTime = medNotificationTimeButton.getText().toString().trim();
                String medNotificationTime2 = medNotificationTimeButton2.getText().toString().trim();


                ArrayList<String> medNotificationTimes = new ArrayList<>();
                medNotificationTimes.add(medNotificationTime);
                medNotificationTimes.add(medNotificationTime2);




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
                intent.putExtra(MED_NOTIFICATION_TIMES_KEY,medNotificationTimes);
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

    private void selectTime(Button button) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                notificationTime = i + ":" + i1; //temp variable to store the time to set alarm

                button.setText(FormatTime(i, i1));                                                //sets the button text as selected time
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    private String FormatTime(int hour, int minute) {
        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }


}


