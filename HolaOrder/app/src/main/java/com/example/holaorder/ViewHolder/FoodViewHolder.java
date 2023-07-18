package com.example.holaorder.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.holaorder.Common.Common;
import com.example.holaorder.Home;
import com.example.holaorder.Interface.ItemClickListener;
import com.example.holaorder.ListProduct;
import com.example.holaorder.Model.Food;
import com.example.holaorder.Model.User;
import com.example.holaorder.Prevalent.Prevalent;
import com.example.holaorder.R;
import com.example.holaorder.SignIn;
import com.google.firebase.database.*;
import io.paperdb.Paper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView tvFoodName;
    public TextView tvPrice;
    private Context context;
    public ImageView imgFood;
    public RatingBar rate;
    public Button btn;
    public ImageView imgFavorite;
    private ItemClickListener itemClickListener;

    public List<Food> foodList = new ArrayList<>(); // Thêm thuộc tính foodList



    public FoodViewHolder(@NonNull @NotNull View itemView, Context context, List<Food> foodList) {
        super(itemView);
        this.context = context;
        this.foodList = foodList;
        tvFoodName = itemView.findViewById(R.id.title);
        tvPrice = itemView.findViewById(R.id.fee);
        imgFood = itemView.findViewById(R.id.pic);
        rate = itemView.findViewById(R.id.ratingBar1);
        btn= itemView.findViewById(R.id.addBtn);

        imgFavorite = itemView.findViewById(R.id.imgFavorite);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query userQuery = database.getReference("Foods");
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Food food = userSnapshot.getValue(Food.class);
                    Log.d("My App", food.toString());
                  //  foodList.add(food);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }


        });



        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && foodList != null && foodList.size() > position) {
                  //  Log.d("My App", foodList.toString());

                    Food food = foodList.get(position);
                    addToFavorites(food);
                }else{
                    Log.d("My App",String.valueOf(position));
                }
            }
        });

        itemView.setOnClickListener(this);
    }

    private void addToFavorites(Food food) {
        String currentUserPhone = Common.currentUser.getPhone();

        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference("Favorites");
        favoritesRef.child(currentUserPhone).child(food.getId()).setValue(food);

        Toast.makeText(context, "Sản phẩm đã được thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
