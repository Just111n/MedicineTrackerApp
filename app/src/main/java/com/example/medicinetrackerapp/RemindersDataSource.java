package com.example.medicinetrackerapp;

import androidx.annotation.NonNull;


// TODO DELETE LATER
public interface RemindersDataSource {

    void addReminder(Reminder reminder);
    Reminder getReminder(int i);
    void updateReminder(int index,Reminder reminder);
    void removeReminder(int i);
    int getSize();

    @NonNull
    String toString();


}
