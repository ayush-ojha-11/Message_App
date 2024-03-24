package com.as.mymessage.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.as.mymessage.R;
import com.as.mymessage.modals.RecyclerModalClass;
import com.as.mymessage.util.ContactCheckerUtil;
import com.as.mymessage.util.UtilityFunctions;

import java.util.List;

public class ConversationRecyclerViewAdapter extends RecyclerView.Adapter<ConversationViewHolder> {

    List<RecyclerModalClass> recyclerList;
    Context context;
    private int position;
    private final RecyclerClickInterface recyclerClickInterface;


    public ConversationRecyclerViewAdapter(List<RecyclerModalClass> recyclerList, Context context, RecyclerClickInterface recyclerClickInterface) {
        this.recyclerList = recyclerList;
        this.context = context;
        this.recyclerClickInterface = recyclerClickInterface;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversationViewHolder(LayoutInflater.from(context).inflate(R.layout.message_recycler_item,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        String text;

        if (recyclerList.get(position).getContactName() != null) {
            String number = recyclerList.get(position).getMobNumber();
            Bitmap contactPhoto = ContactCheckerUtil.getContactPhotoFromPhoneNumber(context, number);
            if (contactPhoto != null)
                holder.imageView.setImageBitmap(contactPhoto);
            else {
                // Generate color
                text = recyclerList.get(position).getContactName();
                holder.imageView.setImageBitmap(UtilityFunctions.generateCircleBitmap(text.charAt(0),Color.rgb(80,200,120)));
            }
        }
        else{
            text = recyclerList.get(position).getMobNumber();
            holder.imageView.setImageBitmap(UtilityFunctions.generateCircleBitmap(text.charAt(0), Color.rgb(253,70,89)));

        }


        String sender;
        sender = recyclerList.get(position).getContactName() != null ? recyclerList.get(position).getContactName() : recyclerList.get(position).getMobNumber();
        holder.nameView.setText(sender);


        holder.bodyView.setText(recyclerList.get(position).getMessage());
        holder.timeView.setText(" "+recyclerList.get(position).getDate()+"\n"+recyclerList.get(position).getTime());

        //OnLongClickListener

        holder.itemView.setOnLongClickListener(v -> {
            setPosition(holder.getPosition());
            return false;
        });

        holder.itemView.setOnClickListener(v -> {

            if(recyclerClickInterface != null){
                int pos = holder.getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
                    recyclerClickInterface.onItemClick(pos);
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return recyclerList.size();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
