package com.example.medicinetrackerapp;

import android.widget.TextView;

import java.util.ArrayList;

public class LocalRemindersData implements RemindersDataSource{

    private static ArrayList<Reminder> remindersList = new ArrayList<>();

    public LocalRemindersData() {
    }




    @Override
    public void addReminder(Reminder reminder) {
        remindersList.add(reminder);

    }

    @Override
    public Reminder getReminder(int i) {
        return remindersList.get(i);
    }

    @Override
    public void updateReminder(int index, Reminder reminder) {
        remindersList.set(index,reminder);

    }

    @Override
    public void removeReminder(int i) {
        remindersList.remove(i);

    }

    @Override
    public int getSize() {
        return remindersList.size();
    }
}
