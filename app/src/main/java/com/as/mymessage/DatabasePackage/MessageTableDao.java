package com.as.mymessage.DatabasePackage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MessageTableDao {


    @Query("SELECT * FROM messagetablemodalclass order by id asc")
    List<MessageTableModalClass> getAllMessages();

    @Insert
    void addMessage(MessageTableModalClass messageTableModalClass);


    @Update
    void updateMessage(MessageTableModalClass messageTableModalClass);

    @Insert
    void newAdd(OutGoingMessageTableModalClass outGoingMessageTableModalClass);
    @Query("SELECT * FROM outgoingmessagetablemodalclass order by id asc")
    List<OutGoingMessageTableModalClass> getAllSentMessages();



}
