package com.example.holaorder;

import android.annotation.SuppressLint;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.holaorder.Common.Common;
import com.example.holaorder.Interface.ItemClickListener;
import com.example.holaorder.Model.Category;
import com.example.holaorder.Model.Food;
import com.example.holaorder.ViewHolder.CategoryViewHolder;
import com.example.holaorder.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ListProduct extends AppCompatActivity {
    RecyclerView recyclerViewCategory, recyclerViewProduct;
    DatabaseReference table_category;
    DatabaseReference table_product;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        // Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
         table_category = database.getReference("Category");
         table_product = database.getReference("Foods");
        //set current user
        ((TextView) findViewById(R.id.tvHelloUser)).setText("Hello, " + Common.currentUser.getName());
        //load category
        recyclerViewCategory = (RecyclerView) findViewById(R.id.categoryRecyclerView);
        recyclerViewCategory.setHasFixedSize(true);
        //layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCategory.setLayoutManager(layoutManager);
        //load product
        recyclerViewProduct = (RecyclerView) findViewById(R.id.productRecyclerView);
        GridLayoutManager layoutManagerGrid = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerViewProduct.setLayoutManager(layoutManagerGrid);
       ((TextView) findViewById(R.id.tvHelloUser)).setText(Common.currentUser.getName());

        loadCategory();
        loadProduct();


    }
    private void loadCategory() {

        FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class, R.layout.viewholder_category, CategoryViewHolder.class, table_category) {
            @Override
            protected void populateViewHolder(CategoryViewHolder categoryViewHolder, Category category, int i) {
                categoryViewHolder.tvCategoryName.setText(category.getName());
                Picasso.get().load(category.getImage()).into(categoryViewHolder.imgCategory);
                Category clickItem = category;
                Log.d("Food", category.toString());
                categoryViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(ListProduct.this, clickItem.getName(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        };
        recyclerViewCategory.setAdapter(adapter);
    }
    private void loadProduct() {
        FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.viewholder_popular, FoodViewHolder.class, table_product) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
                foodViewHolder.tvFoodName.setText(food.getName());
                Picasso.get().load(food.getImage()).into(foodViewHolder.imgFood);
                foodViewHolder.tvPrice.setText(food.getPrice());
                foodViewHolder.rate.setRating(Float.parseFloat(food.getRate()));
                Food clickItem = food;
                Log.d("Food", food.toString());

                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(ListProduct.this, clickItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recyclerViewProduct.setAdapter(adapter);
    }
}