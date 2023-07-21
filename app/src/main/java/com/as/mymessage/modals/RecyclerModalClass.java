package com.as.mymessage.modals;


import androidx.room.Ignore;

import java.io.Serializable;

public class RecyclerModalClass implements Serializable {
    int id;
    int image;
    String contactName;
    String mobNumber;
    String message;
    String date;
    String time;
    long timeStamp;

    public String getMobNumber() {
        return mobNumber;
    }

    public void setMobNumber(String mobNumber) {
        this.mobNumber = mobNumber;
    }

    //For intent through SMSReceiver
    public RecyclerModalClass (int image, String mobNumber,String contactName, String message, String date, String time) {
        this.image = image;
        this.contactName = contactName;
        this.mobNumber= mobNumber;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    // For main activity
    @Ignore
    public RecyclerModalClass(int image, String mobNumber,String contactName, String message,String date, String time, long timeStamp) {

        this.image = image;
        this.contactName = contactName;
        this.mobNumber = mobNumber;
        this.message = message;
        this.date = date;
        this.time = time;
        this.timeStamp = timeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStammp) {
        this.timeStamp = timeStammp;
    }
}
