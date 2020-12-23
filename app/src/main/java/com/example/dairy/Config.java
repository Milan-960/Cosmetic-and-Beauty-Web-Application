package com.example.dairy;

public class Config {

    //    public static final String BASE = "http://192.168.0.112/";
    public static final String BASE = "http://mydata123.pl/bu/";
    public static final String BASE_URL = BASE + "api/Api.php?apicall=";
    public static final String CAT_IMG_URL = BASE + "category_image/";
    public static final String PROD_IMG_URL = BASE + "product_image/";
    public static final String USER_IMG_URL = BASE + "images/";
    public static final String login = BASE_URL + "login";
    public static final String register = BASE_URL + "register";
    public static final String change_password = BASE_URL + "change_pwd";
    public static final String update_details = BASE_URL + "update_details";
    public static final String get_details = BASE_URL + "get_details";
    public static final String get_details_by_email = BASE_URL + "get_details_by_email";
    public static final String uploadProfileImage = BASE_URL + "image";
    public static final String PLACE_ORDER = BASE_URL + "place_order";

}
