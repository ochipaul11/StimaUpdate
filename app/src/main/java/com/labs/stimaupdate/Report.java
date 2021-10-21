package com.labs.stimaupdate;

import com.google.gson.annotations.SerializedName;

public class Report {
    @SerializedName("response")
    private String response;
    @SerializedName("accountnumber")
    private int meteraccount;
    @SerializedName("email")
    private String email;
    @SerializedName("scope")
    private String scope;
    @SerializedName("nature")
    private String nature;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("latitude")
    private double latitude;

    public String getResponse() {
        return response;
    }

    public int getMeteraccount() {
        return meteraccount;
    }

    public String getEmail() {
        return email;
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
}
