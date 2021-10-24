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

    public User(String response, String fname, String lname, String email, int phonenumber) {
        Response = response;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phonenumber = phonenumber;
    }

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
