package com.example.leather_store;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Main2Activity extends AppCompatActivity {


    RecyclerView recyclerView;
    CustomAdpater customAdpater;
    MenuItem menuItem;
    public static String abc;
    Context context = Main2Activity.this;
    NotificationBadge count;
    ImageView cart_img;
    Button btn;
    Toolbar toolbar;

    private ProgressDialog progressDialog;

    private SharedPreferences preferences;
    private String UserId;
    List<Final_records1> list = new ArrayList<>();

    private String cat_id, cat_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        sharedPref();
        getCatIdByIntent();


        recyclerView = (RecyclerView) findViewById(R.id.recycler_cart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        customAdpater = new CustomAdpater(list, context);
        recyclerView.setAdapter(customAdpater);

        getData2(cat_id);
        cart(UserId);
   /* findViewById(R.id.btn_placeorder).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Main2Activity.this,Cart.class));
        }
    });*/


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(cat_name);
    }

    private void getCatIdByIntent() {

        cat_id = getIntent().getStringExtra("cat_id");
        cat_name = getIntent().getStringExtra("cat_name");
    }

    private void sharedPref() {

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        UserId = preferences.getString("user_id", null);
    }


    private void getData2(String cat_id) {

        String url = Api.URL_GET_PRODUCT + cat_id;

        progressDialog = ProgressDialog.show(context, "Loading", "Please Wait...", false, false);

        Call<Final_Model1> mtCall = Api.getPostService().getRecords(url);
        mtCall.enqueue(new Callback<Final_Model1>() {
            @Override
            public void onResponse(Call<Final_Model1> call, Response<Final_Model1> response) {
                progressDialog.dismiss();

                final Final_Model1 finalModel1 = response.body();

                list.addAll(finalModel1.getRecords());
                customAdpater.notifyDataSetChanged();


                //cart();


                //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<Final_Model1> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menus, menu);


//                menuItem = menu.findItem(R.id.cart_menu);
//
//                View v = menuItem.getActionView();
//
//
////                if (v != null) {
//
//
//                    count = v.findViewById(R.id.badge);
//                    count.setText(cart());
//
//
//                    RelativeLayout container = v.findViewById(R.id.cart_container);
//                    ImageView cart_icon = v.findViewById(R.id.cart_icon);
//                    container.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//
//                            startActivity(new Intent(Main2Activity.this,Cart.class));
//
//                        }
//                    });


        //  }


        View v = menu.findItem(R.id.cart_menu).getActionView();
        count = v.findViewById(R.id.badge);
        cart_img = v.findViewById(R.id.cart_img);


        count.setText(abc);

//        cart();
        v.findViewById(R.id.cart_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                cart();
                startActivity(new Intent(Main2Activity.this, Cart.class));
            }
        });

        cart_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                cart();
                startActivity(new Intent(Main2Activity.this, Cart.class));
            }
        });


        return true;
    }


    public void cart(final String userId) {



       /* String url = Api.URL_GET_CART_COUNT;

        Call<Final_Model1> call = Api.getPostService().getCount(url);

        call.enqueue(new Callback<Final_Model1>() {
            @Override
            public void onResponse(Call<Final_Model1> call, Response<Final_Model1> response) {


                Final_Model1 model1 = response.body();
                abc = model1.getMessage();

                invalidateOptionsMenu();

            }

            @Override
            public void onFailure(Call<Final_Model1> call, Throwable t) {

                Toast.makeText(Main2Activity.this, "Error", Toast.LENGTH_LONG).show();

            }
        });

        count.setText(abc);
        return;*/


        StringRequest request = new StringRequest(1, Api.URL_GET_CART_COUNT, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    abc = jsonObject.getString("records");
                    invalidateOptionsMenu();
                    return;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                return params;
            }
        };

        Volley.newRequestQueue(Main2Activity.this).add(request);

//        count.setText(abc);
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

        Main2Activity.this.finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

        sharedPref();
        cart(UserId);
    }
}

