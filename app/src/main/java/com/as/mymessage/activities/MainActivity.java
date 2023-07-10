package com.as.mymessage.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import com.as.mymessage.adapters.ConversationRecyclerViewAdapter;
import com.as.mymessage.R;
import com.as.mymessage.modals.RecyclerModalClass;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{


    List <RecyclerModalClass> recyclerModalClassList;
    IntentFilter intentFilter;
    ConversationRecyclerViewAdapter conversationRecyclerViewAdapter;


    private final BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onReceive(Context context, Intent intent) {
            int id=0;
            //Receiving Intent from broadcast
            Bundle args =intent.getBundleExtra("sms");
            RecyclerModalClass recyclerModalClass = (RecyclerModalClass) args.getSerializable("object");
            recyclerModalClassList.add(recyclerModalClass);
            //notifying adapter about the data change
            conversationRecyclerViewAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(),"A message received!",Toast.LENGTH_SHORT).show();
        }
    };



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
        conversationRecyclerViewAdapter = new ConversationRecyclerViewAdapter(recyclerModalClassList,getApplicationContext());
        recyclerView.setAdapter(conversationRecyclerViewAdapter);

        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");
    }

    @Override
    protected void onResume() {
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }
}