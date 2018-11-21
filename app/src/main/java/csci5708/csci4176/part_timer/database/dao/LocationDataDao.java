package csci5708.csci4176.part_timer.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;



import java.util.List;

import csci5708.csci4176.part_timer.database.entity.LocationData;

@Dao
public interface LocationDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LocationData locationData);

    @Query("select * from location_data")
    List<LocationData> loadLocations();

    @Delete
    void deleteLocation(LocationData locationData);
}
