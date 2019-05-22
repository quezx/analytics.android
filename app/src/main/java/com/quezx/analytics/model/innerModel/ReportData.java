package com.quezx.analytics.model.innerModel;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class ReportData {

    private int report_id;
    @SerializedName("Report")
    private Report report;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public int getReport_id() {
        return report_id;
    }

    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
