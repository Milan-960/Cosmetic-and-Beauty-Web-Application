package com.example.dairy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView product_amount, tv_total;

    List<Final_records1> list = new ArrayList<>();
    CA adapter;
    Context context = Cart.this;
    //int total_amount = 0;
    //int prod_price = 0;

    int spin_value = 1;

    Toolbar toolbar;

    private ListView listView_cart;

    private SharedPreferences preferences;

    private int grand_value = 0;
    private String UserId;
//private ProgressDialog progressDialog;

    // CustomAdpater2 customAdpater2;


    private RelativeLayout cart_container, empty_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cart");


        cart_container = (RelativeLayout) findViewById(R.id.activity_cart_container);
        empty_container = (RelativeLayout) findViewById(R.id.activity_cart_empty);

        product_amount = (TextView) findViewById(R.id.amount_tv);
        tv_total = (TextView) findViewById(R.id.txt);
        product_amount.setText(String.valueOf(0));
        adapter = new CA(list, context);
   /*     recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);*/

        listView_cart = (ListView) findViewById(R.id.listview_data);
        listView_cart.setAdapter(adapter);


        sharedPref();
        getData(UserId);

    }

    private void sharedPref() {

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        UserId = preferences.getString("user_id", null);
    }

//    private void cal(List<Final_records1> list) {
//
//
//        for (int i = 0; i < list.size(); i++) {
//            total_amount += list.get(i).getProductPrice();
//        }
//
//        product_amount.setText(String.valueOf(total_amount));
//
//
//    }


    private void getData(String userId) {


        String url = Api.URL_GET_CART_DATA + userId;


        Call<Final_Model1> mtCall = Api.getPostService().getcartData(url);
        mtCall.enqueue(new Callback<Final_Model1>() {
            @Override
            public void onResponse(Call<Final_Model1> call, Response<Final_Model1> response) {


                if (!response.body().getError()) {
                    final Final_Model1 finalModel1 = response.body();
                    if (!finalModel1.getRecords().get(0).isError()) {
                        list.clear();
                        list.addAll(finalModel1.getRecords());
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, finalModel1.getRecords().get(0).getMessage(), Toast.LENGTH_LONG).show();
                        list.clear();
                        adapter.notifyDataSetChanged();
                        cart_container.setVisibility(View.GONE);
                        empty_container.setVisibility(View.VISIBLE);
                    }

                }

            }

            @Override
            public void onFailure(Call<Final_Model1> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                list.clear();
                adapter.notifyDataSetChanged();
//                    product_amount.setText(String.valueOf(0));

                cart_container.setVisibility(View.GONE);
                empty_container.setVisibility(View.VISIBLE);
            }
        });
    }

    public void proceed_to_pay(View view) {

        Intent intent = new Intent(context, Order.class);
        intent.putExtra("amount", grand_value);
        startActivity(intent);
        finish();
    }



    /*-------------------------------------------------------------------Adapter ---------------------------------------------------------------------*/


