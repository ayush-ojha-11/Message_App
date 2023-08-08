package com.as.mymessage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.as.mymessage.R;
import com.as.mymessage.activities.MainActivity;
import com.as.mymessage.modals.ContactRecyclerModal;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerViewHolder> {

    Context context;
    List<ContactRecyclerModal> list;

    private final RecyclerClickInterface recyclerClickInterface;

    //For searching contacts in searchView
    public void setFilteredList(List<ContactRecyclerModal> filteredList){
        this.list = filteredList;
    }

    public ContactRecyclerAdapter(Context context, List<ContactRecyclerModal> list,RecyclerClickInterface recyclerClickInterface) {
        this.context = context;
        this.list = list;
        this.recyclerClickInterface =recyclerClickInterface;
    }


    @NonNull
    @Override
    public ContactRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.contact_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactRecyclerViewHolder holder, int position) {

        ContactRecyclerModal contact= list.get(position);
        holder.nameTextView.setText(contact.getContactName());
        holder.mobNumberTextView.setText(contact.getMobNumber());

        if(contact.getContactPhoto()!=null){

            Picasso.get().load(contact.getContactPhoto()).into(holder.contactPhotoImageView);

        }
        else{
            if(MainActivity.isNightMode(context)) {
                holder.contactPhotoImageView.setImageResource(R.drawable.baseline_person_white);
            }
            else
                holder.contactPhotoImageView.setImageResource(R.drawable.baseline_person_24);
        }

        holder.itemView.setOnClickListener(v -> {
            if(recyclerClickInterface!=null){
                int pos = holder.getAdapterPosition();
                if(pos!=RecyclerView.NO_POSITION){
                    recyclerClickInterface.onItemClick(pos);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
