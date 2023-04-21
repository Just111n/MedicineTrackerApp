package com.example.medicinetrackerapp;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ReminderModel {

    private String id;
    private String medName;
    private String medType;
    private ArrayList<String> medNotificationTimes;
    private String medDosage;

    // Required empty constructor for Firebase
    public ReminderModel() {
    }

    private ReminderModel(ReminderBuilder builder) {
        this.id = builder.id;
        this.medName = builder.medName;
        this.medType = builder.medType;
        this.medNotificationTimes = builder.medNotificationTimes;
        this.medDosage = builder.medDosage;
    }

    public static class ReminderBuilder {
        private String id;
        private String medName;
        private String medType;
        private ArrayList<String> medNotificationTimes;
        private String medDosage;

        public ReminderBuilder() {

        }

        public ReminderBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public ReminderBuilder setMedName(String medName) {
            this.medName = medName;
            return this;
        }

        public ReminderBuilder setMedType(String medType) {
            this.medType = medType;
            return this;
        }

        public ReminderBuilder setMedNotificationTimes(ArrayList<String> medNotificationTimes) {
            this.medNotificationTimes = medNotificationTimes;
            return this;
        }

        public ReminderBuilder setMedDosage(String medDosage) {
            this.medDosage = medDosage;
            return this;
        }

        public ReminderModel build() {
            return new ReminderModel(this);
        }
    }

    @NonNull
    @Override
    public String toString() {
        String output = "";
        output += "Medication:" + this.getMedName() + "\n";
        output += "Type: " + this.getMedType() + "\n";
        String medNotificationTimesText = this.getMedNotificationTimes().toString();
        output += "Times: " + medNotificationTimesText.substring(1, medNotificationTimesText.length() - 1) + "\n";
        output += "Dose" + this.getMedDosage() + "\n";
        return output;
    }

    public String getId() {
        return id;
    }

    public String getMedName() {
        return medName;
    }

    public String getMedType() {
        return medType;
    }

    public ArrayList<String> getMedNotificationTimes() {
        return medNotificationTimes;
    }

    public String getMedDosage() {
        return medDosage;
    }
}
