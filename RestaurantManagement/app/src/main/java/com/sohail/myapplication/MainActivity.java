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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    MaterialButton scanBtn, uploadBtn,historyBtn;
    TextView txt;
    ArrayList<CouponVerificationModel> list = new ArrayList<>();
    private FirebaseAuth mAuth;
    String name;
    String pictureFilePath;
    ImageView logoutBtn;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting up the xml views
        setContentView(R.layout.activity_main);
        scanBtn = findViewById(R.id.scanBtn);
        txt = findViewById(R.id.txt);
        uploadBtn = findViewById(R.id.uploadBtn);
        historyBtn=findViewById(R.id.historyBtn);
        logoutBtn=findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();

        //getting a reference to firebase storage
        mStorageRef = FirebaseStorage.getInstance().getReference();

        Glide.with(this).load(R.drawable.logout_img).into(logoutBtn);
        txt.setText("Scan to validate");

        //this code gets the name of the restaurant from firebase
        if(mAuth.getCurrentUser()!=null) {
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
        }

        //open the QR code scanner using ZXING library
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(MainActivity.this).initiateScan();
            }
        });

        //open the camera
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check for camera permissions
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                } else {
                    openCamera();
                }
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,CouponHistoryActivity.class);
                startActivity(i);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent i =new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null)
            updateUI();
    }

    //if user is not logged in then switch to login screen
    public void updateUI(){
        Intent i= new Intent(MainActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
    }

    //this method opens the camera and reads image
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

    //this code creates a jpg file and stores it in the device's storage
    private File getImageFromFile() throws IOException {
        Random random = new Random();
        int key = random.nextInt(10000);
        String filename = name + key;
        //get access to the file directory
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        //create a JPG file
        File image = File.createTempFile(filename,  ".jpg", storageDir);

        //get the path of that file
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    //this is called after image is captured or QR code is scanned
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        list = new ArrayList<>();
        if (requestCode == 2 && resultCode == RESULT_OK) {

            //if image is captured then upload the image
            File img = new  File(pictureFilePath);
            if(img.exists())
            {
                uploadImg(Uri.fromFile(img));
            }
        }

        //this is called after the QR code is scanned
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {

                // this code checks if the coupon is valid or not
                db.collection("Coupons").whereEqualTo("isUsed",false)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        //get only the coupon for this restaurant and add them to  a list.
                                        String[] parts = document.getString("code").split("/");
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

                                    // make the changes in the database saying that this coupon is used.
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
                                    Toast.makeText(MainActivity.this, "Error scanning the code", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        }


    }



    // this method uploads the image to firebase database
    public void uploadImg(Uri uri) {
        final StorageReference uploadRef = mStorageRef.child("images").child(uri.getLastPathSegment());

        //create a dialog box
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        //upload the photo to firebaseStorage
        uploadRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploadRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;

                                //after uploading get the url of the image and store its details in the firebase database
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
                    openCamera();

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

}

