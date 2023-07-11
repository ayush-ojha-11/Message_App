package com.as.mymessage.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.as.mymessage.DatabasePackage.DatabaseHelper;
import com.as.mymessage.DatabasePackage.MessageTableModalClass;
import com.as.mymessage.adapters.ConversationRecyclerViewAdapter;
import com.as.mymessage.R;
import com.as.mymessage.modals.RecyclerModalClass;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{


    List <RecyclerModalClass> recyclerModalClassList;
    IntentFilter intentFilter;
    ConversationRecyclerViewAdapter conversationRecyclerViewAdapter;
    DatabaseHelper databaseHelper;



    private final BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onReceive(Context context, Intent intent) {
            int id=0;
            //Receiving Intent from broadcast
            Bundle args =intent.getBundleExtra("sms");
            RecyclerModalClass recyclerModalClass = (RecyclerModalClass) args.getSerializable("object");
            recyclerModalClassList.add(recyclerModalClass);
            conversationRecyclerViewAdapter.notifyDataSetChanged();
            databaseHelper.messageTableModalClassDao().addMessage(new MessageTableModalClass(R.drawable.ic_launcher_foreground,recyclerModalClass.getName()
                    ,recyclerModalClass.getMessage(),recyclerModalClass.getTime()));
        }
    };



    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting access to Room database
        databaseHelper = DatabaseHelper.getDB(this);



        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerModalClassList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        conversationRecyclerViewAdapter = new ConversationRecyclerViewAdapter(recyclerModalClassList,getApplicationContext());
        recyclerView.setAdapter(conversationRecyclerViewAdapter);
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        //Adding Data to recyclerview from the room database

        List<MessageTableModalClass> messageListFromDatabase= databaseHelper.messageTableModalClassDao().getAllMessages();
        for(int i=0;i<messageListFromDatabase.size();i++){
            recyclerModalClassList.add(new RecyclerModalClass(messageListFromDatabase.get(i).getId(),messageListFromDatabase.get(i).getImage(),messageListFromDatabase.get(i).getSender(),
                    messageListFromDatabase.get(i).getMessage(),messageListFromDatabase.get(i).getTime()));
            //conversationRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onResume() {
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }

}