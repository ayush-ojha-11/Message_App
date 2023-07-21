package com.as.mymessage.util;

public class ChatMessagePOJO {
    boolean sent;
    String message;
    long timeStamp;

    public ChatMessagePOJO(boolean sent, String message, long timeStamp) {
        this.sent = sent;
        this.message = message;
        this.timeStamp = timeStamp;
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
