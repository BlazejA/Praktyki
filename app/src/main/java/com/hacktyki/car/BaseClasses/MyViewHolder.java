package com.hacktyki.car.BaseClasses;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hacktyki.car.R;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public ImageView holderCarImage;
    public TextView holderCarName;
    public CardView holderCardView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        holderCarImage = itemView.findViewById(R.id.carPhoto);
        holderCarName = itemView.findViewById(R.id.carName);
        holderCardView = itemView.findViewById(R.id.cardview_id);

    }
}
