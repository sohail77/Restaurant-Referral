package com.sohail.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    EditText loginEdit,passwordEdit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button loginBtn;
    String resValidator;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEdit=findViewById(R.id.loginEmail);
        passwordEdit=findViewById(R.id.loginPass);
        loginBtn=findViewById(R.id.loginBtn);
        mAuth = FirebaseAuth.getInstance();










        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String  email=loginEdit.getText().toString();
                final String  password=passwordEdit.getText().toString();



                if((email.length()!=0) && (password.length()!=0)) {

                    db.collection("RestaurantAdmins").whereEqualTo("email",email)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            resValidator=document.getString("email");
                                            Log.e("Hello", document.getId() + " => " + document.getData());
                                        }

                                        if(resValidator!=null) {
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
                                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Log.e("Not hello", "Error getting documents.", task.getException());
                                    }
                                }
                            });


                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
            updateUI();
    }

    public void updateUI(){
        Intent i= new Intent(LoginActivity.this,MainActivity.class);
        startActivity(i);
    }
}