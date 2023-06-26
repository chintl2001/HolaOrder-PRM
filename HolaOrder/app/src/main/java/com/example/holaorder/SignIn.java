package com.example.holaorder;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.holaorder.Common.Common;
import com.example.holaorder.Model.User;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SignIn extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btn_signin);

        // Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        mDialog.dismiss();
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            String phoneNumber = userSnapshot.getKey();
                            if(phoneNumber == null){
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, "User does not have in database!", Toast.LENGTH_SHORT).show();
                            }
                            String name = userSnapshot.child("Name").getValue(String.class);
                            String password = userSnapshot.child("Password").getValue(String.class);
                            User user = new User(name, password, phoneNumber);
                            Log.d("MyApp", "User: " + phoneNumber + ", Name: " + name + ", Password: " + password);

                            if (Objects.equals(user.getPassword(), edtPassword.getText().toString())) {
                                Toast.makeText(SignIn.this, "Sign in successfully!", Toast.LENGTH_SHORT).show();
                                Intent homeIntent = new Intent(SignIn.this, Home.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                            } else {
                                Toast.makeText(SignIn.this, "Sign in failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        // Handle onCancelled event
                    }
                });
            }
        });
    }
}