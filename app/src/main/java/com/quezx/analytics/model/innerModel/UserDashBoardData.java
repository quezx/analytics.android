package com.quezx.analytics.model.innerModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
        import java.util.Map;

public class UserDashBoardData implements Serializable {

    private Integer id;
    private Boolean is_default;
    @SerializedName("Dashboard")
    private Dashboard dashboard;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIs_default() {
        return is_default;
    }

    public void setIs_default(Boolean is_default) {
        this.is_default = is_default;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}