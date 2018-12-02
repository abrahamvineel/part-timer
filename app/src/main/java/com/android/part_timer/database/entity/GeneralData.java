package com.android.part_timer.database.entity;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "general_data")
public class GeneralData {
    public GeneralData(boolean twentyFourHour) {
        super();
        this.twentyFourHour = twentyFourHour;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    private boolean twentyFourHour;
    private String contactEmail;

    public boolean getTwentyFourHour() {
        return twentyFourHour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTwentyFourHour(boolean twentyFourHour) {
        this.twentyFourHour = twentyFourHour;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
