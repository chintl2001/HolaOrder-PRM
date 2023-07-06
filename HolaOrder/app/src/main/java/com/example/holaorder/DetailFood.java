package com.example.holaorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.holaorder.Adapter.PhotoAdapter;

import me.relex.circleindicator.CircleIndicator;

public class DetailFood extends AppCompatActivity {
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        viewPager=findViewById(R.id.viewPager);
        circleIndicator=findViewById(R.id.circleIndicator);
        photoAdapter=new PhotoAdapter(this);
        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        viewPager.setAdapter(photoAdapter);

    }
}