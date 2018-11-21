package csci5708.csci4176.part_timer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import csci5708.csci4176.part_timer.database.AppDatabase;
import csci5708.csci4176.part_timer.database.entity.LogData;

public class Home extends Fragment {

    View view;
    private final static String TAG = "HomeFragment";
    private TextView checkIn,checkOut,totalTimeStay;
    public static AppDatabase appDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.home_layout, container, false);
        appDatabase=AppDatabase.getDatabaseInstance(getActivity());
        checkIn=view.findViewById(R.id.checkIn);
        checkOut=view.findViewById(R.id.checkOut);
        totalTimeStay=view.findViewById(R.id.hours);
        isNetworkAvailable();
        setLog();
        return view;
    }

    public void setLog() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    LogData logData = appDatabase.logDataDaoModel().getLogInfo();
                    checkIn.setText("Check In : "+logData.getCheckIn().toString());
                    if(logData.getCheckOut()!=null){
                        checkOut.setText("Check Out:"+logData.getCheckOut().toString());
                        String diffs=dateDifference(logData.getCheckIn(),logData.getCheckOut());
                        totalTimeStay.setText("Total No Of Hours : "+diffs);
                    }
                    Log.v(TAG, "In thread after insert");
                }catch (NullPointerException e){
                    Log.e(TAG,e.toString());
                }
                // Get Data   AppDatabase.getInstance(context).userDao().getAllUsers();
            }
        });
    }

    public String dateDifference(Date startDate, Date endDate){

        //milliseconds
        long difference = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        //long elapsedDays = difference / daysInMilli;
        //difference = difference % daysInMilli;

        long hours = difference / hoursInMilli;
        difference = difference % hoursInMilli;

        long minutes = difference / minutesInMilli;
        difference = difference % minutesInMilli;

        long seconds = difference / secondsInMilli;

        return String.format("%02d",(int) (hours))+":"+String.format("%02d",(int) (minutes))+":"+String.valueOf(seconds);

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

    private void showNoConnectionDialog() {
        Toast.makeText(getContext(), "Can't connect to internet,Please check the Internet Connection!", Toast.LENGTH_LONG).show();
    }
}
