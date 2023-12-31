package com.as.mymessage.modals;


import androidx.annotation.Nullable;
import androidx.room.Ignore;

import java.io.Serializable;

public class RecyclerModalClass implements Serializable {
    int id;
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
    public RecyclerModalClass (String mobNumber,String contactName, String message, String date, String time) {
        this.contactName = contactName;
        this.mobNumber= mobNumber;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    // For main activity
    @Ignore
    public RecyclerModalClass(int id,String mobNumber,String contactName, String message,String date, String time, long timeStamp) {
        this.id = id;
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

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        //return true if they are same objects
        if(this == obj)
            return true;

        // if object is null or object is of another class
        if(obj == null || obj.getClass()!= this.getClass())
            return false;

        // type casting of the argument.
        RecyclerModalClass recyclerModalClass = (RecyclerModalClass) obj;

        // comparing the state of argument with
        // the state of 'this' Object.
        return (recyclerModalClass.id == this.id);
    }
}
