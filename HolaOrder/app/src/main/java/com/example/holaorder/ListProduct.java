package com.example.holaorder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.holaorder.Common.Common;
import com.example.holaorder.Interface.ItemClickListener;
import com.example.holaorder.Model.Category;
import com.example.holaorder.Model.Food;
import com.example.holaorder.Model.User;
import com.example.holaorder.ViewHolder.CategoryViewHolder;
import com.example.holaorder.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class ListProduct extends AppCompatActivity {
    RecyclerView recyclerViewCategory, recyclerViewProduct;
    DatabaseReference table_category;
    DatabaseReference table_product;
    NavigationView navigationView;
    CircleImageView imgUser;
    TextView txtFullName, txtEmail;
    ImageView imgUpload;
    DrawerLayout drawerLayout;
    //Search
    EditText searchView;
    public Context context;
    private List<Food> foodList;

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
        //((TextView) findViewById(R.id.tvHelloUser)).setText("Hello, " + Common.currentUser.getName());
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
       //((TextView) findViewById(R.id.tvHelloUser)).setText(Common.currentUser.getName());
        ((TextView) findViewById(R.id.tvHelloUser)).setText(Common.currentUser.getName());

        loadCategory();
        loadProduct("");
        NavSettup();

        ImageButton btn_cart = (ImageButton)  findViewById(R.id.cartViewButton);
        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListProduct.this,CartActivity.class);
                startActivity(intent);
            }
        });

        //ToDo:Search
        searchView = findViewById(R.id.searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                // Intent sang Activity khác
                Intent intent = new Intent(ListProduct.this, SearchActivity.class);
                startActivity(intent);
            }
        });

    }

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
                        // Lấy danh mục được click
                        String selectedCategory = category.getName();
                        // Gọi phương thức loadProduct() với danh mục đã chọn hoặc rỗng
                        loadProduct(selectedCategory.isEmpty() ? "" : selectedCategory);
                        Toast.makeText(ListProduct.this, clickItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recyclerViewCategory.setAdapter(adapter);
        adapter.startListening();
    }


    // bản khác
    private void loadProduct(String category) {
        Query query;

        if (category.isEmpty()) {
            // Hiển thị tất cả sản phẩm nếu không có danh mục được chọn
            query = table_product;
        } else {
            // Hiển thị sản phẩm theo danh mục được chọn
            query = table_product.orderByChild("CategoryId").equalTo(category);
        }
        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(query, Food.class)
                        .build();

        FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {


            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_popular, parent,false);
                // Khởi tạo ViewHolder và truyền foodList vào
                foodList = new ArrayList<>();

                FoodViewHolder holder = new FoodViewHolder(view, parent.getContext(), foodList);
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
                        Intent intent = new Intent(ListProduct.this, DetailFood.class);
                        intent.putExtra("FoodId", foodId);
                        startActivity(intent);
                    }
                });

                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(ListProduct.this, clickItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recyclerViewProduct.setAdapter(adapter);
        adapter.startListening();
    }

    private void NavSettup() {
        navigationView = findViewById(R.id.navbar);
        imgUser = navigationView.getHeaderView(0).findViewById(R.id.img_user);
        txtFullName = navigationView.getHeaderView(0).findViewById(R.id.txt_nav_name);
        txtEmail = navigationView.getHeaderView(0).findViewById(R.id.txt_user_name);
        imgUpload = navigationView.getHeaderView(0).findViewById(R.id.img_upload);
        drawerLayout = findViewById(R.id.drawer_layout);

        User user = Common.currentUser;
        Picasso.get().load(user.getImage()).into(imgUser);
        txtFullName.setText(user.getName());
        txtEmail.setText(user.getEmail());

        findViewById(R.id.img_menu).setOnClickListener(v -> ShowNavigationBar());
    }
    private void ShowNavigationBar() {
        findViewById(R.id.img_menu).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));


    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    public void profile(MenuItem item) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void changePass(MenuItem item) {
        Intent intent = new Intent(this, ChangePasswordAct.class);
        startActivity(intent);
    }

    public void listFood(MenuItem item) {
        Intent intent = new Intent(this, ListProduct.class);
        startActivity(intent);
    }

    public void clickHome(MenuItem item) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}