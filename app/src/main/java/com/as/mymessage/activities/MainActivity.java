package com.as.mymessage.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.role.RoleManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.as.mymessage.DatabasePackage.DatabaseHelper;
import com.as.mymessage.DatabasePackage.MessageTableModalClass;
import com.as.mymessage.R;
import com.as.mymessage.adapters.ConversationRecyclerViewAdapter;
import com.as.mymessage.adapters.RecyclerClickInterface;
import com.as.mymessage.modals.RecyclerModalClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
    RelativeLayout parentLayout;
    FloatingActionButton floatingActionButton;

    Toolbar toolbar;
    Menu optionsMenu;

    private List<MessageTableModalClass> messageListFromDatabase;


    private final BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onReceive(Context context, Intent intent) {
            //Receiving Intent from broadcast and adding data to recyclerView

            Bundle args = intent.getBundleExtra("sms");
            RecyclerModalClass incomingMessage = (RecyclerModalClass) args.getSerializable("object");
            boolean found = false;
            RecyclerModalClass foundItem = null;
            for (RecyclerModalClass item : recyclerModalClassList) {
                if (item.getMobNumber().equalsIgnoreCase(incomingMessage.getMobNumber())) {
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
                    incomingMessage.getMobNumber(), incomingMessage.getContactName(), incomingMessage.getMessage(), incomingMessage.getDate(), incomingMessage.getTime(), incomingMessage.getTimeStamp()));

            if (found) {
                //move item from existing position to last index
                recyclerModalClassList.remove(foundItem);
                recyclerModalClassList.add(foundItem);
            } else {
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
        parentLayout = findViewById(R.id.parent_layout);
        toolbar = findViewById(R.id.main_layout_toolbar);
        floatingActionButton = findViewById(R.id.floating_button);
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        conversationRecyclerViewAdapter = new ConversationRecyclerViewAdapter(recyclerModalClassList, getApplicationContext(), this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");
        //Registering Context Menu on RecyclerView
        registerForContextMenu(recyclerView);

        if (isDefaultSmsApp()) {
            recyclerView.setVisibility(View.VISIBLE);
            floatingActionButton.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            messageListFromDatabase = new ArrayList<>();

        }
        else {
            recyclerView.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            setDefault.setOnClickListener(v -> {
                // Prompting user to select app as default
                RoleManager roleManager = getApplicationContext().getSystemService(RoleManager.class);
                Intent roleRequestIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS);
                startActivityForResult(roleRequestIntent, REQ_CODE_DEFAULT_APP);
            });
        }

        //Listener of Floating Action Button

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ComposeSmsActivity.class));
            }
        });

    }

    private void mainAppFunctioning() {
        try {
            //Getting access to Room database
            databaseHelper = DatabaseHelper.getDB(this);

            //Getting all the message along with other details
            messageListFromDatabase = databaseHelper.messageTableModalClassDao().getAllMessages();

            //Initializing the hashmap to store keys as sender and a list of all the messages of that particular sender

            // Adding data to hashMap
            for (MessageTableModalClass messageTableModalClass : messageListFromDatabase) {
                if (!messagesBySender.containsKey(messageTableModalClass.getMobNumber())) {
                    // Add the sender and a corresponding list, if it is not already in the hashmap
                    messagesBySender.put(messageTableModalClass.getMobNumber(), new ArrayList<>());
                }
                // Add the message and details(messageTableModalClass) in the hashmap to the corresponding key values
                List<MessageTableModalClass> messages =  messagesBySender.get(messageTableModalClass.getMobNumber());
                assert messages != null;
                if(!messages.contains(messageTableModalClass))
                    Objects.requireNonNull(messages).add(messageTableModalClass);
            }


            //Adding Data to recyclerview from the hashmap

            for (Map.Entry<String, List<MessageTableModalClass>> entry : messagesBySender.entrySet()) {
                MessageTableModalClass latestMessage = entry.getValue().get(entry.getValue().size() - 1);
                RecyclerModalClass obj = new RecyclerModalClass(latestMessage.getId(),latestMessage.getImage(), latestMessage.getMobNumber(), latestMessage.getContactName(),
                        latestMessage.getMessage(), latestMessage.getDate(), latestMessage.getTime(), latestMessage.getTimeStamp());
                if(!recyclerModalClassList.contains(obj))
                    recyclerModalClassList.add(obj);
            }
            // Sorting the list obtained from hashmap that most recent message appears on top
            recyclerModalClassList.sort(Comparator.comparingLong(RecyclerModalClass::getTimeStamp));
            recyclerView.setAdapter(conversationRecyclerViewAdapter);
            conversationRecyclerViewAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.getStackTrace();
        }
        if (conversationRecyclerViewAdapter.getItemCount() == 0) {
            Snackbar snackbar = Snackbar.make(parentLayout, R.string.snackbar_msg, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();

                }
            }).show();
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onResume() {
        registerReceiver(intentReceiver, intentFilter);
        mainAppFunctioning();
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
        if (requestCode == REQ_CODE_DEFAULT_APP) {

            //True if user clicked OK
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "App set as default", Toast.LENGTH_SHORT).show();
                linearLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                floatingActionButton.setVisibility(View.VISIBLE);
                mainAppFunctioning();

                if (conversationRecyclerViewAdapter.getItemCount() == 0) {
                    Snackbar snackbar = Snackbar.make(parentLayout, R.string.snackbar_msg, Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();

                        }
                    }).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "App need to be set as default to use it!", Toast.LENGTH_SHORT).show();

            }
        }
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {


        // On long clicking the recyclerView Item

        if (item.getItemId() == 121) {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("If you delete this, it is permanent! Continue to delete?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String mobNumberToBeDeleted = recyclerModalClassList.get(conversationRecyclerViewAdapter.getPosition()).getMobNumber();
                            // deleting all the messages of above number from the database
                            databaseHelper.messageTableModalClassDao().deleteMessage(mobNumberToBeDeleted);
                            databaseHelper.outgoingMessageTableDao().deleteMessages(mobNumberToBeDeleted);
                            //deleting from the list and notifying to the adapter
                            recyclerModalClassList.remove(conversationRecyclerViewAdapter.getPosition());
                            conversationRecyclerViewAdapter.notifyItemRemoved(conversationRecyclerViewAdapter.getPosition());
                            conversationRecyclerViewAdapter.notifyItemRangeChanged(conversationRecyclerViewAdapter.getPosition(),
                                    recyclerModalClassList.size() - conversationRecyclerViewAdapter.getPosition());
                            Toast.makeText(MainActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();

                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.setTitle("Confirm");
            alertDialog.show();
            // Set alertBoc textColor white if app is in night mode
            if(isNightMode(this)) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
            }

        }

        return super.onContextItemSelected(item);

    }

    @Override
    public void onItemClick(int position) {

        String senderClicked = recyclerModalClassList.get(position).getMobNumber();
        List<MessageTableModalClass> messagesByTheSenderClicked = new ArrayList<>();
        messagesByTheSenderClicked = messagesBySender.get(senderClicked);
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        intent.putExtra("list", (Serializable) messagesByTheSenderClicked);
        intent.putExtra("senderMobNumber", senderClicked);
        startActivity(intent);
    }

    //Checking night mode

    public static boolean isNightMode(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        optionsMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

}