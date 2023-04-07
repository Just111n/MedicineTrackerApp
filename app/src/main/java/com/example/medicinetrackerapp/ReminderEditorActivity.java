package com.example.medicinetrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReminderEditorActivity extends AppCompatActivity implements View.OnClickListener {

    /* Views Section */
    Button medNotificationTimeButton, medNotificationTimeButton2, addReminderButton, deleteReminderButton;
    EditText editMedNameEditText;

    ImageButton pills_ImageButton;
    ImageButton syrup_ImageButton;
    ImageButton injection_ImageButton;
    String notificationTime;
    /* End Views Section */

    String medType;
    EditText editDosageEditText;



    public final static String ACTION_KEY = "ACTION_KEY", CREATE = "CREATE", UPDATE = "UPDATE", DELETE = "DELETE";

    public final static String MED_NAME_KEY = "MED_NAME_KEY";// ASK WHAT THIS IS
    public final static String POSITION_KEY = "POSITION_KEY";
    public final static String MED_NOTIFICATION_TIMES_KEY = "NOTIFICATION_TIME_KEY";

    public final static  String MED_DOSAGE_KEY = "MED_DOSAGE_KEY";
    public  final static String MED_TYPE_KEY = "MED_TYPE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_editor);


        editMedNameEditText = findViewById(R.id.edit_medName_editText);
        pills_ImageButton = findViewById(R.id.pills_ImageButton);
        syrup_ImageButton = findViewById(R.id.syrup_ImageButton);
        injection_ImageButton = findViewById(R.id.injection_ImageButton);
        editDosageEditText = findViewById(R.id.Dosage_edittext);
        medNotificationTimeButton = findViewById(R.id.med_notification_time_button);
        medNotificationTimeButton2 = findViewById(R.id.med_notification_time_button2);
        addReminderButton = findViewById(R.id.add_reminder_button);
        deleteReminderButton = findViewById(R.id.delete_reminder_button);


        Intent intent = getIntent();
        String action = intent.getStringExtra(ACTION_KEY);
        int position = intent.getIntExtra(POSITION_KEY,-1);
        ArrayList<String> medNotificationTimes = intent.getStringArrayListExtra(MED_NOTIFICATION_TIMES_KEY);
        String medName = intent.getStringExtra(MED_NAME_KEY);//ASK what does this do
        String medType = intent.getStringExtra(MED_TYPE_KEY);
        String medDosage = intent.getStringExtra(MED_DOSAGE_KEY);


        // TODO SET DATA TO NOTE DATA
        if (action.equals(UPDATE)) {
            editMedNameEditText.setText(medName);
            medNotificationTimeButton.setText(medNotificationTimes.get(0));
            medNotificationTimeButton2.setText(medNotificationTimes.get(1));
            medType.trim();
            editDosageEditText.setText(medDosage);

            addReminderButton.setText("Update Reminder");

        }

        /* Button Functionality Section */
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

        pills_ImageButton.setOnClickListener(this);
        syrup_ImageButton.setOnClickListener(this);
        injection_ImageButton.setOnClickListener(this);



        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String medName = editMedNameEditText.getText().toString();
                String medNotificationTime = medNotificationTimeButton.getText().toString().trim();
                String medNotificationTime2 = medNotificationTimeButton2.getText().toString().trim();
                String medType = ReminderEditorActivity.this.medType.toString();
                String medDosage = editDosageEditText.getText().toString();
                try {
                    setAlarm(medName, medNotificationTime);
                } catch (ParseException e) {
                    Log.d("testing","error");
                }


                ArrayList<String> medNotificationTimes = new ArrayList<>();
                medNotificationTimes.add(medNotificationTime);
                medNotificationTimes.add(medNotificationTime2);




                if (action.equals(CREATE)) {
                    Toast.makeText(getApplicationContext(), medName  +" added successfully", Toast.LENGTH_LONG).show();
                }
                if (action.equals(UPDATE)) {
                    Toast.makeText(getApplicationContext(), medName  +" updated successfully", Toast.LENGTH_LONG).show();
                }


                // TODO 1.0 submit data from ReminderEditorActivity to MainActivity
                Intent intent = new Intent(ReminderEditorActivity.this, MainActivity.class);
                intent.putExtra(ACTION_KEY, action);
                intent.putExtra(POSITION_KEY,position);
                intent.putExtra(MED_NAME_KEY, medName);
                intent.putExtra(MED_NOTIFICATION_TIMES_KEY,medNotificationTimes);
                intent.putExtra(MED_TYPE_KEY, medType);
                intent.putExtra(MED_DOSAGE_KEY, medDosage);

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
        /* End Button Functionality Section */

    }// END of onCreate

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pills_ImageButton:
                Toast.makeText(this, "Medicine is a pill", Toast.LENGTH_SHORT).show();
                medType = "Pills";
                break;
            case R.id.syrup_ImageButton:
                Toast.makeText(this, "Medicine is syrup type", Toast.LENGTH_SHORT).show();
                medType ="Syrup";
                break;
            case R.id.injection_ImageButton:
                Toast.makeText(this, "Medicine is an injection", Toast.LENGTH_SHORT).show();
                medType = "Injection";
                break;
        }
    }

    /* Select Time Section */
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
    /* End Select Time Section */

    private void setAlarm(String medName, String time) throws ParseException {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);   //assigining alaram manager object to set alaram

        //sending data to alarm class to create channel and notification
        Intent intent = new Intent(getApplicationContext(), AlarmBroadcast.class);
        intent.putExtra(AlarmBroadcast.Event_KEY, medName);
        Log.d("testing","setAlarm can accesss medName:"+medName);


        intent.putExtra("time", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // TODO notifications are instant pop off

        Calendar calendar = Calendar.getInstance();


//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.MINUTE, minute);
        Date date1 = calendar.getTime();


        alarmManager.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);



    }


}


