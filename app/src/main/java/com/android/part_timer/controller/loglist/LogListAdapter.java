package com.android.part_timer.controller.loglist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
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
import com.android.part_timer.database.entity.LogData;

import static com.android.part_timer.Utils.formatDate;

public class LogListAdapter extends RecyclerView.Adapter<LogListAdapter.LogHolder> {
    private final static String TAG = "LogListAdapter";
    private List<LogData> logs = new ArrayList<>();
    private int year, month, day, hour, minute;
    private Context context;
    private Activity activity;
    private Boolean checkInAction = false,twentyFourHour;
    public static AppDatabase appDatabase;
    private Date checkInDate = null, checkOutDate = null;
    private int position;

    //constructor
    public LogListAdapter(Context context, Activity activity,Boolean twentyFourHour) {
        this.context = context;
        this.activity = activity;
        appDatabase = AppDatabase.getDatabaseInstance(context);
        this.twentyFourHour=twentyFourHour;
    }

    @NonNull
    @Override
    public LogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.log_item, parent, false);
        return new LogHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LogHolder logHolder, int position) {

        //here we need to set the listeners for images and set the text views
        LogData currentLogData = logs.get(position);
        String[] checkIn = formatDate(currentLogData.getCheckIn(),twentyFourHour);
        logHolder.item_checkInTime.setText(checkIn[0]);
        logHolder.item_checkInYearMonth.setText(checkIn[1]);
        logHolder.setCheckInListeners();
        String[] checkOut = formatDate(currentLogData.getCheckOut(),twentyFourHour);
        logHolder.item_checkOutTime.setText(checkOut[0]);
        logHolder.item_checkOutYearMonth.setText(checkOut[1]);
        logHolder.setCheckOutListeners();

    }


    @Override
    public int getItemCount() {
        return logs.size();
    }


    //this class holds the logic for listeners and initialize the views
    class LogHolder extends RecyclerView.ViewHolder {
        private TextView item_checkInYearMonth, item_checkInTime, item_checkOutYearMonth, item_checkOutTime;
        private ImageView item_checkInEdit, item_checkInDelete, item_checkOutEdit;

        public LogHolder(View itemView) {
            super(itemView);
            item_checkInYearMonth = itemView.findViewById(R.id.item_checkInYearMonth);
            item_checkInTime = itemView.findViewById(R.id.item_checkInTime);
            item_checkOutYearMonth = itemView.findViewById(R.id.item_checkOutYearMonth);
            item_checkOutTime = itemView.findViewById(R.id.item_checkOutTime);
            item_checkInEdit = itemView.findViewById(R.id.item_checkInEdit);
            item_checkInDelete = itemView.findViewById(R.id.item_checkInDelete);
            item_checkOutEdit = itemView.findViewById(R.id.item_checkOutEdit);
        }

        public void setCheckInListeners() {
            item_checkInEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //get the position of the card the user pressed on
                    position = getAdapterPosition();
                    Log.v(TAG, logs.get(position).getId() + "," + logs.get(position).getCheckIn().toString());
                    checkInAction = true;
                    Calendar checkInCalendar = Calendar.getInstance();
                    checkInCalendar.setTime(logs.get(position).getCheckIn());
                    checkInDate = logs.get(position).getCheckIn();
                    checkOutDate = logs.get(position).getCheckOut();
                    //call the date listener to allow the user select the desired date
                    DatePickerDialog checkInDatePickerDialog = new DatePickerDialog(context, datePickerListener,
                            checkInCalendar.get(Calendar.YEAR), checkInCalendar.get(Calendar.MONTH), checkInCalendar.get(Calendar.DAY_OF_MONTH));
                    //avoid setting future date
                    checkInDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    checkInDatePickerDialog.show();

                }
            });
            item_checkInDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = getAdapterPosition();
                    Log.v(TAG, logs.get(position).getId() + "," + logs.get(position).getCheckIn().toString());
                    if (null != logs.get(position).getCheckIn()) {
                        //get dialog alert class to confirm the action
                        new DialogAlert(context).yesNoDialog(dialogClickListener, "Part-Timer").show();
                    }
                }
            });
        }

        public void setCheckOutListeners() {
            item_checkOutEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = getAdapterPosition();
                    Log.v(TAG, logs.get(position).getId() + "," + logs.get(position).getCheckIn().toString());
                    Calendar checkOutCalendar = Calendar.getInstance();
                    //if checkout date is null then checkout date has to be current time
                    if (null != logs.get(position).getCheckOut()) {
                        checkOutCalendar.setTime(logs.get(position).getCheckOut());
                        checkOutDate = logs.get(position).getCheckOut();
                    } else {
                        checkOutDate = new Date();
                    }
                    checkInDate = logs.get(position).getCheckIn();
                    //to differentiate the action from check-in listeners
                    checkInAction = false;
                    DatePickerDialog checkOutDatePickerDialog = new DatePickerDialog(context, datePickerListener,
                            checkOutCalendar.get(Calendar.YEAR), checkOutCalendar.get(Calendar.MONTH), checkOutCalendar.get(Calendar.DAY_OF_MONTH));
                    checkOutDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    checkOutDatePickerDialog.show();
                }
            });

        }
    }

    //listener for the dialog to delete the log time
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Log.v(TAG, "yes");
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            Log.v(TAG, "Id to delete " + logs.get(position).getId());
                            appDatabase.logDataDaoModel().deleteLocation(logs.get(position));
                        }
                    });
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    Log.v(TAG, "no");
                    break;
            }
        }
    };

    public void setLogs(List<LogData> logs) {
        this.logs = logs;
        notifyDataSetChanged();
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
            //time picker to set the time
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
            Log.v(TAG, selectedDate.toString());
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //check for the check-in and check-out relation errors
                        if (null != checkOutDate &&
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
                            Log.v(TAG, "in else" + selectedDate.toString() + " check in action " + checkInAction);
                            //query to check if selected date is not in between any other Log date of DB
                            LogData betweenData = appDatabase.logDataDaoModel().getLogDateBetween(logs.get(position).getId(), selectedDate.getTime());
                            //if either there is not between data
                           if (betweenData == null || logs.get(position).getId() == betweenData.getId()) {
                                if (checkInAction) {
                                    logs.get(position).setCheckIn(selectedDate);
                                } else {
                                    logs.get(position).setCheckOut(selectedDate);
                                    logs.get(position).setTracking(false);
                                }
                                Log.v(TAG, "in between" + betweenData);
                                appDatabase.logDataDaoModel().update(logs.get(position));
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, Constants.LOGTIME_UPDATE_SUCCESS, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            //shows error dialog if already data exists for selected date
                            else {
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
