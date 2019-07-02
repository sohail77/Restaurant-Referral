package com.sohail.myapplication.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.sohail.myapplication.Model.CouponModel;
import com.sohail.myapplication.R;


import java.util.ArrayList;

public class CouponAdpater extends RecyclerView.Adapter<CouponAdpater.ViewHolder> {

    Context context;
    ArrayList<CouponModel> list=new ArrayList<>();


    public CouponAdpater(Context context, ArrayList<CouponModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CouponAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.coupon_item,viewGroup,false);
        return new CouponAdpater.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponAdpater.ViewHolder viewHolder, int i) {

        viewHolder.usedByTxt.setText("Used By: " + list.get(i).getToEmail());

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(list.get(i).getCode(), BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            viewHolder.couponImg.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView usedByTxt;
        ImageView couponImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usedByTxt=itemView.findViewById(R.id.forTxt);
            couponImg=itemView.findViewById(R.id.couponImg);
        }
    }
}
