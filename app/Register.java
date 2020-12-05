package com.example.dairy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputAddress;
    private TextInputLayout textInputCity;
    private TextInputLayout textInputMobile;
    private TextInputLayout textInputFirstname;
    private TextInputLayout textInputLastname;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputConfirmPassword;

    Button button;

   /* private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            //"(?=.*[0-9])" +         //at least 1 digit
            //"(?=.*[a-z])" +         //at least 1 lower case letter
            //"(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{4,}" +               //at least 4 characters
            "$");*/


    private TextInputEditText nf, cnf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!isConnected(Register.this)) {

            Toast.makeText(Register.this, "No Internet Connection", Toast.LENGTH_LONG).show();

            new AlertDialog.Builder(Register.this).setTitle("No Internet Connection").setIcon(android.R.drawable.stat_notify_error)
                    .setMessage("Check your internet connection and try again.")
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).show();

        }


        setContentView(R.layout.register_layout);
        textInputEmail = findViewById(R.id.text_input_email);
        textInputAddress = findViewById(R.id.text_input_address);
        textInputCity = findViewById(R.id.text_input_city);
        textInputMobile = findViewById(R.id.text_input_mobile);
        textInputFirstname = findViewById(R.id.text_input_firstname);
        textInputLastname = findViewById(R.id.text_input_lastname);
        textInputPassword = findViewById(R.id.text_input_password);
        textInputConfirmPassword = findViewById(R.id.text_input_confirmpassword);

//        nf = (TextInputEditText) findViewById(R.id.nf);
//        cnf = (TextInputEditText) findViewById(R.id.cnf);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.register);


        TextView link_login = (TextView) findViewById(R.id.link_login);
        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Register.this, Login.class));
            }
        });


        button = (Button) findViewById(R.id.button_register);


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
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Please Enter a valid Email Address");
            return false;

        } else {
            textInputEmail.setError(null);
            return true;
        }


    }

    private boolean validateUsername() {

        String firstnameInput = textInputFirstname.getEditText().getText().toString().toLowerCase().trim();
        String lastnameInput = textInputLastname.getEditText().getText().toString().toLowerCase().trim();

        if (firstnameInput.isEmpty()) {
            textInputFirstname.setError("Field Can't be empty");
            return false;
        } else if (lastnameInput.isEmpty()) {
            textInputLastname.setError("Field Can't be empty");
            return false;
        } else {
            textInputFirstname.setError(null);
            textInputLastname.setError(null);
            return true;
        }

    }


    private boolean validateCity() {

        String cityInput = textInputCity.getEditText().getText().toString().trim();

        if (cityInput.isEmpty()) {
            textInputCity.setError("Field Can't be empty");
            return false;
        } else {
            textInputCity.setError(null);
            return true;
        }

    }


    private boolean validateAddress() {

        String addressInput = textInputAddress.getEditText().getText().toString().trim();

        if (addressInput.isEmpty()) {
            textInputAddress.setError("Field Can't be empty");
            return false;
        } else {

            textInputAddress.setError(null);
            return true;
        }

    }

    private boolean validateMobile() {

        String mobileInput = textInputMobile.getEditText().getText().toString();

        if (mobileInput.isEmpty()) {
            textInputMobile.setError("Field Can't be empty");
            return false;
        } else if (mobileInput.length() < 10 || mobileInput.length() > 10) {

            textInputMobile.setError("Mobile No. should be 10 digit");
            return false;
        } else {
            textInputMobile.setError(null);
            return true;
        }


    }

    private boolean validatePassword() {

        String passwordInput = textInputPassword.getEditText().getText().toString().trim();
        String passwordConfirm = textInputConfirmPassword.getEditText().getText().toString().trim();


        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field Can't be empty");
            return false;
        } else if (passwordConfirm.isEmpty()) {
            textInputConfirmPassword.setError("Field Can't be empty");
            return false;

        } else if (!passwordInput.equals(passwordConfirm)) {
            textInputConfirmPassword.setError("Please Confirm Password");
            return false;
        } else {

            textInputPassword.setError(null);
            textInputConfirmPassword.setError(null);

            return true;

        }

    }


    public void register(View view) {

        if (!validateEmail() | !validateUsername() | !validatePassword() | !validateAddress() | !validateCity() | !validateMobile()) {

            return;
        }

        final String fname = textInputFirstname.getEditText().getText().toString().trim();
        final String lname = textInputLastname.getEditText().getText().toString().trim();
        final String email = textInputEmail.getEditText().getText().toString().trim();
        final String city = textInputCity.getEditText().getText().toString().trim();
        final String mobile = textInputMobile.getEditText().getText().toString().trim();
        final String address = textInputAddress.getEditText().getText().toString().trim();
        final String password = textInputPassword.getEditText().getText().toString().trim();


//        Log.d("Params", name + email + city + mobile + address + password);


       /* textInputPassword.getEditText().setText(null);
        textInputConfirmPassword.getEditText().setText(null);
        textInputAddress.getEditText().setText(null);
        textInputMobile.getEditText().setText(null);
        textInputCity.getEditText().setText(null);
        textInputUsername.getEditText().setText(null);
        textInputEmail.getEditText().setText(null);*/

        final ProgressDialog progressDialog = ProgressDialog.show(Register.this, "Please Wait", "Loading...", false, false);

        StringRequest request = new StringRequest(1, Config.register, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getBoolean("error")) {
                        Toast.makeText(Register.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        openActivity(Login.class);
                    } else {
                        Toast.makeText(Register.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Register.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();


                params.put("firstname", fname);
                params.put("lastname", lname);
                params.put("address", address);
                params.put("city", city);
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("password", password);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }


    private void openActivity(Class aclass) {

        startActivity(new Intent(Register.this, aclass));
    }
}
