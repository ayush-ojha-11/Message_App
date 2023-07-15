package com.as.mymessage.DatabasePackage;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class MessageTableModalClass {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="image")
    private int image;

    @ColumnInfo(name="sender")
    @NonNull
    private String sender;

    @ColumnInfo(name="message")
    private String message;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name="time")
    private String time;

    @ColumnInfo(name="timeStamp")
    private long timeStamp;

   public MessageTableModalClass(int id, int image, String sender, String message,String date, String time) {
        this.id = id;
        this.image = image;
        this.sender = sender;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    @Ignore
   public MessageTableModalClass(int image, String sender, String message,String date, String time, long timeStamp) {

        this.image = image;
        this.sender = sender;
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
    public String getSender() {
        return sender;
    }

    public void setSender(@NonNull String sender) {
        this.sender = sender;
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
