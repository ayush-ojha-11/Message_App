package com.as.mymessage.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.as.mymessage.DatabasePackage.DatabaseHelper;
import com.as.mymessage.DatabasePackage.MessageTableModalClass;
import com.as.mymessage.DatabasePackage.OutGoingMessageTableModalClass;
import com.as.mymessage.R;
import com.as.mymessage.adapters.ChatRecyclerViewAdapter;
import com.as.mymessage.modals.ChatRecyclerModal;
import com.as.mymessage.modals.RecyclerModalClass;
import com.as.mymessage.util.ContactCheckerUtil;
import com.as.mymessage.util.TimeStampUtil;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    RecyclerView chatRecyclerView;
    List<MessageTableModalClass> messages;

    Button sendButton;
    EditText messageEditText;

    String receiverMobNumber;

    String receiverContactName;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

          sendButton = findViewById(R.id.send_button);
          messageEditText = findViewById(R.id.message_edit_text);
          DatabaseHelper databaseHelper = DatabaseHelper.getDB(this);

           chatRecyclerView = findViewById(R.id.chat_recycler_view);
           messages = new ArrayList<>();

           //Getting intent from MainActivity
           Intent i = getIntent();

           // Getting the sender that is clicked to send him the message
           receiverMobNumber = i.getStringExtra("senderMobNumber");

           if(ContactCheckerUtil.isNumberInContacts(this,receiverMobNumber)){
               receiverContactName = ContactCheckerUtil.getContactNameFromNumber(this,receiverMobNumber);
           }

           messages = (List<MessageTableModalClass>) i.getSerializableExtra("list");
           LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
           chatRecyclerView.setLayoutManager(linearLayoutManager);
           ChatRecyclerViewAdapter chatAdapter = new ChatRecyclerViewAdapter(getApplicationContext(),messages);
           chatRecyclerView.setAdapter(chatAdapter);
           chatAdapter.notifyDataSetChanged();

           sendButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   String message = messageEditText.getText().toString();
                   SmsManager smsManager = SmsManager.getDefault();
                   smsManager.sendTextMessage(receiverMobNumber,null,message,null,null);
                   Toast.makeText(ChatActivity.this,"Message sent!",Toast.LENGTH_SHORT).show();


                   databaseHelper.outgoingMessageTableDao().addSentMessage(new OutGoingMessageTableModalClass(
                           R.drawable.baseline_message_24,receiverMobNumber,receiverContactName,message,TimeStampUtil.getDate()
                           ,TimeStampUtil.getTime(),TimeStampUtil.getTheTimeStamp()));

               }
           });

    }

}