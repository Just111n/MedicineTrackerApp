package com.example.medicinetrackerapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final static String CLIPBOARD_LABEL = "CLIPBOARD_LABEL";
    private FirebaseAuth mAuth;
    static DatabaseReference mbaseUser;
    static DatabaseReference mbaseUserReminders;
    private RecyclerView remindersRecyclerView;
    private FloatingActionButton addReminderFab;
    private FirebaseRemindersAdapter adapter;
    private ArrayList<ReminderModel> remindersList;
    private String uid;
    private FirebaseUser user;


    // Create options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Select option in menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        int selectedMenuItemId = item.getItemId();
        mAuth = FirebaseAuth.getInstance();

        String clipBoardData = generateClipBoardData(remindersList);

        // Copy data to clipboard
        if (selectedMenuItemId == R.id.copy_menu_item) {
           copyToClipboard(clipBoardData);
            return true;
        }
        // Sign out
        if (selectedMenuItemId == R.id.log_out_menu_item) {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return true;
        }

        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            uid = mAuth.getCurrentUser().getUid();
        }
        mbaseUser = FirebaseDatabase.getInstance().getReference("users"+"/"+uid);
        mbaseUserReminders = FirebaseDatabase.getInstance().getReference("users"+"/"+uid+"/"+"reminders");

        addReminderFab = findViewById(R.id.add_reminder_fab);
        remindersRecyclerView = findViewById(R.id.reminders_recyclerView);
        remindersRecyclerView.setLayoutManager(
                new LinearLayoutManager(this));


        /*
        create a new FirebaseRecyclerOptions object using the FirebaseRecyclerOptions.Builder.
        This builder allows you to specify the query to be executed on the Firebase Realtime Database
        and the model class that should be used to deserialize the data.
        */
        FirebaseRecyclerOptions<ReminderModel> options
                = new FirebaseRecyclerOptions.Builder<ReminderModel>()
                .setQuery(mbaseUserReminders, ReminderModel.class)
                .build();

        /* creates an adapter that is ready to listen to data changes on the specified Firebase Realtime Database query.
        The adapter's constructor handles the setup of a listener on the query, and when changes occur,
         the adapter will automatically update the RecyclerView.
        */
        adapter = new FirebaseRemindersAdapter(options,this);

        /*
        The adapter is attached to the RecyclerView using the setAdapter() method.
        This will cause the FirebaseRecyclerAdapter to start listening for data changes on the
        Firebase Realtime Database query and update the RecyclerView accordingly.
        */
        remindersRecyclerView.setAdapter(adapter);

        // Read data from firebase and store inside remindersList
        fetchRemindersFromFirebase();

        addReminderFab.setOnClickListener(view -> startReminderEditorActivity());
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else {
            adapter.startListening();
        }
    }

    @Override protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void copyToClipboard(CharSequence clipBoardData) {
        // Gets a handle to the clipboard service.
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        // Creates a new text clip to put on the clipboard.
        ClipData clip = ClipData.newPlainText(MainActivity.CLIPBOARD_LABEL, clipBoardData.toString());

        // Set the clipboard's primary clip.
        clipboard.setPrimaryClip(clip);

        // Only show a toast for Android 12 and lower.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
            Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();
    }

    private void startReminderEditorActivity() {
        Intent intent = new Intent(MainActivity.this, ReminderEditorActivity.class);
        intent.putExtra(ReminderEditorActivity.ACTION_KEY, ReminderEditorActivity.CREATE);
        startActivity(intent);
    }

    private String generateClipBoardData(ArrayList<ReminderModel> remindersList) {
        StringBuilder clipBoardData = new StringBuilder();
        for (ReminderModel reminderModel : remindersList) {
            clipBoardData.append(reminderModel.toString());
            clipBoardData.append("\n");
        }
        return clipBoardData.toString();
    }

    private void fetchRemindersFromFirebase() {
        remindersList = new ArrayList<>();
        mbaseUserReminders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                remindersList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ReminderModel reminderModel = dataSnapshot.getValue(ReminderModel.class);
                    remindersList.add(reminderModel);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }






}