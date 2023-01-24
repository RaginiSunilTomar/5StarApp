package com.ovot.fiveStarapp_test1;

import android.content.Context;

public class JobLogs {
    Context context;
    String  updateDate, statusCode, description,subhead,bill_type;
    int jobLogID;


    public JobLogs(Context context, String updateDate, String statusCode, String description, String subhead, String bill_type, int jobLogID) {
        this.context = context;
        this.updateDate = updateDate;
        this.statusCode = statusCode;
        this.description = description;
        this.subhead = subhead;
        this.bill_type = bill_type;
        this.jobLogID = jobLogID;
    }

    public String getSubhead() {
        return subhead;
    }

    public void setSubhead(String subhead) {
        this.subhead = subhead;
    }

    public String getBill_type() {
        return bill_type;
    }

    public void setBill_type(String bill_type) {
        this.bill_type = bill_type;
    }

    public JobLogs(Context context) {
        this.context = context;
    }

    public int getJobLogID() {
        return jobLogID;
    }

    public void setJobLogID(int jobLogID) {
        this.jobLogID = jobLogID;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
