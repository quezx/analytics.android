package com.quezx.analytics.model;

import com.quezx.analytics.model.innerModel.ReportCategoryInner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportCategory {

    private List<ReportCategoryInner> data = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<ReportCategoryInner> getData() {
        return data;
    }

    public void setData(List<ReportCategoryInner> data) {
        this.data = data;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}