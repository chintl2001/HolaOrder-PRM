package com.example.holaorder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.holaorder.Common.Common;
import com.example.holaorder.Interface.ItemClickListener;
import com.example.holaorder.Model.Category;
import com.example.holaorder.Model.Food;
import com.example.holaorder.Prevalent.Prevalent;
import com.example.holaorder.ViewHolder.CartViewHolder;
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
import java.util.Calendar;
import java.util.HashMap;

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategory.setLayoutManager(layoutManager);
        //load product
        recyclerViewProduct = (RecyclerView) findViewById(R.id.productRecyclerView);
        GridLayoutManager layoutManagerGrid = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerViewProduct.setLayoutManager(layoutManagerGrid);
        ((TextView) findViewById(R.id.tvHelloUser)).setText(Common.currentUser.getName());

        loadCategory();
        loadProduct();

        ImageButton btn_cart = (ImageButton)  findViewById(R.id.cartViewButton);
        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListProduct.this,CartActivity.class);
                startActivity(intent);
            }
        });


    }
//    private void loadCategory() {
//
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
//                        Toast.makeText(ListProduct.this, clickItem.getName(), Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//            }
//        };
//        recyclerViewCategory.setAdapter(adapter);
//    }

    //Bản khác
    private void loadCategory() {

        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(table_category, Category.class)
                        .build();

        FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter
                = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {
            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category, parent,false);
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
                        Toast.makeText(ListProduct.this, clickItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recyclerViewCategory.setAdapter(adapter);
        adapter.startListening();
    }
//    private void loadProduct() {
//        FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.viewholder_popular, FoodViewHolder.class, table_product) {
//
//
//            @Override
//            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
//                foodViewHolder.tvFoodName.setText(food.getName());
//                Picasso.get().load(food.getImage()).into(foodViewHolder.imgFood);
//                foodViewHolder.tvPrice.setText(food.getPrice());
//                foodViewHolder.rate.setRating(Float.parseFloat(food.getRate()));
//                Food clickItem = food;
//                Log.d("Food", food.toString());
//
//                foodViewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(ListProduct.this, clickItem.getName(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//                foodViewHolder.btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final String imageUrl = food.getImage();
//                        String saveCurrentTime, saveCurrentDate;
//
//                        Calendar calForDate = Calendar.getInstance();
//                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
//                        saveCurrentDate = currentDate.format(calForDate.getTime());
//
//                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
//                        saveCurrentTime = currentTime.format(calForDate.getTime());
//
//                        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
//
//                        final HashMap<String, Object> cartMap = new HashMap<>();
//                        cartMap.put("fname", foodViewHolder.tvFoodName.getText().toString());
//                        cartMap.put("price", foodViewHolder.tvPrice.getText().toString());
//                        cartMap.put("date", saveCurrentDate);
//                        cartMap.put("time", saveCurrentTime);
//                        cartMap.put("img", imageUrl);
//
//
//
//                        String cartItemId = cartListRef.child("Cart View")
//                                .child(Prevalent.currentOnlineUser.getPhone()).child("Foods")
//                                .push().getKey();
//                        cartListRef.child("Cart View")
//                                .child(Prevalent.currentOnlineUser.getPhone())
//                                .child("Foods").child(cartItemId).setValue(cartMap)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            Toast.makeText(ListProduct.this, "Added to Cart ", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                    }
//                });
//            }
//        };
//        recyclerViewProduct.setAdapter(adapter);
//    }



    // bản khác
    private void loadProduct() {

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
                        Toast.makeText(ListProduct.this, clickItem.getName(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(ListProduct.this, "Added to Cart ", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
            }
        };
        recyclerViewProduct.setAdapter(adapter);
        adapter.startListening();
    }
}