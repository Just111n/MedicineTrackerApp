package com.example.medicinetrackerapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReminderEditorActivity extends AppCompatActivity {

    /* Views Section */
    Button medNotificationTimeButton, medNotificationTimeButton2, addReminderButton, deleteReminderButton;
    EditText editMedNameEditText;

    ImageButton pills_ImageButton, syrup_ImageButton, injection_ImageButton;

    String notificationTime;
    /* End Views Section */


    EditText editDosageEditText;



    public final static String ACTION_KEY = "ACTION_KEY", CREATE = "CREATE", UPDATE = "UPDATE", DELETE = "DELETE";

    public final static String MED_NAME_KEY = "MED_NAME_KEY";// ASK WHAT THIS IS
    public final static String POSITION_KEY = "POSITION_KEY";
    public final static String MED_NOTIFICATION_TIMES_KEY = "NOTIFICATION_TIME_KEY";

    public final static  String MED_DOSAGE_KEY = "MED_DOSAGE_KEY";
    public  final static String MED_TYPE_KEY = "MED_TYPE_KEY";

    boolean isPillsButtonClicked = false;
    boolean isSyrupButtonClicked = false;
    boolean isInjectionButtonClicked = false;


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
            switch (medType) {
                case "pills":
                    pills_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.purple_theme_dark)));

                    break;
                case "syrup":
                    syrup_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.purple_theme_dark)));

                    break;
                case "injection":
                    injection_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.purple_theme_dark)));

                    break;

            }
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
        // TODO Image Buttons


        pills_ImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!isPillsButtonClicked) {
                    // If the button is already clicked, set the background color to the original color
                    isPillsButtonClicked = true;
                    pills_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.purple_theme_dark)));


                    isSyrupButtonClicked = false;
                    syrup_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));

                    isInjectionButtonClicked = false;
                    injection_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));


                } else {
                    // If the button is not clicked, set the background color to the new color
                    pills_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));
                    isPillsButtonClicked = false;
                }


            }
        });
        syrup_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSyrupButtonClicked) {

                    isPillsButtonClicked = false;
                    pills_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));


                    isSyrupButtonClicked = true;
                    syrup_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.purple_theme_dark)));

                    isInjectionButtonClicked = false;
                    injection_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));

                } else {
                    // If the button is not clicked, set the background color to the new color
                    syrup_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));
                    isSyrupButtonClicked = false;
                }


            }
        });
        injection_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInjectionButtonClicked) {

                    isPillsButtonClicked = false;
                    pills_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));


                    isSyrupButtonClicked = false;
                    syrup_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));

                    isInjectionButtonClicked = true;
                    injection_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.purple_theme_dark)));

                } else {

                    injection_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));
                    isInjectionButtonClicked = false;
                }


            }
        });



        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String medName = editMedNameEditText.getText().toString();
                String medNotificationTime = medNotificationTimeButton.getText().toString().trim();
                String medNotificationTime2 = medNotificationTimeButton2.getText().toString().trim();



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
                Log.d("imageButton",String.valueOf(isPillsButtonClicked));
                if (isPillsButtonClicked) {
                    intent.putExtra(MED_TYPE_KEY,"pills");
                    Log.d("imageButton","if statement is running");
                }

                if (isSyrupButtonClicked) {
                    intent.putExtra(MED_TYPE_KEY,"syrup");
                }
                if (isInjectionButtonClicked) {
                    intent.putExtra(MED_TYPE_KEY,"injection");
                }

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
        //assigning alarm manager object to set alarm
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //sending data to alarm class to create channel and notification
        Intent intent = new Intent(getApplicationContext(), AlarmBroadcast.class);
        intent.putExtra(AlarmBroadcast.Event_KEY, medName);



        intent.putExtra(AlarmBroadcast.TIME_KEY, time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);





        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        Date date = sdf.parse(time);
        Calendar calendar1 = Calendar.getInstance();
        assert date != null;
        calendar1.setTime(date);
        int hour = calendar1.get(Calendar.HOUR_OF_DAY);
        int minute = calendar1.get(Calendar.MINUTE);

        // get current date and time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
//        calendar.set(Calendar.SECOND, 0);



        long alarmTime = calendar.getTimeInMillis();

        Log.d("testing","setAlarm can access date:"+ calendar.getTime());
        Log.d("testing","setAlarm can access date:"+ alarmTime);

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);




    }


}


