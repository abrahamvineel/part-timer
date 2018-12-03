package com.android.part_timer.database.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "log_data")
public class LogData {

    //constructor
    public LogData() {
        super();
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @NonNull
    public int getId() {
        return id;
    }

    @ForeignKey(entity = LocationData.class, parentColumns = "placeID", childColumns = "placeID")
    @NonNull
    @ColumnInfo(name = "place_id")
    private String placeID;


    private Date checkIn;
    private Date checkOut;

    private long monthly_total;
    private long weekly_diff;
    private int month;
    private int dayNum;

    private Boolean tracking;

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @NonNull
    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(@NonNull String placeID) {
        this.placeID = placeID;
    }

    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }

    public void setWeekly_diff(long weekly_diff) {

        this.weekly_diff = weekly_diff;
    }

    public int getDayNum() {

        return dayNum;
    }

    public long getWeekly_diff() {

        return weekly_diff;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public int getMonth() {
        return month;
    }

    public long getMonthly_total() {

        return monthly_total;
    }

    public void setMonthly_total(long monthly_total) {
        this.monthly_total = monthly_total;
    }

    public void setMonth(int month) {
        this.month = month;
    }


    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public Boolean getTracking() {
        return tracking;
    }

    public void setTracking(Boolean tracking) {
        this.tracking = tracking;
    }
}
