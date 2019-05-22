package com.quezx.analytics.model.innerModel;


import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
        import java.util.Map;

public class MappedReportData {

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}