package com.android.part_timer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String[] formatDate(Date formatDate,boolean twentyFour) {

        String[] checkTime = {"", ""};

        if (null != formatDate) {
            checkTime[0] = new SimpleDateFormat(twentyFour?"HH:mm":"hh:mm a").format(formatDate);
            checkTime[1] = new SimpleDateFormat("yyyy MMM dd").format(formatDate);
        }
        return checkTime;

    }

    public static String greeting(int timeOfDay){

        String greeting="";
        if (timeOfDay >= 0 && timeOfDay < 12) {
            greeting=Constants.MORNING;
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            greeting=Constants.AFTERNOON;
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            greeting=Constants.EVENING;
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            greeting=Constants.NIGHT;
        }
        return greeting;
    }


}
