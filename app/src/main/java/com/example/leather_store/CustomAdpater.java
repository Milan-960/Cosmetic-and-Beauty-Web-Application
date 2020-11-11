package com.example.leather_store;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomAdpater extends RecyclerView.Adapter<CustomAdpater.ViewHolder> {


    List<Final_records1> list;
    private Context context;
    SharedPreferences preferences;
    String UserId;

//    int product_id;
//    int product_price;


    public CustomAdpater(List<Final_records1> list, Context context) {
        this.list = list;
        this.context = context;

        preferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        UserId = preferences.getString("user_id", null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.product_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final Final_records1 records1 = list.get(i);


        viewHolder.product_name.setText(records1.getProductName());
        viewHolder.product_price.setText("Rs. " + String.valueOf(records1.getProductPrice()));


        Glide.with(context).load(Config.PROD_IMG_URL + records1.getProduct_img()).into(viewHolder.img_product);

        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(context,"Item Added Successfully",Toast.LENGTH_LONG).show();
                insertCart(records1.getProductId(), records1.getProductPrice(), records1.getCat_id(), UserId);

            }
        });


//        product_id = list.get(i).getProductId();


//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                counter++;
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView product_name, product_price;
        Button add;
        ImageView img_product;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.txt_product_name);
            product_price = itemView.findViewById(R.id.txt_product_price);
            add = itemView.findViewById(R.id.add);
            img_product = itemView.findViewById(R.id.img_product);


        }


    }


    private void insertCart(int productId, int product_price, String catId, String userId) {

        String url = Api.URL_GET_CART;

        Call<Final_Model1> call = Api.getPostService().cart(url, productId, product_price, catId, userId);

        call.enqueue(new Callback<Final_Model1>() {
            @Override
            public void onResponse(Call<Final_Model1> call, Response<Final_Model1> response) {


                if (!response.body().getError()) {


                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();


                    if (context instanceof Main2Activity) {
                        ((Main2Activity) context).cart(UserId);
//                    ((Activity) context).invalidateOptionsMenu();
                    }

                } else {

                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Final_Model1> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Errrror", t.getMessage());

            }
        });


    }


}

