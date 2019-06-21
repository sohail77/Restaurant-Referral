package com.sohail.restaurant_referral.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sohail.restaurant_referral.Adapters.RestaurantListAdapter;
import com.sohail.restaurant_referral.MainActivity;
import com.sohail.restaurant_referral.Models.RestaurantModel;
import com.sohail.restaurant_referral.R;
import com.sohail.restaurant_referral.RestaurantDetailActivity;

import java.util.HashMap;


public class ShareSheetFragment extends BottomSheetDialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText shareEmail;
    MaterialButton shareBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore db2 = FirebaseFirestore.getInstance();
    RestaurantDetailActivity act;
    String email;
    boolean isAvailable=false;

    private FirebaseAuth mAuth;

    private OnFragmentInteractionListener mListener;

    public ShareSheetFragment() {
        // Required empty public constructor
    }

    public static ShareSheetFragment newInstance(){
        return new ShareSheetFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_share_sheet, container, false);
        shareEmail=view.findViewById(R.id.shareEmail);
        shareBtn=view.findViewById(R.id.sendBtn);
        mAuth = FirebaseAuth.getInstance();
        act=(RestaurantDetailActivity)getActivity();
        email=act.email;


        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAvailable=false;
                if((shareEmail.getText()!=null) && (!shareEmail.getText().toString().equals(mAuth.getCurrentUser().getEmail()))){
                    db.collection("Users")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                            if(document.getString("email").equals(shareEmail.getText().toString())){
                                                isAvailable=true;
                                            }
                                        }

                                        if(isAvailable) {
                                            HashMap<String, Object> coupon = new HashMap<>();
                                            coupon.put("code", email + "/" + mAuth.getCurrentUser().getEmail());
                                            coupon.put("fromEmail", mAuth.getCurrentUser().getEmail());
                                            coupon.put("toEmail", shareEmail.getText().toString());
                                            coupon.put("place",act.name);
                                            db2.collection("Coupons")
                                                    .add(coupon)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Toast.makeText(getActivity(), "Sharing Successful", Toast.LENGTH_LONG).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getActivity(), "Error Sharing Coupon", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                        }else{
                                            Toast.makeText(getActivity(), "No Such User Available ", Toast.LENGTH_LONG).show();
                                        }

                                    } else {
                                        Log.e("Not hello", "Error getting documents.", task.getException());
                                    }
                                }
                            });
                }
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
