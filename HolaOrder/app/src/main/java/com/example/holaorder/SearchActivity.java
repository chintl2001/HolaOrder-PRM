package com.example.holaorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.holaorder.Interface.ItemClickListener;
import com.example.holaorder.Model.Food;
import com.example.holaorder.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.ImageButton;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {

    private ImageButton searchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    private String SearchInput;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        inputText = findViewById(R.id.searchEditText);
        searchBtn = findViewById(R.id.searchButton);
        searchList = (RecyclerView) findViewById(R.id.searchList);
        GridLayoutManager layoutManagerGrid = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        searchList.setLayoutManager(layoutManagerGrid);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchInput = inputText.getText().toString();
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Foods");

        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(reference.orderByChild("Name").startAt(SearchInput) , Food.class)
                .build();

        FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter =
                new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food food) {
                        holder.tvFoodName.setText(food.getName());
                        Picasso.get().load(food.getImage()).into(holder.imgFood);
                        holder.tvPrice.setText(food.getPrice());
                        holder.rate.setRating(Float.parseFloat(food.getRate()));
                        Food clickItem = food;
                        Log.d("Food", food.toString());

                        holder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {
                                Toast.makeText(SearchActivity.this, clickItem.getName(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_popular, parent,false);
                        FoodViewHolder holder = new FoodViewHolder(view);
                        return holder;
                    }
                } ;
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}