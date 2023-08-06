package com.as.mymessage.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.as.mymessage.DatabasePackage.DatabaseHelper;
import com.as.mymessage.DatabasePackage.OutGoingMessageTableModalClass;
import com.as.mymessage.R;
import com.as.mymessage.adapters.ContactRecyclerAdapter;
import com.as.mymessage.adapters.RecyclerClickInterface;
import com.as.mymessage.modals.ContactRecyclerModal;
import com.as.mymessage.util.TimeStampUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ComposeSmsActivity extends AppCompatActivity implements RecyclerClickInterface {

    private List<ContactRecyclerModal> contactList, filteredList;
    private ContactRecyclerAdapter adapter;

    private ImageView checkButton, sendButton;
    private String contactClicked;
    private String mobNumberOfClickedContact;
    EditText toEditText,messageEditText;
    RecyclerView contactRecyclerView;
    TextView toolbarTextView;

    boolean isContact= false;

    private String receiverMobNumber, receiverContactName;
    private String message;

    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_sms);

        toEditText = findViewById(R.id.to_edit_text);
        messageEditText = findViewById(R.id.message_edit_text);
        toolbarTextView = findViewById(R.id.toolbar_textView);
        checkButton = findViewById(R.id.check_button);
        sendButton = findViewById(R.id.send_button);
        contactRecyclerView = findViewById(R.id.contact_recycler_view);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactList = new ArrayList<>();
        filteredList = new ArrayList<>();
        contactList = getContacts();
        adapter = new ContactRecyclerAdapter(this, contactList, this);
        contactRecyclerView.setAdapter(adapter);

        databaseHelper = DatabaseHelper.getDB(this);

        //Adding  textWatcher to edittext
        toEditText.addTextChangedListener(new PhoneNumberTextWatcher());

        //Making edittext to automatically get focused when activity opens
        toEditText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message = messageEditText.getText().toString();
                if(!message.isEmpty()){
                    if(isContact) {
                        sendSms(mobNumberOfClickedContact, message);
                        receiverMobNumber = mobNumberOfClickedContact;
                        receiverMobNumber = receiverMobNumber.replaceAll(" ","");
                        receiverContactName = contactClicked;
                    }
                    else{
                        receiverMobNumber = toEditText.getText().toString();
                        receiverMobNumber = receiverMobNumber.replaceAll(" ","");
                        receiverContactName = null;
                        sendSms(receiverMobNumber,message);
                    }
                }

            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarTextView.setText("Sending Message to "+toEditText.getText());
                //Making the message edittext to get focused when checkButton is clicked
                messageEditText.requestFocus();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                //Make isContact false as it is not a contact
                isContact = false;
            }
        });

    }

    private List<ContactRecyclerModal> getContacts() {
        List<ContactRecyclerModal> contacts = new ArrayList<>();
        Cursor cursor = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                null,
                ContactsContract.Data.MIMETYPE + " = ?",
                new String[]{ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE},
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC"
        );

        if(cursor!=null){
            HashSet<String> contactIds = new HashSet<>();
            while (cursor.moveToNext()){
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID));
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                phoneNumber = phoneNumber.replaceAll(" ","");
                phoneNumber = phoneNumber.trim();

                if (contactIds.contains(phoneNumber)) {
                    continue; // Skip this contact if already added
                }
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

                contactIds.add(phoneNumber);
                contacts.add(new ContactRecyclerModal(id,photoUri,name,phoneNumber));

            }
            cursor.close();
        }
        return contacts;
    }


    @Override
    public void onItemClick(int position) {


        if (filteredList.isEmpty()) {
            //Taking data from contactList if the filtered is null
            contactClicked = contactList.get(position).getContactName();
            mobNumberOfClickedContact = contactList.get(position).getMobNumber();
        } else {
            contactClicked = filteredList.get(position).getContactName();
            mobNumberOfClickedContact = filteredList.get(position).getMobNumber();
        }

        toolbarTextView.setText("Sending Message to "+contactClicked);
        toEditText.setText(contactClicked);
        //Making the message edittext to get focused when a contact is clicked
        messageEditText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        isContact = true;
    }

    private class PhoneNumberTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void afterTextChanged(Editable s) {

            String searchText = s.toString().trim();
            if(searchText.matches(getString(R.string.phoneRegularExp))){
                checkButton.setVisibility(View.VISIBLE);
                checkButton.setEnabled(true);
            }
            else {
                checkButton.setEnabled(false);
                checkButton.setVisibility(View.GONE);
            }
            filteredList.clear();
            for(ContactRecyclerModal contact : contactList){
                if(contact.getContactName().toLowerCase().contains(searchText.toLowerCase()) ||
                        contact.getMobNumber().contains(searchText)){
                    filteredList.add(contact);
                    adapter.setFilteredList(filteredList);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }


    private void sendSms(String phoneNumber,String message){
        String sent = "SMS_SENT";
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,new Intent(sent),PendingIntent.FLAG_IMMUTABLE);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber,null,message,pendingIntent,null);
    }

    private final BroadcastReceiver smsStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    // SMS sent successfully
                    Toast.makeText(context, "SMS sent!", Toast.LENGTH_SHORT).show();

                    OutGoingMessageTableModalClass sentMessage = new OutGoingMessageTableModalClass(
                            receiverMobNumber,receiverContactName,message,TimeStampUtil.getDate(),TimeStampUtil.getTime(),TimeStampUtil.getTheTimeStamp());
                    databaseHelper.outgoingMessageTableDao().addSentMessage(sentMessage);

                    Intent intentToChatActivity = new Intent(ComposeSmsActivity.this,ChatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("sentMessageObject", sentMessage);
                    intentToChatActivity.putExtra("sentMessageBundle",bundle);


                    //THIS LINE IS FOR MainActivity to get result of the message sent inorder to update the recyclerView
                    ComposeSmsActivity.this.setResult(RESULT_OK,intentToChatActivity);

                    startActivity(intentToChatActivity);
                    finish();
                    break;

                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    // Generic failure
                    Toast.makeText(context, "SMS sending failed.", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    // No service (airplane mode, no signal, etc.)
                    Toast.makeText(context, "No SMS service.", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    // Null PDU
                    Toast.makeText(context, "Null PDU.", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    // Radio off (SIM card removed)
                    Toast.makeText(context, "SMS Not Sent", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onResume() {
        registerReceiver(smsStatusReceiver,new IntentFilter("SMS_SENT"));
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(smsStatusReceiver);
        super.onPause();
    }
}