package com.labs.stimaupdate;

import com.google.gson.annotations.SerializedName;

public class ReportStatus {
    @SerializedName("id")
    private int reportNumber;

    @SerializedName("address")
    private String address ;

    @SerializedName("report_received_date")
    private String reportReceivedDate;

    @SerializedName("technician_on_site")
    private int technicianOnSite;

    @SerializedName("restored")
    private int restored;

    public ReportStatus() {
    }

    public ReportStatus(int reportNumber, String address, String reportReceivedDate, int technicianOnSite, int restored) {
        this.reportNumber = reportNumber;
        this.address = address;
        this.reportReceivedDate = reportReceivedDate;
        this.technicianOnSite = technicianOnSite;
        this.restored = restored;
    }

    public int getReportNumber() {
        return reportNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getReportReceivedDate() {
        return reportReceivedDate;
    }

    public int getTechnicianOnSite() {
        return technicianOnSite;
    }

    public int getRestored() {
        return restored;
    }
}
