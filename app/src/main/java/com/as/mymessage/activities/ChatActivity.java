package com.as.mymessage.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.as.mymessage.DatabasePackage.DatabaseHelper;
import com.as.mymessage.DatabasePackage.MessageTableModalClass;
import com.as.mymessage.DatabasePackage.OutGoingMessageTableModalClass;
import com.as.mymessage.R;
import com.as.mymessage.adapters.ChatRecyclerViewAdapter;
import com.as.mymessage.modals.RecyclerModalClass;
import com.as.mymessage.util.ChatMessagePOJO;
import com.as.mymessage.util.ContactCheckerUtil;
import com.as.mymessage.util.TimeStampUtil;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    RecyclerView chatRecyclerView;
    IntentFilter intentFilter;
    ImageView sendButton;
    EditText messageEditText;
    TextView contactNameOnToolbar;
    ImageView backButtonImageView;
    String receiverMobNumber;
    String receiverContactName;
    List<ChatMessagePOJO> allMessages;
    List<MessageTableModalClass> receivedMessages;
    List<OutGoingMessageTableModalClass> sentMessages;
    ChatRecyclerViewAdapter chatAdapter;
    LinearLayout sendLinearLayout;



    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        sendButton = findViewById(R.id.send_button);
        sendLinearLayout = findViewById(R.id.send_linear_layout);
        messageEditText = findViewById(R.id.message_edit_text);
        contactNameOnToolbar = findViewById(R.id.toolbar_contact_tv);
        backButtonImageView = findViewById(R.id.back_button_IV);

        DatabaseHelper databaseHelper = DatabaseHelper.getDB(this);
        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        receivedMessages = new ArrayList<>();

        //Getting intent
        Intent i = getIntent();

        // Getting the sender that is clicked to send him the message
        //senderMobNumber is passed from MainActivity
        receiverMobNumber = i.getStringExtra("senderMobNumber");

        //if it is null it means the intent is from ComposeActivity
        if (receiverMobNumber == null) {
            //intent is from ComposeActivity
            receiverMobNumber =i.getStringExtra("msgReceiverNumber");
            receiverContactName=i.getStringExtra("msgReceiverName");
            receivedMessages = databaseHelper.messageTableModalClassDao().getAllMessagesOfASender(receiverMobNumber);
        }
        else {
            //Intent is from MainActivity
            if (ContactCheckerUtil.isNumberInContacts(this, receiverMobNumber)) {
                receiverContactName = ContactCheckerUtil.getContactNameFromNumber(this, receiverMobNumber);
            }
            receivedMessages = (List<MessageTableModalClass>) i.getSerializableExtra("list");
        }

            //setting contact name on toolbar if present
            //else setting the receiver's phone number

            if (receiverContactName != null) {
                contactNameOnToolbar.setText(receiverContactName);
            }
            else {
                contactNameOnToolbar.setText(receiverMobNumber);
                if (!receiverMobNumber.matches("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$\n")) {
                    sendLinearLayout.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(sendLinearLayout, "The sender does not support replies!", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    }).show();
                }
            }

            sentMessages = databaseHelper.outgoingMessageTableDao().getAllSentMessages(receiverMobNumber);
            allMessages = new ArrayList<>();

            //Adding all received and sent messages in allMessages list
            if(receivedMessages!=null) {

                for (MessageTableModalClass message : receivedMessages) {
                    allMessages.add(new ChatMessagePOJO(false, message.getMessage(), message.getTimeStamp()));
                }
            }
            if(sentMessages!=null) {

                for (OutGoingMessageTableModalClass message : sentMessages) {
                    allMessages.add(new ChatMessagePOJO(true, message.getMessage(), message.getTimeStamp()));
                }
            }

            //Sorting all the messages so that the recyclerview is displayed exactly like
            //a timed chat
            allMessages.sort(Comparator.comparingLong(ChatMessagePOJO::getTimeStamp));

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            chatRecyclerView.setLayoutManager(linearLayoutManager);
            chatAdapter = new ChatRecyclerViewAdapter(getApplicationContext(), allMessages);
            chatRecyclerView.setAdapter(chatAdapter);

            // Scrolling RecyclerView to the last received message
//            if(receivedMessages!=null) {
//                chatRecyclerView.post(() -> chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1));
//            }

            chatAdapter.notifyDataSetChanged();


            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String message = messageEditText.getText().toString();
                    messageEditText.getText().clear();
                    if (!message.isEmpty()) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(receiverMobNumber, null, message, null, null);
                        Toast.makeText(ChatActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();

                        OutGoingMessageTableModalClass sentMessage = new OutGoingMessageTableModalClass(
                                R.drawable.baseline_message_24, receiverMobNumber, receiverContactName, message, TimeStampUtil.getDate()
                                , TimeStampUtil.getTime(), TimeStampUtil.getTheTimeStamp());

                        allMessages.add(new ChatMessagePOJO(true, sentMessage.getMessage(), sentMessage.getTimeStamp()));
                        // Scrolling RecyclerView to the last sent or received message
                        chatRecyclerView.post(() -> chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1));
                        chatAdapter.notifyDataSetChanged();
                        databaseHelper.outgoingMessageTableDao().addSentMessage(sentMessage);
                    } else {
                        messageEditText.setError("Type a message!");
                    }

                }
            });

            //Setting up backButtonImageView listener
            backButtonImageView.setOnClickListener(v -> finish());
    }

    private final BroadcastReceiver intentReceiver = new BroadcastReceiver() {

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle args = intent.getBundleExtra("sms");
            RecyclerModalClass incomingMessage = (RecyclerModalClass) args.getSerializable("object");
            allMessages.add(new ChatMessagePOJO(false, incomingMessage.getMessage(), incomingMessage.getTimeStamp()));
            chatRecyclerView.post(() -> chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1));
            chatAdapter.notifyDataSetChanged();
        }
    };

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
    }
}