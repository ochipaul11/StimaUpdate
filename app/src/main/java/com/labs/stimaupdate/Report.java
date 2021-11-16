package com.labs.stimaupdate;


import com.backendless.BackendlessUser;

import java.util.Date;

public class Report {

    private int id;
    Date created;
    Date updated;
    private String scope;
    private String nature;
    private double longitude;
    private double latitude;
    private String address;
    private boolean restored;
    private boolean technicianOnSite;
    String objectId;
    String ownerId;
    private MeterAccount meterAccountId;
    private BackendlessUser consumerId;
    private Date restoredDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public MeterAccount getMeterAccountId() {
        return meterAccountId;
    }

    public void setMeterAccountId(MeterAccount meterAccountId) {
        this.meterAccountId = meterAccountId;
    }

    public BackendlessUser getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(BackendlessUser consumerId) {
        this.consumerId = consumerId;
    }

    public Date getRestoredDate() {
        return restoredDate;
    }

    public void setRestoredDate(Date restoredDate) {
        this.restoredDate = restoredDate;
    }
}
