package com.as.mymessage.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeStampUtil {
    public static String convertToTime(long timestamp){
        Date date = new Date(timestamp);
        Format format = new SimpleDateFormat("hh:mm a");
        return format.format(date);
    }


    public static String convertToDate(long timestamp){
        Date date = new Date(timestamp);
        Format format = new SimpleDateFormat("d/M/yy");
        return format.format(date);
    }


    public static String getDate(){
        Calendar c  = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");
        return sdf.format(c.getTime());
    }

    public static String getTime(){
        Calendar c  = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(c.getTime());
    }

    public static long getTheTimeStamp(){
       // Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return System.currentTimeMillis();

    }

}
