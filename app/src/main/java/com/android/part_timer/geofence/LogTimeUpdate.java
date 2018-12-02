package com.android.part_timer.geofence;

import android.util.Log;

import com.google.android.gms.location.Geofence;

import java.util.Calendar;
import java.util.Date;

import com.android.part_timer.database.AppDatabase;
import com.android.part_timer.database.entity.LogData;

public class LogTimeUpdate {

    private int geofenceTransition;
    protected static final String TAG = "LogTimeUpdate";
    private static AppDatabase appDatabase;

    public LogTimeUpdate(int geofenceTransition ,AppDatabase appdatabase) {
        this.geofenceTransition = geofenceTransition;
        appDatabase=appdatabase;

    }

    public void SetLogTime(String placeID) {
        if(!placeID.equals("")) {
            try {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND,0);
                Date logDate = cal.getTime();
                Log.v(TAG,logDate.toString());
                if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                    LogData lastLogData = appDatabase.logDataDaoModel().getLogInfo();
                    if(lastLogData==null){
                        LogData logData = new LogData();
                        logData.setPlaceID(placeID);
                        logData.setCheckIn(logDate);
                        logData.setTracking(true);
                        appDatabase.logDataDaoModel().insert(logData);
                        Log.v(TAG, "inserted check in time to DB with log data null");
                    }else {
                        LogData logData = new LogData();
                        logData.setPlaceID(placeID);
                        logData.setCheckIn(logDate);
                        logData.setTracking(true);
                        appDatabase.logDataDaoModel().insert(logData);
                        Log.v(TAG, "inserted check in time to DB");
                    }
                } else {
                    LogData logData = appDatabase.logDataDaoModel().getLogInfo();
                    Log.v(TAG, logData.getTracking() + "   trakcing" + logData.getCheckIn().toString() + logData.getId());
                    if (logData.getTracking() == true) {
                        Log.v(TAG, new Date().toString() + "check out date");
                        //  mDb.logDataDaoModel().setCheckOut(new Date(),false,logData.getId());
                        logData.setCheckOut(logDate);
                        logData.setTracking(false);
                        appDatabase.logDataDaoModel().update(logData);
                        Log.v(TAG, "inserted check out time to DB");
                    }
                }
            }catch (NullPointerException e){
                Log.e(TAG,e.toString());
            }
        }
    }
}
