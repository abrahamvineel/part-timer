package com.android.part_timer.controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.part_timer.Constants;
import com.android.part_timer.geofence.Geofencing;
import com.android.part_timer.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedHashMap;
import java.util.List;

import com.android.part_timer.database.AppDatabase;
import com.android.part_timer.database.entity.GeneralData;
import com.android.part_timer.database.entity.LocationData;

import static android.app.Activity.RESULT_OK;

public class Settings extends Fragment {

    View view;
    private Button workLocation, payPerHourButton;
    private final static String TAG = "SettingsLayout";
    private final int MY_PERMISSION_ACCESS_FINE_LOCATION = 20;
    private static final int PLACE_PICKER_REQUEST = 1;
    public static AppDatabase appDatabase;
    private Geofencing geofencing;
    private GeofencingClient mGeofencingClient;
    private TextView locationAddress;
    private EditText payHour;
    private ImageView editLocation, contactMail;
    private String address = "";
    private double payPerHour = 0;
    private Switch twentyHour;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_layout, container, false);

        workLocation = view.findViewById(R.id.workLocation);
        locationAddress = view.findViewById(R.id.locationAddress);
        contactMail = view.findViewById(R.id.contactMail);
        editLocation = view.findViewById(R.id.editLocation);
        twentyHour = view.findViewById(R.id.hourFormat);
        payPerHourButton = view.findViewById(R.id.payHourButton);
        payHour = view.findViewById(R.id.payHour);
        appDatabase = AppDatabase.getDatabaseInstance(getActivity());
        mGeofencingClient = LocationServices.getGeofencingClient(getActivity());
        geofencing = new Geofencing(getActivity(),getContext(), mGeofencingClient);
        checkPremission();
        //get the twenty-four hour format and pay per hour from DB
        AsyncTask.execute(new Runnable() {
            List<LocationData> locationDataList;
            GeneralData generalData;

            @Override
            public void run() {
                generalData = appDatabase.generalDataDaoModel().getGeneralSettings();
                if (null == generalData) {
                    appDatabase.generalDataDaoModel().insert(new GeneralData(false, 0));
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            twentyHour.setChecked(generalData.getTwentyFourHour());
                            payPerHour=generalData.getPayPerHour();
                            String pay=String.valueOf(payPerHour);
                            if((int)payPerHour==payPerHour){
                                pay=String.valueOf((int)payPerHour);
                            }
                            payHour.setText(pay);
                        }
                    });
                }
                locationDataList = appDatabase.locationDataDaoModel().loadLocations();
                for (LocationData locationData : locationDataList) {
                    address = locationData.getAddress();
                }
                locationAddress.post(new Runnable() {
                    @Override
                    public void run() {
                        locationAddress.setText(address);
                    }
                });
            }
        });
        //if user presses ok then below method updates it to DB
        payPerHourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payHour.getText().toString() != "") {
                    payPerHour = Double.parseDouble(payHour.getText().toString());
                }
                AsyncTask.execute(new Runnable() {
                    GeneralData generalData;

                    @Override
                    public void run() {
                        generalData = appDatabase.generalDataDaoModel().getGeneralSettings();
                        generalData.setPayPerHour(payPerHour);
                        appDatabase.generalDataDaoModel().update(generalData);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), Constants.UPDATE_SUCCESS, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
        workLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "In setting via fragmet");
                addLocation();
            }
        });
        //if user changes the state of switch then below method will update the same to DB
        twentyHour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                final boolean checked = isChecked;
                AsyncTask.execute(new Runnable() {
                    GeneralData generalData;

                    @Override
                    public void run() {
                        generalData = appDatabase.generalDataDaoModel().getGeneralSettings();
                        generalData.setTwentyFourHour(checked);
                        appDatabase.generalDataDaoModel().update(generalData);
                    }
                });
            }
        });

        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLocation();
            }
        });

        contactMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, Constants.mailIds);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        return view;
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
        }
    }

    private Boolean checkPremission() {
        Log.v(TAG, "check and requesting permission");
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);
        }
        return true;
    }


    //executes when user picks a place from maps
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(getContext(), data);
            if (place == null) {
                Log.i(TAG, "No place selected");
                return;
            }

            // Extract the place information from the API
            String placeName = Constants.WORK_LOCATION_NAME;
            String placeAddress = place.getAddress().toString();
            String placeID = place.getId();
            LatLng latLng = place.getLatLng();
            final LocationData locationData = new LocationData(placeName, placeAddress, latLng.latitude, latLng.longitude);


            //insert location data into db
            AsyncTask.execute(new Runnable() {
                List<LocationData> locationDataList;

                @Override
                public void run() {
                    locationDataList = appDatabase.locationDataDaoModel().loadLocations();
                    for (LocationData locationData : locationDataList) {
                        Log.v(TAG, locationData.getPlaceID());
                        appDatabase.locationDataDaoModel().deleteLocation(locationData);
                    }
                    appDatabase.locationDataDaoModel().insert(locationData);
                    Log.v(TAG, "In thread after insert");
                }
            });
            Log.v(TAG, "lines after thread after insert");
            LinkedHashMap<String, LatLng> location =
                    new LinkedHashMap<>();
            location.put(placeName, latLng);
            geofencing.removeGeofence();
            geofencing.createGeofence(location);
            geofencing.addGeofence();
            locationAddress.setText(placeAddress);
            Log.v(TAG, "placeName: " + placeName + " ,placeAddress: " + placeAddress + ", placeID: " + placeID + ", latitude: " + latLng.latitude + ", longitude: " + latLng.longitude);

        }
    }
}
