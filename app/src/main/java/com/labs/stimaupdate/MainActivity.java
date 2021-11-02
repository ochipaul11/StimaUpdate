package com.labs.stimaupdate;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.OnLoginFormActivityListener,
        RegistrationFragment.RegisterFormListener,
        DashboardFrag.DashboardFragmentListener,
        OutageReportsFrag.OutageReportsListener, ReportOutageFrag.ReportOutageActivityListener {

    public static PrefConfig prefConfig;
    public static ApiInterface apiInterface;
    public static List<ReportStatus> reportStatuses;
    public static ReportStatusAdapter reportStatusAdapter;
    public static double latitude;
    public static double longitude;
    public static String address;
    public LocationManager locationManager;
    public LocationListener locationListener = new MyLocati0nListener();
    Geocoder geocoder;
    List<Address> myAddress;
    private boolean gps_enable = false;
    private boolean network_enable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String LOG_TAG = MainActivity.class.getSimpleName();
        prefConfig = new PrefConfig(this);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
            } else if (prefConfig.readingLoginStatus()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, new DashboardFrag())
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
                .replace(R.id.fragment_container, new DashboardFrag())
                .commit();
    }

    @Override
    public void BackToLoginFromRegistrationFrag() {
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
        checkLocationPermission();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ReportOutageFrag())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openOutageReportsFrag() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new OutageReportsFrag())
                .addToBackStack(null)
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


    private boolean checkLocationPermission() {
        int location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int location2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> listPermission = new ArrayList<>();

        if (location != PackageManager.PERMISSION_GRANTED) {
            listPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (location2 != PackageManager.PERMISSION_GRANTED) {
            listPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermission.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermission.toArray(new String[listPermission.size()]), 1);
        }
        return true;
    }

    @Override
    public void getLatitudeLogitude() {
        try {
            gps_enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {

        }
        try {
            network_enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {

        }

        if (!gps_enable && !network_enable) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("Attention");
            builder.setMessage("Please enable location service!");
            builder.create().show();
        }
        if (gps_enable) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        }
        if (network_enable) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }


    private class MyLocati0nListener implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                locationManager.removeUpdates(locationListener);
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                try {
                    myAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                address = myAddress.get(0).getAddressLine(0);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }


}
