package com.example.holaorder.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.holaorder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerSignIn extends AppCompatActivity {
    private Button sellerSignIn;
    private EditText edt_Password, edt_Email;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_sign_in);

        sellerSignIn = findViewById(R.id.btn_Login_Seller);
        edt_Password = findViewById(R.id.edt_Password_Seller);
        edt_Email = findViewById(R.id.edt_Email_Seller);
        firebaseAuth = FirebaseAuth.getInstance();
        sellerSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInSeller();
            }
        });
    }

    private void signInSeller() {
        final String email = edt_Email.getText().toString();
        final String password = edt_Password.getText().toString();

        if (!email.equals("") && !password.equals("")){
            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()){
                           Toast.makeText(SellerSignIn.this, "You are SignIn Successful!!", Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(SellerSignIn.this, SellerHomeActivity.class);
                           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(intent);
                           finish();
                       }
                        }
                    });
        }else {
            Toast.makeText(this, "Please complete the Login form", Toast.LENGTH_SHORT).show();
        }
    }
}