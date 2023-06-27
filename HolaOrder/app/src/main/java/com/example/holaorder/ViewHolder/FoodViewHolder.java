package com.example.holaorder.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.holaorder.Interface.ItemClickListener;
import com.example.holaorder.R;
import org.jetbrains.annotations.NotNull;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView tvFoodName;
    public TextView tvPrice;
    public ImageView imgFood;
    public RatingBar rate;
    private ItemClickListener itemClickListener;

    public FoodViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        tvFoodName = ((TextView)itemView.findViewById(R.id.productName) );
        tvPrice = ((TextView)itemView.findViewById(R.id.price) );
        imgFood = ((ImageView) itemView.findViewById(R.id.productImage));
        rate = ((RatingBar) itemView.findViewById(R.id.ratingBar));
        itemView.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
