package com.android.part_timer;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.android.part_timer.database.AppDatabase;
import com.android.part_timer.database.entity.LogData;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";
    private AppDatabase appDatabase;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v(TAG, "Notification receiver");
        appDatabase = AppDatabase.getDatabaseInstance(context);
        Log.v(TAG, "intent action"+intent.getAction());
        mContext=context;
        if(null!=intent.getAction() && Constants.NO_ACTION.equals(intent.getAction())) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    LogData logData = appDatabase.logDataDaoModel().getLogInfo();
                    Log.v(TAG, "Deleting the id:  " + logData.getId()+" as user pressed no for tracking notification");
                    appDatabase.logDataDaoModel().deleteLocation(logData);
                }
            });
        }
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Constants.NOTIFICATION_ID);
    }
}