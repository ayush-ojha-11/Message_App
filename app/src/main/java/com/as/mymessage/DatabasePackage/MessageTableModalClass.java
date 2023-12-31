package com.as.mymessage.DatabasePackage;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class MessageTableModalClass implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="mobNumber")
    @NonNull
    private String mobNumber;

    @ColumnInfo(name="contactName")
    private String contactName;

    @ColumnInfo(name="message")
    private String message;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name="time")
    private String time;

    @ColumnInfo(name="timeStamp")
    private long timeStamp;

    public MessageTableModalClass(@NonNull String mobNumber, String contactName, String message, String date, String time, long timeStamp) {


        if(mobNumber.matches("([0-9])") && mobNumber.charAt(0) != '+'){
            mobNumber += '+';
        }
        this.mobNumber = mobNumber;
        this.contactName = contactName;
        this.message = message;
        this.date=date;
        this.time = time;
        this.timeStamp = timeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }


    @NonNull
    public String getMobNumber() {
        return mobNumber;
    }

    public void setMobNumber(@NonNull String mobNumber) {
        this.mobNumber = mobNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
