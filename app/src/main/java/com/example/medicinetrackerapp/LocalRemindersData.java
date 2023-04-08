package com.example.medicinetrackerapp;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class LocalRemindersData implements RemindersDataSource {

    private static LocalRemindersData instance;
    private static ArrayList<Reminder> remindersList = new ArrayList<>();

    private LocalRemindersData() {
    }

    public static LocalRemindersData getInstance() {
        if (instance == null) {
            instance = new LocalRemindersData();
        }
        return instance;
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
        remindersList.set(index, reminder);
    }

    @Override
    public void removeReminder(int i) {
        remindersList.remove(i);
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
            output += reminder.getMedName() + "\n";
            output += reminder.getMedType() + "\n";
            String medNotificationTimesText = reminder.getMedNotificationTimes().toString();
            output += medNotificationTimesText.substring(1, medNotificationTimesText.length() - 1) + "\n";
            output += reminder.getMedDosage() +"\n";

            output += "\n";
        }



        return output;
    }
}
