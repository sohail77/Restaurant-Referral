package com.sohail.restaurant_referral;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sohail.restaurant_referral.Adapters.MenuAdapter;
import com.sohail.restaurant_referral.Fragments.ShareSheetFragment;

import java.util.ArrayList;

public class RestaurantDetailActivity extends AppCompatActivity implements ShareSheetFragment.OnFragmentInteractionListener {

    TextView nameTxt, descTxt;
    ImageView detailImg;
    Toolbar toolbar;
    FloatingActionButton fab;
    CollapsingToolbarLayout collapsingToolbar;
    LinearLayout callLayout, directionLayout;
    public String email, phone;
    public String name;
    double lat,lon;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView galleryRv;
    MenuAdapter adapter;
    Boolean isPermissionGiven=false;
    ArrayList<String> galleryList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        nameTxt = findViewById(R.id.nameTxt);
        descTxt = findViewById(R.id.descTxt);
        detailImg = findViewById(R.id.detailImg);
        collapsingToolbar = findViewById(R.id.collapsing_bar);
        callLayout = findViewById(R.id.callLayout);
        directionLayout = findViewById(R.id.directionLayout);
        galleryRv=findViewById(R.id.galleryRv);
        galleryRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));


        email = getIntent().getStringExtra("email");
        name = getIntent().getStringExtra("name");
        phone = "tel:" + getIntent().getStringExtra("phone");
        lat=getIntent().getDoubleExtra("lat",0);
        lon=getIntent().getDoubleExtra("lon",0);
        collapsingToolbar.setTitle(getIntent().getStringExtra("name"));
        Glide.with(this).load(getIntent().getStringExtra("image")).into(detailImg);
        descTxt.setText(getIntent().getStringExtra("desc"));



        db.collection("Images").whereEqualTo("name",name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                galleryList.add(document.getString("image"));

                            }

                            adapter=new MenuAdapter(RestaurantDetailActivity.this,galleryList);
                            galleryRv.setAdapter(adapter);
                        }
                    }
                });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareSheetFragment fragment = ShareSheetFragment.newInstance();
                fragment.show(getSupportFragmentManager(), "share_sheet_fragment");
            }
        });

        callLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(phone));


                if (ActivityCompat.checkSelfPermission(RestaurantDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(RestaurantDetailActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            1);
                }else{
                   startActivity(callIntent);
                }

            }
        });

        directionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri.Builder directions = new Uri.Builder()
                        .scheme("https")
                        .authority("www.google.com")
                        .appendPath("maps")
                        .appendPath("dir")
                        .appendPath("")
                        .appendQueryParameter("api", "1")
                        .appendQueryParameter("destination", lat + "," + lon);

                Intent dirIntent=new Intent(Intent.ACTION_VIEW,directions.build());
                dirIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dirIntent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        isPermissionGiven=true;
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                }else {
                    isPermissionGiven=false;
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
