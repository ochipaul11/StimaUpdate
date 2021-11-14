package com.labs.stimaupdate;

import com.google.gson.annotations.SerializedName;

public class Coordinates {
   @SerializedName("latitude")
    double latitude;
   @SerializedName("longitude")
    double longitude;

    public Coordinates(double lat, double lng) {
        this.latitude = lat;
        this.longitude = lng;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
