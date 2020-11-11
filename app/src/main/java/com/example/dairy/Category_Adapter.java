package com.example.dairy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.ViewHolder> {


    List<Final_records1> list;
    private Context context;
    SharedPreferences preferences;
    String UserId;

//    int product_id;
//    int product_price;


    public Category_Adapter(List<Final_records1> list, Context context) {
        this.list = list;
        this.context = context;

        preferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        UserId = preferences.getString("user_id", null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final Final_records1 records1 = list.get(i);


        viewHolder.cat_name.setText(records1.getCat_name());
        viewHolder.cat_id.setText(String.valueOf(records1.getCat_id()));
        Glide.with(context).load(Config.CAT_IMG_URL + records1.getCat_image()).into(viewHolder.img_cat);

        Log.d("CATEGORY IMG ===== ", Config.CAT_IMG_URL + records1.getCat_image());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Main2Activity.class);
                intent.putExtra("cat_id", records1.getCat_id());
                intent.putExtra("cat_name", records1.getCat_name());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView cat_name, cat_id;
        ImageView img_cat;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cat_name = itemView.findViewById(R.id.txt_cat_name);
            cat_id = itemView.findViewById(R.id.txt_cat_id);
            img_cat = itemView.findViewById(R.id.img_cat);

        }


    }


}




