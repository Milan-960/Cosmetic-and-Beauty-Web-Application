package com.example.dairy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class Change_Password extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    TextInputLayout textInputOldPassword;
    TextInputLayout textInputNewPassword;
    TextInputLayout textInputConfirmPassword;

    /*private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            //"(?=.*[0-9])" +         //at least 1 digit
            //"(?=.*[a-z])" +         //at least 1 lower case letter
            //"(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{4,}" +               //at least 4 characters
            "$");*/


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_changepwd);

        textInputOldPassword = findViewById(R.id.text_input_old_password);
        textInputNewPassword = findViewById(R.id.text_input_new_password);
        textInputConfirmPassword = findViewById(R.id.text_input_confirmpassword);


        final Button button = (Button) findViewById(R.id.button_change_password);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!validatePassword()) {

                    return;
                }


                SharedPreferences sh = getSharedPreferences("login", Context.MODE_PRIVATE);
                String sh_email = sh.getString("email", "");


                final String old = textInputOldPassword.getEditText().getText().toString();
                final String newpwd = textInputNewPassword.getEditText().getText().toString();


                change_password(old, newpwd, sh_email);

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void change_password(final String old, final String newpwd, final String sh_email) {

        final ProgressDialog progressDialog = ProgressDialog.show(Change_Password.this, "Please Wait", "Loading...", false, false);

        StringRequest request = new StringRequest(1, Config.change_password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getBoolean("error")) {
                        Toast.makeText(Change_Password.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        Change_Password.this.finish();
                    } else {
                        Toast.makeText(Change_Password.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Change_Password.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Change_Password.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("old", old);
                params.put("newpwd", newpwd);
                params.put("email", sh_email);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);


    }


    private boolean validatePassword() {

        String passwordOldInput = textInputOldPassword.getEditText().getText().toString();
        String passwordNewInput = textInputNewPassword.getEditText().getText().toString().trim();
        String passwordConfirm = textInputConfirmPassword.getEditText().getText().toString().trim();


        if (passwordOldInput.isEmpty()) {
            textInputOldPassword.setError("Field Can't be empty");
            return false;
        } else if (passwordNewInput.isEmpty()) {

            textInputOldPassword.setError(null);

            textInputNewPassword.setError("Field Can't be empty");
            return false;
        } else if (passwordConfirm.isEmpty()) {
            textInputNewPassword.setError(null);
            textInputConfirmPassword.setError("Field Can't be empty");
            return false;

        } else if (!passwordNewInput.equals(passwordConfirm)) {
            textInputConfirmPassword.setError("Please Confirm Password");
            return false;
        } else {

            textInputOldPassword.setError(null);
            textInputNewPassword.setError(null);
            textInputConfirmPassword.setError(null);

            return true;

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
