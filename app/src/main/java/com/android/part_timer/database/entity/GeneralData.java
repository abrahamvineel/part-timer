package com.android.part_timer.database.entity;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "general_data")
public class GeneralData {
    public GeneralData(boolean twentyFourHour,double payPerHour) {
        super();
        this.twentyFourHour = twentyFourHour;
        this.payPerHour=payPerHour;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    private boolean twentyFourHour;
    private double payPerHour;

    public double getPayPerHour() {
        return payPerHour;
    }

    public void setPayPerHour(double payPerHour) {
        this.payPerHour = payPerHour;
    }

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

}
