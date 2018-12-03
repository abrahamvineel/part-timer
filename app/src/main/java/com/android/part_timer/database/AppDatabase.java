package com.android.part_timer.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.android.part_timer.database.dao.GeneralDataDao;
import com.android.part_timer.database.dao.LocationDataDao;
import com.android.part_timer.database.dao.LogDataDao;
import com.android.part_timer.database.entity.GeneralData;
import com.android.part_timer.database.entity.LocationData;
import com.android.part_timer.database.entity.LogData;


@Database(entities = {LocationData.class, LogData.class, GeneralData.class}, version = 1)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;
    private final static String TAG = "AppDatabase";
    public abstract LocationDataDao locationDataDaoModel();
    public abstract LogDataDao logDataDaoModel();
    public abstract GeneralDataDao generalDataDaoModel();

    public static AppDatabase getDatabaseInstance(Context context) {
        if (INSTANCE == null) {
            Log.v(TAG,"Creating new instance");
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,"LocationTrackingDatabase")
                        .build();
        }
        return INSTANCE;
    }

}
