package com.as.mymessage.activities;

import static com.as.mymessage.activities.MainActivity.isNightMode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.as.mymessage.R;
import com.as.mymessage.adapters.ContactRecyclerAdapter;
import com.as.mymessage.adapters.RecyclerClickInterface;
import com.as.mymessage.modals.ContactRecyclerModal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ComposeSmsActivity extends AppCompatActivity implements RecyclerClickInterface {

    private List<ContactRecyclerModal> contactList, filteredList;
    private ContactRecyclerAdapter adapter;

    private ImageView checkButton;
    private String contactClicked;
    private String mobNumberOfClickedContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_sms);

        EditText toEditText = findViewById(R.id.to_edit_text);
        checkButton = findViewById(R.id.check_button);
        RecyclerView contactRecyclerView = findViewById(R.id.contact_recycler_view);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactList = new ArrayList<>();
        filteredList = new ArrayList<>();
        contactList = getContacts();
        adapter = new ContactRecyclerAdapter(this, contactList, this);
        contactRecyclerView.setAdapter(adapter);

        //Adding  textWatcher to edittext
        toEditText.addTextChangedListener(new PhoneNumberTextWatcher());

        //Making edittext to automatically get focused when activity opens
        toEditText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

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

                if (contactIds.contains(id)) {
                    continue; // Skip this contact if already added
                }
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                @SuppressLint("Range") String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

                    contactIds.add(id);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Open Chat with " + contactClicked + "?")
                .setCancelable(true)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(ComposeSmsActivity.this, ChatActivity.class);
                        intent.putExtra("msgReceiverName", contactClicked);
                        intent.putExtra("msgReceiverNumber", mobNumberOfClickedContact);
                        startActivity(intent);
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Confirm");
        alertDialog.show();
        // Set alertBoc textColor white if app is in night mode
        if (isNightMode(this)) {
            alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
            alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
        }

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
}