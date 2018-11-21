package csci5708.csci4176.part_timer.database.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;



import java.util.List;

import csci5708.csci4176.part_timer.database.DateTypeConverter;
import csci5708.csci4176.part_timer.database.entity.LogData;

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


    @Query("select * FROM log_data order by id DESC LIMIT 1")
    LogData getLogInfo();

    @Delete
    void deleteLocation(LogData logData);
}
