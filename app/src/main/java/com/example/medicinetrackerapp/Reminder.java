package com.example.medicinetrackerapp;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Reminder {

    private String id;

    private String medName;
    private String medType;
    private ArrayList<String> medNotificationTimes;
    private String medDosage;

    Reminder() {

    }

    @Override
    public String toString() {
        String output = "";
        output += this.getMedName() + "\n";
        output += this.getMedType() + "\n";
        String medNotificationTimesText = this.getMedNotificationTimes().toString();
        output += medNotificationTimesText.substring(1, medNotificationTimesText.length() - 1) + "\n";
        output += this.getMedDosage() +"\n";
        return output;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getMedType() {
        return medType;
    }

    public void setMedType(String medType) {
        this.medType = medType;
    }

    public ArrayList<String> getMedNotificationTimes() {
        return medNotificationTimes;
    }

    public void setMedNotificationTimes(ArrayList<String> medNotificationTimes) {
        this.medNotificationTimes = medNotificationTimes;
    }

    public String getMedDosage() {
        return medDosage;
    }

    public void setMedDosage(String medDosage) {
        this.medDosage = medDosage;
    }


}
