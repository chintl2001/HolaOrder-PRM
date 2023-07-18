package com.example.holaorder.Sellers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.holaorder.Home;
import com.example.holaorder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerSignUp extends AppCompatActivity {

    private Button btn_sellerSignIn, btn_sellerSignUp;
    private EditText edt_Name, edt_Phone, edt_Password, edt_Email;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_sign_up);

        btn_sellerSignIn = findViewById(R.id.btn_haveAA_S);
        edt_Name = findViewById(R.id.edt_UserName_S);
        edt_Phone = findViewById(R.id.edt_Phone_S);
        edt_Password = findViewById(R.id.edt_Password_S);
        edt_Email = findViewById(R.id.edt_Email);
        btn_sellerSignUp = findViewById(R.id.btn_SignUp_S);
        firebaseAuth = FirebaseAuth.getInstance();

        btn_sellerSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SellerSignUp.this, SellerSignIn.class);
                startActivity(intent);
            }
        });

        btn_sellerSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpSeller();
            }
        });

    }

    private void signUpSeller() {
        String name = edt_Name.getText().toString();
        String phone = edt_Phone.getText().toString();
        String password = edt_Password.getText().toString();
        String email = edt_Email.getText().toString();

        if (!name.equals("") && !phone.equals("") && !password.equals(""))
        {
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                final DatabaseReference rootRef;

                                rootRef = FirebaseDatabase.getInstance().getReference();

                                String sid  = firebaseAuth.getCurrentUser().getUid();

                                HashMap<String, Object> sellerMap = new HashMap<>();
                                sellerMap.put("sid", sid);
                                sellerMap.put("name", name);
                                sellerMap.put("phone", phone);
                                sellerMap.put("email", email);

                                rootRef.child("Sellers").child(sid).updateChildren(sellerMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(SellerSignUp.this, "You are SignUp Successful!!", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(SellerSignUp.this, SellerHomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Please complete the SignUp form.", Toast.LENGTH_SHORT).show();
        }
    }
}