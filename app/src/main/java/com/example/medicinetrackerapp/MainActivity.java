package com.example.medicinetrackerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView remindersRecyclerView;



    FloatingActionButton addReminderFab;

    RemindersDataSource remindersDataSource;

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

            // Going from MainActivity to NotesEditorActivity
//            Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
//            startActivity(intent);

            Toast.makeText(this, "Dummy menu item 1 is clicked!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.dummy2) {

            // Going from MainActivity to NotesEditorActivity
            Toast.makeText(this, "Dummy menu item 2 is clicked!", Toast.LENGTH_SHORT).show();
        }

        return false;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addReminderFab = findViewById(R.id.add_reminder_fab);


        remindersRecyclerView = findViewById(R.id.reminders_recyclerView);

        // initialize the reminders data source, remindersList is a private static variable
        remindersDataSource = new LocalRemindersData();

        RecyclerView.Adapter<RemindersAdapter.RemindersHolder> remindersAdapter
                = new RemindersAdapter(this, remindersDataSource);
        remindersRecyclerView.setAdapter(remindersAdapter);
        remindersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();




        String action = intent.getStringExtra(ReminderEditorActivity.ACTION_KEY);
        if (action == null) {
            action = "OPEN_APP";
        }
        // position >= 0 => update/delete reminder
        // postition == -1 => create reminder
        // position == -2 => open app
        int position = intent.getIntExtra(ReminderEditorActivity.POSITION_KEY, -2);
        String newMedName = intent.getStringExtra(ReminderEditorActivity.MED_NAME_KEY);

        if (action.equals(ReminderEditorActivity.CREATE) ) {

            Reminder reminder = new Reminder();
            reminder.setMedName(newMedName);
            remindersDataSource.addReminder(reminder);
            remindersAdapter.notifyDataSetChanged();

        }


        if (action.equals(ReminderEditorActivity.UPDATE) ) {
            Reminder reminder = remindersDataSource.getReminder(position);

            reminder.setMedName(newMedName);
            remindersDataSource.updateReminder(position, reminder);
            remindersAdapter.notifyDataSetChanged();


        }

        if (action.equals(ReminderEditorActivity.DELETE) && position>=0) {
            remindersDataSource.removeReminder(position);
            remindersAdapter.notifyDataSetChanged();



        }


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