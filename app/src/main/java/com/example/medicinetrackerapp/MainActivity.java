package com.example.medicinetrackerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final static String CLIPBOARD_LABEL = "CLIPBOARD_LABEL";
    RecyclerView remindersRecyclerView;
    RecyclerView.Adapter<RemindersAdapter.ReminderViewHolder> remindersAdapter;
    FloatingActionButton addReminderFab;

    RemindersDataSource remindersDataSource = LocalRemindersData.getInstance();
//    RemindersDataSource remindersDataSource = FirebaseRemindersData.getInstance();



    // Creating options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Selecting options on menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.profile_menu_item) {

            String message = String.valueOf(remindersDataSource.getSize());
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            return true;
        }
        if (item.getItemId() == R.id.copy_menu_item) {
            String clipBoardData = remindersDataSource.toString();
            // Gets a handle to the clipboard service.
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            // Creates a new text clip to put on the clipboard.
            ClipData clip = ClipData.newPlainText(CLIPBOARD_LABEL, clipBoardData);

            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip);

            // Only show a toast for Android 12 and lower.
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
                Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();

            return true;
        }

        return false;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /* Find Views Section */
        addReminderFab = findViewById(R.id.add_reminder_fab);
        remindersRecyclerView = findViewById(R.id.reminders_recyclerView);
        /* End Find Views Section */


        remindersAdapter = new RemindersAdapter(this, remindersDataSource);

        remindersRecyclerView.setAdapter(remindersAdapter);

        remindersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // TODO (DEL later), in onCreate, remindersDateSource is all empty => remindersList is empty
        String testName = "NIL";
        int size = -1;
        if (remindersDataSource.getSize()>=1) {
            Reminder testRem = remindersDataSource.getReminder(0);
             testName= testRem.getMedName();
        }
        size = remindersDataSource.getSize();
        Log.d(FirebaseRemindersData.FIREBASE_TESTING, "onCreate: "+size);
        Log.d(FirebaseRemindersData.FIREBASE_TESTING, "onCreate: "+testName);
        // END //

        // TODO 1.1 Receive data from ReminderEditorActivity to MainActivity
        Intent intent = getIntent();
        String action = intent.getStringExtra(ReminderEditorActivity.ACTION_KEY);


        // Update/Delete reminder  =>  position >= 0
        //  Create reminder =>  position == -1
        //  open app => position == -2
        int position = intent.getIntExtra(ReminderEditorActivity.POSITION_KEY, -2);
        String newMedName = intent.getStringExtra(ReminderEditorActivity.MED_NAME_KEY);
        ArrayList<String> medNotificationTimes = intent.getStringArrayListExtra(ReminderEditorActivity.MED_NOTIFICATION_TIMES_KEY);
        String medType = intent.getStringExtra(ReminderEditorActivity.MED_TYPE_KEY);
        String medDosage = intent.getStringExtra(ReminderEditorActivity.MED_DOSAGE_KEY);

        if (action == null) {
            action = "OPEN_APP";
        }
        /* Check action Section */
        switch (action) {
            case ReminderEditorActivity.CREATE:
                Reminder newReminder = new Reminder();
                newReminder.setMedName(newMedName);
                newReminder.setMedNotificationTimes(medNotificationTimes);
                newReminder.setMedDosage(medDosage);
                newReminder.setMedType(medType);
                remindersDataSource.addReminder(newReminder);
                remindersAdapter.notifyDataSetChanged();
                break;

            case ReminderEditorActivity.UPDATE:
                Reminder reminder = remindersDataSource.getReminder(position);
                reminder.setMedName(newMedName);
                reminder.setMedNotificationTimes(medNotificationTimes);
                reminder.setMedType(medType);
                reminder.setMedDosage(medDosage);
                remindersDataSource.updateReminder(position, reminder);
                remindersAdapter.notifyItemChanged(position);
                break;

            case ReminderEditorActivity.DELETE:
                if (position >= 0) {
                    remindersDataSource.removeReminder(position);
                    remindersAdapter.notifyItemRemoved(position);
                }
                break;

        }
        /* End Check action Section */

        addReminderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ReminderEditorActivity.class);
                intent.putExtra(ReminderEditorActivity.ACTION_KEY,ReminderEditorActivity.CREATE);
                startActivity(intent);
            }
        });





    }







}