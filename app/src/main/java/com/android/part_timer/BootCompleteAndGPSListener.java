package com.android.part_timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;


import com.android.part_timer.geofence.Geofencing;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedHashMap;
import java.util.List;

import com.android.part_timer.database.AppDatabase;
import com.android.part_timer.database.entity.LocationData;

public class BootCompleteAndGPSListener extends BroadcastReceiver {
    private static final String TAG = "BootAndGPSReceiver";
    private GeofencingClient mGeofencingClient;
    private AppDatabase appDatabase;
    private Geofencing geofencing;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v(TAG, "intent received : "+intent.getAction());
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            appDatabase = AppDatabase.getDatabaseInstance(context);
            mGeofencingClient = LocationServices.getGeofencingClient(context);
            geofencing = new Geofencing(context, mGeofencingClient);
            //re-register geofence after location is turned back on or phone rebooted
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    List<LocationData> locationDataList;
                    LinkedHashMap<String, LatLng> location =
                            new LinkedHashMap<>();
                    locationDataList = appDatabase.locationDataDaoModel().loadLocations();
                    for (LocationData locationData : locationDataList) {
                        String placeName = locationData.getPlaceID();
                        LatLng latLng = new LatLng(locationData.getLatitude(), locationData.getLongitude());
                        location.put(placeName, latLng);
                    }
                    geofencing.removeGeofence();
                    geofencing.createGeofence(location);
                    geofencing.addGeofence();
                }
            });
        } else {
            Log.v(TAG, "location turned off");
        }
    }

}