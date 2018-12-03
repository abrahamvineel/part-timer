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

    @Update()
    void update(LogData logData);

    //get the latest log time to show in home page
    @Query("select * FROM log_data order by checkIn DESC LIMIT 1")
    LogData getLogInfo();

    //get all the log times to show it to user to edit and delete
    @Query("select * from log_data order by checkIn desc")
    LiveData<List<LogData>> getAllLogs();

   // log times for specified date
    @Query("select * from log_data where checkIn=:date")
    LogData getLogFromCheckIn(long date);

    //get the week log times to calculate weekly hours
    @Query("select * from log_data where strftime('%Y-%W', checkIn / 1000, 'unixepoch','localtime')== strftime('%Y-%W',date('now','localtime'))")
    List<LogData> getWeekLogs();

    //get data per day in current week
    @Query("select id,place_id,month,monthly_total,(checkOut-checkIn) as weekly_diff, strftime('%w', checkIn / 1000, 'unixepoch','localtime') as dayNum from log_data where strftime('%Y-%W', checkIn / 1000, 'unixepoch','localtime')== strftime('%Y-%W',date('now','localtime'))")
    List<LogData> getWeek();

    //get monthly data
    @Query("select id,place_id,weekly_diff,dayNum,sum(checkOut-checkIn) as monthly_total, strftime('%m', checkIn / 1000, 'unixepoch','localtime')as month from log_data group by strftime('%m', checkIn / 1000, 'unixepoch','localtime')")
    List<LogData> getMonthly();


    //query to check entered date from date listeners is already available
    @Query("select * from log_data where id <> :id and (:selectedDate between checkIn and checkOut)")
    LogData getLogDateBetween(int id, long selectedDate);

    @Delete
    void deleteLocation(LogData logData);

    @Query("delete from log_data")
    void deleteAllLocations();
}
