package csci5708.csci4176.part_timer;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;


import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import csci5708.csci4176.part_timer.database.AppDatabase;

public  class GeofenceTransitionsIntentServices extends IntentService {
    protected static final String TAG = "gfservices";
    private AppDatabase appDatabase;

    public GeofenceTransitionsIntentServices() {
        super(TAG);
        Log.v(TAG, "in constructor");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        appDatabase = AppDatabase.getDatabaseInstance(this);
        Log.v(TAG, "in create");
    }

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
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );
            sendNotification(geofenceTransitionDetails);
            appDatabase = AppDatabase.getDatabaseInstance(this);
            LogTimeUpdate logTimeUpdate=new LogTimeUpdate(geofenceTransition,appDatabase);
            String placeId="";
            for (Geofence geofence : triggeringGeofences) {
                placeId=geofence.getRequestId();
            }
            logTimeUpdate.SetLogTime(placeId);
            Log.i(TAG, geofenceTransitionDetails);
        } else {
            Log.e(TAG, "Invalid geo fence: " + geofenceTransition);
        }

    }

    private String getGeofenceTransitionDetails(Context context, int geofenceTransition, List<Geofence> triggeringGeofences) {

        //change this code to switch case or new class
        String geofenceTransitionString = "";
        if (geofenceTransition == 1) {
            geofenceTransitionString = "Enter";
        } else if (geofenceTransition == 2) {
            geofenceTransitionString = "Exit";
        }
        ArrayList triggeringGeofenceList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofenceList.add(geofence.getRequestId());
        }
        String triggeringgeofenceId = TextUtils.join(",", triggeringGeofenceList);
        return geofenceTransitionString + ":" + triggeringgeofenceId;
    }

    private void sendNotification(String notificationDetails) {
        Intent notificationInent = new Intent(getApplicationContext(), MainActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(notificationInent);
        PendingIntent notificationPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CH_ID");
        builder.setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText("You entered/exit the location")
                .setContentIntent(notificationPendingIntent);
        builder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
