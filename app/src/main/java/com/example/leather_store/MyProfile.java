package com.example.leather_store;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyProfile extends AppCompatActivity implements View.OnClickListener {


    CircularImageView circularImageView;
    TextView txt_upload_image, txt_email, txt_mobile;
    EditText editText_firstname, editText_lastname, editText_city, editText_address;
    TextView tv_change_fname, tv_change_lname, tv_change_city, tv_change_address, tv_mobile, tv_email;
    private Uri filePath;
    Bitmap bitmap;
    LinearLayout update;
    ImageView img_profile;

    private Context context = this;

    private String first_name, last_name, address, city, mobile_no, email;

    SharedPreferences sharedPreferences;
    String UserID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView((int) R.layout.activity_myprofile);

        initview();

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Profile");


        shared_pref();

        get_details(UserID);

    }

    private void shared_pref() {
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        UserID = sharedPreferences.getString("user_id", null);
    }

    private void initview() {

        txt_email = (TextView) findViewById(R.id.edt_email);
        txt_mobile = (TextView) findViewById(R.id.edt_mobile);
        txt_upload_image = (TextView) findViewById(R.id.txt_uploadImageGallery);

        img_profile = (CircularImageView) findViewById(R.id.img_profile);
        img_profile.setOnClickListener(this);

        editText_firstname = (EditText) findViewById(R.id.edt_firstName);
        editText_lastname = (EditText) findViewById(R.id.edt_lastName);
        editText_city = (EditText) findViewById(R.id.edt_city);
        editText_address = (EditText) findViewById(R.id.edt_address);

     /*   editText_firstname.setFocusable(false);
        editText_lastname.setFocusable(false);
        editText_city.setFocusable(false);
        editText_address.setFocusable(false);*/

        tv_change_fname = (TextView) findViewById(R.id.txt_changefirstName);
        tv_change_lname = (TextView) findViewById(R.id.txt_changelastName);
        tv_change_city = (TextView) findViewById(R.id.txt_changecity);
        tv_change_address = (TextView) findViewById(R.id.txt_changeaddress);
        tv_email = (TextView) findViewById(R.id.edt_email);
        tv_mobile = (TextView) findViewById(R.id.edt_mobile);

        tv_change_fname.setOnClickListener(this);
        tv_change_lname.setOnClickListener(this);
        tv_change_city.setOnClickListener(this);
        tv_change_address.setOnClickListener(this);

        update = (LinearLayout) findViewById(R.id.btn_update);
        update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txt_changefirstName:
//                Toast.makeText(this, "Hello1", Toast.LENGTH_SHORT).show();
                editText_firstname.setEnabled(true);
                editText_firstname.setSelection(editText_firstname.getText().length());
                break;

            case R.id.txt_changelastName:
//                Toast.makeText(this, "Hello2", Toast.LENGTH_SHORT).show();
                editText_lastname.setEnabled(true);
                editText_lastname.setSelection(editText_lastname.getText().length());

                break;

            case R.id.txt_changecity:
//                Toast.makeText(this, "Hello3", Toast.LENGTH_SHORT).show();
                editText_city.setEnabled(true);
                editText_city.setSelection(editText_city.getText().length());
                break;

            case R.id.txt_changeaddress:
//                Toast.makeText(this, "Hello4", Toast.LENGTH_SHORT).show();
                editText_address.setEnabled(true);
                editText_address.setSelection(editText_address.getText().length());

                break;

            case R.id.btn_update:

                first_name = editText_firstname.getText().toString();
                last_name = editText_lastname.getText().toString();
                address = editText_address.getText().toString();
                city = editText_city.getText().toString();

                update_details(first_name, last_name, address, city, txt_email.getText().toString());
                break;


            case R.id.img_profile:

                Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ((ImageView) findViewById(R.id.img_profile)).setImageBitmap(this.bitmap);
                uploadBitmap(UserID, this.bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadBitmap(final String userID, final Bitmap bitmap) {

        final ProgressDialog progressDialog = ProgressDialog.show(MyProfile.this, "Uploading", "Please Wait..", false, false);

        StringRequest r0 = new StringRequest(Request.Method.POST, Config.uploadProfileImage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(MyProfile.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    Toast.makeText(MyProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                error.printStackTrace();
                Toast.makeText(MyProfile.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                String image = getStringImage(bitmap);
                params.put("userid", userID);
                params.put("image", image);

                return params;
            }
        };


        Volley.newRequestQueue(this).add(r0);

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void update_details(final String first_name, final String last_name, final String address, final String city, final String email) {


        StringRequest request = new StringRequest(1, Config.update_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getBoolean("error")) {
//                        Toast.makeText(MyProfile.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = getSharedPreferences("login", Context.MODE_PRIVATE).edit();
                        editor.putString("first_name", first_name);
                        editor.putString("last_name", last_name);
                        editor.putString("address", address);
                        editor.putString("city", city);
                        editor.commit();

                        MyProfile.this.finish();
                        openActivity(MyProfile.class);
                        return;

                    } else {
                        Toast.makeText(MyProfile.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MyProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            public Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("fname", first_name);
                params.put("lname", last_name);
                params.put("address", address);
                params.put("city", city);
                params.put("email", email);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void get_details(final String userID) {


        StringRequest request = new StringRequest(1, Config.get_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject2 = (JSONObject) jsonObject.getJSONArray("records").get(0);
                    if (!jsonObject.getBoolean("error")) {
//                        Toast.makeText(MyProfile.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                        editText_firstname.setText(jsonObject2.getString("first_name"));
                        editText_lastname.setText(jsonObject2.getString("last_name"));
                        editText_address.setText(jsonObject2.getString("address"));
                        editText_city.setText(jsonObject2.getString("city"));
                        tv_email.setText(jsonObject2.getString("email"));
                        tv_mobile.setText(jsonObject2.getString("mobile_no"));


                        if (jsonObject2.getString("image") != null) {
                            Glide.with(MyProfile.this)
                                    .load(Config.USER_IMG_URL + jsonObject2.getString("image"))
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(img_profile);

                        }


                        return;
                    } else {
                        Toast.makeText(MyProfile.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MyProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            public Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("userId", userID);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
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

        MyProfile.this.finish();
    }

    private void openActivity(Class aclass) {
        startActivity(new Intent(context, aclass));
    }
}
