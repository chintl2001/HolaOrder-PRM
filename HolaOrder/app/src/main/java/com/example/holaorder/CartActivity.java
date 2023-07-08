package com.example.holaorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.holaorder.Interface.ItemClickListener;
import com.example.holaorder.Model.Cart;
import com.example.holaorder.Model.Category;
import com.example.holaorder.Prevalent.Prevalent;
import com.example.holaorder.ViewHolder.CartViewHolder;
import com.example.holaorder.ViewHolder.CategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button OrderBtn;
    private TextView txtTotalAmount;
    DatabaseReference table_cart;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        table_cart = database.getReference("CartList")
                .child("CartView")
                .child(Prevalent.currentOnlineUser.getPhone())
                .child("Foods");

        recyclerView = (RecyclerView) findViewById(R.id.rv_cart);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);


        loadCart();

        OrderBtn = (Button) findViewById(R.id.btn_order_C);
        txtTotalAmount = (TextView) findViewById(R.id.tv_total_C);


    }

    private void loadCart() {

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(table_cart, Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;

            }
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                Picasso.get().load(model.getImage()).into(holder.imgFood);
                holder.tvFoodName.setText(model.getFname());
                holder.tvPrice.setText("Price: " + model.getPrice() + "$");
                holder.tvQuantity.setText("Quantity" + model.getQuantity());
                Cart clickItem = model;
                Log.d("Cart", clickItem.toString());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(CartActivity.this, clickItem.getFname(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
