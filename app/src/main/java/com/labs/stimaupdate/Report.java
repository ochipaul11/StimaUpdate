package com.labs.stimaupdate;


public class Report {

    private int id;
    private int meterAccountId;
    private int customerId;
    private String scope;
    private String nature;
    private double longitude;
    private double latitude;
    private String address;
    private boolean restored;
    private boolean technicianOnSite;
    private String restoredDate;

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScope() {
        return scope;
    }

    public String getNature() {
        return nature;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public int getMeteraccountid() {
        return meterAccountId;
    }

    public void setMeteraccountid(int meteraccountid) {
        this.meterAccountId = meteraccountid;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMeterAccountId() {
        return meterAccountId;
    }

    public void setMeterAccountId(int meterAccountId) {
        this.meterAccountId = meterAccountId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public boolean isRestored() {
        return restored;
    }

    public void setRestored(boolean restored) {
        this.restored = restored;
    }

    public boolean isTechnicianOnSite() {
        return technicianOnSite;
    }

    public void setTechnicianOnSite(boolean technicianOnSite) {
        this.technicianOnSite = technicianOnSite;
    }

    public String getRestoredDate() {
        return restoredDate;
    }

    public void setRestoredDate(String restoredDate) {
        this.restoredDate = restoredDate;
    }
}
