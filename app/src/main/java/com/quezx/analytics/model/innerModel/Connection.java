package com.quezx.analytics.model.innerModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Connection implements Serializable {

    @SerializedName("metabase_url")
    private String metabase_url;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getMetabase_url() {
        return metabase_url;
    }

    public void setMetabase_url(String metabase_url) {
        this.metabase_url = metabase_url;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}