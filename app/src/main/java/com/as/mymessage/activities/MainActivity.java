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
import android.widget.Toast;

import com.as.mymessage.DatabasePackage.DatabaseHelper;
import com.as.mymessage.DatabasePackage.MessageTableModalClass;
import com.as.mymessage.adapters.ConversationRecyclerViewAdapter;
import com.as.mymessage.R;
import com.as.mymessage.modals.RecyclerModalClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    List<RecyclerModalClass> recyclerModalClassList;
    IntentFilter intentFilter;
    ConversationRecyclerViewAdapter conversationRecyclerViewAdapter;
    DatabaseHelper databaseHelper;

    ActivityResultLauncher<String[]> mPermissionResultLauncher;
    private boolean isReadContactPermissionGranted = false;
    private boolean isNotificationPermissionGranted = false;
    private boolean isSmsPermissionGranted = false;


    private final BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onReceive(Context context, Intent intent) {
            int id = 0;
            //Receiving Intent from broadcast
            Bundle args = intent.getBundleExtra("sms");
            RecyclerModalClass recyclerModalClass = (RecyclerModalClass) args.getSerializable("object");
            recyclerModalClassList.add(recyclerModalClass);
            conversationRecyclerViewAdapter.notifyDataSetChanged();
        }
    };


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();

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

        //Adding Data to recyclerview from the room database

        List<MessageTableModalClass> messageListFromDatabase = databaseHelper.messageTableModalClassDao().getAllMessages();
        for (int i = 0; i < messageListFromDatabase.size(); i++) {
            recyclerModalClassList.add(new RecyclerModalClass(messageListFromDatabase.get(i).getId(), messageListFromDatabase.get(i).getImage(), messageListFromDatabase.get(i).getSender(),
                    messageListFromDatabase.get(i).getMessage(), messageListFromDatabase.get(i).getTime()));
            conversationRecyclerViewAdapter.notifyDataSetChanged();
        }


        mPermissionResultLauncher =registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions()
                , new ActivityResultCallback<Map<String, Boolean>>() {
                    @Override
                    public void onActivityResult(Map<String, Boolean> result) {
                        if(result.get(Manifest.permission.READ_CONTACTS) != null){
                            isReadContactPermissionGranted = result.get(Manifest.permission.READ_CONTACTS);
                        }
                        if(result.get(Manifest.permission.READ_SMS) != null){
                            isSmsPermissionGranted = result.get(Manifest.permission.READ_SMS);
                        }
                        if(result.get(POST_NOTIFICATIONS) != null){
                            isNotificationPermissionGranted = result.get(POST_NOTIFICATIONS);
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void requestPermission(){
        isReadContactPermissionGranted = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED;

        isNotificationPermissionGranted= ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED;

        isSmsPermissionGranted = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED;

        List<String> permissionRequest = new ArrayList<>();

        if(!isSmsPermissionGranted){
            permissionRequest.add(Manifest.permission.READ_SMS);
        }

        if(!isReadContactPermissionGranted){
            permissionRequest.add(Manifest.permission.READ_CONTACTS);
        }

        if(!isNotificationPermissionGranted){
            permissionRequest.add(POST_NOTIFICATIONS);
        }

        if(!permissionRequest.isEmpty()){
            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));
        }

    }
}