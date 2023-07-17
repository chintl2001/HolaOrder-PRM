package com.example.holaorder;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Home extends AppCompatActivity {
    Context context;
    Button LogoutBtn;
    DatabaseReference table_category;
    DatabaseReference table_product;
    TextView textItem;
    private List<Food> foodList;
    private RecyclerView.Adapter adapter, adapter2;
    private RecyclerView recyclerViewCaregoryList, recyclerViewPopularList;
    //Search
    EditText searchHome;
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
        recyclerViewPopular("");

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

        searchHome = findViewById(R.id.searchHome);
        searchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                // Intent sang Activity khác
                Intent intent = new Intent(Home.this, SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    private void recyclerViewCaregory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCaregoryList = findViewById(R.id.recyclerView);
        recyclerViewCaregoryList.setLayoutManager(linearLayoutManager);

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
                        String selectedCategory = category.getName();
                        recyclerViewPopular(selectedCategory.isEmpty() ? "" : selectedCategory);
                        Toast.makeText(Home.this, clickItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recyclerViewCaregoryList.setAdapter(adapter);
        adapter.startListening();
    }

    private void recyclerViewPopular(String category) {
        Query qrr;
        if (category.isEmpty()) {
            qrr = table_product;
        } else {
            qrr = table_product.orderByChild("CategoryId").equalTo(category);
        }
        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(qrr, Food.class)
                        .build();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPopularList = findViewById(R.id.recyclerView2);
        recyclerViewPopularList.setLayoutManager(linearLayoutManager);


        FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_popular, parent,false);
                FoodViewHolder holder = new FoodViewHolder(view, context, foodList);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int position, @NonNull Food food) {
                String foodId = getRef(position).getKey();
                foodViewHolder.tvFoodName.setText(food.getName());
                Picasso.get().load(food.getImage()).into(foodViewHolder.imgFood);
                foodViewHolder.tvPrice.setText(food.getPrice());
                foodViewHolder.rate.setRating(Float.parseFloat(food.getRate()));
                Food clickItem = food;
                Log.d("Food", food.toString());

                foodViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Home.this, DetailFood.class);
                        intent.putExtra("FoodId", foodId);
                        startActivity(intent);
                    }
                });
                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(Home.this, clickItem.getName(), Toast.LENGTH_SHORT).show();
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