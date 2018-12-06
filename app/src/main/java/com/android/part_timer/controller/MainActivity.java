package com.android.part_timer.controller;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.part_timer.R;


public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private final int MY_PERMISSION_ACCESS_FINE_LOCATION = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sets the bottom navigation bar
        BottomNavigationView bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setOnNavigationItemSelectedListener(navigation_listener);
        bottom_nav.setSelectedItemId(R.id.home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        checkPremission();

    }

    private Boolean checkPremission() {
        Log.v(TAG, "check and requesting permission");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);
        }
        return true;
    }

    //listener for the bottom navigation bar
    private BottomNavigationView.OnNavigationItemSelectedListener navigation_listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment fragment = null;
                    switch (item.getItemId()) {
                        case R.id.home:
                            fragment = new Home();
                            break;
                        case R.id.stats:
                            fragment = new Stats();
                            break;
                        case R.id.settings:
                            fragment = new Settings();
                            break;

                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, fragment).commit();

                    return true;

                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.info) {
            Intent info_intent = new Intent(MainActivity.this, Info_Activity.class);
            startActivity(info_intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
