package com.sohail.restaurant_referral;

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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    MaterialButton signUpBtn;
    TextInputEditText email,pass,name;
    TextView loginTxt;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageView coverImg;
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting up the xml views
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        signUpBtn=findViewById(R.id.SignUpBtn);
        email=findViewById(R.id.loginEmail);
        pass=findViewById(R.id.loginPass);
        loginTxt=findViewById(R.id.loginTxt);
        name=findViewById(R.id.nameSignUp);
        coverImg=findViewById(R.id.coverImg);
        loader=findViewById(R.id.loader);

        //Glide loads images asynchronously
        Glide.with(this).load(R.drawable.cover_img).into(coverImg);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //display the loader
                loader.setVisibility(View.VISIBLE);

                //get the values from the text fields
                final String emailTxt=email.getText().toString();
                String passTxt=pass.getText().toString();
                final String nameTxt=name.getText().toString();

                if(!isInternetAvailable()){
                    loader.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Check your internet connection.",
                            Toast.LENGTH_SHORT).show();
                }else {
                    //if email or password or name not empty
                    if (!emailTxt.equals("") && !passTxt.equals("") && !nameTxt.equals("")) {
                        //create user with email
                        mAuth.createUserWithEmailAndPassword(emailTxt, passTxt)
                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            // Sign Up success, update UI with the signed-in user's information
                                            Toast.makeText(SignUpActivity.this, "Account created.",
                                                    Toast.LENGTH_SHORT).show();

                                            HashMap<Object, String> map = new HashMap<>();
                                            map.put("email", emailTxt);
                                            map.put("username", nameTxt);

                                            //add new users data to the database
                                            db.collection("Users")
                                                    .add(map)
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                            loader.setVisibility(View.GONE);
                                                            updateUI();

                                                        }
                                                    });
                                        } else {
                                            loader.setVisibility(View.GONE);
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {

                        loader.setVisibility(View.GONE);
                        //if input fields are empty
                        Toast.makeText(SignUpActivity.this, "Please fill in the fields",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //take the user to login screen.
        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

    //take the user to the main activity
    public void updateUI(){
        Intent i= new Intent(SignUpActivity.this,MainActivity.class);
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
