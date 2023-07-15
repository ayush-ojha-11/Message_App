package com.as.mymessage.activities;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.as.mymessage.DatabasePackage.DatabaseHelper;
import com.as.mymessage.DatabasePackage.MessageTableModalClass;
import com.as.mymessage.adapters.ConversationRecyclerViewAdapter;
import com.as.mymessage.R;
import com.as.mymessage.modals.RecyclerModalClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    List<RecyclerModalClass> recyclerModalClassList;
    IntentFilter intentFilter;
    ConversationRecyclerViewAdapter conversationRecyclerViewAdapter;
    DatabaseHelper databaseHelper;



    private final BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onReceive(Context context, Intent intent) {
            //Receiving Intent from broadcast and adding data to recyclerView

            Bundle args = intent.getBundleExtra("sms");
            RecyclerModalClass incomingMessage = (RecyclerModalClass) args.getSerializable("object");
            boolean found = false;
            RecyclerModalClass foundItem = null;
            for(RecyclerModalClass item : recyclerModalClassList){
                if(item.getName().equalsIgnoreCase(incomingMessage.getName())) {
                    item.setMessage(incomingMessage.getMessage());
                    item.setDate(incomingMessage.getDate());
                    item.setTime(incomingMessage.getTime());
                    foundItem = item;
                    found = true;
                    break;
                }
            }
            if(found){
                //move item from existing position to last index
                recyclerModalClassList.remove(foundItem);
                recyclerModalClassList.add(foundItem);
            }
            else{
                recyclerModalClassList.add(incomingMessage);
            }
            conversationRecyclerViewAdapter.notifyDataSetChanged();
        }
    };


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerModalClassList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        //Adding Data to hashmap from the database

          //Getting access to Room database
          databaseHelper = DatabaseHelper.getDB(this);

          //Getting all the message along with other details
          List<MessageTableModalClass> messageListFromDatabase = databaseHelper.messageTableModalClassDao().getAllMessages();

          //Creating a hashmap to store keys as sender and a list of all the messages of that particular sender
          Map<String, List<MessageTableModalClass>> messagesBySender = new HashMap<>();

          // Adding data to hashMap
          for(MessageTableModalClass messageTableModalClass : messageListFromDatabase){
              if(!messagesBySender.containsKey(messageTableModalClass.getSender())){
                  // Add the sender and a corresponding list, if it is not already in the hashmap
                  messagesBySender.put(messageTableModalClass.getSender(), new ArrayList<>());
              }
              // Add the message and details(messageTableModalClass) in the hashmap to the corresponding key values
              Objects.requireNonNull(messagesBySender.get(messageTableModalClass.getSender())).add(messageTableModalClass);
          }


          //Adding Data to recyclerview from the hashmap

          for(Map.Entry<String, List<MessageTableModalClass>> entry : messagesBySender.entrySet()){
              MessageTableModalClass latestMessage = entry.getValue().get(entry.getValue().size()-1);
              recyclerModalClassList.add(new RecyclerModalClass(latestMessage.getImage(), latestMessage.getSender(),
                      latestMessage.getMessage(),latestMessage.getDate(),latestMessage.getTime()));
          }
        conversationRecyclerViewAdapter = new ConversationRecyclerViewAdapter(recyclerModalClassList, getApplicationContext());
        recyclerView.setAdapter(conversationRecyclerViewAdapter);
          conversationRecyclerViewAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(intentReceiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}