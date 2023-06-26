package com.example.holaorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.holaorder.Adaptor.CategoryAdaptor;
import com.example.holaorder.Adaptor.PopularAdaptor;
import com.example.holaorder.Domain.CategoryDomain;
import com.example.holaorder.Domain.FoodDomain;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private RecyclerView.Adapter adapter, adapter2 ;
    private RecyclerView recyclerViewCaregoryList, recyclerViewPopularList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerViewCaregory();
        recyclerViewPopular();
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

        adapter = new CategoryAdaptor(category);
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

        adapter2 = new PopularAdaptor(foodList);
        recyclerViewPopularList.setAdapter(adapter2);

    }
}