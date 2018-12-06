package com.android.part_timer.geofence;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.Log;


import com.android.part_timer.Constants;
import com.android.part_timer.NotificationReceiver;
import com.android.part_timer.R;
import com.android.part_timer.controller.MainActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import com.android.part_timer.database.AppDatabase;

import static com.android.part_timer.Constants.CHANNEL_1_ID;


public class GeofenceTransitionsIntentServices extends IntentService {
    protected static final String TAG = "gfservices";
    private NotificationManagerCompat notificationManager;
    private AppDatabase appDatabase;

    public GeofenceTransitionsIntentServices() {
        super(TAG);
        Log.v(TAG, "in constructor");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        appDatabase = AppDatabase.getDatabaseInstance(this);
        notificationManager = NotificationManagerCompat.from(this);
        Log.v(TAG, "in create");
    }

    //receives intent when user enters/exists the geofence
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = String.valueOf(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        Log.v(TAG, "transition" + geofenceTransition);
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    geofenceTransition,
                    triggeringGeofences
            );
            if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
                sendNotification(geofenceTransitionDetails, Constants.GEOFENCE_ENTER_NOTIFICATION_MESSAGE,true);
            }else{
                sendNotification(geofenceTransitionDetails,Constants.GEOFENCE_EXIT_NOTIFICATION_MESSAGE,false);
            }
            //calls the logTimeUpdate to update the time to DB
            LogTimeUpdate logTimeUpdate = new LogTimeUpdate(geofenceTransition, appDatabase);
            String placeId = "";
            for (Geofence geofence : triggeringGeofences) {
                placeId = geofence.getRequestId();
            }
            logTimeUpdate.SetLogTime(placeId);
            Log.i(TAG, geofenceTransitionDetails);
        } else {
            Log.e(TAG, "Invalid geo fence: " + geofenceTransition);
        }

    }

    private String getGeofenceTransitionDetails(int geofenceTransition, List<Geofence> triggeringGeofences) {

        ArrayList triggeringGeofenceList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofenceList.add(geofence.getRequestId());
        }
        String triggeringGeofenceId = TextUtils.join(",", triggeringGeofenceList);
        if(geofenceTransition == 1){
            return Constants.GEOFENCE_ENTER_NOTIFICATION_TITLE +triggeringGeofenceId;
        }else{
            return Constants.GEOFENCE_EXIT_NOTIFICATION_TITLE +triggeringGeofenceId;
        }
    }

    private void sendNotification(String notificationDetails,String text,Boolean action) {

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification;
        if(action){
        Intent broadcastYesIntent = new Intent(this, NotificationReceiver.class);
        broadcastYesIntent.setAction(Constants.YES_ACTION);
        PendingIntent yesActionIntent = PendingIntent.getBroadcast(this,
                1, broadcastYesIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent broadcastNoIntent = new Intent(this, NotificationReceiver.class);
        broadcastNoIntent.setAction(Constants.NO_ACTION);
        PendingIntent noActionIntent = PendingIntent.getBroadcast(this,
                2, broadcastNoIntent, PendingIntent.FLAG_UPDATE_CURRENT);
         notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .addAction(R.mipmap.ic_launcher, Constants.NOTIFICATION_YES, yesActionIntent)
                .addAction(R.mipmap.ic_launcher, Constants.NOTIFICATION_NO, noActionIntent)
                .build();
        }else{
            notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setColor(Color.RED)
                    .setContentTitle(notificationDetails)
                    .setContentText(text)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(notificationPendingIntent)
                    .setAutoCancel(true)
                    .build();
        }
        notificationManager.notify(Constants.NOTIFICATION_ID, notification);

    }
}
