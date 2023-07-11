package com.as.mymessage.DatabasePackage;

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
    private String sender;

    @ColumnInfo(name="message")
    private String message;

    @ColumnInfo(name="time")
    private String time;

   public MessageTableModalClass(int id, int image, String sender, String message, String time) {
        this.id = id;
        this.image = image;
        this.sender = sender;
        this.message = message;
        this.time = time;
    }

    @Ignore
   public MessageTableModalClass(int image, String sender, String message, String time) {

        this.image = image;
        this.sender = sender;
        this.message = message;
        this.time = time;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
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
}
