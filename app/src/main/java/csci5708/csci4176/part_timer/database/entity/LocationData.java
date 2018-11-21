package csci5708.csci4176.part_timer.database.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "location_data")
public class LocationData {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "place_id")
    private String placeID;

    @NonNull
    private Double latitude;

    @NonNull
    private Double longitude;

    public LocationData(@NonNull String placeID, @NonNull Double latitude, @NonNull Double longitude) {
        this.placeID = placeID;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    @NonNull
    public String getPlaceID() {
        return this.placeID;
    }

    @NonNull
    public Double getLatitude() {
        return this.latitude;
    }

    @NonNull
    public Double getLongitude() {
        return this.longitude;
    }

}
