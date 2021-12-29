package com.labs.stimaupdate;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.OnLoginFormActivityListener,
        UserRegistrationFragment.RegisterFormListener,
        DashboardFrag.DashboardFragmentListener,
        OutageReportsFrag.OutageReportsListener,
        ReportOutageFrag.ReportOutageActivityListener,
        HeatMapsFragment.HeatMapFragLister, FieldAdminMapsFragment.OnFieldAdminMapsFragListener,
        FieldAdminLoginFragment.OnFieldAdminLoginListener,
        FieldAdminDashboardFragment.FieldAdminDashboardFragListener,
        FieldAdminProfileFragment.FieldAdminProfileFragListener,
        MyProfileFrag.MyprofileFragListener {

    public static PrefConfig prefConfig;
    public static ReportAdapter reportAdapter;
    public static List<Report> reports;

    public static double latitude;
    public static double longitude;
    public static String address;
    public static LinearLayout linearLayout;
    public static BackendlessUser backendlessUser;

    public LocationManager locationManager;
    public LocationListener locationListener;
    Geocoder geocoder;
    List<Address> myAddress;
    ProgressDialog progressDialog;
    private boolean gps_enable = false;
    private boolean network_enable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String LOG_TAG = MainActivity.class.getSimpleName();
        prefConfig = new PrefConfig(this);
        locationListener = new MyLocationListener();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        linearLayout = findViewById(R.id.linearLayoutMainActivity);
/*
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

 */
        if (findViewById(R.id.fragment_container) != null) {

            progressDialog = ProgressDialog.show(this, "Loading...", null, true, true);
            Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
                @Override
                public void handleResponse(Boolean response) {
                    if (response) {
                        String userObjectID = UserIdStorageFactory.instance().getStorage().get();

                        Backendless.Data.of(BackendlessUser.class).findById(userObjectID, new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {
                                backendlessUser = response;
                                progressDialog.dismiss();
                                if (response.getEmail().equals(prefConfig.readEmail()) && prefConfig.readRole().equals("admin")) {
                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .add(R.id.fragment_container, new FieldAdminDashboardFragment())
                                            .commit();
                                }
                                else if(response.getEmail().equals(prefConfig.readEmail()) && prefConfig.readRole().equals("customer")){
                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .add(R.id.fragment_container, new DashboardFrag())
                                            .commit();
                                }
                                else {
                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .add(R.id.fragment_container, new LoginFragment())
                                            .commit();
                                }
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                MainActivity.prefConfig.displayToast("Error: " + fault.getMessage());
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .add(R.id.fragment_container, new LoginFragment())
                                        .commit();
                                progressDialog.dismiss();
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.fragment_container, new LoginFragment())
                                .commit();

                    }
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    progressDialog.dismiss();
                    MainActivity.prefConfig.displayToast("Error: " + fault.getMessage());

                }
            });

        }

    }


    @Override
    public void performRegister() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                )
                .replace(R.id.fragment_container, new UserRegistrationFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openFieldAdminLoginFrag() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new FieldAdminLoginFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void performLogin(String firstName, String lastName, String email, String phoneNumber, String consumerId) {
        prefConfig.writeFirstName(firstName);
        prefConfig.writeLastName(lastName);
        prefConfig.writeEmail(email);
        prefConfig.writePhoneNumber(phoneNumber);
        prefConfig.writeConsumerId(consumerId);
        prefConfig.writeRole("customer");

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new DashboardFrag())
                .commit();

    }

    @Override
    public void BackToLoginFromRegistrationFrag() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                )
                .replace(R.id.fragment_container, new LoginFragment())
                .commit();
    }

    @Override
    public void LogoutListener() {
        prefConfig.writeLoginStatus(false);
        prefConfig.writeFirstName("user");
        prefConfig.writeLastName("user");
        prefConfig.writeEmail("email");
        prefConfig.writePhoneNumber("");
        prefConfig.writeRole("");
        backendlessUser = null;
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
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openMyProfileFrag() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new MyProfileFrag())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openHeatMap() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new HeatMapsFragment())
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
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, i)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openHistoryOfRestoredFrag() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new HistoryOfRestoredFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void backFromOutageReportsFrag() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new DashboardFrag())
                .commit();
    }

    /*
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
     */
    @Override
    public void getLongitudeLatitude() {
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

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        if (network_enable) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    public void backFromReportOutageToDashboard() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_out,  // enter
                        // popEnter
                        R.anim.slide_in  // popExit
                )
                .replace(R.id.fragment_container, new DashboardFrag())
                .commit();
    }

    @Override
    public void backFromHeatMapFragToDashboard() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new DashboardFrag())
                .commit();
    }

    @Override
    public void backFromMyProfileToDashboard() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new DashboardFrag())
                .commit();
    }

    @Override
    public void updateMyProfile(String firstName, String lastName, String email, String phoneNumber, String consumerId) {
        prefConfig.writeFirstName(firstName);
        prefConfig.writeLastName(lastName);
        prefConfig.writeEmail(email);
        prefConfig.writePhoneNumber(phoneNumber);
        prefConfig.writeConsumerId(consumerId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new DashboardFrag())
                .commit();
    }

    @Override
    public void resetPassword() {
        prefConfig.writeLoginStatus(false);
        prefConfig.writeFirstName("user");
        prefConfig.writeLastName("user");
        prefConfig.writeEmail("email");
        prefConfig.writePhoneNumber("");
        backendlessUser = null;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .commit();
    }

    @Override
    public void performFieldAdminLogin(String firstName, String lastName, String email, String phoneNumber, String consumerId) {
        prefConfig.writeFirstName(firstName);
        prefConfig.writeLastName(lastName);
        prefConfig.writeEmail(email);
        prefConfig.writePhoneNumber(phoneNumber);
        prefConfig.writeConsumerId(consumerId);
        prefConfig.writeRole("admin");

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new FieldAdminDashboardFragment())
                .commit();
    }

    @Override
    public void backFromAdminLoginToLoginFrag() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .commit();
    }

    @Override
    public void performFieldAdminLogout() {
        prefConfig.writeLoginStatus(false);
        prefConfig.writeFirstName("user");
        prefConfig.writeLastName("user");
        prefConfig.writeEmail("email");
        prefConfig.writePhoneNumber("");
        prefConfig.writeRole("");
        backendlessUser = null;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .commit();
    }

    @Override
    public void openFieldAdmnProfileFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new FieldAdminProfileFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openFieldAdminReports() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new FieldAdminReportsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void backFromFieldAdminProfileToDashboard() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_out,  // enter
                        // popEnter
                        R.anim.slide_in  // popExit
                )
                .replace(R.id.fragment_container, new FieldAdminDashboardFragment())
                .commit();
    }

    @Override
    public void updateFieldAdminProfile(String firstName, String lastName, String email, String phoneNumber, String consumerId) {
        prefConfig.writeFirstName(firstName);
        prefConfig.writeLastName(lastName);
        prefConfig.writeEmail(email);
        prefConfig.writePhoneNumber(phoneNumber);
        prefConfig.writeConsumerId(consumerId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new FieldAdminDashboardFragment())
                .commit();
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                locationManager.removeUpdates(locationListener);
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                try {
                    myAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
                    //               Log.d("Mainactivity", "Address:              "+myAddress.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                address = myAddress.get(2).getAddressLine(0);
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
