package com.sohail.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button scanBtn;
    TextView txt;
    ArrayList<String> list=new ArrayList<>();
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanBtn=findViewById(R.id.scanBtn);
        txt=findViewById(R.id.txt);
        mAuth = FirebaseAuth.getInstance();

        txt.setText("Scan to validate");


        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("Hello", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.e("Not hello", "Error getting documents.", task.getException());
                        }
                    }
                });

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(MainActivity.this).initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        list=new ArrayList<>();
        final IntentResult result =   IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this,    "Cancelled",Toast.LENGTH_LONG).show();
            } else {

                db.collection("Coupons")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        String[] parts = document.getString("code").split("/");
                                        String part1 = parts[0]; // 004
                                        String part2 = parts[1];
                                        if(part1.equals(mAuth.getCurrentUser().getEmail())){
                                            list.add(document.getString("code"));
                                        }
                                    }
                                    if(list.contains(result.getContents())){
                                        txt.setText("Verified");
                                        Toast.makeText(MainActivity.this,    result.getContents(),Toast.LENGTH_LONG).show();

                                    }else{
                                        txt.setText("Invalid Coupon");
                                    }
                                } else {
                                    Log.e("Not hello", "Error getting documents.", task.getException());
                                }
                            }
                        });

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
