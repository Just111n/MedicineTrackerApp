package com.example.medicinetrackerapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class ReminderEditorActivity extends AppCompatActivity {
    private Button medNotificationTimeButton, medNotificationTimeButton2, addReminderButton, deleteReminderButton;
    private EditText editMedNameEditText, editDosageEditText;
    private ImageButton pillsImageButton, syrupImageButton, injectionImageButton;

    public final static String ACTION_KEY = "ACTION_KEY", CREATE = "CREATE", UPDATE = "UPDATE";

    public static final String MED_ID_KEY = "MED_ID_KEY", MED_NAME_KEY = "MED_NAME_KEY", MED_NOTIFICATION_TIMES_KEY = "NOTIFICATION_TIME_KEY",
            MED_DOSAGE_KEY = "MED_DOSAGE_KEY", MED_TYPE_KEY = "MED_TYPE_KEY";
    public final static String PILLS = "pills", SYRUP = "syrup", INJECTION = "injection";
    private boolean isPillsButtonSelected, isSyrupButtonSelected, isInjectionButtonSelected;
    private FirebaseAuth mAuth;

    private String uid;
    private DatabaseReference mbaseUserReminders;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_editor);


        mAuth = FirebaseAuth.getInstance();
        uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        mbaseUserReminders = FirebaseDatabase.getInstance().getReference("users"+"/"+uid+"/"+"reminders");

        isPillsButtonSelected = false;
        isSyrupButtonSelected = false;
        isInjectionButtonSelected = false;


        editMedNameEditText = findViewById(R.id.edit_medName_editText);
        pillsImageButton = findViewById(R.id.pills_ImageButton);
        syrupImageButton = findViewById(R.id.syrup_ImageButton);
        injectionImageButton = findViewById(R.id.injection_ImageButton);
        editDosageEditText = findViewById(R.id.Dosage_edittext);
        medNotificationTimeButton = findViewById(R.id.med_notification_time_button);
        medNotificationTimeButton2 = findViewById(R.id.med_notification_time_button2);
        addReminderButton = findViewById(R.id.add_reminder_button);
        deleteReminderButton = findViewById(R.id.delete_reminder_button);



        // Receive data from MainActivity
        Intent intent = getIntent();
        String action = intent.getStringExtra(ACTION_KEY);
        String medId = intent.getStringExtra(MED_ID_KEY);
        ArrayList<String> medNotificationTimes = intent.getStringArrayListExtra(MED_NOTIFICATION_TIMES_KEY);
        String medName = intent.getStringExtra(MED_NAME_KEY);
        String medType = intent.getStringExtra(MED_TYPE_KEY);
        String medDosage = intent.getStringExtra(MED_DOSAGE_KEY);

        if (action.equals(UPDATE)) {
            showReminderDataInViews(medName,medNotificationTimes,medType,medDosage);
        }

        medNotificationTimeButton.setOnClickListener(view -> selectTime(medNotificationTimeButton));
        medNotificationTimeButton2.setOnClickListener(view -> selectTime(medNotificationTimeButton2));
        pillsImageButton.setOnClickListener(view -> selectMedType(!isPillsButtonSelected, false, false));
        syrupImageButton.setOnClickListener(view -> selectMedType(false, !isSyrupButtonSelected, false));
        injectionImageButton.setOnClickListener(view -> selectMedType(false, false, !isInjectionButtonSelected));
        addReminderButton.setOnClickListener(view -> {
            String medNameSubmit = editMedNameEditText.getText().toString();
            String medNotificationTimeSubmit = medNotificationTimeButton.getText().toString().trim();
            String medNotificationTimeSubmit2 = medNotificationTimeButton2.getText().toString().trim();
            String medTypeSubmit = getMedTypeSelected();
            String medDosageSubmit = editDosageEditText.getText().toString();
            ArrayList<String> medNotificationTimesSubmit = new ArrayList<>();
            medNotificationTimesSubmit.add(medNotificationTimeSubmit);
            medNotificationTimesSubmit.add(medNotificationTimeSubmit2);
            switch (action) {
                case CREATE:
                    String newKey = mbaseUserReminders.push().getKey();
                    addReminderToDatabase(newKey,medNameSubmit,medNotificationTimesSubmit,medDosageSubmit,medTypeSubmit);
                    break;
                case ReminderEditorActivity.UPDATE:
                   updateReminderInDatabase(medNameSubmit,medId,medNotificationTimesSubmit,medDosageSubmit,medTypeSubmit);
                    break;
            }

            startActivity(new Intent(ReminderEditorActivity.this,MainActivity.class));
        });

        deleteReminderButton.setOnClickListener(view -> new AlertDialog.Builder(ReminderEditorActivity.this)
                .setTitle(getString(R.string.are_you_sure))
                .setMessage(getString(R.string.ask_delete_reminder))
                .setIcon(R.drawable.ic_app)
                .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                    if (action.equals(UPDATE)) {
                        mbaseUserReminders.child(medId).removeValue();
                    }
                    startActivity(new Intent(ReminderEditorActivity.this,MainActivity.class));
                }).setNegativeButton(getString(R.string.no),null).show());

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(ReminderEditorActivity.this, LoginActivity.class));
        }
    }

    private void selectMedType(boolean isPillsSelected, boolean isSyrupSelected, boolean isInjectionSelected) {
        isPillsButtonSelected = isPillsSelected;
        isSyrupButtonSelected = isSyrupSelected;
        isInjectionButtonSelected = isInjectionSelected;
        updateImageButtonState(pillsImageButton,isPillsSelected);
        updateImageButtonState(syrupImageButton,isSyrupSelected);
        updateImageButtonState(injectionImageButton,isInjectionSelected);
    }

    private void updateImageButtonState(ImageButton imageButton, boolean isSelected) {
        if (isSelected) {
            imageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.purple_theme_dark)));
        } else {
            imageButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_background_colour)));
        }
    }

    private void selectTime(Button button) {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hourOfDay, minute) -> {
            button.setText(FormatTimeTo12h(hourOfDay, minute)); //sets the button text as selected time
        }, currentHour, currentMinute, false);
        timePickerDialog.show();
    }

    private String FormatTimeTo12h(int hourOfDay, int minute) {
        String time;
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hourOfDay == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hourOfDay < 12) {
            time = hourOfDay + ":" + formattedMinute + " AM";
        } else if (hourOfDay == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hourOfDay - 12;
            time = temp + ":" + formattedMinute + " PM";
        }
        return time;
    }

    private void showReminderDataInViews(String medName, ArrayList<String> medNotificationTimes, String medType, String medDosage) {
        editMedNameEditText.setText(medName);
        medNotificationTimeButton.setText(medNotificationTimes.get(0));
        medNotificationTimeButton2.setText(medNotificationTimes.get(1));
        showMedType(medType);
        editDosageEditText.setText(medDosage);
        addReminderButton.setText(R.string.update_reminder);
    }
    private String getMedTypeSelected() {
        if (isPillsButtonSelected) {
            return PILLS;
        }
        if (isSyrupButtonSelected) {
            return SYRUP;
        }
        if (isInjectionButtonSelected) {
            return INJECTION;
        }
        return "";
    }

    private void showMedType(String medType) {
        switch (medType) {
            case PILLS:
                selectMedType(true, false, false);
                break;
            case SYRUP:
                selectMedType(false, true, false);
                break;
            case INJECTION:
                selectMedType(false, false, true);
                break;
            default:
                // handle invalid medType values
                break;
        }
    }


    private void addReminderToDatabase(String newKey,String medNameSubmit, ArrayList<String> medNotificationTimesSubmit, String medDosageSubmit, String medTypeSubmit) {
        ReminderModel newReminder = new ReminderModel();
        newReminder.setMedName(medNameSubmit);
        newReminder.setMedNotificationTimes(medNotificationTimesSubmit);
        newReminder.setMedDosage(medDosageSubmit);
        newReminder.setMedType(medTypeSubmit);
        newReminder.setId(newKey);
        assert newKey != null;
        mbaseUserReminders.child(newKey).setValue(newReminder);
        Toast.makeText(getApplicationContext(), medNameSubmit +" added successfully", Toast.LENGTH_LONG).show();
    }

    private void updateReminderInDatabase(String medNameSubmit, String medId, ArrayList<String> medNotificationTimesSubmit, String medDosageSubmit, String medTypeSubmit) {
        ReminderModel reminder = new ReminderModel();
        reminder.setMedName(medNameSubmit);
        reminder.setId(medId);
        reminder.setMedNotificationTimes(medNotificationTimesSubmit);
        reminder.setMedType(medTypeSubmit);
        reminder.setMedDosage(medDosageSubmit);
        mbaseUserReminders.child(medId).setValue(reminder);
        Toast.makeText(getApplicationContext(), medNameSubmit +" updated successfully", Toast.LENGTH_LONG).show();
    }
}


