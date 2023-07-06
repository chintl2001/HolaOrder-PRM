package com.example.holaorder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.holaorder.Adapter.CategoryAdapter;
import com.example.holaorder.Adapter.PopularAdapter;

import com.example.holaorder.Common.Common;

import com.example.holaorder.Domain.CategoryDomain;
import com.example.holaorder.Domain.FoodDomain;
import com.example.holaorder.Interface.ItemClickListener;
import com.example.holaorder.Model.Category;
import com.example.holaorder.Model.Food;
import com.example.holaorder.Prevalent.Prevalent;
import com.example.holaorder.ViewHolder.CategoryViewHolder;
import com.example.holaorder.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    Button LogoutBtn;
    DatabaseReference table_category;
    DatabaseReference table_product;
    TextView textItem;
    private RecyclerView.Adapter adapter, adapter2;
    private RecyclerView recyclerViewCaregoryList, recyclerViewPopularList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        table_category = database.getReference("Category");
        table_product = database.getReference("Food");
        DatabaseReference table_product = database.getReference("Product");

        ((TextView) findViewById(R.id.textHello)).setText("Hello, " + Common.currentUser.getName());
        recyclerViewCaregory();
        recyclerViewPopular();

        textItem = ((TextView) findViewById(R.id.textItem));

        textItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent product = new Intent(Home.this, ListProduct.class);
                startActivity(product);
            }
        });

        LogoutBtn = (Button) findViewById(R.id.textView5);

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prevalent.currentOnlineUser = null;
                Intent intent = new Intent(Home.this, SignIn.class);
                startActivity(intent);
            }
        });


    }

    private void recyclerViewCaregory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCaregoryList = findViewById(R.id.recyclerView);
        recyclerViewCaregoryList.setLayoutManager(linearLayoutManager);

        /*ArrayList<CategoryDomain> category = new ArrayList<CategoryDomain>();
        category.add(new CategoryDomain("Pizza","cat_1"));
        category.add(new CategoryDomain("Burger","cat_2"));
        category.add(new CategoryDomain("Hotdog","cat_3"));
        category.add(new CategoryDomain("Drink","cat_4"));
        category.add(new CategoryDomain("Donut","cat_5"));

        adapter = new CategoryAdapter(category);
        recyclerViewCaregoryList.setAdapter(adapter);*/

//        FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class, R.layout.viewholder_category, CategoryViewHolder.class, table_category) {
//            @Override
//            protected void populateViewHolder(CategoryViewHolder categoryViewHolder, Category category, int i) {
//                categoryViewHolder.tvCategoryName.setText(category.getName());
//                Picasso.get().load(category.getImage()).into(categoryViewHolder.imgCategory);
//                Category clickItem = category;
//                Log.d("Food", category.toString());
//                categoryViewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(Home.this, clickItem.getName(), Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//            }
//        };
        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(table_category, Category.class)
                        .build();

        FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {
            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
                CategoryViewHolder holder = new CategoryViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int position, @NonNull Category category) {
                categoryViewHolder.tvCategoryName.setText(category.getName());
                Picasso.get().load(category.getImage()).into(categoryViewHolder.imgCategory);
                Category clickItem = category;
                Log.d("Food", category.toString());
                categoryViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(Home.this, clickItem.getName(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        };
        recyclerViewCaregoryList.setAdapter(adapter);
        adapter.startListening();
    }

    private void recyclerViewPopular() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPopularList = findViewById(R.id.recyclerView2);
        recyclerViewPopularList.setLayoutManager(linearLayoutManager);

        ArrayList<FoodDomain> foodList = new ArrayList<>();
        foodList.add(new FoodDomain("Pepperoni Pizza", "pizza1", "slices pepperoni, mozzerella cheese", 9.76));
        foodList.add(new FoodDomain("Cheese Burger", "burger", "beef, cheese, sauce, tomato", 8.79));
        foodList.add(new FoodDomain("Vegetable pizza", "pizza2", "olive oil, vegetable oil, cherry tomatoes, basil", 8.5));

        adapter2 = new PopularAdapter(foodList);
        recyclerViewPopularList.setAdapter(adapter2);
        /*FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.viewholder_popular, FoodViewHolder.class, table_product) {
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
                        Toast.makeText(Home.this, clickItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
                }
            };
        recyclerViewPopularList.setAdapter(adapter);*/

    }

    public void viewAllProducts(View view) {
        Intent productListIntent = new Intent(Home.this, ListProduct.class);
        startActivity(productListIntent);

    }
}