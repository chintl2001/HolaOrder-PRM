package com.example.holaorder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
        table_product = database.getReference("Foods");
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
        //GridLayoutManager layoutManagerGrid = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        //recyclerViewPopularList.setLayoutManager(layoutManagerGrid);

        /*ArrayList<FoodDomain> foodList = new ArrayList<>();
        foodList.add(new FoodDomain("Pepperoni Pizza", "pizza1", "slices pepperoni, mozzerella cheese", 9.76));
        foodList.add(new FoodDomain("Cheese Burger", "burger", "beef, cheese, sauce, tomato", 8.79));
        foodList.add(new FoodDomain("Vegetable pizza", "pizza2", "olive oil, vegetable oil, cherry tomatoes, basil", 8.5));

        adapter2 = new PopularAdapter(foodList);
        recyclerViewPopularList.setAdapter(adapter2);*/

        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(table_product, Food.class)
                        .build();

        FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_popular, parent,false);
                FoodViewHolder holder = new FoodViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int position, @NonNull Food food) {
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
                foodViewHolder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String imageUrl = food.getImage();
                        String saveCurrentTime, saveCurrentDate;

                        Calendar calForDate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                        saveCurrentDate = currentDate.format(calForDate.getTime());

                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                        saveCurrentTime = currentTime.format(calForDate.getTime());

                        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("CartList");

                        final HashMap<String, Object> cartMap = new HashMap<>();
                        cartMap.put("fname", foodViewHolder.tvFoodName.getText().toString());
                        cartMap.put("price", foodViewHolder.tvPrice.getText().toString());
                        cartMap.put("date", saveCurrentDate);
                        cartMap.put("time", saveCurrentTime);
                        cartMap.put("img", imageUrl);



                        String cartItemId = cartListRef.child("CartView")
                                .child(Prevalent.currentOnlineUser.getPhone()).child("Foods")
                                .push().getKey();
                        cartListRef.child("CartView")
                                .child(Prevalent.currentOnlineUser.getPhone())
                                .child("Foods").child(cartItemId).setValue(cartMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Home.this, "Added to Cart ", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
            }
        };
        recyclerViewPopularList.setAdapter(adapter);
        adapter.startListening();
    }

    public void viewAllProducts(View view) {
        Intent productListIntent = new Intent(Home.this, ListProduct.class);
        startActivity(productListIntent);

    }
}