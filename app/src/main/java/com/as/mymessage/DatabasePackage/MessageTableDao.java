package com.as.mymessage.DatabasePackage;

import androidx.room.Dao;
import androidx.room.Delete;
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

    @Query("DELETE FROM messagetablemodalclass WHERE mobNumber = :mobNumber")
    void deleteMessage(String mobNumber);

}
