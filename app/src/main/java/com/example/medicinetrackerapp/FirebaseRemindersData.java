package com.example.medicinetrackerapp;



import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseRemindersData implements RemindersDataSource {

    public static final String FIREBASE_TESTING = "FIREBASE_TESTING";
    private static FirebaseRemindersData instance;
    private static DatabaseReference databaseReference;
    private static ArrayList<Reminder> remindersList = new ArrayList<>();

    private FirebaseRemindersData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("reminders");



        // Attach a value event listener to the database reference
        // to get the initial list of reminders and listen for changes
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                countListItems(snapshot);
                repopulateList(snapshot);
                Log.d(FIREBASE_TESTING,"onDataChange is called");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FirebaseRemindersData", "onCancelled: " + error.getMessage());
            }
        });
    }

    public static FirebaseRemindersData getInstance() {
        if (instance == null) {
            instance = new FirebaseRemindersData();
        }
        return instance;
    }

    @Override
    public void addReminder(Reminder reminder) {
        // Generate a new key for the reminder
        String key = databaseReference.push().getKey();
        reminder.setId(key);

        // Add the reminder to the database reference
        databaseReference.child(key).setValue(reminder);
    }

    @Override
    public Reminder getReminder(int i) {
        return remindersList.get(i);
    }

    @Override
    public void updateReminder(int index, Reminder reminder) {
        Reminder oldReminder = remindersList.get(index);
        String key = oldReminder.getId();

        // Update the reminder in the database reference
        databaseReference.child(key).setValue(reminder);
    }

    @Override
    public void removeReminder(int i) {
        Reminder reminder = remindersList.get(i);
        String key = reminder.getId();

        // Remove the reminder from the database reference
        databaseReference.child(key).removeValue();
    }

    @Override
    public int getSize() {
        return remindersList.size();
    }

    @NonNull
    @Override
    public String toString() {

        String output = "";
        for (Reminder reminder:remindersList) {
            output = output + reminder.getMedName() + "\n";
            String medNotificationTimesText = reminder.getMedNotificationTimes().toString();
            output = output + medNotificationTimesText.substring(1, medNotificationTimesText.length() - 1);


            output = output + "\n\n";
        }



        return output;
    }



    private void countListItems(DataSnapshot snapshot){
        int size = (int) snapshot.getChildrenCount();
        Log.d(FIREBASE_TESTING, "constructor size " + size);
    }

    private void repopulateList(DataSnapshot snapshot){
        remindersList.clear();
        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
            Reminder reminder = dataSnapshot.getValue(Reminder.class);
            remindersList.add(reminder);
        }
    }


}



