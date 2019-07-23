package com.sohail.restaurant_referral;

import android.content.Intent;
import android.opengl.Visibility;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sohail.restaurant_referral.Adapters.CouponAdpater;
import com.sohail.restaurant_referral.Models.CouponModel;

import java.util.ArrayList;

public class CouponsActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    RecyclerView couponRv;
    CouponAdpater adpater;
    ArrayList<CouponModel> list=new ArrayList<>();
    ImageView backImg;
    MaterialButton historyImg;
    TextView emptyTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set up the xml views
        setContentView(R.layout.activity_coupon);
        couponRv=findViewById(R.id.couponRv);
        historyImg=findViewById(R.id.historyImg);
        backImg=findViewById(R.id.backImg);
        emptyTxt=findViewById(R.id.emptyTxt);
        couponRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        //get user's instance from the firebase
        mAuth = FirebaseAuth.getInstance();

        //get coupons from the firebase
        getData();

        //take the user to the history screen
        historyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CouponsActivity.this, CouponHistoryActivity.class);
                startActivity(i);
            }
        });

        //go back to previous activity
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void getData(){

        // access the collection "coupons" in the firebase database and get only those items where email address is of this user.
        //and also only those items that are not used.
        db.collection("Coupons").whereEqualTo("toEmail",mAuth.getCurrentUser().getEmail()).whereEqualTo("isUsed",false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            list=new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CouponModel model=new CouponModel();
                                model.setCode(document.getString("code"));
                                model.setFromEmail(document.getString("fromEmail"));
                                model.setToEmail(document.getString("toEmail"));
                                model.setPlace(document.getString("place"));
                                //add all the coupons to a list.
                                list.add(model);

                                Log.e("data", document.getId() + " => " + document.getString("code"));
                            }

                            //set the adapter using that list
                            adpater=new CouponAdpater(CouponsActivity.this,list);
                            couponRv.setAdapter(adpater);

                            //if there are no coupons then display a message saying no coupons
                            if(list.size()==0){
                                couponRv.setVisibility(View.GONE);
                                emptyTxt.setVisibility(View.VISIBLE);
                            }else{
                                if(couponRv.getVisibility()== View.GONE){
                                    couponRv.setVisibility(View.VISIBLE);

                                }
                                if(emptyTxt.getVisibility()==View.VISIBLE){
                                    emptyTxt.setVisibility(View.GONE);
                                }
                            }



                        } else {
                            Log.e("Not hello", "Error getting documents.", task.getException());
                        }
                    }
                });


    }
}
