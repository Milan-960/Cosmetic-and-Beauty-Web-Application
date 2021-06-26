package com.example.dairy;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrders extends AppCompatActivity {

    private ListView listView;
    private MyOrderAdapter orderAdapter;
    private Context context = this;
    private List<Final_records1> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        init();
        getMyOrderDetails();
    }


    private void init() {


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Orders");


        listView = findViewById(R.id.listView);
        orderAdapter = new MyOrderAdapter(list, context);
        listView.setAdapter(orderAdapter);

    }

    private void getMyOrderDetails() {

        String url = Api.URL_GET_MY_ORDER;
        String id = getSharedPreferences("login", Context.MODE_PRIVATE).getString("user_id", null);


        Call<Final_Model1> mtCall = Api.getPostService().MyOrder(url, id);
        mtCall.enqueue(new Callback<Final_Model1>() {
            @Override
            public void onResponse(Call<Final_Model1> call, Response<Final_Model1> response) {


                if (!response.body().getError()) {
                    final Final_Model1 finalModel1 = response.body();

                    list.clear();
                    list.addAll(finalModel1.getRecords());
                    orderAdapter.notifyDataSetChanged();

                    return;

                }

            }

            @Override
            public void onFailure(Call<Final_Model1> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
