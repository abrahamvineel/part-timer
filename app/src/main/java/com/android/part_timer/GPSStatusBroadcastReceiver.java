package com.android.part_timer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.part_timer.controller.MainActivity;

public class GPSStatusBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("Location listener","location turned off/on");
        if(context instanceof MainActivity){
            Log.v("GPSBrod","got the main act");
        }
    }
}
