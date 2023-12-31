package com.example.holaorder;

import android.content.Intent;
import android.media.Image;
import android.os.Debug;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.holaorder.Common.Common;
import com.example.holaorder.Model.User;
import com.example.holaorder.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    /*TextView tvPhone ;
    TextView tvName ;*/
    ImageView imgUser;
    EditText edtEmail;
    EditText edtPhone;
    EditText edtName;
    Button btnEdit;
    TextView tv_Order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
           /* tvPhone = findViewById(R.id.tvPhone);
            tvName = findViewById(R.id.tvNam);*/
            imgUser = findViewById(R.id.imgUser);
            edtEmail = findViewById(R.id.edtEmail);
            edtPhone = findViewById(R.id.edtPhone);
            edtName = findViewById(R.id.edtName);
            btnEdit = findViewById(R.id.btnEdit);
            tv_Order = findViewById(R.id.tvMyOrder);
            User user = Common.currentUser;
            /*tvPhone.setText(user.getPhone());
            tvName.setText(user.getName());*/
            edtEmail.setText(user.getEmail());
            edtPhone.setText(user.getPhone());
            edtName.setText(user.getName());
            Picasso.get().load(user.getImage()).into(imgUser);


        tv_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

    }



    public void onClickEdit(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("User");

        String oldPhone = Common.currentUser.getPhone();
        String newPhone = edtPhone.getText().toString();
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();

        // Tạo nút mới với số điện thoại mới
        Map<String, Object> newUserData = new HashMap<>();
        newUserData.put("Name", name);
        newUserData.put("Email", email);
        newUserData.put("Password", Common.currentUser.getPassword());
        newUserData.put("Image", Common.currentUser.getImage());
        userRef.child(newPhone).setValue(newUserData);

        // Sao chép dữ liệu từ nút cũ sang nút mới
        userRef.child(oldPhone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    userRef.child(newPhone).setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi
            }
        });

        // Xóa nút cũ
        userRef.child(oldPhone).removeValue();

        // Cập nhật thông tin người dùng hiện tại
        Common.currentUser.setPhone(newPhone);
        Common.currentUser.setName(name);
        Common.currentUser.setEmail(email);
        Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
    }

}