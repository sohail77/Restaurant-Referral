package com.sohail.restaurant_referral;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sohail.restaurant_referral.Adapters.RestaurantListAdapter;
import com.sohail.restaurant_referral.Models.RestaurantModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageView logoutBtn,shareBtn;
    LinearLayout couponLayout,logoutLayout;
    private FirebaseAuth mAuth;
    RecyclerView rv;
    TextView noConnectionTxt;
    MaterialButton noConnectionBtn;
    ArrayList<RestaurantModel> list=new ArrayList<>();

    RestaurantListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up the xml ui elements
        logoutBtn=findViewById(R.id.logout);
        shareBtn=findViewById(R.id.share);
        rv=findViewById(R.id.rv);
        couponLayout=findViewById(R.id.couponLayout);
        logoutLayout=findViewById(R.id.logoutLayout);
        noConnectionTxt=findViewById(R.id.noConnectionTxt);
        noConnectionBtn=findViewById(R.id.noConnectionBtn);
        rv.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();

        //load data if there is internet connection else hide the ui elements
        if(isInternetAvailable()){
            getData();
        }else{
            hideViews();
        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //show an alert dialog box when the logout button is clicked
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("LOGOUT")
                        .setMessage("Are you sure you want to Logout ?")
                        // user presses logout and is taken to the sign in screen
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                                Intent i =new Intent(MainActivity.this,LoginActivity.class);
                                startActivity(i);
                                //clears the activity stack
                                finish();
                            }
                        })
                        // closes the dialog box.
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });

        //open coupon activity
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, CouponsActivity.class);
                startActivity(i);
            }
        });

        noConnectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when retry button is clicked check if there is internet and if true
                if(isInternetAvailable()){
                    //check if user is logged in
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser == null)
                        //take him to sign in screen if not logged in.
                        updateUI();
                    //if user is logged in then get the data.
                    getData();
                    //show the views
                    showViews();
                }else{

                    //if no internet then display a toast message.
                    Toast.makeText(MainActivity.this,"Can't connect Retry later",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //if internet available check if user is logged in or not
        if(isInternetAvailable()) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null)
                //if not logged in take the user to login screen
                updateUI();
        }else{
            hideViews();
        }
    }

    //this function takes the user to login screen.
    public void updateUI(){
        Intent i= new Intent(MainActivity.this,LoginActivity.class);
        startActivity(i);
        //clear the Activity stack so that back button doesn't bring the user back to this activity
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //check if there is internet connection or not. If there is connection then return true else return false;
    private boolean isInternetAvailable() {
        ConnectivityManager manager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if(info != null && info.isConnected()){
            return true;
        }
        else {
            return false;
        }
    }

    //hide all the views and show the no connection view
    public void hideViews(){
        logoutBtn.setVisibility(View.GONE);
        shareBtn.setVisibility(View.GONE);
        rv.setVisibility(View.GONE);
        couponLayout.setVisibility(View.GONE);
        logoutLayout.setVisibility(View.GONE);
        noConnectionTxt.setVisibility(View.VISIBLE);
        noConnectionBtn.setVisibility(View.VISIBLE);
    }

    //show all the views and hide no connection view.
    public void showViews(){
        logoutBtn.setVisibility(View.VISIBLE);
        shareBtn.setVisibility(View.VISIBLE);
        rv.setVisibility(View.VISIBLE);
        couponLayout.setVisibility(View.VISIBLE);
        logoutLayout.setVisibility(View.VISIBLE);
        noConnectionTxt.setVisibility(View.GONE);
        noConnectionBtn.setVisibility(View.GONE);
    }

    //This method fetches the data from the Firebase and add it to a recycler view.
    public void getData(){

        if(list.size()>0) {
            list=new ArrayList<>();
        }
        //collection name is Restaurants
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
                                //populate the recycler view adapter with the data
                                adapter=new RestaurantListAdapter(MainActivity.this,list);
                                rv.setAdapter(adapter);

                                Log.e("Hello", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.e("Not hello", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
