package com.as.mymessage.DatabasePackage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OutgoingMessageTableDao {

    @Query("SELECT * FROM outgoingmessagetablemodalclass where mobNumber=:mobNumber order by id asc")
    List<OutGoingMessageTableModalClass> getAllSentMessages(String mobNumber);
    @Query("SELECT * FROM outgoingmessagetablemodalclass order by id asc")
    List<OutGoingMessageTableModalClass> getAllSentMessages();

    @Insert
    void addSentMessage(OutGoingMessageTableModalClass outGoingMessageTableModalClass);

    @Query("DELETE FROM outgoingmessagetablemodalclass WHERE mobNumber =:mobNumber")
    void deleteMessages(String mobNumber);
}
