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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText loginEdit,passwordEdit;
    MaterialButton loginBtn;
    private FirebaseAuth mAuth;
    TextView signUpTxt;
    ImageView coverImg;
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set up the xml views
        setContentView(R.layout.activity_login);
        loginEdit=findViewById(R.id.loginEmail);
        passwordEdit=findViewById(R.id.loginPass);
        loginBtn=findViewById(R.id.loginBtn);
        signUpTxt=findViewById(R.id.newUserTxt);
        mAuth = FirebaseAuth.getInstance();
        coverImg=findViewById(R.id.coverImg);
        loader=findViewById(R.id.loader);

        //glide loads images asynchronously
        Glide.with(this).load(R.drawable.cover_img).into(coverImg);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //display the loader
                loader.setVisibility(View.VISIBLE);

                //get email and password value from the text fields
                String email=loginEdit.getText().toString();
                String password=passwordEdit.getText().toString();

                if(!isInternetAvailable()){
                    loader.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Check your internet connection.",
                            Toast.LENGTH_SHORT).show();
                }else {
                    //if email and password are not empty
                    if ((email.length() != 0) && (password.length() != 0)) {
                        //sign in using Firebase authentication
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            loader.setVisibility(View.GONE);
                                            // Sign in success, update UI with the signed-in user's information
                                            Toast.makeText(LoginActivity.this, "Authentication Successful",
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
                        //if email or password is empty
                        Toast.makeText(LoginActivity.this, "Please fill in all the fields",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //take the user to SignUp activity
        signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    //take the user to main Activity
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
