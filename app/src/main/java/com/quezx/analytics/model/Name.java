package com.quezx.analytics.model;

import com.google.gson.annotations.SerializedName;

public class Name {
    @SerializedName("name")
    String name;

    public Name(String name){
        this.name=name;
    }

}
