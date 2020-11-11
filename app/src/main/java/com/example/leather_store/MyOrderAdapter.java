package com.example.leather_store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyOrderAdapter extends BaseAdapter {


    List<Final_records1> list;
    private Context context;


    public MyOrderAdapter(List<Final_records1> list, Context context) {
        this.list = list;
        this.context = context;

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.layout_my_orders, parent, false);
        }


        TextView tv_name, tv_price, tv_date;
        ImageView iv;

        tv_name = convertView.findViewById(R.id.txt_product_name);
        tv_price = convertView.findViewById(R.id.txt_product_price);
        tv_date = convertView.findViewById(R.id.txt_date);

        iv = convertView.findViewById(R.id.img_product);

        Final_records1 records1 = (Final_records1) getItem(position);

        tv_name.setText(records1.getProductName());
        tv_price.setText("Rs. " + String.valueOf(records1.getProductPrice()));
        tv_date.setText(records1.getDate());

        Glide.with(context).load(Config.PROD_IMG_URL + records1.getProduct_img()).into(iv);


        return convertView;
    }
}




