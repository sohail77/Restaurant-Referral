package com.sohail.restaurant_referral;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText loginEdit,passwordEdit;
    MaterialButton loginBtn;
    private FirebaseAuth mAuth;
    TextView loginTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEdit=findViewById(R.id.loginEmail);
        passwordEdit=findViewById(R.id.loginPass);
        loginBtn=findViewById(R.id.loginBtn);
        loginTxt=findViewById(R.id.newUserTxt);
        mAuth = FirebaseAuth.getInstance();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=loginEdit.getText().toString();
                String password=passwordEdit.getText().toString();
                if((email.length()!=0) && (password.length()!=0)) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("hello", "signInWithEmail:success");
                                        Toast.makeText(LoginActivity.this, "Authentication Success.",
                                                Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Hello", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
//                                        updateUI(null);
                                    }

                                    // ...
                                }
                            });
                }else{
                    Toast.makeText(LoginActivity.this, "Please fill in all the fields",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    public void updateUI(){
        Intent i= new Intent(LoginActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}
