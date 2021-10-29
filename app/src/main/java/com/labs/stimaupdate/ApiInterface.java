package com.labs.stimaupdate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("register.php")
    Call<User> performRegistration(@Query("fname") String fname,
                                   @Query("lname") String lname,
                                   @Query("email") String email,
                                   @Query("phonenumber") int phonenumber,
                                   @Query("password") String password);

    @GET("login.php")
    Call<User> performUserLogin(@Query("email") String email,
                                @Query("password") String password);

    @GET("getoutagereports.php")
    Call<List<ReportStatus>> getOutageReports(@Query("email") String email);

    @GET("reportanoutage.php")
    Call<Report> reportAnOutage(@Query("accountnumber") int meteraccount,
                                @Query("email") String email,
                                @Query("scope") String scope,
                                @Query("nature") String nature,
                                @Query("longitude") double longitude,
                                @Query("latitude") double latitude,
                                @Query("address") String address);


}
