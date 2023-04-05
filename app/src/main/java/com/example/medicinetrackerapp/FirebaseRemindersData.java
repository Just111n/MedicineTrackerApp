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


    private static final String FIREBASE_TESTING = "FIREBASE_TESTING" ;
    private DatabaseReference databaseReference;
    private static ArrayList<Reminder> remindersList = new ArrayList<>();


    int size;

    public FirebaseRemindersData() {

        databaseReference = FirebaseDatabase.getInstance().getReference("reminders");


        // Attach a value event listener to the database reference
        // to get the initial list of reminders and listen for changes
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                countListItems(snapshot);
                repopulateList(snapshot);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FirebaseRemindersData", "onCancelled: " + error.getMessage());
            }
        });
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


    private void countListItems(DataSnapshot snapshot){
         size = (int) snapshot.getChildrenCount();

         Log.d(FIREBASE_TESTING, "constructor size " + size);
        }
    private void repopulateList(DataSnapshot snapshot){
        remindersList = new ArrayList<>();
        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
            Reminder reminder = dataSnapshot.getValue(Reminder.class);
            remindersList.add(reminder);

            }

        }
}





//    public class FirebaseRemindersData implements RemindersDataSource {
//
//        public static final String FIREBASE_TESTING = "FirebaseTesting";
//        DatabaseReference databaseReference;
//        DatabaseReference remindersDatabaseReference;
//        int size;
//        List<Reminder> remindersList;
//
//        FirebaseRemindersData(){
//
//            databaseReference = FirebaseDatabase.getInstance().getReference();
//            remindersDatabaseReference = databaseReference.child("medicine");
//
//            remindersDatabaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    countListItems(snapshot);
//                    repopulateList(snapshot);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//
//        }
//
//        private void countListItems(DataSnapshot snapshot){
//            size = (int) snapshot.getChildrenCount();
//
//            Log.d(FIREBASE_TESTING, "constructor size " + size);
//        }
//
//        private void repopulateList(DataSnapshot snapshot){
//            remindersList = new ArrayList<>();
//            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
//                Reminder reminder = dataSnapshot.getValue(Reminder.class);
//                remindersList.add(reminder);
////            Log.d(FIREBASE_TESTING, dataSnapshot.getValue(Reminder.class));
//            }
//        }
//        @Override
//        public void addReminder(Reminder reminder) {
//            remindersDatabaseReference.push().setValue(reminder);
//            Log.d(FIREBASE_TESTING, "add word size " + size);
//
//        }
//
//        @Override
//        public Reminder getReminder(int i) {
//            return remindersList.get(i);
//        }
//
//        @Override
//        public void updateReminder(int index, Reminder reminder) {
//
//        }
//
//        @Override
//        public void removeReminder(int i) {
//
//            Reminder reminder = remindersList.get(i);
////
//            remindersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                        if( dataSnapshot.getValue().toString().equals(reminder) ){
////                        Log.d(FIREBASE_TESTING, "word:" + reminder + dataSnapshot.getValue().toString());
//                            remindersDatabaseReference.child(dataSnapshot.getKey()).removeValue();
//
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//        }
//
//        @Override
//        public int getSize() {
//            return size;
//        }
