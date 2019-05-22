package com.quezx.analytics.model;
import com.quezx.analytics.model.innerModel.MappedReportData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappedReport {

    private List<MappedReportData> data = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<MappedReportData> getData() {
        return data;
    }

    public void setData(List<MappedReportData> data) {
        this.data = data;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
