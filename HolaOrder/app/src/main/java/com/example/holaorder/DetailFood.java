package com.example.holaorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.holaorder.Adapter.PhotoAdapter;
import com.example.holaorder.Model.Food;
import com.example.holaorder.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import me.relex.circleindicator.CircleIndicator;

public class DetailFood extends AppCompatActivity {
    private ImageView fImg;
    private ElegantNumberButton numberButton;
    private TextView fName;
    private TextView fPrice;
    private TextView fDes;
    private RatingBar fRate;
    private Button addToCartBtn;
    private String foodId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);

        foodId = getIntent().getStringExtra("FoodId");


        addToCartBtn = (Button) findViewById(R.id.addToCart_Dt);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        fImg = (ImageView) findViewById(R.id.img_fDetail);
        fName = (TextView) findViewById(R.id.txtFoodName);
        fPrice = (TextView) findViewById(R.id.price);
        fDes = (TextView) findViewById(R.id.txtDescription);
        fRate = (RatingBar) findViewById(R.id.ratingBar_Detail);

        getFoodDetails(foodId);


        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCartList();
            }
        });


    }

    private void addToCartList() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("CartList");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("FoodId", foodId);
        cartMap.put("fname", "");
        cartMap.put("price", "");
        cartMap.put("rate", 0);
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");
        cartMap.put("imageUrl", ""); // Thêm đường dẫn URL của ảnh

        DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference().child("Foods").child(foodId);
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    cartMap.put("fname", food.getName());
                    cartMap.put("price", food.getPrice());
                    cartMap.put("rate", food.getRate());
                    cartMap.put("imageUrl", food.getImage()); // Thêm đường dẫn URL của ảnh

                    cartListRef.child("CartView")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .child("Foods")
                            .child(foodId)
                            .updateChildren(cartMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(DetailFood.this, "Add to cart successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
    }


    private void getFoodDetails(String foodId) {
        DatabaseReference foodDetailsRef = FirebaseDatabase.getInstance().getReference().child("Foods");
        foodDetailsRef.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Food food = snapshot.getValue(Food.class);
                    fName.setText(food.getName());
                    fDes.setText(food.getDescription());
                    fPrice.setText(food.getPrice() + "$");
                    Picasso.get().load(food.getImage()).into(fImg);
                    fRate.setRating(Float.parseFloat(food.getRate()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}