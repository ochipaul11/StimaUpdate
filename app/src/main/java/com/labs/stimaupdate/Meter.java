package com.labs.stimaupdate;

import java.util.Date;

class Meter
{
    int meterNumber;
    String town;
    String county;
    Date created;
    Date updated;
    String objectId;


    public int getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(int meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
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

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
