package com.android.part_timer.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.android.part_timer.Constants;
import com.android.part_timer.DialogAlert;
import com.android.part_timer.R;
import com.android.part_timer.database.AppDatabase;
import com.android.part_timer.database.entity.GeneralData;
import com.android.part_timer.database.entity.LocationData;
import com.android.part_timer.database.entity.LogData;

import static com.android.part_timer.Utils.formatDate;

public class AddLogTimeDialog {

    private final static String TAG = "AddLogTimeDialog";
    private Context context;
    private Activity activity;
    LayoutInflater layoutInflater;
    private ImageView addLog_checkInEdit, addLog_checkOutEdit, addLog_checkInDelete;
    private TextView addLog_checkInTime, addLog_checkInYearMonth, addLog_checkOutTime, addLog_checkOutYearMonth;
    private Spinner locationSpinner;
    public static AppDatabase appDatabase;
    private int year, month, day, hour, minute;
    private LinearLayout addLog_checkOutAction;
    private Boolean checkInAction = false, twentyFourHour = false;
    private Date checkInDate = null, checkOutDate = null;
    private String selectedLocation;
    private LogData logData;
    private ArrayList<String> locations;
    private List<LocationData> locationDataList;
    private ArrayAdapter<String> adapter;
    private AlertDialog alertDialog;

    public AddLogTimeDialog(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(context);
        appDatabase = AppDatabase.getDatabaseInstance(context);
    }

    public void createDialog(AlertDialog alertDialog) {
        this.alertDialog = alertDialog;
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }

