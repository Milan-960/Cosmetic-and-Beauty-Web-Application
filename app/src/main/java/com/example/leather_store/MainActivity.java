package com.example.leather_store;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    RecyclerView recyclerView_category_list;
    Category_Adapter category_adapter;

    List<Final_records1> list = new ArrayList<>();

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        is_connection();
        initview();
        get_shared_pref();
        click_listener();

        getCategory();

    }

    private void getCategory() {

        String url = Api.URL_GET_CATEGORY;

        Call<Final_Model1> call = Api.getPostService().getCategory(url);
        call.enqueue(new Callback<Final_Model1>() {
            @Override
            public void onResponse(Call<Final_Model1> call, Response<Final_Model1> response) {

                if (response.isSuccessful()) {

                    if (!response.body().getError()) {
                        Final_Model1 model1 = response.body();

                        list.addAll(model1.getRecords());
                        category_adapter.notifyDataSetChanged();

                    }
                }

            }

            @Override
            public void onFailure(Call<Final_Model1> call, Throwable t) {

                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void get_shared_pref() {


        SharedPreferences sh = getSharedPreferences("login", Context.MODE_PRIVATE);
        userId = sh.getString("user_id", "");
        String sh_email = sh.getString("email", "");
        String sh_fname = sh.getString("first_name", "");
        String sh_lname = sh.getString("last_name", "");
        String sh_image = sh.getString("image", "");


        ImageView head_image = navigationView.getHeaderView(0).findViewById(R.id.header_image);
        TextView put_name = navigationView.getHeaderView(0).findViewById(R.id.putname);
        TextView put_email = navigationView.getHeaderView(0).findViewById(R.id.putemail);

        if (sh_email == "") {
            startActivity(new Intent(MainActivity.this, Login.class));
        }

        Glide.with(this)
                .load(Config.USER_IMG_URL + userId + "_userImage" + ".jpg")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(head_image);

        put_name.setText(sh_fname + " " + sh_lname);
        put_email.setText(sh_email);
    }

    private void click_listener() {
        navigationView.setNavigationItemSelectedListener(this);

    }


    private void initview() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Category");
        navigationView = (NavigationView) findViewById(R.id.navigationView);

/*        recyclerView_category_list = (RecyclerView)findViewById(R.id.recycler_category);
        category_adapter = new Category_Adapter(list,MainActivity.this);
        recyclerView_category_list.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_category_list.setAdapter(category_adapter);*/


        recyclerView_category_list = (RecyclerView) findViewById(R.id.recycler_category);
        recyclerView_category_list.setHasFixedSize(true);
//        recyclerView_category_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView_category_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        category_adapter = new Category_Adapter(list, MainActivity.this);
        recyclerView_category_list.setAdapter(category_adapter);

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

    private void is_connection() {
        if (!isConnected(MainActivity.this)) {

            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

            new AlertDialog.Builder(MainActivity.this).setTitle("No Internet Connection").setIcon(android.R.drawable.stat_notify_error)
                    .setMessage("Check your internet connection and try again.")
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).show();

        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {


            case R.id.visit_profile:

                openActivity((MyProfile.class));
                return true;


            case R.id.update_details:

                openActivity(MyOrders.class);
                return true;
            case R.id.about:

                return true;
            case R.id.contact:

                return true;
            case R.id.change_password:


                openActivity(Change_Password.class);

                return true;


            case R.id.logout:

                logout();
                return true;

            default:
                return false;
        }
    }

    public void logout() {


        SharedPreferences sh = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sh.edit();

        editor.putString("email", "");
        editor.commit();

        startActivity(new Intent(MainActivity.this, Login.class));
        MainActivity.this.finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


  /*  @Override
    protected void onStart() {
        super.onStart();


    }*/

    private void openActivity(Class aclass) {

        startActivity(new Intent(MainActivity.this, aclass));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();

        get_shared_pref();
        return;
    }
}
