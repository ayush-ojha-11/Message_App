package com.as.mymessage.util;

import java.util.ArrayList;
import java.util.List;

public class MainActivityRecyclerItemPOJO<T> {
    List<T> messageList;

    public MainActivityRecyclerItemPOJO() {
        this.messageList = new ArrayList<>();
    }

    public MainActivityRecyclerItemPOJO(List<T> messageList) {
        this.messageList = messageList;
    }

    public List<T> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<T> messageList) {
        this.messageList = messageList;
    }

    public void joinList(List<T> newMessageList){
        this.messageList.addAll(newMessageList);
    }
}
