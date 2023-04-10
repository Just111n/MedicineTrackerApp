package com.example.medicinetrackerapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReminderEditorActivity extends AppCompatActivity {

    public static final String MED_ID_KEY = "MED_ID_KEY";

    private Button medNotificationTimeButton, medNotificationTimeButton2, addReminderButton, deleteReminderButton;
    private EditText editMedNameEditText, editDosageEditText;

    private ImageButton pills_ImageButton, syrup_ImageButton, injection_ImageButton;

    String notificationTime;

    public final static String ACTION_KEY = "ACTION_KEY", CREATE = "CREATE", UPDATE = "UPDATE", DELETE = "DELETE";

    public final static String MED_NAME_KEY = "MED_NAME_KEY";
    public final static String MED_NOTIFICATION_TIMES_KEY = "NOTIFICATION_TIME_KEY";
    public final static  String MED_DOSAGE_KEY = "MED_DOSAGE_KEY";
    public  final static String MED_TYPE_KEY = "MED_TYPE_KEY";

    boolean isPillsButtonSelected;
    boolean isSyrupButtonSelected;
    boolean isInjectionButtonSelected;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_editor);

        mAuth = FirebaseAuth.getInstance();
         isPillsButtonSelected = false;
         isSyrupButtonSelected = false;
         isInjectionButtonSelected = false;


        editMedNameEditText = findViewById(R.id.edit_medName_editText);
        pills_ImageButton = findViewById(R.id.pills_ImageButton);
        syrup_ImageButton = findViewById(R.id.syrup_ImageButton);
        injection_ImageButton = findViewById(R.id.injection_ImageButton);
        editDosageEditText = findViewById(R.id.Dosage_edittext);
        medNotificationTimeButton = findViewById(R.id.med_notification_time_button);
        medNotificationTimeButton2 = findViewById(R.id.med_notification_time_button2);
        addReminderButton = findViewById(R.id.add_reminder_button);
        deleteReminderButton = findViewById(R.id.delete_reminder_button);



        // From MainActivity
        Intent intent = getIntent();
        String action = intent.getStringExtra(ACTION_KEY);
        String medId = intent.getStringExtra(MED_ID_KEY);
        ArrayList<String> medNotificationTimes = intent.getStringArrayListExtra(MED_NOTIFICATION_TIMES_KEY);
        String medName = intent.getStringExtra(MED_NAME_KEY);

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
                    isPillsButtonSelected = true;

                    break;
                case "syrup":
                    syrup_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.purple_theme_dark)));
                    isSyrupButtonSelected = true;

                    break;
                case "injection":
                    injection_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.purple_theme_dark)));
                    isInjectionButtonSelected = true;
                    break;

            }
            editDosageEditText.setText(medDosage);

            addReminderButton.setText(R.string.update_reminder);

        }

        /* Button Functionality Section */
        medNotificationTimeButton.setOnClickListener(view -> selectTime(medNotificationTimeButton));
        medNotificationTimeButton2.setOnClickListener(view -> selectTime(medNotificationTimeButton2));
        // TODO Image Buttons


        pills_ImageButton.setOnClickListener(view -> {
            if (!isPillsButtonSelected) {
                // If the button is already clicked, set the background color to the original color
                isPillsButtonSelected = true;
                pills_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.purple_theme_dark)));


                isSyrupButtonSelected = false;
                syrup_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));

                isInjectionButtonSelected = false;
                injection_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));


            } else {
                // If the button is not clicked, set the background color to the new color
                pills_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));
                isPillsButtonSelected = false;
            }


        });
        syrup_ImageButton.setOnClickListener(view -> {
            if (!isSyrupButtonSelected) {

                isPillsButtonSelected = false;
                pills_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));


                isSyrupButtonSelected = true;
                syrup_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.purple_theme_dark)));

                isInjectionButtonSelected = false;
                injection_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));

            } else {
                // If the button is not clicked, set the background color to the new color
                syrup_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));
                isSyrupButtonSelected = false;
            }


        });
        injection_ImageButton.setOnClickListener(view -> {
            if (!isInjectionButtonSelected) {

                isPillsButtonSelected = false;
                pills_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));


                isSyrupButtonSelected = false;
                syrup_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));

                isInjectionButtonSelected = true;
                injection_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.purple_theme_dark)));

            } else {

                injection_ImageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));
                isInjectionButtonSelected = false;
            }


        });



        addReminderButton.setOnClickListener(view -> {
            String medName1 = editMedNameEditText.getText().toString();
            String medNotificationTime = medNotificationTimeButton.getText().toString().trim();
            String medNotificationTime2 = medNotificationTimeButton2.getText().toString().trim();
            String medType1 = "";
            if (isPillsButtonSelected) {
                medType1 = "pills";

            }

            if (isSyrupButtonSelected) {
                medType1 = "syrup";
            }
            if (isInjectionButtonSelected) {
                medType1 ="injection";
            }


            String medDosage1 = editDosageEditText.getText().toString();



            try {
                setAlarm(medName1, medNotificationTime, medDosage1);
            } catch (ParseException e) {
                Log.d("testing","error");
            }


            ArrayList<String> medNotificationTimes1 = new ArrayList<>();
            medNotificationTimes1.add(medNotificationTime);
            medNotificationTimes1.add(medNotificationTime2);

            // TODO 1.0 submit data from ReminderEditorActivity to MainActivity



            /* Check action Section */
            switch (action) {
                case CREATE:
                    ReminderModel newReminderModel = new ReminderModel();
                    newReminderModel.setMedName(medName1);
                    newReminderModel.setMedNotificationTimes(medNotificationTimes1);
                    newReminderModel.setMedDosage(medDosage1);
                    newReminderModel.setMedType(medType1);
                    String keyToAdd = MainActivity.mbase.push().getKey();
                    newReminderModel.setId(keyToAdd);
                    assert keyToAdd != null;
                    MainActivity.mbase.child(keyToAdd).setValue(newReminderModel);
                    Toast.makeText(getApplicationContext(), medName1 +" added successfully", Toast.LENGTH_LONG).show();
                    break;

                case ReminderEditorActivity.UPDATE:
                    ReminderModel reminderModel = new ReminderModel();
                    reminderModel.setMedName(medName1);
                    reminderModel.setId(medId);
                    reminderModel.setMedNotificationTimes(medNotificationTimes1);
                    reminderModel.setMedType(medType1);
                    reminderModel.setMedDosage(medDosage1);
                    MainActivity.mbase.child(medId).setValue(reminderModel);
                    Toast.makeText(getApplicationContext(), medName1 +" updated successfully", Toast.LENGTH_LONG).show();
                    break;

                case ReminderEditorActivity.DELETE:
                {
                    MainActivity.mbase.child(medId).removeValue();
                }
                    break;

            }
            /* End Check action Section */


            startActivity(new Intent(ReminderEditorActivity.this,MainActivity.class));
        });

        deleteReminderButton.setOnClickListener(view -> new AlertDialog.Builder(ReminderEditorActivity.this)
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this reminder?")
                .setIcon(R.drawable.ic_app)
                .setPositiveButton("Yes", (dialogInterface, i) -> {

                    if (action.equals(UPDATE)) {


                        MainActivity.mbase.child(medId).removeValue();

                    }

                    startActivity(new Intent(ReminderEditorActivity.this,MainActivity.class));

                }).setNegativeButton("No",null).show());
        /* End Button Functionality Section */

    }// END of onCreate




    /* Select Time Section */
    private void selectTime(Button button) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, i, i1) -> {
            notificationTime = i + ":" + i1; //temp variable to store the time to set alarm

            button.setText(FormatTime(i, i1));                                                //sets the button text as selected time
        }, hour, minute, false);
        timePickerDialog.show();
    }

    private String FormatTime(int hour, int minute) {
        String time;
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

    private void setAlarm(String medName, String time, String medDosage) throws ParseException {
        //assigning alarm manager object to set alarm
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //sending data to alarm class to create channel and notification
        Intent intent = new Intent(getApplicationContext(), AlarmBroadcast.class);
        intent.putExtra(AlarmBroadcast.Event_KEY, medName);

        intent.putExtra(AlarmBroadcast.TIME_KEY, time);
        intent.putExtra(AlarmBroadcast.DOSAGE_KEY,medDosage);

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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(ReminderEditorActivity.this, LoginActivity.class));
        }
    }
}


