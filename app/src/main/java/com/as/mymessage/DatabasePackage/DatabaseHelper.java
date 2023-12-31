package com.as.mymessage.DatabasePackage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MessageTableModalClass.class, OutGoingMessageTableModalClass.class},exportSchema = false, version = 31)
public abstract class DatabaseHelper extends RoomDatabase {
    private static final String DB_NAME = "MessageDatabase";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getDB(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context,DatabaseHelper.class,DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract MessageTableDao messageTableModalClassDao();
    public abstract OutgoingMessageTableDao outgoingMessageTableDao();
}
