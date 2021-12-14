package com.labs.stimaupdate;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.material.snackbar.Snackbar;

public class PrefConfig {

    private final SharedPreferences sharedPreferences;
    private final Context context;

    public PrefConfig(Context context) {

        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file), Context.MODE_PRIVATE);

    }

    public void writeLanguageSettings(String language) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_language_setting), language);
        editor.commit();
    }

    public String readLanguageSettings() {
        return sharedPreferences.getString(context.getString(R.string.pref_language_setting), "English");
    }

    public void writeLoginStatus(boolean status) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_login_status), status);
        editor.commit();
    }

    public boolean readingLoginStatus() {

        return sharedPreferences.getBoolean(context.getString(R.string.pref_login_status), false);
    }

    public void writeFirstName(String firstName) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_first_name), firstName);
        editor.commit();
    }

    public void writeLastName(String lastName) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_last_name), lastName);
        editor.commit();
    }

    public void writeConsumerId(String id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_consumer_id), id);
    }

    public void writeEmail(String email) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_email), email);
        editor.commit();
    }

    public void writePhoneNumber(String phoneNumber) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_phone_number), phoneNumber);
    }


    public String readFirstName() {
        return sharedPreferences.getString(context.getString(R.string.pref_first_name), "firstName");
    }

    public String readLastName() {
        return sharedPreferences.getString(context.getString(R.string.pref_last_name), "lastName");
    }

    public String readConsumerId() {
        return sharedPreferences.getString(context.getString(R.string.pref_consumer_id), "consumerId");
    }
    public String readPhoneNumber() {
        return sharedPreferences.getString(context.getString(R.string.pref_phone_number), "phoneNumber");
    }

    public String readEmail() {
        return sharedPreferences.getString(context.getString(R.string.pref_email), "email");
    }

    public void displayToast(String message) {
        // Toast.makeText(context, message, Toast.LENGTH_LONG).show();
       Snackbar snackbar = Snackbar
                .make(MainActivity.linearLayout, message, Snackbar.LENGTH_LONG)
                .setDuration(10000);
        snackbar.show();




    }


}
