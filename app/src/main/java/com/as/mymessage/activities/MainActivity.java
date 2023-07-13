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
import java.util.HashMap;
import java.util.HashSet;
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
            RecyclerModalClass recyclerModalClass = (RecyclerModalClass) args.getSerializable("object");
            for(RecyclerModalClass item : recyclerModalClassList){
                if(item.getName().equalsIgnoreCase(recyclerModalClass.getName())) {
                    item.setMessage(recyclerModalClass.getMessage());
                    conversationRecyclerViewAdapter.notifyItemChanged(recyclerModalClassList.indexOf(item));

                }
                else{
                    recyclerModalClassList.add(recyclerModalClass);
                    conversationRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
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
        conversationRecyclerViewAdapter = new ConversationRecyclerViewAdapter(recyclerModalClassList, getApplicationContext());
        recyclerView.setAdapter(conversationRecyclerViewAdapter);
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        //Adding Data to hashmap from the database
        List<MessageTableModalClass> messageListFromDatabase = databaseHelper.messageTableModalClassDao().getAllMessages();
        Map<String, List<MessageTableModalClass>> messagesBySender = new HashMap<>();
        for(MessageTableModalClass messageTableModalClass : messageListFromDatabase){
            if(!messagesBySender.containsKey(messageTableModalClass.getSender())){
                messagesBySender.put(messageTableModalClass.getSender(), new ArrayList<MessageTableModalClass>());
            }
            Objects.requireNonNull(messagesBySender.get(messageTableModalClass.getSender())).add(messageTableModalClass);
        }


        //Adding Data to recyclerview from the hashmap
        for(Map.Entry<String, List<MessageTableModalClass>> entry : messagesBySender.entrySet()){
            MessageTableModalClass latestMessage = entry.getValue().get(entry.getValue().size()-1);
            recyclerModalClassList.add(new RecyclerModalClass(latestMessage.getImage(), latestMessage.getSender(),
                    latestMessage.getMessage(),latestMessage.getDate(),latestMessage.getTime()));
                conversationRecyclerViewAdapter.notifyDataSetChanged();
        }

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