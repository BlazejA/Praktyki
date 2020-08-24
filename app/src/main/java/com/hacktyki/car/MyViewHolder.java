package com.hacktyki.car;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView carImage;
    TextView carName;
    CardView cv;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        carImage = itemView.findViewById(R.id.carPhoto);
        carName = itemView.findViewById(R.id.carName);
        cv = itemView.findViewById(R.id.cardview_id);

    }
}
