package com.example.dairy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Order extends AppCompatActivity {

    TextView tv_name, tv_amount;
    EditText edt_address, edt_email, edt_mobile;
    RadioButton radioButton, radioButton2;
    RadioGroup radioGroup;

    private String name, address, email, mobile, order_type, userId;

    private SharedPreferences preferences;

    private Context context = this;

    private int wallet, amount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        init();
        intent();
        sharedPref();
    }

    private void sharedPref() {

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        address = preferences.getString("address", null);
        userId = preferences.getString("user_id", null);
        email = preferences.getString("email", null);
        mobile = preferences.getString("mobile", null);
        address = preferences.getString("address", null);
        name = preferences.getString("first_name", null);

        tv_name.setText(name);
        edt_address.setText(address);
        edt_address.setSelection(edt_address.getText().length());
        edt_mobile.setText(mobile);
        edt_email.setText(email);

    }

    private void intent() {
        amount = getIntent().getIntExtra("amount", 0);
        tv_amount.setText("Rs. " + amount);
    }

    private void init() {

        tv_name = (TextView) findViewById(R.id.txt_name);
        tv_amount = (TextView) findViewById(R.id.txt_amount);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        edt_address = (EditText) findViewById(R.id.edt_address);

        radioGroup = findViewById(R.id.radio_group);
        radioButton = findViewById(R.id.radio_cod);
        radioButton2 = findViewById(R.id.radio_online);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radio_cod) {
                    order_type = "COD";
                } else {
                    order_type = "Online";
                }
            }
        });


        getSupportActionBar().setTitle("Order Details");
    }

    public void place_order(View view) {
        getMoney();
    }

    private void displayDialog(final int wallet) {

        new AlertDialog.Builder(context)
                .setTitle("Your Wallet")
                .setMessage("Wallet Balance " + wallet)


                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        if (wallet < amount) {
                            Toast.makeText(context, "Insufficient Balance in wallet", Toast.LENGTH_SHORT).show();
                        } else {
                            final_order(tv_name.getText().toString(), tv_amount.getText().toString(), edt_email.getText().toString(), edt_mobile.getText().toString(), edt_address.getText().toString());
                        }

                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void final_order(final String name, final String amount, final String email, final String mobile, final String address) {

        StringRequest request = new StringRequest(1, Config.PLACE_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")) {
                        Toast.makeText(Order.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        openActivity(MyOrders.class);
                    } else {
                        Toast.makeText(Order.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Order.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Order.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("name", name);
                params.put("email", email);
                params.put("amount", amount);
                params.put("mobile", mobile);
                params.put("address", address);
                params.put("order_type", order_type);


                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }


    private void getMoney() {

        String url = Api.URL_GET_MONEY;

        Call<Final_Model1> call = Api.getPostService().getMoney(url, userId);
        call.enqueue(new Callback<Final_Model1>() {
            @Override
            public void onResponse(Call<Final_Model1> call, retrofit2.Response<Final_Model1> response) {

                if (response.isSuccessful()) {

                    if (!response.body().getError()) {
                        Final_Model1 model1 = response.body();

                        wallet = model1.getRecords().get(0).getWallet();
                        displayDialog(wallet);
                    }
                }

            }

            @Override
            public void onFailure(Call<Final_Model1> call, Throwable t) {

                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openActivity(Class aclass) {

        startActivity(new Intent(context, aclass));
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
