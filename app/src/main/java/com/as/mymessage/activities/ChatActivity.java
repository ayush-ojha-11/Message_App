package com.as.mymessage.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.as.mymessage.R;
import com.as.mymessage.adapters.ChatRecyclerViewAdapter;
import com.as.mymessage.modals.ChatRecyclerModal;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    RecyclerView chatRecyclerView;
    List<ChatRecyclerModal> messages;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

           chatRecyclerView = findViewById(R.id.chat_recycler_view);
           messages = new ArrayList<>();
           messages.add(new ChatRecyclerModal("AYush Here"));
           LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
           chatRecyclerView.setLayoutManager(linearLayoutManager);
           ChatRecyclerViewAdapter chatAdapter = new ChatRecyclerViewAdapter(getApplicationContext(),messages);
           chatRecyclerView.setAdapter(chatAdapter);
           chatAdapter.notifyDataSetChanged();

    }
}