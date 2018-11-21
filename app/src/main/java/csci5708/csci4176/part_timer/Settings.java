package csci5708.csci4176.part_timer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedHashMap;
import java.util.List;

import csci5708.csci4176.part_timer.database.AppDatabase;
import csci5708.csci4176.part_timer.database.entity.LocationData;

import static android.app.Activity.RESULT_OK;

public class Settings extends Fragment {

    View view;
    private Button homeLocation;
    private final static String TAG = "SettingsLayout";
    private final int MY_PERMISSION_ACCESS_FINE_LOCATION = 20;
    private static final int PLACE_PICKER_REQUEST = 1;
    public static AppDatabase appDatabase;
    private Geofencing geofencing;
    private GeofencingClient mGeofencingClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.settings_layout, container, false);

         homeLocation=view.findViewById(R.id.homeLocation);
        appDatabase = AppDatabase.getDatabaseInstance(getActivity());
        isNetworkAvailable();
        mGeofencingClient = LocationServices.getGeofencingClient(getActivity());
        geofencing = new Geofencing(getActivity(), getContext(), mGeofencingClient);
        checkPremission();
        homeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG,"In setting via fragmet");
                addLocation();
            }
        });

        return view;
    }

    private boolean isNetworkAvailable() {
        Boolean networkAvailable = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        //sometimes there might be no active networks so there is chance that it returns null
        if (activeNetworkInfo != null
                && activeNetworkInfo.isConnected()) {
            networkAvailable = true;
        }
        return networkAvailable;
    }
    private void addLocation() {
        if (checkPremission()) {
            try {
                // Start a new Activity for the Place Picker API, this will trigger {@code #onActivityResult}
                // when a place is selected or with the user cancels.
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent i = builder.build(getActivity());
                startActivityForResult(i, PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
            } catch (GooglePlayServicesNotAvailableException e) {
                Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
            } catch (Exception e) {
                Log.e(TAG, String.format("PlacePicker Exception: %s", e.getMessage()));
            }
        } else {
            showNoConnectionDialog();
        }
    }
    private Boolean checkPremission() {
        Log.v(TAG, "check and requesting permission");
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);
        }
        return true;
    }
    private void showNoConnectionDialog() {
        Toast.makeText(getActivity(), "Can't connect to internet,Please check the Internet Connection!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(getContext(), data);
            if (place == null) {
                Log.i(TAG, "No place selected");
                return;
            }

            // Extract the place information from the API
            String placeName = place.getName().toString();
            String placeAddress = place.getAddress().toString();
            String placeID = place.getId();
            LatLng latLng = place.getLatLng();
            final LocationData locationData = new LocationData(placeName, latLng.latitude, latLng.longitude);


            //insert location data into db
            AsyncTask.execute(new Runnable() {
                List<LocationData> locationDataList;
                @Override
                public void run() {
                    locationDataList=appDatabase.locationDataDaoModel().loadLocations();
                    for(LocationData locationData: locationDataList){
                        Log.v(TAG,locationData.getPlaceID());
                        appDatabase.locationDataDaoModel().deleteLocation(locationData);
                    }
                    appDatabase.locationDataDaoModel().insert(locationData);
                    Log.v(TAG,"In thread after insert");
                    // Get Data   AppDatabase.getInstance(context).userDao().getAllUsers();
                }
            });

            Log.v(TAG,"lines after thread after insert");
            LinkedHashMap<String, LatLng> location =
                    new LinkedHashMap< >();
            location.put(placeName, latLng);
            geofencing.removeGeofence();
            geofencing.createGeofence(location);
            geofencing.addGeofence();
            Log.v(TAG, "placeName: " + placeName + " ,placeAddress: " + placeAddress + ", placeID: " + placeID + ", latitude: " + latLng.latitude + ", longitude: " + latLng.longitude);
            //showData();
        }
    }
}
