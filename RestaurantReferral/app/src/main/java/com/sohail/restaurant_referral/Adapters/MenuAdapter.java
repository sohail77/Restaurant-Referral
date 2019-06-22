package com.sohail.restaurant_referral.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sohail.restaurant_referral.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    Context context;
    ArrayList<String> list=new ArrayList<>();

    public MenuAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_item,viewGroup,false);
        return new MenuAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ViewHolder viewHolder, int i) {
        Glide.with(context).load(list.get(i)).into(viewHolder.menuImg);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView menuImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            menuImg=itemView.findViewById(R.id.menuImg);
        }
    }
}
