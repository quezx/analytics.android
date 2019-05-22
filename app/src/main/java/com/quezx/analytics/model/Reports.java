package com.quezx.analytics.model;

import com.quezx.analytics.model.innerModel.Datum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reports {

    private List<Datum> data = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
