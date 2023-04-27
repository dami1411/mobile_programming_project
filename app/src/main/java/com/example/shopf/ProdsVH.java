package com.example.shopf;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class ProdsVH extends RecyclerView.ViewHolder {
    public TextView txtName;
    public TextView txtPrice;
    public RatingBar txtRating;
    public TextView txtOptions;
    public ImageView prodImg;
    public CardView rvItem;

    public ProdsVH(@NonNull View itemView) {
        super(itemView);
        prodImg = itemView.findViewById(R.id.imageProd);
        txtName = itemView.findViewById(R.id.nameProd);
        txtPrice = itemView.findViewById(R.id.priceProd);
        txtRating = itemView.findViewById(R.id.prodRating);
        txtRating.setIsIndicator(true);
        txtOptions = itemView.findViewById(R.id.txt_option);
        rvItem = itemView.findViewById(R.id.rv_item);
    }


}
