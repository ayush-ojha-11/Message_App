package com.as.mymessage.modals;


import androidx.room.Ignore;

import java.io.Serializable;

public class RecyclerModalClass implements Serializable {
    int id;
    int image;
    String name;
    String message;
    String time;

    public RecyclerModalClass (int id,int image, String name, String message, String time) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.message = message;
        this.time = time;
    }
    @Ignore
    public RecyclerModalClass(int image, String name, String message, String time) {

        this.image = image;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
