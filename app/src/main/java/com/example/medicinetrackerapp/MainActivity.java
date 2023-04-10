package com.example.medicinetrackerapp;

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
    RecyclerView remindersRecyclerView;
    FloatingActionButton addReminderFab;

    final static DatabaseReference mbase = FirebaseDatabase.getInstance().getReference("reminders");
    FirebaseRemindersAdapter adapter;
    ArrayList<Reminder> remindersList;


    // Creating options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

//     Selecting options on menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        int selectedMenuItemId = item.getItemId();
        mAuth = FirebaseAuth.getInstance();

        StringBuilder clipBoardData = new StringBuilder();
        for (Reminder reminder: remindersList) {
            clipBoardData.append(reminder.toString());
            clipBoardData.append("\n");
        }

        switch (selectedMenuItemId) {
            case R.id.copy_menu_item:
                // Gets a handle to the clipboard service.
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                // Creates a new text clip to put on the clipboard.
                ClipData clip = ClipData.newPlainText(CLIPBOARD_LABEL, clipBoardData.toString());

                // Set the clipboard's primary clip.
                clipboard.setPrimaryClip(clip);

                // Only show a toast for Android 12 and lower.
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
                    Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();

                Log.d("copy", clipBoardData.toString());
                Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.log_out_menu_item:
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return true;

            default:
                return false;
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        addReminderFab = findViewById(R.id.add_reminder_fab);
        remindersRecyclerView = findViewById(R.id.reminders_recyclerView);


        remindersRecyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Reminder> options
                = new FirebaseRecyclerOptions.Builder<Reminder>()
                .setQuery(mbase, Reminder.class)
                .build();

        adapter = new FirebaseRemindersAdapter(options,this);
        remindersRecyclerView.setAdapter(adapter);


        remindersList = new ArrayList<>();
        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                remindersList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Reminder reminder = dataSnapshot.getValue(Reminder.class);
                    remindersList.add(reminder);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
        addReminderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ReminderEditorActivity.class);
                intent.putExtra(ReminderEditorActivity.ACTION_KEY,ReminderEditorActivity.CREATE);
                startActivity(intent);
            }
        });

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

    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
}