package com.labs.stimaupdate;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("response")
    private String Response;

    @SerializedName("fname")
    private String fname;

    @SerializedName("lname")
    private String lname;

    @SerializedName("email")
    private String email;

    @SerializedName("phonenumber")
    private int phonenumber;

    public String getResponse() {
        return Response;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }

    public int getPhonenumber() {
        return phonenumber;
    }
}
