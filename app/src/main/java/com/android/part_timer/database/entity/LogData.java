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

    public Date getCheckIn() {
        return checkIn;
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
