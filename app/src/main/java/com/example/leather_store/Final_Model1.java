
package com.example.leather_store;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Final_Model1 {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("records")
    @Expose
    private List<Final_records1> records = null;


    //private int type;


    public Final_Model1( Boolean error, String message, List<Final_records1> records) {
        this.error = error;
        this.message = message;
        this.records = records;
    }





    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Final_records1> getRecords() {
        return records;
    }

    public void setRecords(List<Final_records1> records) {
        this.records = records;
    }



}
