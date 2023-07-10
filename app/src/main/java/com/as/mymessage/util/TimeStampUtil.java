package com.as.mymessage.util;

import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;

public class TimeStampUtil {
    public static String convert(long timestamp){
        Date date = new Date(timestamp);
        Format format = new SimpleDateFormat("hh:mm a");
        return format.format(date);
    }
}
