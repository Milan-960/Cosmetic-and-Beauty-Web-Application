
package com.example.leather_store;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Final_records1 {

    @SerializedName("product_id")
    @Expose
    private Integer productId;

    @SerializedName("cart_id")
    @Expose
    private Integer cartId;

    @SerializedName("user_id")
    @Expose
    private Integer userId;

    @SerializedName("user_name")
    @Expose
    private String userName;

    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_price")
    @Expose
    private int productPrice;

    @SerializedName("qty")
    @Expose
    private int qty;

    @SerializedName("total_price")
    @Expose
    private int totalPrice;

    @SerializedName("grand_total")
    @Expose
    private int grandTotal;

    @SerializedName("count")
    @Expose
    private String Count;

    @SerializedName("product_img")
    @Expose
    private String product_img;

    @SerializedName("cat_id")
    @Expose
    private String cat_id;

    @SerializedName("cat_name")
    @Expose
    private String cat_name;

    @SerializedName("cat_image")
    @Expose
    private String cat_image;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("date")
    @Expose
    private String date;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }


    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }


    public int getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(int grandTotal) {
        this.grandTotal = grandTotal;
    }


    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_image() {
        return cat_image;
    }

    public void setCat_image(String cat_image) {
        this.cat_image = cat_image;
    }


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
