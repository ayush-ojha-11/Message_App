package com.as.mymessage.adapters;

import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.as.mymessage.R;

public class ReceiveViewHolder extends RecyclerView.ViewHolder{

    TextView receivedMessageTextView, receivedTimeView;

    public ReceiveViewHolder(@NonNull View itemView) {
        super(itemView);
        receivedMessageTextView = itemView.findViewById(R.id.received_text_view);
        receivedTimeView = itemView.findViewById(R.id.received_time_text_view);
    }
}
