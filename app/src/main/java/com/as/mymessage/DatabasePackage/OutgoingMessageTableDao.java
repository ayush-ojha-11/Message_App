package com.as.mymessage.DatabasePackage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OutgoingMessageTableDao {

    @Query("SELECT * FROM outgoingmessagetablemodalclass where mobNumber=:mobNumber order by id asc")
    List<OutGoingMessageTableModalClass> getAllSentMessages(String mobNumber);

    @Insert
    void addSentMessage(OutGoingMessageTableModalClass outGoingMessageTableModalClass);
}
