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
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.as.mymessage.R;
import com.as.mymessage.adapters.ContactRecyclerAdapter;
import com.as.mymessage.adapters.RecyclerClickInterface;
import com.as.mymessage.modals.ContactRecyclerModal;

import java.util.ArrayList;
import java.util.List;

public class ComposeSmsActivity extends AppCompatActivity implements RecyclerClickInterface {

    RecyclerView contactRecyclerView;
    List<ContactRecyclerModal> contactList,filteredList;
    ContactRecyclerAdapter adapter;

    SearchView searchView;
    String contactClicked;
    String mobNumberOfClickedContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_sms);

        searchView = findViewById(R.id.search_view);

        contactRecyclerView = findViewById(R.id.contact_recycler_view);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactList = new ArrayList<>();

        adapter = new ContactRecyclerAdapter(this,contactList,this);
        contactRecyclerView.setAdapter(adapter);

        //Get all the contact details

        getContacts();

        //Working with SearchView

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });


    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterList(String newText) {

        filteredList = new ArrayList<>();
        for(ContactRecyclerModal contact: contactList){
            if(contact.getContactName().toLowerCase().contains(newText.toLowerCase())){
               filteredList.add(contact);

            }
            if(contact.getMobNumber().contains(newText)){
                filteredList.add(contact);
            }
        }

        adapter.setFilteredList(filteredList);
        adapter.notifyDataSetChanged();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void getContacts(){
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null,null,null);
        String tempNo = "";
        while (phones.moveToNext()){
            @SuppressLint("Range")
            String name =phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range")
            String number =phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            //Replace all spaces in number with empty string
            number = number.replaceAll("\\s","");

            @SuppressLint("Range")
            String photoUri = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            //To ensure no duplicate Contacts are displayed
            if(!number.equals(tempNo)) {
                ContactRecyclerModal contactRecyclerModal = new ContactRecyclerModal(photoUri, name, number);
                contactList.add(contactRecyclerModal);
                adapter.notifyDataSetChanged();
                tempNo = number;
            }
        }
    }

    @Override
    public void onItemClick(int position) {


        if(filteredList==null){
            //Taking data from contactList if the filtered is null
            contactClicked = contactList.get(position).getContactName();
            mobNumberOfClickedContact = contactList.get(position).getMobNumber();
        }
        else{
            contactClicked = filteredList.get(position).getContactName();
            mobNumberOfClickedContact = filteredList.get(position).getMobNumber();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Open Chat with "+contactClicked+"?")
                .setCancelable(true)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(ComposeSmsActivity.this,ChatActivity.class);
                        intent.putExtra("msgReceiverName",contactClicked);
                        intent.putExtra("msgReceiverNumber",mobNumberOfClickedContact);
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
        if(isNightMode(this)) {
            alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
            alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
        }

    }
}