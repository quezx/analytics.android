package com.quezx.analytics.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArrayListIds {
    @SerializedName("reportIds")
    @Expose
    private ArrayList<Integer> ages;
    public ArrayListIds(ArrayList<Integer> ages) {
        this.ages=ages;
    }
}
