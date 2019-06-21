package com.sohail.restaurant_referral;

import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sohail.restaurant_referral.Fragments.ShareSheetFragment;

public class RestaurantDetailActivity extends AppCompatActivity implements ShareSheetFragment.OnFragmentInteractionListener {

    TextView nameTxt,descTxt;
    ImageView detailImg;
    Toolbar toolbar;
    FloatingActionButton fab;
    CollapsingToolbarLayout collapsingToolbar;
   public String email,phone;
   public String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab=findViewById(R.id.fab);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameTxt=findViewById(R.id.nameTxt);
        descTxt=findViewById(R.id.descTxt);
        detailImg=findViewById(R.id.detailImg);
        collapsingToolbar=findViewById(R.id.collapsing_bar);
        email=getIntent().getStringExtra("email");
        name=getIntent().getStringExtra("name");
        collapsingToolbar.setTitle(getIntent().getStringExtra("name"));
        Glide.with(this).load(getIntent().getStringExtra("image")).into(detailImg);
        descTxt.setText(getIntent().getStringExtra("desc"));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareSheetFragment fragment=ShareSheetFragment.newInstance();
                fragment.show(getSupportFragmentManager(),"share_sheet_fragment");
            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
