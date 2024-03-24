package com.as.mymessage.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.as.mymessage.R;

public class SentViewHolder extends RecyclerView.ViewHolder {
    TextView sentMessageTextView, sentTimeView;
    public SentViewHolder(@NonNull View itemView) {
        super(itemView);
       sentMessageTextView = itemView.findViewById(R.id.sent_text_view);
       sentTimeView = itemView.findViewById(R.id.time_text_view);
    }
}
