package com.sohail.restaurant_referral;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sohail.restaurant_referral.Adapters.RestaurantListAdapter;
import com.sohail.restaurant_referral.Models.RestaurantModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageView logoutBtn,shareBtn;
    private FirebaseAuth mAuth;
    RecyclerView rv;
    ArrayList<RestaurantModel> list=new ArrayList<>();

    RestaurantListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logoutBtn=findViewById(R.id.logout);
        shareBtn=findViewById(R.id.share);
        rv=findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();

        db.collection("Restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                RestaurantModel model=new RestaurantModel();
                                model.setName(document.getString("name"));
                                model.setDescription(document.getString("description"));
                                model.setImageUrl(document.getString("image"));
                                model.setEmail(document.getString("email"));
                                model.setPhone(document.getString("phone"));
                                GeoPoint g=document.getGeoPoint("location");
                                model.setLat(g.getLatitude());
                                model.setLon(g.getLongitude());

                                list.add(model);
                                list.add(model);
                                adapter=new RestaurantListAdapter(MainActivity.this,list);
                                rv.setAdapter(adapter);

                                Log.e("Hello", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.e("Not hello", "Error getting documents.", task.getException());
                        }
                    }
                });


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent i =new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, CouponsActivity.class);
                startActivity(i);
            }
        });
    }
}
