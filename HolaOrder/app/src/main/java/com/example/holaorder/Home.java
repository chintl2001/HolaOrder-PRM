package com.example.holaorder;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.holaorder.Adapter.CategoryAdapter;
import com.example.holaorder.Adapter.PopularAdapter;

import com.example.holaorder.Common.Common;

import com.example.holaorder.Domain.CategoryDomain;
import com.example.holaorder.Domain.FoodDomain;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    TextView textItem;
    private RecyclerView.Adapter adapter, adapter2 ;
    private RecyclerView recyclerViewCaregoryList, recyclerViewPopularList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_category = database.getReference("Category");
        DatabaseReference table_product = database.getReference("Product");

        ((TextView) findViewById(R.id.textHello)).setText("Hello, " + Common.currentUser.getUsername());
        recyclerViewCaregory();
        recyclerViewPopular();

        textItem = ((TextView)findViewById(R.id.textItem));

        textItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent product = new Intent(Home.this,ListProduct.class);
                startActivity(product);
            }
        });


    }

    private void recyclerViewCaregory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCaregoryList=findViewById(R.id.recyclerView);
        recyclerViewCaregoryList.setLayoutManager(linearLayoutManager);

        ArrayList<CategoryDomain> category = new ArrayList<CategoryDomain>();
        category.add(new CategoryDomain("Pizza","cat_1"));
        category.add(new CategoryDomain("Burger","cat_2"));
        category.add(new CategoryDomain("Hotdog","cat_3"));
        category.add(new CategoryDomain("Drink","cat_4"));
        category.add(new CategoryDomain("Donut","cat_5"));

        adapter = new CategoryAdapter(category);
        recyclerViewCaregoryList.setAdapter(adapter);
    }

    private void recyclerViewPopular(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPopularList = findViewById(R.id.recyclerView2);
        recyclerViewPopularList.setLayoutManager(linearLayoutManager);

        ArrayList<FoodDomain> foodList = new ArrayList<>();
        foodList.add(new FoodDomain("Pepperoni Pizza", "pizza1", "slices pepperoni, mozzerella cheese",9.76));
        foodList.add(new FoodDomain("Cheese Burger", "burger", "beef, cheese, sauce, tomato",8.79));
        foodList.add(new FoodDomain("Vegetable pizza", "pizza2", "olive oil, vegetable oil, cherry tomatoes, basil",8.5));

        adapter2 = new PopularAdapter(foodList);
        recyclerViewPopularList.setAdapter(adapter2);

    }

    public void viewAllProducts(View view) {
        Intent productListIntent = new Intent(Home.this, ListProduct.class);
        startActivity(productListIntent);

    }
}