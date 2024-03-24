package com.as.mymessage.util;

import android.annotation.SuppressLint;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeStampUtil {
    public static String convertToTime(long timestamp){
        Date date = new Date(timestamp);
        @SuppressLint("SimpleDateFormat") Format format = new SimpleDateFormat("hh:mm a");
        return format.format(date);
    }


    public static String convertToDate(long timestamp){
        Date date = new Date(timestamp);
        @SuppressLint("SimpleDateFormat") Format format = new SimpleDateFormat("dd MMM yy");
        return format.format(date);
    }


    public static String getDate(){
        Calendar c  = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy");
        return sdf.format(c.getTime());
    }

    public static String getTime(){
        Calendar c  = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(c.getTime());
    }

    public static long getTheTimeStamp(){
        return System.currentTimeMillis();
    }

}
