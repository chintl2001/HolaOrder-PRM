package com.example.holaorder;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    private EditText etUser, etPhone, etPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        etUser = findViewById(R.id.edt_userName);
        etPhone = findViewById(R.id.edt_phone);
        etPassword = findViewById(R.id.edt_password);
        btnRegister = findViewById(R.id.btn_signUp);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = etUser.getText().toString();
                String phone = etPhone.getText().toString();
                String password = etPassword.getText().toString();

                registerUser(user, phone, password);
            }
        });
    }

    private void registerUser(String user, String phone, String password) {
        // TODO: Thực hiện việc đăng ký tài khoản
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(user, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUp.this, "Đăng ký tài khoản thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
