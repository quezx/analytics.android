package com.quezx.analytics.model.innerModel;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Datum {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("ReportCount")
    private int reportCount;
    @SerializedName("ReportCategories.id")
    private int reportCategories_id;
    @SerializedName("ReportCategories.Report.id")
    private int reportCategories_Report_id;
    @SerializedName("ReportCategories.Report.UserReports.id")
    private int reportCategories_Report_UserReports_id;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    public int getReportCategories_id() {
        return reportCategories_id;
    }

    public void setReportCategories_id(int reportCategories_id) {
        this.reportCategories_id = reportCategories_id;
    }

    public int getReportCategories_Report_id() {
        return reportCategories_Report_id;
    }

    public void setReportCategories_Report_id(int reportCategories_Report_id) {
        this.reportCategories_Report_id = reportCategories_Report_id;
    }

    public int getReportCategories_Report_UserReports_id() {
        return reportCategories_Report_UserReports_id;
    }

    public void setReportCategories_Report_UserReports_id(int reportCategories_Report_UserReports_id) {
        this.reportCategories_Report_UserReports_id = reportCategories_Report_UserReports_id;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}