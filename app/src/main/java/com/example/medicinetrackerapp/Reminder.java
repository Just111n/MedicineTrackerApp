package com.example.medicinetrackerapp;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Reminder {

    private String id;

    private String medName;
    private String medType;
    private ArrayList<LocalDateTime> medNotificationTimes;
    private String medDosage;

    Reminder() {
//        this.medName = "Paracetemol";
//        this.medType = "tablet";
//        // TODO Reminder Class, add medNotificationTime
//        this.medDosage = "2 Tablets";



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

    public ArrayList<LocalDateTime> getMedNotificationTimes() {
        return medNotificationTimes;
    }

    public void setMedNotificationTimes(ArrayList<LocalDateTime> medNotificationTimes) {
        this.medNotificationTimes = medNotificationTimes;
    }

    public String getMedDosage() {
        return medDosage;
    }

    public void setMedDosage(String medDosage) {
        this.medDosage = medDosage;
    }
}
