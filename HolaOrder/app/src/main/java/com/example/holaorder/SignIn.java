package com.example.holaorder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.holaorder.Common.Common;
import com.example.holaorder.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    }
}
