package com.as.mymessage.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.as.mymessage.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactRecyclerViewHolder extends RecyclerView.ViewHolder {

    CircleImageView contactPhotoImageView;
    TextView nameTextView, mobNumberTextView;
    public ContactRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

       contactPhotoImageView =  itemView.findViewById(R.id.contact_photo_circular_image_view);
       nameTextView = itemView.findViewById(R.id.contact_name_text_view);
       mobNumberTextView = itemView.findViewById(R.id.mobNumber_text_view);
    }
}
