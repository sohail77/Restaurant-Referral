package com.sohail.restaurant_referral;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.sohail.restaurant_referral.Adapters.CouponAdpater;
import com.sohail.restaurant_referral.Models.CouponModel;

import java.util.ArrayList;

public class CouponsActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    String code;
    RecyclerView couponRv;
    CouponAdpater adpater;
    ArrayList<CouponModel> list=new ArrayList<>();
    ImageView historyImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        couponRv=findViewById(R.id.couponRv);
        historyImg=findViewById(R.id.historyImg);
        couponRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mAuth = FirebaseAuth.getInstance();


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
                                list.add(model);

                                Log.e("data", document.getId() + " => " + document.getString("code"));
                            }

                            adpater=new CouponAdpater(CouponsActivity.this,list);
                            couponRv.setAdapter(adpater);


                        } else {
                            Log.e("Not hello", "Error getting documents.", task.getException());
                        }
                    }
                });

        historyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CouponsActivity.this, CouponHistoryActivity.class);
                startActivity(i);
            }
        });

    }
}
