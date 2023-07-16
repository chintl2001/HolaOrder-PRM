package com.example.holaorder;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.holaorder.Common.Common;
import com.example.holaorder.Model.User;
import com.example.holaorder.Prevalent.Prevalent;
import com.example.holaorder.Sellers.SellerSignIn;
import com.example.holaorder.Sellers.SellerSignUp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnSignIn, btn_SellerSignUp;
    CheckBox checkBoxRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btn_signin);
        btn_SellerSignUp = findViewById(R.id.btn_seller_SI);
        checkBoxRemember = findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        //Lưu tài khoản đăng nhập
        String savedPhone = Paper.book().read(Prevalent.UserPhoneKey);
        String savedPassword = Paper.book().read(Prevalent.UserPasswordKey);

        if (savedPhone != null && savedPassword != null) {
            // Thông tin đăng nhập đã được lưu
            // Hiển thị thông báo hoặc thực hiện các hành động tương ứng
            edtUsername.setText(savedPhone);
            edtPassword.setText(savedPassword);
            checkBoxRemember.setChecked(true);
            Toast.makeText(this, "Đã lưu thông tin của bạn", Toast.LENGTH_SHORT).show();
           //Lưu thông tin
            User user = new User();
            user.setPhone(savedPhone);
            user.setPassword(savedPassword);
            Prevalent.currentOnlineUser = user;

        } else {
            // Thông tin đăng nhập chưa được lưu
            Toast.makeText(this, "Chưa lưu thông tin của bạn", Toast.LENGTH_SHORT).show();
        }
        // Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query userQuery = database.getReference("User").orderByChild("phone");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edtPassword.getText().toString().trim();
                if (password.isEmpty()) {
                    Toast.makeText(SignIn.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }

                ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please wait ...");
                mDialog.show();

                userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mDialog.dismiss();
                        boolean isSignInSuccessful = false;

                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            User user = userSnapshot.getValue(User.class);
                            if (user != null) {
                                Common.currentUser = user;
                                Log.d("My App", user.toString());
                                isSignInSuccessful = true;
                                break;
                            }
                        }

                        if (isSignInSuccessful) {
                            Toast.makeText(SignIn.this, "Sign in successfully!", Toast.LENGTH_SHORT).show();
                            if (checkBoxRemember.isChecked()) {
                                String phone = edtUsername.getText().toString().trim();
                                // Lưu thông tin phone và password bằng Paper
                                Paper.book().write(Prevalent.UserPhoneKey, phone);
                                Paper.book().write(Prevalent.UserPasswordKey, password);
                            }
                            Intent homeIntent = new Intent(SignIn.this, Home.class);
                            startActivity(homeIntent);
                            finish();
                        } else {
                            Toast.makeText(SignIn.this, "Sign in failed!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        mDialog.dismiss();
                        Log.d("SignIn", "onCancelled: " + error.getMessage());
                        Toast.makeText(SignIn.this, "Failed to sign in", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //Sign Up Seller
        btn_SellerSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SellerSignUp.class);
                startActivity(intent);
            }
        });



    }
}
