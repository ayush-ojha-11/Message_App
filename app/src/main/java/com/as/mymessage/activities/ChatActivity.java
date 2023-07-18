package com.as.mymessage.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.as.mymessage.DatabasePackage.MessageTableModalClass;
import com.as.mymessage.R;
import com.as.mymessage.adapters.ChatRecyclerViewAdapter;
import com.as.mymessage.modals.ChatRecyclerModal;
import com.as.mymessage.modals.RecyclerModalClass;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    RecyclerView chatRecyclerView;
    List<MessageTableModalClass> messages;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

           chatRecyclerView = findViewById(R.id.chat_recycler_view);
           messages = new ArrayList<>();

           Intent i = getIntent();
           messages = (List<MessageTableModalClass>) i.getSerializableExtra("list");
           LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
           chatRecyclerView.setLayoutManager(linearLayoutManager);
           ChatRecyclerViewAdapter chatAdapter = new ChatRecyclerViewAdapter(getApplicationContext(),messages);
           chatRecyclerView.setAdapter(chatAdapter);
           chatAdapter.notifyDataSetChanged();

    }

    private final BroadcastReceiver intentReceiver  = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle args = intent.getBundleExtra("sms");
            RecyclerModalClass incomingMessage = (RecyclerModalClass) args.getSerializable("object");
        }
    };
}