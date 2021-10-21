package com.labs.stimaupdate;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.OnLoginFormActivityListener,
        RegistrationFragment.RegisterFormListener,
        HomeFragment.HomeFragmentListener, OutageReportsFrag.OutageReportsListener {

    public static PrefConfig prefConfig;
    public static ApiInterface apiInterface;
    public static List<ReportStatus> reportStatuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String LOG_TAG = MainActivity.class.getSimpleName();
        prefConfig = new PrefConfig(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
            } else if (prefConfig.readingLoginStatus()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, new HomeFragment())
                        .commit();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, new LoginFragment())
                        .commit();
            }
        }
    }

    @Override
    public void performRegister() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new RegistrationFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void performLogin(String firstName, String lastName, String email, int phoneNumber) {
        prefConfig.writeFirstName(firstName);
        prefConfig.writeLastName(lastName);
        prefConfig.writeEmail(email);
        prefConfig.writePhoneNumber(phoneNumber);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
    }

    @Override
    public void BackToLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .commit();
    }

    @Override
    public void LogoutListener() {
        prefConfig.writeLoginStatus(false);
        prefConfig.writeFirstName("user");
        prefConfig.writeLastName("user");
        prefConfig.writeEmail("email");

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .commit();
    }

    @Override
    public void openReportOutageFrag() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ReportOutageFrag())
                .commit();
    }

    @Override
    public void BackFromHomeFragment() {

    }

    @Override
    public void openReportStatusFrag(ReportStatusFrag i) {

    }

    @Override
    public void backFromOutageReportsFrag() {

    }
}
