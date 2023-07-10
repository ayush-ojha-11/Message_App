package com.as.mymessage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.as.mymessage.R;
import com.as.mymessage.modals.RecyclerModalClass;

import java.util.List;

public class ConversationRecyclerViewAdapter extends RecyclerView.Adapter<ConversationViewHolder> {

    List<RecyclerModalClass> recyclerList;
    Context context;


    public ConversationRecyclerViewAdapter(List<RecyclerModalClass> recyclerList, Context context) {
        this.recyclerList = recyclerList;
        this.context = context;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversationViewHolder(LayoutInflater.from(context).inflate(R.layout.message_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {

        holder.imageView.setImageResource(recyclerList.get(position).getImage());
        holder.nameView.setText(recyclerList.get(position).getName());
        holder.bodyView.setText(recyclerList.get(position).getMessage());
        holder.timeView.setText(recyclerList.get(position).getTime());

        //OnClickListener

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return recyclerList.size();
    }
}
