package com.labs.stimaupdate;

import java.util.Date;

public class ReportStatus {
    private int reportNumber;
    private String reportStatus;
    private Date reportDate;
    private String outageReceived;
    private String technicianOnSite;
    private String restored;

    public int getReportNumber() {
        return reportNumber;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public String getOutageReceived() {
        return outageReceived;
    }

    public String getTechnicianOnSite() {
        return technicianOnSite;
    }

    public String getRestored() {
        return restored;
    }
}
