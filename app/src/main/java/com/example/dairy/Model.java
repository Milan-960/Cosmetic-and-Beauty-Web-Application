package com.example.dairy;

public class Model {

    String product_name;
    String price;


    public Model(String product_name, String price) {
        this.product_name = product_name;
        this.price = price;
    }

    public String getProduct_name() {
        return product_name;
    }


    public String getPrice() {
        return price;
    }




}
