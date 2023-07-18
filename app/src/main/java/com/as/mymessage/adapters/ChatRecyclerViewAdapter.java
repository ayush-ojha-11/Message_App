package com.as.mymessage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.as.mymessage.DatabasePackage.MessageTableModalClass;
import com.as.mymessage.R;
import com.as.mymessage.modals.ChatRecyclerModal;

import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<MessageTableModalClass> receivedMessages;

    public ChatRecyclerViewAdapter(Context context, List<MessageTableModalClass> messages) {
        this.context = context;
        this.receivedMessages = messages;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1){
            View view = LayoutInflater.from(context).inflate(R.layout.received_message_layout,parent,false);
            return new ReceiveViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.sent_message_layout,parent,false);
            return new SentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       ((ReceiveViewHolder) holder).receivedMessageTextView.setText(receivedMessages.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return receivedMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
       return 1;
    }
}
