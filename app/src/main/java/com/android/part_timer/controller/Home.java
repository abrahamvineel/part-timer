package com.android.part_timer.controller;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.android.part_timer.Constants;
import com.android.part_timer.DialogAlert;
import com.android.part_timer.R;
import com.android.part_timer.Utils;
import com.android.part_timer.controller.loglist.LogItemsListActivity;
import com.android.part_timer.database.AppDatabase;
import com.android.part_timer.database.entity.GeneralData;
import com.android.part_timer.database.entity.LocationData;
import com.android.part_timer.database.entity.LogData;

import static com.android.part_timer.Constants.PAY_INFO_MESSAGE;
import static com.android.part_timer.Constants.PAY_INFO_TITLE;
import static com.android.part_timer.Utils.formatDate;

public class Home extends Fragment {

    View view;
    private final static String TAG = "HomeFragment";
    private TextView checkInYearMonth, checkInTime, checkOutYearMonth, checkOutTime, totalTimeStay;
    private FloatingActionButton floatingMenu_add, floatingAdd, floatingEdit;
    private Animation fabOpen, fabClose, fabClock, fabAntiClock;
    public static AppDatabase appDatabase;
    private ImageView checkInEdit, checkInDelete, checkOutEdit,payInfo;
    private TextView floating_edit_text, floating_add_text, greeting, estimatedPay;
    private LinearLayout checkInActionLayout, checkOutActionLayout;
    private SwipeRefreshLayout pullToRefresh;
    private int year, month, day, hour, minute;
    private double payPerHour = 0;
    private List<LocationData> locationDataList;
    private Date checkInDate = null, checkOutDate = null;
    private String checkInText, checkOutText;
    private Boolean checkInAction = false, isPressed = false, twentyFourHour = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_layout, container, false);
        appDatabase = AppDatabase.getDatabaseInstance(getActivity());
        checkInYearMonth = view.findViewById(R.id.checkInYearMonth);
        checkInTime = view.findViewById(R.id.checkInTime);
        checkOutYearMonth = view.findViewById(R.id.checkOutYearMonth);
        checkOutTime = view.findViewById(R.id.checkOutTime);
        greeting = view.findViewById(R.id.greeting);
        checkInActionLayout = view.findViewById(R.id.checkInAction);
        checkOutActionLayout = view.findViewById(R.id.checkOutAction);
        totalTimeStay = view.findViewById(R.id.hours);
        estimatedPay = view.findViewById(R.id.estimatedPay);
        payInfo=view.findViewById(R.id.payInfo);
        floatingMenu_add = view.findViewById(R.id.floatingMenu_add);
        floatingAdd = view.findViewById(R.id.floating_add);
        floating_add_text = view.findViewById(R.id.floating_add_text);
        floatingEdit = view.findViewById(R.id.floating_edit);
        floating_edit_text = view.findViewById(R.id.floating_edit_text);
        checkInEdit = view.findViewById(R.id.checkInEdit);
        checkOutEdit = view.findViewById(R.id.checkOutEdit);
        checkInDelete = view.findViewById(R.id.checkInDelete);
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        fabClock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_clockwise);
        fabAntiClock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_anticlockwise);
        pullToRefresh = view.findViewById(R.id.homeLayout);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setLog();
                pullToRefresh.setRefreshing(false);
            }
        });
        AsyncTask.execute(new Runnable() {
            GeneralData generalData;

            @Override
            public void run() {
                generalData = appDatabase.generalDataDaoModel().getGeneralSettings();
                locationDataList = appDatabase.locationDataDaoModel().loadLocations();
                if (null != generalData && generalData.getTwentyFourHour()) {
                    twentyFourHour = true;
                }
                if (null != generalData && generalData.getPayPerHour() > 0) {
                    payPerHour = generalData.getPayPerHour();
                }
            }
        });
        setLog();
        greeting.setText(Utils.greeting(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
        checkInText = (String) checkInTime.getText();
        checkOutText = (String) checkOutTime.getText();
        Log.v(TAG, "text " + checkInText);
        if (null == checkInText || checkInText.equals("")) {
            checkInActionLayout.setVisibility(View.INVISIBLE);
        }
        if (null == checkOutText || checkOutText.equals("")) {
            checkOutActionLayout.setVisibility(View.INVISIBLE);
        }
        payInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogAlert(getContext()).buildOkDialog(PAY_INFO_TITLE,PAY_INFO_MESSAGE).setIcon(R.drawable.ic_info_inline).show();
            }
        });
        floatingMenu_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "Floating menu add pressed");
                fabAnimation();
            }
        });
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "Floating add pressed");
                fabAnimation();
                if (locationDataList.size() > 0) {
                    AddLogTimeDialog addLogTimeDialog = new AddLogTimeDialog(getContext(), getActivity());
                    addLogTimeDialog.createDialog(addLogTimeDialog.showLogDialog().show());
                } else {
                    new DialogAlert(getContext()).buildOkDialog("No Work Location", "Please add work location").show();
                }
            }
        });

        floatingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "Floating edit pressed");
                fabAnimation();
                Intent intent = new Intent(getActivity(), LogItemsListActivity.class);
                startActivity(intent);
            }
        });
        checkInEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.v(TAG, "check in text in edit button" + checkInText);
                Calendar checkInCalendar = Calendar.getInstance();
                checkInCalendar.setTime(checkInDate);
                checkInAction = true;
                DatePickerDialog checkInDatePickerDialog = new DatePickerDialog(getContext(), datePickerListener,
                        checkInCalendar.get(Calendar.YEAR), checkInCalendar.get(Calendar.MONTH), checkInCalendar.get(Calendar.DAY_OF_MONTH));
                checkInDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                checkInDatePickerDialog.show();
            }
        });
        checkOutEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.v(TAG, "check out text in edit button" + checkOutText);
                Calendar checkOutCalendar = Calendar.getInstance();
                checkOutCalendar.setTime(checkOutDate);
                checkInAction = false;
                DatePickerDialog checkOutDatePickerDialog = new DatePickerDialog(getContext(), datePickerListener,
                        checkOutCalendar.get(Calendar.YEAR), checkOutCalendar.get(Calendar.MONTH), checkOutCalendar.get(Calendar.DAY_OF_MONTH));
                checkOutDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                checkOutDatePickerDialog.show();
            }
        });
        checkInDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (null != checkInDate) {
                    new DialogAlert(getContext()).yesNoDialog(dialogClickListener, "Part-Timer").show();
                }

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isNetworkAvailable()) {
            new DialogAlert(getContext()).buildOkDialog(Constants.NO_INTERNET_TITLE,
                    Constants.NO_INTERNET_MESSAGE).show();
        } else {
            if (!checkGpsStatus(getContext())) {
                new DialogAlert(getContext()).buildOkDialog(Constants.NO_GPS_TITLE,
                        Constants.NO_GPS_MESSAGE).show();
            }
        }
        setLog();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Log.v(TAG, "yes");
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            LogData logData = appDatabase.logDataDaoModel().getLogFromCheckIn(checkInDate.getTime());
                            Log.v(TAG, "Id to delete " + logData.getId());
                            appDatabase.logDataDaoModel().deleteLocation(logData);
                            setLog();
                        }
                    });
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    Log.v(TAG, "no");
                    break;
            }
        }
    };

    private void fabAnimation() {
        if (isPressed) {
            floatingMenu_add.startAnimation(fabAntiClock);
            floatingAdd.startAnimation(fabClose);
            floatingEdit.startAnimation(fabClose);
            floatingAdd.setClickable(false);
            floating_add_text.startAnimation(fabClose);
            floating_edit_text.startAnimation(fabClose);
            floatingEdit.setClickable(false);
            isPressed = false;
        } else {
            floatingMenu_add.startAnimation(fabClock);
            floatingAdd.startAnimation(fabOpen);
            floatingEdit.startAnimation(fabOpen);
            floating_add_text.startAnimation(fabOpen);
            floating_edit_text.startAnimation(fabOpen);
            floatingAdd.setClickable(true);
            floatingEdit.setClickable(true);
            isPressed = true;
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            Log.v(TAG, selectedYear + "," + selectedMonth + "," + selectedDay);
            Calendar calendar = Calendar.getInstance();
            if (checkInAction) {
                calendar.setTime(checkInDate);
            } else {
                calendar.setTime(checkOutDate);
            }
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), timePickerListener,
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), twentyFourHour);
            timePickerDialog.show();
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int selectedMinute) {
            hour = hourOfDay;
            minute = selectedMinute;
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(year, month, day, hour, minute);
            selectedCalendar.set(Calendar.SECOND, 0);
            selectedCalendar.set(Calendar.MILLISECOND, 0);
            final Date selectedDate = selectedCalendar.getTime();
            AsyncTask.execute(new Runnable() {
                String message = "No message";

                @Override
                public void run() {
                    try {
                        if ((null != checkOutDate || !checkOutText.equals("")) &&
                                ((checkInAction && (selectedDate.after(checkOutDate) || selectedDate.equals(checkOutDate))) ||
                                        (!checkInAction && (selectedDate.before(checkInDate) || selectedDate.equals(checkInDate))))) {
                            if (checkInAction && selectedDate.after(checkOutDate)) {
                                message = Constants.CHECKIN_ERROR;
                            } else if (!checkInAction && selectedDate.before(checkInDate)) {
                                message = Constants.CHECKOUT_ERROR;
                            } else {
                                message = Constants.CHECKIN_CHECKOUT_ERROR;
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new DialogAlert(getContext()).buildOkDialog(Constants.CHECKIN_CHEKOUT_ERROR_NOTIFICATION_TITLE, message).show();
                                }
                            });
                        } else {
                            LogData logData = appDatabase.logDataDaoModel().getLogFromCheckIn(checkInDate.getTime());
                            LogData betweenData = appDatabase.logDataDaoModel().getLogDateBetween(logData.getId(), selectedDate.getTime());
                            if (betweenData == null || logData.getId() == betweenData.getId()) {
                                if (checkInAction) {
                                    logData.setCheckIn(selectedDate);
                                } else {
                                    logData.setCheckOut(selectedDate);
                                }
                                //Log.v(TAG,logData.getId()+","+logData.getCheckIn().toString()+","+
                                //      logData.getCheckOut()+ ","+betweenData.getId()+","+betweenData.getCheckIn()+","+betweenData.getCheckOut());
                                Log.v(TAG, "in between" + betweenData);
                                appDatabase.logDataDaoModel().update(logData);
                                setLog();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), Constants.LOGTIME_UPDATE_SUCCESS, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Log.v(TAG, betweenData.getId() + "");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new DialogAlert(getContext()).buildOkDialog(Constants.DATA_EXISTS_TILE, Constants.DATA_EXISTS_MESSAGE).show();
                                    }
                                });
                            }
                        }
                    } catch (NullPointerException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            });
            Log.v(TAG, hourOfDay + "," + selectedMinute);
        }
    };

    public void setLog() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final LogData logData = appDatabase.logDataDaoModel().getLogInfo();
                final List<LogData> weekLogList = appDatabase.logDataDaoModel().getWeekLogs();
                if (logData != null) {

                    checkInTime.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String[] checkIn = formatDate(logData.getCheckIn(), twentyFourHour);
                                checkInTime.setText(checkIn[0]);
                                checkInYearMonth.setText(checkIn[1]);
                                checkInActionLayout.setVisibility(View.VISIBLE);
                                checkInText = (String) checkInTime.getText();
                                checkInDate = logData.getCheckIn();
                            } catch (NullPointerException e) {
                                Log.e(TAG, e.toString());
                            }
                        }
                    });

                    checkOutTime.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (logData.getCheckOut() != null) {
                                    String[] checkOut = formatDate(logData.getCheckOut(), twentyFourHour);
                                    checkOutTime.setText(checkOut[0]);
                                    checkOutYearMonth.setText(checkOut[1]);
                                    checkOutActionLayout.setVisibility(View.VISIBLE);
                                    checkOutDate = logData.getCheckOut();
                                } else {
                                    checkOutTime.setText("");
                                    checkOutYearMonth.setText("");
                                    checkOutDate = null;
                                }
                            } catch (NullPointerException e) {
                                Log.e(TAG, e.toString());
                            }
                        }
                    });

                    totalTimeStay.post(new Runnable() {
                        @Override
                        public void run() {
                            String time = dateDifference(weekLogList);
                            totalTimeStay.setText(time);
                            int hours = Integer.parseInt(time.split(":")[0]);
                            int min = Integer.parseInt(time.split(":")[1]);
                            double pay = (hours * payPerHour) + ((double) min / 60) * payPerHour;
                            estimatedPay.setText(Constants.DOLLAR + String.format("%.2f", pay));
                        }
                    });
                } else {
                    checkInTime.post(new Runnable() {
                        @Override
                        public void run() {
                            checkInTime.setText("");
                            checkInYearMonth.setText("");
                            checkInActionLayout.setVisibility(View.INVISIBLE);
                            checkInText = "";
                            checkInDate = null;
                            checkOutTime.setText("");
                            checkOutYearMonth.setText("");
                            checkOutActionLayout.setVisibility(View.INVISIBLE);
                            checkOutDate = null;
                            totalTimeStay.setText("");
                            estimatedPay.setText("");
                        }
                    });
                }
            }
        });
    }

    public String dateDifference(List<LogData> weekLogDataList) {

        long totalTimeMillis = 0L;
        //milliseconds
        for (LogData logData : weekLogDataList) {
            if (logData.getCheckOut() != null) {
                totalTimeMillis = totalTimeMillis + (logData.getCheckOut().getTime() - logData.getCheckIn().getTime());
            }
        }
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        long hours = totalTimeMillis / hoursInMilli;
        totalTimeMillis = totalTimeMillis % hoursInMilli;

        long minutes = totalTimeMillis / minutesInMilli;

        return String.format("%02d", (int) (hours)) + ":" + String.format("%02d", (int) (minutes));

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

    public boolean checkGpsStatus(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
