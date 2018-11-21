package csci5708.csci4176.part_timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedHashMap;
import java.util.List;

import csci5708.csci4176.part_timer.database.AppDatabase;
import csci5708.csci4176.part_timer.database.entity.LocationData;

public class BootCompleteListener extends BroadcastReceiver {
    private static final String TAG = "BootCompleteReceiver";
    private GeofencingClient mGeofencingClient;
    private AppDatabase appDatabase;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "Started after restart");
        appDatabase = AppDatabase.getDatabaseInstance(context);
        mGeofencingClient = LocationServices.getGeofencingClient(context);
       // Geofencing geofencing = new Geofencing(context, mGeofencingClient);
        List<LocationData> locationDataList;
        LinkedHashMap<String, LatLng> location =
                new LinkedHashMap<>();
        locationDataList = appDatabase.locationDataDaoModel().loadLocations();
        for (LocationData locationData : locationDataList) {
            String placeName = locationData.getPlaceID();
            LatLng latLng=new LatLng(locationData.getLatitude(),locationData.getLongitude());
            location.put(placeName, latLng);
        }
        //geofencing.createGeofence(location);
       // geofencing.addGeofence();
    }
}