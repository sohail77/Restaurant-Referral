package com.sohail.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    MaterialButton scanBtn, uploadBtn;
    TextView txt;
    ArrayList<CouponVerificationModel> list = new ArrayList<>();
    private FirebaseAuth mAuth;
    String name;
    String pictureFilePath;
    Uri uri;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanBtn = findViewById(R.id.scanBtn);
        txt = findViewById(R.id.txt);
        uploadBtn = findViewById(R.id.uploadBtn);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        txt.setText("Scan to validate");


        db.collection("RestaurantAdmins").whereEqualTo("email", mAuth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                name = document.getString("name");
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

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                } else {
                    openCamera();
                }
            }
        });
    }


    private void openCamera() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File file = null;
            try {
                file = getImageFromFile();
            } catch (IOException ex) {
                Toast.makeText(this,
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (file != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.sohail.myapplication.fileprovider",
                        file);


                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, 2);
        }
    }

}
    private File getImageFromFile() throws IOException {
        Random random = new Random();
        int key = random.nextInt(10000);
        String filename = name + key;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(filename,  ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        list = new ArrayList<>();
        if (requestCode == 2 && resultCode == RESULT_OK) {

            File img = new  File(pictureFilePath);
            if(img.exists())
            {
                uploadImg(Uri.fromFile(img));
            }
        }

        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {

                db.collection("Coupons").whereEqualTo("isUsed",false)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        String[] parts = document.getString("code").split("/");
                                        Log.e("Hellooooooooo",document.getId());
                                        String part1 = parts[0];
                                        String part2 = parts[1];
                                        if (part1.equals(mAuth.getCurrentUser().getEmail())) {
                                            CouponVerificationModel model=new CouponVerificationModel();
                                            model.code=document.getString("code");
                                            model.id=document.getId();
                                            list.add(model);
                                        }
                                    }
                                    boolean isValid=false;
                                    for(int i=0;i<list.size();i++){
                                        if(list.get(i).code.equals(result.getContents())){
                                            HashMap<Object,Boolean> map=new HashMap<>();
                                            map.put("isUsed",true);
                                            db.collection("Coupons").document(list.get(i).id)
                                                    .set(map,SetOptions.merge());
                                            txt.setText("Verified");
                                            Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                                            isValid=true;
                                            return;
                                        }
                                    }

                                    if(!isValid){
                                         txt.setText("Invalid Coupon");
                                    }

                                } else {
                                    Log.e("Not hello", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        }


    }




    public void uploadImg(Uri uri) {
        final StorageReference uploadRef = mStorageRef.child("images").child(uri.getLastPathSegment());
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        uploadRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploadRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;

                                HashMap<String,Object> map=new HashMap<>();
                                map.put("image",downloadUrl.toString());
                                map.put("name",name);
                                db.collection("Images")
                                        .add(map)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                progressDialog.dismiss();
                                                Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(MainActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

//                                Toast.makeText(getBaseContext(), "Upload success! URL - " + downloadUrl.toString() , Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

}

