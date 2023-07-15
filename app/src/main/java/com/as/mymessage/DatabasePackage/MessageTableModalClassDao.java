package com.as.mymessage.DatabasePackage;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
