package com.as.mymessage.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.as.mymessage.R;
import com.as.mymessage.activities.MainActivity;
import com.as.mymessage.modals.RecyclerModalClass;
import com.as.mymessage.util.ContactCheckerUtil;

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

       if(recyclerList.get(position).getContactName()!=null){
           String number = recyclerList.get(position).getMobNumber();
           Log.d("number",number);



               Bitmap contactPhoto = ContactCheckerUtil.getContactPhotoFromPhoneNumber(context,number);
               if(contactPhoto!=null)
               holder.imageView.setImageBitmap(contactPhoto);
               else {
                   if(MainActivity.isNightMode(context)){
                       holder.imageView.setImageResource(R.drawable.baseline_person_white);
                   }
                   else
                       holder.imageView.setImageResource(R.drawable.baseline_person_24);
               }
       }
       else {

           if(MainActivity.isNightMode(context)){

               holder.imageView.setImageResource(R.drawable.baseline_message_white);
           }
           else
               holder.imageView.setImageResource(R.drawable.baseline_message_24);

       }
        String sender = null;
        sender = recyclerList.get(position).getContactName() != null ? recyclerList.get(position).getContactName() : recyclerList.get(position).getMobNumber();
        holder.nameView.setText(sender);
        holder.bodyView.setText(recyclerList.get(position).getMessage());
        holder.timeView.setText(" "+recyclerList.get(position).getDate()+"\n"+recyclerList.get(position).getTime());

        //OnLongClickListener

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getPosition());
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(recyclerClickInterface != null){
                    int pos = holder.getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        recyclerClickInterface.onItemClick(pos);
                    }
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
