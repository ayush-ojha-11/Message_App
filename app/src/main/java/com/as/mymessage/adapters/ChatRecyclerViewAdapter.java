package com.as.mymessage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.as.mymessage.R;
import com.as.mymessage.util.ChatMessagePOJO;

import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;

    List<ChatMessagePOJO> allMessages;

    public ChatRecyclerViewAdapter(Context context, List<ChatMessagePOJO> allMessages) {
        this.context = context;
        this.allMessages = allMessages;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0){
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
        if(allMessages.get(position).isSent())
            ((SentViewHolder) holder).sentMessageTextView.setText(allMessages.get(position).getMessage());
        else
            ((ReceiveViewHolder) holder).receivedMessageTextView.setText(allMessages.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return allMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
       if(allMessages.get(position).isSent()) return 1;
       else return 0;
    }
}
