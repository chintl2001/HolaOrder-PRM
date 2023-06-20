package com.example.holaorder;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.holaorder.Model.User;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

public class SignIn extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnSignIn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignin);

        //Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_user = database.getReference("User");
         btnSignIn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                 mDialog.setMessage("Please wwaiting ....");
                 mDialog.show();
                 table_user.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                         //check user if not exist in database
                         if(snapshot.child(edtUsername.getText().toString()).exists()){
                             //Get user information
                             mDialog.dismiss();
                             User user = snapshot.child(edtUsername.getText().toString()).getValue(User.class);
                             if(user.getPassword().equals(edtPassword.getText().toString())){
                                 Toast.makeText(SignIn.this, "Sign in successfully !", Toast.LENGTH_SHORT).show();
                             }else{
                                 Toast.makeText(SignIn.this, "Sign in failed !", Toast.LENGTH_SHORT).show();
                             }
                         }
                         else{
                             mDialog.dismiss();
                             Toast.makeText(SignIn.this, "User not exist in database ", Toast.LENGTH_SHORT).show();
                         }


                     }

                     @Override
                     public void onCancelled(@NonNull @NotNull DatabaseError error) {

                     }
                 });
             }
         });
    }
}