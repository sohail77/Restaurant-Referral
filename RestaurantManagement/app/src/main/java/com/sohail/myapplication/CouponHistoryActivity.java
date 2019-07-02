package com.sohail.myapplication;

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
import com.sohail.myapplication.Adapters.CouponAdpater;
import com.sohail.myapplication.Model.CouponModel;
import com.sohail.myapplication.R;

import java.util.ArrayList;

public class CouponHistoryActivity extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    String code;
    RecyclerView couponRv;
    CouponAdpater adpater;
    ArrayList<CouponModel> list=new ArrayList<>();
    ImageView backImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_history);

        couponRv=findViewById(R.id.couponRv);
        backImg=findViewById(R.id.backImg);
        couponRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mAuth = FirebaseAuth.getInstance();


        db.collection("Coupons").whereEqualTo("isUsed",true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            list=new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String[] parts = document.getString("code").split("/");
                                String part1 = parts[0];
                                String part2 = parts[1];
                                if (part1.equals(mAuth.getCurrentUser().getEmail())) {
                                    CouponModel model=new CouponModel();
                                    model.setCode(document.getString("code"));
                                    model.setFromEmail(document.getString("fromEmail"));
                                    model.setToEmail(document.getString("toEmail"));
                                    model.setPlace(document.getString("place"));
                                    list.add(model);

                                }
                            }

                            adpater=new CouponAdpater(CouponHistoryActivity.this,list);
                            couponRv.setAdapter(adpater);


                        } else {
                            Log.e("Not hello", "Error getting documents.", task.getException());
                        }
                    }
                });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
