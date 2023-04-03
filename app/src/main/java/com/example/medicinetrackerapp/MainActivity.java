package com.example.medicinetrackerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView remindersRecyclerView;



    FloatingActionButton addReminderFab;

    RemindersDataSource remindersDataSource;



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
            action = "OPEN";
        }
        int position = intent.getIntExtra(ReminderEditorActivity.POSITION_KEY, -2);
        String newMedName = intent.getStringExtra(ReminderEditorActivity.MED_NAME_KEY);

        if (action.equals("CREATE") ) {
            Log.d("add reminder form",action);

            Reminder reminder = new Reminder();
            reminder.setMedName(newMedName);
            remindersDataSource.addReminder(reminder);
            remindersAdapter.notifyDataSetChanged();

        }


        if (action.equals("UPDATE") ) {
            Reminder reminder = remindersDataSource.getReminder(position);

            reminder.setMedName(newMedName);
            remindersDataSource.updateReminder(position, reminder);
            remindersAdapter.notifyDataSetChanged();


        }

        if (action.equals("DELETE") && position>=0) {


            Reminder reminder = remindersDataSource.getReminder(position);
            remindersDataSource.removeReminder(position);
            remindersAdapter.notifyDataSetChanged();



        }


        addReminderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ReminderEditorActivity.class);
                intent.putExtra(ReminderEditorActivity.ACTION_KEY,"CREATE");
                startActivity(intent);
            }
        });



    }
}