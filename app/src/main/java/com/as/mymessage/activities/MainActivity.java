package com.as.mymessage.activities;

import static com.as.mymessage.util.ContactCheckerUtil.getContactNameFromNumber;
import static com.as.mymessage.util.ContactCheckerUtil.isNumberInContacts;

import android.annotation.SuppressLint;
import android.app.role.RoleManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.as.mymessage.DatabasePackage.DatabaseHelper;
import com.as.mymessage.DatabasePackage.MessageTableModalClass;
import com.as.mymessage.MessageReceiverClasses.SmsReceiver;
import com.as.mymessage.R;
import com.as.mymessage.adapters.ConversationRecyclerViewAdapter;
import com.as.mymessage.adapters.RecyclerClickInterface;
import com.as.mymessage.modals.RecyclerModalClass;
import com.as.mymessage.util.TimeStampUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerClickInterface {


    private static final int REQ_CODE_DEFAULT_APP = 1000;
    List<RecyclerModalClass> recyclerModalClassList = new ArrayList<>();

    Map<String, List<MessageTableModalClass>> messagesBySender = new LinkedHashMap<>();
    IntentFilter intentFilter;
    ConversationRecyclerViewAdapter conversationRecyclerViewAdapter;
    DatabaseHelper databaseHelper;
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    TextView textView;
    Button setDefault;




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
                if(item.getMobNumber().equalsIgnoreCase(incomingMessage.getMobNumber())) {
                    item.setMessage(incomingMessage.getMessage());
                    item.setDate(incomingMessage.getDate());
                    item.setTime(incomingMessage.getTime());
                    foundItem = item;
                    found = true;
                    break;
                }
            }
            if (!messagesBySender.containsKey(incomingMessage.getMobNumber())) {
                // Add the sender and a corresponding list, if it is not already in the hashmap
                messagesBySender.put(incomingMessage.getMobNumber(), new ArrayList<>());
            }
            // Add the message and details(messageTableModalClass) in the hashmap to the corresponding key values
            Objects.requireNonNull(messagesBySender.get(incomingMessage.getMobNumber())).add(new MessageTableModalClass(incomingMessage.getImage(),
                    incomingMessage.getMobNumber(),incomingMessage.getContactName(),incomingMessage.getMessage(),incomingMessage.getDate(),incomingMessage.getTime(), incomingMessage.getTimeStammp()));

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


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // This linear layout holds the animation and set Default SMS app button and is displayed only
        // when the app is not made as default SMS app
         linearLayout = findViewById(R.id.linearLayout);
         recyclerView = findViewById(R.id.recyclerView);
         textView = findViewById(R.id.textDesc);
         setDefault = findViewById(R.id.setDefaultBtn);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        conversationRecyclerViewAdapter = new ConversationRecyclerViewAdapter(recyclerModalClassList, getApplicationContext(), this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");
        //Registering Context Menu on RecyclerView
        registerForContextMenu(recyclerView);

        if(isDefaultSmsApp()){
            recyclerView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }

        else{
            recyclerView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            setDefault.setOnClickListener(v -> {
                // Prompting user to select app as default
                RoleManager roleManager = getApplicationContext().getSystemService(RoleManager.class);
                Intent roleRequestIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS);
                startActivityForResult(roleRequestIntent, REQ_CODE_DEFAULT_APP);
            });
        }


       // recyclerModalClassList = new ArrayList<>();



        //Adding Data to hashmap from the database
        try {
            //Getting access to Room database
            databaseHelper = DatabaseHelper.getDB(this);

            //Getting all the message along with other details
            List<MessageTableModalClass> messageListFromDatabase = databaseHelper.messageTableModalClassDao().getAllMessages();

            //Initializing the hashmap to store keys as sender and a list of all the messages of that particular sender
//            messagesBySender = new LinkedHashMap<>();

            // Adding data to hashMap
            for (MessageTableModalClass messageTableModalClass : messageListFromDatabase) {
                if (!messagesBySender.containsKey(messageTableModalClass.getMobNumber())) {
                    // Add the sender and a corresponding list, if it is not already in the hashmap
                    messagesBySender.put(messageTableModalClass.getMobNumber(), new ArrayList<>());
                }
                // Add the message and details(messageTableModalClass) in the hashmap to the corresponding key values
                Objects.requireNonNull(messagesBySender.get(messageTableModalClass.getMobNumber())).add(messageTableModalClass);
            }


            //Adding Data to recyclerview from the hashmap

            for (Map.Entry<String, List<MessageTableModalClass>> entry : messagesBySender.entrySet()) {
                MessageTableModalClass latestMessage = entry.getValue().get(entry.getValue().size() - 1);
                recyclerModalClassList.add(new RecyclerModalClass(latestMessage.getImage(), latestMessage.getMobNumber(),latestMessage.getContactName(),
                        latestMessage.getMessage(), latestMessage.getDate(), latestMessage.getTime(), latestMessage.getTimeStamp()));
            }
            // Sorting the list obtained from hashmap that most recent message appears on top
            recyclerModalClassList.sort(Comparator.comparingLong(RecyclerModalClass::getTimeStammp));
            recyclerView.setAdapter(conversationRecyclerViewAdapter);
            conversationRecyclerViewAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.getStackTrace();
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

    public boolean isDefaultSmsApp() {
        return this.getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Checking if it is the Prompt intent request code
        if(requestCode == REQ_CODE_DEFAULT_APP){

            //True if user clicked OK
            if(resultCode == RESULT_OK){
                Toast.makeText(MainActivity.this,"App set as default",Toast.LENGTH_SHORT).show();
                linearLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(MainActivity.this,"App need to be set as default to use it!",Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == 121) {
            Toast.makeText(MainActivity.this,"Deleted!",Toast.LENGTH_SHORT).show();
        }

        return super.onContextItemSelected(item);

    }

    @Override
    public void onItemClick(int position) {

        String senderClicked = recyclerModalClassList.get(position).getMobNumber();
        List<MessageTableModalClass> messagesByTheSenderClicked = new ArrayList<>();
        messagesByTheSenderClicked = messagesBySender.get(senderClicked);
        Intent intent = new Intent(MainActivity.this,ChatActivity.class);
        intent.putExtra("list",(Serializable) messagesByTheSenderClicked);
        startActivity(intent);

    }
}