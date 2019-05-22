package com.quezx.analytics.model.innerModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Dashboard implements Serializable {

    @SerializedName("dId")
    private int dId;
    @SerializedName("dashboard_id")
    private int dashboard_id;
    @SerializedName("alias")
    private String alias;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private Object description;
    private Connection connection;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public int getDId() {
        return dId;
    }

    public void setDId(int dId) {
        this.dId = dId;
    }

    public int getDashboard_id() {
        return dashboard_id;
    }

    public void setDashboard_id(int dashboard_id) {
        this.dashboard_id = dashboard_id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}