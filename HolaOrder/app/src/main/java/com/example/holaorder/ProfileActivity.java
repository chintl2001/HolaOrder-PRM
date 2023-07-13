package com.example.holaorder;

import android.media.Image;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.holaorder.Common.Common;
import com.example.holaorder.Model.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ProfileActivity extends AppCompatActivity {
    TextView tvPhone;
    TextView tvName;
    Image imgUser;
    EditText edtEmail;
    EditText edtPhone;
    EditText edtName;
    Button btnEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        User user = Common.currentUser;
        tvPhone.setText(user.getPhone());
        tvName.setText(user.getName());
        edtEmail.setText(user.getEmail());
        edtPhone.setText(user.getPhone());
        edtName.setText(user.getName());
        Picasso.get().load(user.getImage()).into((Target) imgUser);

    }

}