    public AlertDialog.Builder showLogDialog() {
        LinearLayout addLogLayout = (LinearLayout) layoutInflater.inflate(R.layout.add_logtime, null);
        logData = new LogData();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(addLogLayout);
        addLog_checkInEdit = addLogLayout.findViewById(R.id.addLog_checkInEdit);
        addLog_checkOutEdit = addLogLayout.findViewById(R.id.addLog_checkOutEdit);
        addLog_checkInTime = addLogLayout.findViewById(R.id.addLog_checkInTime);
        addLog_checkInYearMonth = addLogLayout.findViewById(R.id.addLog_checkInYearMonth);
        addLog_checkOutTime = addLogLayout.findViewById(R.id.addLog_checkOutTime);
        addLog_checkOutYearMonth = addLogLayout.findViewById(R.id.addLog_checkOutYearMonth);
        addLog_checkOutAction = addLogLayout.findViewById(R.id.addLog_checkOutAction);
        addLog_checkInDelete = addLogLayout.findViewById(R.id.addLog_checkInDelete);
        locationSpinner = addLogLayout.findViewById(R.id.locationSpinner);
        locations = new ArrayList<>();

        //get the locations from db and set it to spinner
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                locationDataList = appDatabase.locationDataDaoModel().loadLocations();
                for (LocationData locationData : locationDataList) {
                    locations.add(locationData.getPlaceID());
                }
                if (locations != null) {
                    selectedLocation = locations.get(0);
                    adapter = new ArrayAdapter<String>(context,
                            android.R.layout.simple_spinner_item, locations);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.post(new Runnable() {
                        @Override
                        public void run() {
                            locationSpinner.setAdapter(adapter);
                            locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                    selectedLocation = locations.get(position);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parentView) {
                                    Log.v(TAG, "nothing selected");
                                }

                            });
                        }
                    });
                    GeneralData generalData;
                    generalData = appDatabase.generalDataDaoModel().getGeneralSettings();
                    if (null != generalData && generalData.getTwentyFourHour()) {
                        twentyFourHour = true;
                    }
                }
            }
        });

        //listeners for the check-in and check-out edit and delete image views
        addLog_checkInEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar checkInCalendar = Calendar.getInstance();
                checkInAction = true;
                DatePickerDialog checkInDatePickerDialog = new DatePickerDialog(context, datePickerListener,
                        checkInCalendar.get(Calendar.YEAR), checkInCalendar.get(Calendar.MONTH), checkInCalendar.get(Calendar.DAY_OF_MONTH));
                checkInDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                checkInDatePickerDialog.show();
            }
        });
        addLog_checkInDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLog_checkInTime.setText("");
                addLog_checkInYearMonth.setText("");
                addLog_checkOutTime.setText("");
                addLog_checkOutYearMonth.setText("");
                checkInDate = null;
                checkOutDate = null;
                //check-out edit is disable
                addLog_checkOutAction.setVisibility(View.INVISIBLE);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                });
            }
        });
        addLog_checkOutEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar checkInCalendar = Calendar.getInstance();
                checkInAction = false;
                checkOutDate = new Date();
                DatePickerDialog checkInDatePickerDialog = new DatePickerDialog(context, datePickerListener,
                        checkInCalendar.get(Calendar.YEAR), checkInCalendar.get(Calendar.MONTH), checkInCalendar.get(Calendar.DAY_OF_MONTH));
                checkInDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                checkInDatePickerDialog.show();
            }
        });
        builder.setPositiveButton(Constants.OK_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        logData.setTracking(false);
                        logData.setPlaceID(selectedLocation);
                        appDatabase.logDataDaoModel().insert(logData);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, Constants.LOGTIME_UPDATE_SUCCESS, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }
                });

            }
        });
        builder.setNegativeButton(Constants.CANCEL_BUTTON, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        return builder;
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
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, timePickerListener,
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), twentyFourHour);
            timePickerDialog.show();
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        String message = "No message";

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
                @Override
                public void run() {
                    try {
                        //check for the check-in and check-out relation errors
                        if ((null != checkOutDate) &&
                                ((checkInAction && (selectedDate.after(checkOutDate) || selectedDate.equals(checkOutDate))) ||
                                        (!checkInAction && (selectedDate.before(checkInDate) || selectedDate.equals(checkInDate))))) {
                             //check to see selected date is not greater than check-out time
                            if (checkInAction && selectedDate.after(checkOutDate)) {
                                message = Constants.CHECKIN_ERROR;
                            }
                            //check to see selected date is not lesser than check-in date
                            else if (!checkInAction && selectedDate.before(checkInDate)) {
                                message = Constants.CHECKOUT_ERROR;
                            }
                            //error check if check-in and check-out are equal
                            else {
                                message = Constants.CHECKIN_CHECKOUT_ERROR;
                            }
                            //shows the dialog error based on the message got from above checks
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new DialogAlert(context).buildOkDialog(Constants.CHECKIN_CHEKOUT_ERROR_NOTIFICATION_TITLE, message).show();
                                }
                            });
                        }
                        //if there are no check errors
                        else {
                            LogData betweenData = appDatabase.logDataDaoModel().getLogDateBetween(logData.getId(), selectedDate.getTime());
                            if (betweenData == null || logData.getId() == betweenData.getId()) {
                                if (checkInAction) {
                                    logData.setCheckIn(selectedDate);
                                    checkInDate = selectedDate;
                                    addLog_checkInTime.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                String[] checkIn = formatDate(logData.getCheckIn(), twentyFourHour);
                                                addLog_checkInTime.setText(checkIn[0]);
                                                addLog_checkInYearMonth.setText(checkIn[1]);
                                                addLog_checkOutAction.setVisibility(View.VISIBLE);
                                            } catch (NullPointerException e) {
                                                Log.e(TAG, e.toString());
                                            }
                                        }
                                    });
                                } else {
                                    logData.setCheckOut(selectedDate);
                                    checkOutDate = selectedDate;
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                        }
                                    });
                                    addLog_checkOutTime.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                String[] checkOut = formatDate(logData.getCheckOut(), twentyFourHour);
                                                addLog_checkOutTime.setText(checkOut[0]);
                                                addLog_checkOutYearMonth.setText(checkOut[1]);
                                            } catch (NullPointerException e) {
                                                Log.e(TAG, e.toString());
                                            }
                                        }
                                    });
                                }
                            } else {
                                Log.v(TAG, betweenData.getId() + "");
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new DialogAlert(context).buildOkDialog(Constants.DATA_EXISTS_TILE, Constants.DATA_EXISTS_MESSAGE).show();
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
}
