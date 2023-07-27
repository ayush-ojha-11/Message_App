package com.as.mymessage.DatabasePackage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MessageTableModalClass implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="image")
    private int image;

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

    public MessageTableModalClass(int image, @NonNull String mobNumber, String contactName, String message, String date, String time, long timeStamp) {

        this.image = image;
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

    public int getImage() {
        return image;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setImage(int image) {
        this.image = image;
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
        MessageTableModalClass messageTableModalClass = (MessageTableModalClass) obj;

        // comparing the state of argument with
        // the state of 'this' Object.
        return (messageTableModalClass.id == this.id);
    }
}
