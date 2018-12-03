package com.android.part_timer;

public final class Constants {

    private Constants() {
    }
    public static final String CHANNEL_1_ID = "Enter/Exit";

    public static final float GEOFENCE_RADIUS_IN_METERS = 150; //

    public static final int NOTIFICATION_ID = 10;
    public static final String YES_ACTION = "yes";
    public static final String NO_ACTION = "no";

    //DialogButtons
    public static final String OK_BUTTON = "Ok";
    public static final String CANCEL_BUTTON = "Cancel";

    //yes/no dialog
    public static final String YES_NO_DIALOG_MESSAGE = "Are you sure?";
    public static final String YES_NO_DIALOG_YES_BUTTON = "Yes";
    public static final String YES_NO_DIALOG_NO_BUTTON = "No";

    public static final String WORK_LOCATION_NAME = "Work";

    //Toast
    public static final String LOGTIME_UPDATE_SUCCESS = "Successfully Updated Log times";
    public static final String UPDATE_SUCCESS = "Successfully Updated";

    public static final String DOLLAR = "$";
    //No Internet
    public static final String NO_INTERNET_TITLE = "No Internet Connection";
    public static final String NO_INTERNET_MESSAGE = "Please connect to internet to track the Log time";

    //No GPS
    public static final String NO_GPS_TITLE = "No GPS";
    public static final String NO_GPS_MESSAGE = "Please turn on the GPS";

    //Greeting message
    public static final String MORNING = "Good Morning";
    public static final String AFTERNOON = "Good Afternoon";
    public static final String EVENING = "Good Evening";
    public static final String NIGHT = "Good Night";

    //contact mailId's
    public static final String[] mailIds=new String[] { "part.timer@helpdesk.com" };


    //LogListAdapter
    public static final String CHECKIN_CHEKOUT_ERROR_NOTIFICATION_TITLE = "Wrong Log times";
    public static final String CHECKIN_ERROR = "Check-In time should not be greater than Check-Out time";
    public static final String CHECKOUT_ERROR = "Check-Out time should not be lesser than Check-In time";
    public static final String CHECKIN_CHECKOUT_ERROR = "Check-In and Check-Out time cannot be equal";
    public static final String DATA_EXISTS_TILE = "Data already exists";
    public static final String DATA_EXISTS_MESSAGE = "Log time is already available for selected range of time";



    //GeofenceTransitionsIntentServices
    public static final String GEOFENCE_ENTER_NOTIFICATION_TITLE = "You have entered ";
    public static final String GEOFENCE_ENTER_NOTIFICATION_MESSAGE = "Do you want to Log your work hours?";
    public static final String GEOFENCE_EXIT_NOTIFICATION_TITLE = "You have left work at ";
    public static final String GEOFENCE_EXIT_NOTIFICATION_MESSAGE = "Have a great day";
    public static final String NOTIFICATION_YES = "Yes";
    public static final String NOTIFICATION_NO = "No";



}

