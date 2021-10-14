package com.labs.stimaupdate;

import com.google.gson.annotations.SerializedName;

public class Account {
    @SerializedName("response")
    private String response;
    @SerializedName("accountnumber")
    private int accountNumber;

    public String getResponse() {
        return response;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

}