//    class CA extends RecyclerView.Adapter<CA.ViewHolder> {
//
//
//        ProgressDialog progressDialog;
//
//        List<Final_records1> list;
//        private Context context;
//        // String product_id;
//
//
//        public CA(List<Final_records1> list, Context context) {
//            this.list = list;
//            this.context = context;
//
//        }
//
//        @NonNull
//        @Override
//        public CA.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//
//
//            LayoutInflater inflater = LayoutInflater.from(context);
//            View view = inflater.inflate(R.layout.cart_data, viewGroup, false);
//
//            return new CA.ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull final CA.ViewHolder viewHolder, int i) {
//
//
//            final Final_records1 records1 = list.get(i);
//
//            viewHolder.product_name.setText(records1.getProductName());
//
//            viewHolder.spin.setSelection(records1.getQty() - 1);
//
//
//            //prod_price = records1.getProductPrice();
//            viewHolder.product_price.setText("Rs. "+records1.getProductPrice());
//            // product_amount.setText(String.valueOf(prod_price));
//            // product_id = String.valueOf(records1.getProductId());
//
//            Glide.with(context).load(records1.getProduct_img()).into(viewHolder.img_product);
//
//            viewHolder.remove.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Toast.makeText(context, "Item " + records1.getProductId(), Toast.LENGTH_LONG).show();
//
//                    removeCart(records1.getProductId(), UserId);
//                    //viewHolder.prod_qty.setText("1");
//
//
//                }
//            });
//
//
//            product_amount.setText("Rs. "+String.valueOf(records1.getGrandTotal()));
//
//            viewHolder.spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//
//                    switch (i) {
//
//                        case 0:
//
//                            spin_value = 1;
//
//     progressDialog = ProgressDialog.show(context,"Please Wait","",false,false);
//                            //Toast.makeText(context, String.valueOf(spin_value), Toast.LENGTH_LONG).show();
//
//                            updateCart(spin_value, records1.getProductPrice(), records1.getProductId(), records1.getUserId());
//
//progressDialog.dismiss();
//                            break;
//
//
//                        case 1:
//                            spin_value = 2;
//              progressDialog = ProgressDialog.show(context,"Please Wait","",false,false);
//
//                            //Toast.makeText(context, String.valueOf(spin_value), Toast.LENGTH_LONG).show();
//                            updateCart(spin_value, records1.getProductPrice(), records1.getProductId(), records1.getUserId());
//progressDialog.dismiss();
//                            break;
//
//                        case 2:
//                            spin_value = 3;
//    progressDialog = ProgressDialog.show(context,"Please Wait","",false,false);
//
//                            //Toast.makeText(context, String.valueOf(spin_value), Toast.LENGTH_LONG).show();
//                            updateCart(spin_value, records1.getProductPrice(), records1.getProductId(), records1.getUserId());
//                 progressDialog.dismiss();
//                            break;
//
//                        case 3:
//                            spin_value = 4;
//                            progressDialog = ProgressDialog.show(context,"Please Wait","",false,false);
//
//                            //Toast.makeText(context, String.valueOf(spin_value), Toast.LENGTH_LONG).show();
//                            updateCart(spin_value, records1.getProductPrice(), records1.getProductId(), records1.getUserId());
//                        progressDialog.dismiss();
//                            break;
//
//
//                        default:
//                            spin_value = 1;
//                            progressDialog = ProgressDialog.show(context,"Please Wait","",false,false);
//
//                            // Toast.makeText(context, "Default " + String.valueOf(spin_value), Toast.LENGTH_LONG).show();
//                            updateCart(spin_value, records1.getProductPrice(), records1.getProductId(), records1.getUserId());
//                          progressDialog.dismiss();
//                            break;
//
//                    }
//
//
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
//
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return list.size();
//        }
//
//        class ViewHolder extends RecyclerView.ViewHolder {
//
//            TextView product_name, product_price;
//            Button remove;
//            Spinner spin;
//            ImageView img_product;
//
//            public ViewHolder(@NonNull View itemView) {
//                super(itemView);
//
//                product_name = itemView.findViewById(R.id.txt_product_name);
//                product_price = itemView.findViewById(R.id.txt_product_price);
//
//                img_product = itemView.findViewById(R.id.img_product);
//                remove = itemView.findViewById(R.id.remove);
//                spin = itemView.findViewById(R.id.spinner);
//
//
//            }
//
//
//        }
//
//
//        private void updateCart(int qty, int pprice, int pid, int uid) {
//
////            progressDialog = ProgressDialog.show(context,"Please Wait","Loaing",false,false);
//
//            int total = 0;
//
//            total = pprice * qty;
//
//
//            //Toast.makeText(context, String.valueOf(total), Toast.LENGTH_LONG).show();
//
//            String url = Api.URL_UPDATE_CART + qty + "&total_price=" + total + "&user_id=" + uid + "&product_id=" + pid;
//
//
//            Call<Final_Model1> call = Api.getPostService().updatecart(url);
//
//            call.enqueue(new Callback<Final_Model1>() {
//                @Override
//                public void onResponse(Call<Final_Model1> call, Response<Final_Model1> response) {
//
//
//
//                    list.clear();
//
//                    Final_Model1 model1 = response.body();
//                    //Toast.makeText(context, model1.getMessage(), Toast.LENGTH_LONG).show();
//
//
////                    product_amount.setText(records1.getGrandTotal());
//
//                    list.addAll(model1.getRecords());
//                    adapter.notifyDataSetChanged();
//
//                    return;
////                    progressDialog.dismiss();
//
//                }
//
//                @Override
//                public void onFailure(Call<Final_Model1> call, Throwable t) {
//
//                    progressDialog.dismiss();
//                    Toast.makeText(Cart.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//
//
//        }
//
//
//        private void removeCart(final int productId, String userId) {
//
//            progressDialog = ProgressDialog.show(context,"Please Wait","",false,false);
//
//            String url = Api.URL_DELETE_DATA + productId + "&user_id=" + userId;
//
//            Call<Final_Model1> call = Api.getPostService().removeCart(url);
//
//
//            list.clear();
//
//
//            call.enqueue(new Callback<Final_Model1>() {
//                @Override
//                public void onResponse(Call<Final_Model1> call, Response<Final_Model1> response) {
//
//                    progressDialog.dismiss();
//
//                    Final_Model1 model1 = response.body();
//
//                          if (!model1.getError()) {
//
//
//                        list.addAll(model1.getRecords());
//
//                        //  Toast.makeText(context, model1.getMessage(), Toast.LENGTH_LONG).show();
//
//
//                        adapter.notifyDataSetChanged();
//                        return;
//                    } else {
//                        Toast.makeText(context, model1.getMessage(), Toast.LENGTH_LONG).show();
//                        product_amount.setText(String.valueOf(0));
//
//
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<Final_Model1> call, Throwable t) {
//                    progressDialog.dismiss();
//
//                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
//                    list.clear();
//                    adapter.notifyDataSetChanged();
////                    product_amount.setText(String.valueOf(0));
//                    product_amount.setVisibility(View.GONE);
//                    tv_total.setText("Cart is Empty");
//
//                }
//            });
//
//
//        }
//
//    }

    public class CA extends BaseAdapter {
        private TextView product_name, product_price;
        private ImageButton remove;
        private Spinner spin;
        private ImageView img_product;
        private ProgressDialog progressDialog;

        private List<Final_records1> list;
        private Context context;


        public CA(List<Final_records1> list, Context context) {
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
        public View getView(int position, View itemView, ViewGroup parent) {

            if (itemView == null) {

                itemView = LayoutInflater.from(context).inflate(R.layout.cart_data, parent, false);
            }
            product_name = itemView.findViewById(R.id.txt_product_name);
            product_price = itemView.findViewById(R.id.txt_product_price);

            img_product = itemView.findViewById(R.id.img_product);
            remove = itemView.findViewById(R.id.remove);
            spin = itemView.findViewById(R.id.spinner);


            final Final_records1 records1 = (Final_records1) getItem(position);

            product_name.setText(records1.getProductName());
            spin.setSelection(records1.getQty() - 1);
            product_price.setText("Rs. " + records1.getProductPrice());
            Glide.with(context).load(Config.PROD_IMG_URL + records1.getProduct_img()).into(img_product);

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    Toast.makeText(context, "Item " + records1.getProductId(), Toast.LENGTH_LONG).show();

                    removeCart(records1.getProductId(), UserId);
                    //viewHolder.prod_qty.setText("1");

                }
            });


            product_amount.setText("Rs. " + String.valueOf(records1.getGrandTotal()));

            grand_value = records1.getGrandTotal();

            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                    switch (i) {

                        case 0:

                            spin_value = 1;

                            progressDialog = ProgressDialog.show(context, "Please Wait", "", false, false);

//     Toast.makeText(context, String.valueOf(spin_value), Toast.LENGTH_LONG).show();

                            updateCart(spin_value, records1.getProductPrice(), records1.getProductId(), records1.getUserId());

                            progressDialog.dismiss();
                            break;


                        case 1:
                            spin_value = 2;
                            progressDialog = ProgressDialog.show(context, "Please Wait", "", false, false);

                            //         Toast.makeText(context, String.valueOf(spin_value), Toast.LENGTH_LONG).show();
                            updateCart(spin_value, records1.getProductPrice(), records1.getProductId(), records1.getUserId());
                            progressDialog.dismiss();
                            break;

                        case 2:
                            spin_value = 3;
                            progressDialog = ProgressDialog.show(context, "Please Wait", "", false, false);

//    Toast.makeText(context, String.valueOf(spin_value), Toast.LENGTH_LONG).show();

                            updateCart(spin_value, records1.getProductPrice(), records1.getProductId(), records1.getUserId());
                            progressDialog.dismiss();
                            break;

                        case 3:
                            spin_value = 4;
                            progressDialog = ProgressDialog.show(context, "Please Wait", "", false, false);

//                            Toast.makeText(context, String.valueOf(spin_value), Toast.LENGTH_LONG).show();
                            updateCart(spin_value, records1.getProductPrice(), records1.getProductId(), records1.getUserId());
                            progressDialog.dismiss();
                            break;


                        default:
                            spin_value = 1;
                            progressDialog = ProgressDialog.show(context, "Please Wait", "", false, false);

                            //         Toast.makeText(context, "Default " + String.valueOf(spin_value), Toast.LENGTH_LONG).show();
                            updateCart(spin_value, records1.getProductPrice(), records1.getProductId(), records1.getUserId());
                            progressDialog.dismiss();
                            break;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            return itemView;
        }


        private void updateCart(int qty, int pprice, int pid, int uid) {

//            progressDialog = ProgressDialog.show(context,"Please Wait","Loaing",false,false);

            int total = 0;

            total = pprice * qty;


            //Toast.makeText(context, String.valueOf(total), Toast.LENGTH_LONG).show();

            String url = Api.URL_UPDATE_CART + qty + "&total_price=" + total + "&user_id=" + uid + "&product_id=" + pid;


            Call<Final_Model1> call = Api.getPostService().updatecart(url);

            call.enqueue(new Callback<Final_Model1>() {
                @Override
                public void onResponse(Call<Final_Model1> call, Response<Final_Model1> response) {


                    list.clear();

                    Final_Model1 model1 = response.body();
                    //Toast.makeText(context, model1.getMessage(), Toast.LENGTH_LONG).show();


//                    product_amount.setText(records1.getGrandTotal());

                    list.addAll(model1.getRecords());
                    adapter.notifyDataSetChanged();

                    return;
//                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(Call<Final_Model1> call, Throwable t) {

                    progressDialog.dismiss();
                    Toast.makeText(Cart.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        private void removeCart(final int productId, String userId) {

            progressDialog = ProgressDialog.show(context, "Please Wait", "", false, false);

            String url = Api.URL_DELETE_DATA + productId + "&user_id=" + userId;

            Call<Final_Model1> call = Api.getPostService().removeCart(url);

//            list.clear();


            call.enqueue(new Callback<Final_Model1>() {
                @Override
                public void onResponse(Call<Final_Model1> call, Response<Final_Model1> response) {

                    progressDialog.dismiss();

                    Final_Model1 model1 = response.body();

                    if (!model1.getError()) {

                        //  Toast.makeText(context, model1.getMessage(), Toast.LENGTH_LONG).show();


                        if (!model1.getRecords().get(0).isError()) {
                            list.clear();
                            list.addAll(model1.getRecords());
                            adapter.notifyDataSetChanged();
                            return;
                        } else {
                            list.clear();
                            adapter.notifyDataSetChanged();
                            cart_container.setVisibility(View.GONE);
                            empty_container.setVisibility(View.VISIBLE);
                            Toast.makeText(Cart.this, model1.getRecords().get(0).getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }


                    } else {
                        Toast.makeText(context, model1.getMessage(), Toast.LENGTH_LONG).show();
                        product_amount.setText(String.valueOf(0));


                    }

                }

                @Override
                public void onFailure(Call<Final_Model1> call, Throwable t) {
                    progressDialog.dismiss();

                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                    list.clear();
                    adapter.notifyDataSetChanged();
//                    product_amount.setText(String.valueOf(0));
                    cart_container.setVisibility(View.GONE);
                    empty_container.setVisibility(View.VISIBLE);

                }
            });


        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Cart.this.finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

        sharedPref();
        getData(UserId);
    }
}
