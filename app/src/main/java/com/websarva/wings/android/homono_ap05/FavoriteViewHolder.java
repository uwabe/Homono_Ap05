package com.websarva.wings.android.homono_ap05;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FavoriteViewHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    public LinearLayout linearLayout;
    public FavoriteViewHolder(View itemView){
        super(itemView);
        linearLayout = itemView.findViewById(R.id.linear_layout);
        titleView= itemView.findViewById(R.id.favoriteTitle);
    }
}
