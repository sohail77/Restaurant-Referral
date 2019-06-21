package com.sohail.restaurant_referral;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class RestaurantDetailActivity extends AppCompatActivity {

    TextView nameTxt,descTxt;
    ImageView detailImg;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameTxt=findViewById(R.id.nameTxt);
        descTxt=findViewById(R.id.descTxt);
        detailImg=findViewById(R.id.detailImg);
        collapsingToolbar=findViewById(R.id.collapsing_bar);

        collapsingToolbar.setTitle(getIntent().getStringExtra("name"));
        Glide.with(this).load(getIntent().getStringExtra("image")).into(detailImg);
        descTxt.setText(getIntent().getStringExtra("desc"));


    }
}
