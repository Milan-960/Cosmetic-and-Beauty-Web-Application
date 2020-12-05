package com.example.dairy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends AppCompatActivity {

    private Button button;
    private EditText editText;
    private TextView wallet;
    String UserId;
    SharedPreferences preferences;
    private Context context = this;

    private static final String TAG = "WalletActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);


        button = findViewById(R.id.btn_add);
        editText = findViewById(R.id.edt_add_money);
        wallet = findViewById(R.id.tv_wallet);

        preferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        UserId = preferences.getString("user_id", null);
        init();
    }

    private void init() {

        getMoney();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount = editText.getText().toString().trim();

                if (Integer.parseInt(amount) <= 0) {
                    Toast.makeText(WalletActivity.this, "Invalid Amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                addMoney(amount);
            }
        });
    }

    private void addMoney(String amount) {

        String url = Api.URL_ADD_MONEY;

        Log.d(TAG, "Amount - " + amount + "---ID  " + UserId + "----Url  " + url);

        Call<Final_Model1> call = Api.getPostService().AddMoney(url, amount, UserId);
        call.enqueue(new Callback<Final_Model1>() {
            @Override
            public void onResponse(Call<Final_Model1> call, Response<Final_Model1> response) {

                if (response.isSuccessful()) {

                    if (!response.body().getError()) {
                        Final_Model1 model1 = response.body();

                        Toast.makeText(context, model1.getMessage(), Toast.LENGTH_SHORT).show();
                        getMoney();
                    }
                }

            }

            @Override
            public void onFailure(Call<Final_Model1> call, Throwable t) {

                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMoney() {

        String url = Api.URL_GET_MONEY;

        Call<Final_Model1> call = Api.getPostService().getMoney(url, UserId);
        call.enqueue(new Callback<Final_Model1>() {
            @Override
            public void onResponse(Call<Final_Model1> call, Response<Final_Model1> response) {

                if (response.isSuccessful()) {

                    if (!response.body().getError()) {
                        Final_Model1 model1 = response.body();

                        wallet.setText(String.valueOf(model1.getRecords().get(0).getWallet()));

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
    }


}