package com.as.mymessage.DatabasePackage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MessageTableModalClassDao {

    @Query("SELECT * FROM messagetablemodalclass order by id asc")
    List<MessageTableModalClass> getAllMessages();

    @Insert
    void addMessage(MessageTableModalClass messageTableModalClass);

    @Update
    void updateMessage(MessageTableModalClass messageTableModalClass);

    @Delete
    void deleteMessage(MessageTableModalClass messageTableModalClass);
}
