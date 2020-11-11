package com.example.leather_store;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {


    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;


    String email;
    String name;
    String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!isConnected(Login.this)) {

            Toast.makeText(Login.this, "No Internet Connection", Toast.LENGTH_LONG).show();

            new AlertDialog.Builder(Login.this).setTitle("No Internet Connection").setIcon(android.R.drawable.stat_notify_error)
                    .setMessage("Check your internet connection and try again.")
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).show();

        }


        setContentView(R.layout.login_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.login);


        TextView link_register = (TextView) findViewById(R.id.link_register);
        link_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Login.this, Register.class));
            }
        });

        textInputEmail = findViewById(R.id.text_input_email);
        textInputPassword = findViewById(R.id.text_input_password);

    }


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sh = getSharedPreferences("login", Context.MODE_PRIVATE);
        String sh_email = sh.getString("email", "");
        String sh_name = sh.getString("name", "");

        if (!(sh_email == "")) {
            Intent intent = new Intent(Login.this,MainActivity.class);
            intent.putExtra("email",sh_email);
            intent.putExtra("name",sh_name);

            startActivity(intent);
            startActivity(new Intent(Login.this, MainActivity.class));
            Login.this.finish();
        }

    }


    public static boolean isConnected(Context context) {


        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {


            NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile_data = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile_data != null && mobile_data.isConnected()) || (wifi != null && wifi.isConnected())) {

                return true;
            } else {

                return false;
            }

        } else {

            return false;
        }


    }


    private boolean validateEmail() {

        String emailInput = textInputEmail.getEditText().getText().toString().toLowerCase().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field Can't be empty");
            return false;

        } else {
            textInputEmail.setError(null);
            return true;
        }


    }


    private boolean validatePassword() {

        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field Can't be empty");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;

        }

    }


    public void login(View view) {


        if (!validateEmail() | !validatePassword()) {

            return;
        }


        email = textInputEmail.getEditText().getText().toString();
        password = textInputPassword.getEditText().getText().toString();


        final ProgressDialog progressDialog = ProgressDialog.show(Login.this, "Please Wait", "Loading...", false, false);

    StringRequest request = new StringRequest(1, Config.login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
        progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(!jsonObject.getBoolean("error")){
//                        Toast.makeText(Login.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();



                        getUserDetailsByEmail(email);


                        openActivity(MainActivity.class);
                        Login.this.finish();
                    }
                    else {
                        Toast.makeText(Login.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
             public Map<String, String> getParams(){

                Map<String,String> params = new HashMap<>();
                params.put("email",email);
                params.put("password",password);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void getUserDetailsByEmail(final String email) {



        StringRequest request = new StringRequest(1, Config.get_details_by_email, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject2 = (JSONObject) jsonObject.getJSONArray("records").get(0);
                    if(!jsonObject.getBoolean("error")){


                        if(jsonObject2.getString("block").equals("1")){

                            Toast.makeText(Login.this, "Your Account has been blocked", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(Login.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        SharedPreferences sh = getSharedPreferences("login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sh.edit();

                        editor.putString("user_id", jsonObject2.getString("user_id"));
                        editor.putString("email", jsonObject2.getString("email"));
                        editor.putString("first_name", jsonObject2.getString("first_name"));
                        editor.putString("last_name", jsonObject2.getString("last_name"));
                        editor.putString("image", jsonObject2.getString("image"));
                        editor.putString("address", jsonObject2.getString("address"));
                        editor.putString("mobile", jsonObject2.getString("mobile_no"));
                        editor.putString("city", jsonObject2.getString("city"));


                        editor.commit();

                        return;
                    }
                    else {
                        Toast.makeText(Login.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            public Map<String, String> getParams(){

                Map<String,String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent finish = new Intent(Intent.ACTION_MAIN);
        finish.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish.addCategory(Intent.CATEGORY_HOME);
        startActivity(finish);
     }

    private void openActivity(Class aclass){

        startActivity(new Intent(Login.this,aclass));
    }

}
