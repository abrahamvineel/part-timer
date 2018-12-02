package com.android.part_timer.database.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;


import java.util.List;

import com.android.part_timer.database.DateTypeConverter;
import com.android.part_timer.database.entity.LogData;

@TypeConverters({DateTypeConverter.class})
@Dao
public interface LogDataDao {

    @Insert()
    void insert(LogData logData);

    @Query("select * from log_data")
    List<LogData> logData();

    /*@Query("update log_data set checkIn=:checkIn where id=:id")
    void setCheckIn(Date checkIn,int id);

    @Query("update log_data set checkOut=Date(:checkOut) and tracking=:tracking where id=:id")
    void setCheckOut(Date checkOut,Boolean tracking, int id);*/

    @Update()
    void update(LogData logData);

    @Query("select * FROM log_data order by checkIn DESC LIMIT 1")
    LogData getLogInfo();

    @Query("select * from log_data order by checkIn desc")
    LiveData<List<LogData>> getAllLogs();


    @Query("select * from log_data where checkIn=:date")
    LogData getLogFromCheckIn(long date);
/*
    @Query("select * from log_data where id <> :id and (:checkInDate between checkIn and checkOut) or (:checkOutDate between checkIn and checkOut)")
    LogData getLogDateBetween(int id, long checkInDate, long checkOutDate);*/

    @Query("select * from log_data where strftime('%Y-%W', checkIn / 1000, 'unixepoch','localtime')== strftime('%Y-%W',date('now','localtime'))")
    List<LogData> getWeekLogs();

    @Query("select * from log_data where id <> :id and (:selectedDate between checkIn and checkOut)")
    LogData getLogDateBetween(int id, long selectedDate);


    @Delete
    void deleteLocation(LogData logData);

    @Query("delete from log_data")
    void deleteAllLocations();
}
