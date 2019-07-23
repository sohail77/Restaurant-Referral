package com.sohail.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText loginEdit,passwordEdit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    MaterialButton loginBtn;
    String resValidator;
    private FirebaseAuth mAuth;
    ImageView coverImg;
    ProgressBar loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting up the xml elements
        setContentView(R.layout.activity_login);
        loginEdit=findViewById(R.id.loginEmail);
        passwordEdit=findViewById(R.id.loginPass);
        loginBtn=findViewById(R.id.loginBtn);
        mAuth = FirebaseAuth.getInstance();
        loader=findViewById(R.id.loader);
        coverImg=findViewById(R.id.coverImg);

        Glide.with(this).load(R.drawable.cover_img).into(coverImg);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //display the loader
                loader.setVisibility(View.VISIBLE);

                //get the data from the edit text fields
                final String  email=loginEdit.getText().toString();
                final String  password=passwordEdit.getText().toString();

                if(!isInternetAvailable()){
                    loader.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Check your internet connection.",
                            Toast.LENGTH_SHORT).show();
                }else {

                    //check if all the edit texts are filled or not
                    if ((email.length() != 0) && (password.length() != 0)) {

                        //check if the email address is  actually an email address of a restaurant
                        db.collection("RestaurantAdmins").whereEqualTo("email", email)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                resValidator = document.getString("email");
                                            }

                                            //if the email is present then login the user
                                            if (resValidator != null) {
                                                mAuth.signInWithEmailAndPassword(email, password)
                                                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                if (task.isSuccessful()) {
                                                                    loader.setVisibility(View.GONE);
                                                                    // Sign in success, update UI with the signed-in user's information
                                                                    Toast.makeText(LoginActivity.this, "Authentication Success.",
                                                                            Toast.LENGTH_SHORT).show();
                                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                                    updateUI();
                                                                } else {
                                                                    loader.setVisibility(View.GONE);
                                                                    // If sign in fails, display a message to the user.
                                                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        });
                                            } else {
                                                loader.setVisibility(View.GONE);
                                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });


                    } else {
                        loader.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Please Fill in all the fields",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }



    //on successful login take the user to the Main activity
    public void updateUI(){
        Intent i= new Intent(LoginActivity.this,MainActivity.class);
        startActivity(i);
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

}