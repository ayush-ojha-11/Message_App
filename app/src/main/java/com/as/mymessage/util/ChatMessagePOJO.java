package com.as.mymessage.util;

public class ChatMessagePOJO {
    boolean sent;
    String message;
    String time;

    public ChatMessagePOJO(boolean sent, String message, String time) {
        this.sent = sent;
        this.message = message;
        this.time = time;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
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
