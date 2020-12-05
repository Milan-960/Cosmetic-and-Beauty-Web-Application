package com.example.dairy;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public class Api {

    //    private static final String root_url = "http://moviestime0017.000webhostapp.com/cart/cart/api/Api.php?apicall=";
    private static final String root_url = Config.BASE + "leather/api/Api.php?apicall=";
//   private static final String root_url="http://192.168.0.103:9999/akil_surti/api/Api.php?apicall=";
    //private static final String root_url="http://192.168.1.6/abc/api/Api.php?apicall=";


    //private static final String root_url="http://abc.is-best.net/api/Api.php?apicall=";


    //         public static final String URL_INSERT_DATA=root_url+"register";
//          public static final String URL_update_DATA=root_url+"updatedata";
    public static final String URL_GET_PRODUCT = root_url + "getproduct&cat_id=";
    public static final String URL_GET_CART = root_url + "cart";
    public static final String URL_GET_CART_DATA = root_url + "cartdata&user_id=";
    public static final String URL_UPDATE_CART = root_url + "updatecart&qty=";
    public static final String URL_GET_CART_COUNT = root_url + "count";
    public static final String URL_GET_CATEGORY = root_url + "category";
    public static final String URL_GET_MY_ORDER = root_url + "my_order_details";


    public static final String URL_LOGIN = root_url + "login";
    public static final String URL_changepwd = root_url + "changepwd";
    public static final String URL_DELETE_DATA = root_url + "deletedata&product_id=";


    public static final String url3 = "http://moviestime0017.000webhostapp.com/";
    // public static final String key3= "/api/Api.phpgetdata";


    public static PostService postService = null;

    public static PostService getPostService() {

        if (postService == null) {

            Retrofit retrofit = new Retrofit.Builder().baseUrl(url3).addConverterFactory(GsonConverterFactory.create()).build();
            postService = retrofit.create(PostService.class);


        }
        return postService;
    }


    public interface PostService {
        //@GET("?key="+key)
        @GET
        Call<Final_Model1> getRecords(@Url String url);

        @GET
        Call<Final_Model1> getCount(@Url String url);

        @FormUrlEncoded
        @POST
        Call<Final_Model1> cart(
                @Url String url,
                @Field("product_id") int product_id,
                @Field("product_price") int product_price,
                @Field("cat_id") String cat_id,
                @Field("user_id") String user_id
        );


        @GET
        Call<Final_Model1> getcartData(
                @Url String url

        );

        @GET
        Call<Final_Model1> updatecart(
                @Url String url

        );

        @GET
        Call<Final_Model1> removeCart(
                @Url String url
        );


        @GET
        Call<Final_Model1> getCategory(
                @Url String url

        );

        @FormUrlEncoded
        @POST
        Call<Final_Model1> loginUser(
                @Url String url,
                @Field("user_name") String email
        );

        @FormUrlEncoded
        @POST
        Call<Final_Model1> MyOrder(
                @Url String url,
                @Field("user_id") String user_id
        );


    }


}
