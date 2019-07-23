package com.sohail.restaurant_referral.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sohail.restaurant_referral.Models.RestaurantModel;
import com.sohail.restaurant_referral.R;
import com.sohail.restaurant_referral.RestaurantDetailActivity;
import java.util.ArrayList;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

    Context context;

    //This list will contain the data regarding the restaurant
    ArrayList<RestaurantModel> list=new ArrayList<>();


    public RestaurantListAdapter(Context context, ArrayList<RestaurantModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RestaurantListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.restaurant_item,viewGroup,false);
        return new RestaurantListAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final RestaurantListAdapter.ViewHolder viewHolder, int i) {

        //load the images using Glide library
        Glide.with(context).load(list.get(i).getImageUrl()).into(viewHolder.restImg);

        //get the name of the restaurant from the list
        viewHolder.restTxt.setText(list.get(i).getName());

        //when an item is clicked send the data to the detailActivity
        viewHolder.restTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, RestaurantDetailActivity.class);
                int pos=viewHolder.getAdapterPosition();
                intent.putExtra("name",list.get(pos).getName());
                intent.putExtra("desc",list.get(pos).getDescription());
                intent.putExtra("image",list.get(pos).getImageUrl());
                intent.putExtra("lat",list.get(pos).getLat());
                intent.putExtra("lon",list.get(pos).getLon());
                intent.putExtra("phone",list.get(pos).getPhone());
                intent.putExtra("email",list.get(pos).getEmail());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView restImg;
        TextView restTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //set up views of the xml
            restImg=itemView.findViewById(R.id.restImg);
            restTxt=itemView.findViewById(R.id.restName);
        }
    }
}
