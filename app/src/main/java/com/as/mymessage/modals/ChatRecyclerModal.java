package com.as.mymessage.modals;

import java.io.Serializable;

public class ChatRecyclerModal  {
    String message;

    public ChatRecyclerModal(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
