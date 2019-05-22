package com.quezx.analytics.model;

import com.quezx.analytics.model.innerModel.ReportAddCategoryData;

import java.util.HashMap;
import java.util.Map;

public class ReportAddCategory {

    private ReportAddCategoryData data;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public ReportAddCategoryData getData() {
        return data;
    }

    public void setData(ReportAddCategoryData data) {
        this.data = data;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}