package csci5708.csci4176.part_timer.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import csci5708.csci4176.part_timer.database.dao.LocationDataDao;
import csci5708.csci4176.part_timer.database.dao.LogDataDao;
import csci5708.csci4176.part_timer.database.entity.LocationData;
import csci5708.csci4176.part_timer.database.entity.LogData;


@Database(entities = {LocationData.class, LogData.class}, version = 1)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract LocationDataDao locationDataDaoModel();
    public abstract LogDataDao logDataDaoModel();

    public static AppDatabase getDatabaseInstance(Context context) {
        if (INSTANCE == null) {
            Log.v("APPDATABSE","Creating new instance");
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,"LocationTrackingDatabase")
                        .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
