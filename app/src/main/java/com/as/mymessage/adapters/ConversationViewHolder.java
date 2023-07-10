package com.as.mymessage.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.as.mymessage.R;

public class ConversationViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nameView,bodyView,timeView;

    public ConversationViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imageView);
        nameView = itemView.findViewById(R.id.textViewName);
        bodyView = itemView.findViewById(R.id.textViewBody);
        timeView = itemView.findViewById(R.id.textViewTime);
    }

}
