package com.sohail.restaurant_referral;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        signUpBtn=findViewById(R.id.SignUpBtn);
        email=findViewById(R.id.loginEmail);
        pass=findViewById(R.id.loginPass);
        loginTxt=findViewById(R.id.loginTxt);
        name=findViewById(R.id.nameSignUp);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String emailTxt=email.getText().toString();
                String passTxt=email.getText().toString();
                final String nameTxt=name.getText().toString();

                if(!emailTxt.equals("") && !passTxt.equals("") && !nameTxt.equals("")){
                    mAuth.createUserWithEmailAndPassword(emailTxt, passTxt)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
//                                        Log.d(TAG, "createUserWithEmail:success");
//                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(SignUpActivity.this, "Account created.",
                                                Toast.LENGTH_SHORT).show();

                                        HashMap<Object,String> map=new HashMap<>();
                                        map.put("email",emailTxt);
                                        map.put("username",nameTxt);
                                        db.collection("Users")
                                                .add(map)
                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        updateUI();

                                                    }
                                                });


                                    } else {
                                        // If sign in fails, display a message to the user.
//                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(SignUpActivity.this, "Please fill in the fields",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

    public void updateUI(){
        Intent i= new Intent(SignUpActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}
