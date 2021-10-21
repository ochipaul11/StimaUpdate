package com.labs.stimaupdate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportOutageFrag extends Fragment implements LocationListener {

    LocationManager locationManager;
    double longitude, latitude = 0.00;
    String email, scope, nature;
    private TextInputLayout tilScope;
    private AutoCompleteTextView dpScopes, dpComplaintNature;
    private ArrayList<String> arrayList_scope;
    private ArrayList<String> arrayList_complaintNature;
    private ArrayAdapter<String> arrayAdapter_scope;
    private ArrayAdapter<String> arrayAdapter_complaintNature;
    private TextInputEditText etAccountNumber;
    private Button btnReportComplaint;

    public ReportOutageFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//LOCATION SERVICE RUNTIME PEROMISSIONS
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }

        View view = inflater.inflate(R.layout.fragment_report_outage, container, false);

        etAccountNumber = view.findViewById(R.id.etAccountNumber);

        tilScope = view.findViewById(R.id.tilScope);
        dpComplaintNature = view.findViewById(R.id.dpComplaintNature);
        dpScopes = view.findViewById(R.id.dpScopes);
        btnReportComplaint = view.findViewById(R.id.btnReportComplaint);



        btnReportComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportOutage();


            }
        });


        arrayList_scope = new ArrayList<>();
        arrayList_scope.add("House");
        arrayList_scope.add("Estate");
        arrayList_scope.add("Flat");
        arrayList_scope.add("Club");
        arrayList_scope.add("Hospital");

        arrayList_complaintNature = new ArrayList<>();
        arrayList_complaintNature.add("No Supply");
        arrayList_complaintNature.add("Irregular Supply");
        arrayList_complaintNature.add("Lines on Groung");
        arrayList_complaintNature.add("Fire");
        arrayList_complaintNature.add("Meter Damaged");
        arrayList_complaintNature.add("Tree Branches on Poweline");
        arrayList_complaintNature.add("Faulty Transformer");

        arrayAdapter_scope = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                arrayList_scope);
        dpScopes.setAdapter(arrayAdapter_scope);
        dpScopes.setThreshold(1); //how many characters required to load suggestion spinner

        arrayAdapter_complaintNature = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                arrayList_complaintNature);
        dpComplaintNature.setAdapter(arrayAdapter_complaintNature);
        dpComplaintNature.setThreshold(1);

        return view;
    }

    @SuppressLint("MissingPermission")
    private void reportOutage() {
        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, (LocationListener) getContext());

        } catch (Exception e) {
            e.printStackTrace();
        }

        String email, scope, nature;

        email = MainActivity.prefConfig.readEmail();
        scope = dpScopes.getText().toString();
        nature = dpComplaintNature.getText().toString();
        int accountnumber = etAccountNumber.getInputType();
        Call<Report> call = MainActivity.apiInterface.reportAnOutage(accountnumber, email, scope, nature, longitude,latitude);
        call.enqueue(new Callback<Report>() {
            @Override
            public void onResponse(Call<Report> call, Response<Report> response) {
                if (response.body().getResponse().equals("ok")) {
                    //STOP PROGREES POP UP
                    MainActivity.prefConfig.displayToast("Outage reported Successfully!");
                }
                else if(response.body().getResponse().equals("exists")){
                    MainActivity.prefConfig.displayToast("Account Number Does Not Exist!");
                }
                else if(response.body().getResponse().equals("error")){
                    MainActivity.prefConfig.displayToast("Database Error!");
                }
            }

            @Override
            public void onFailure(Call<Report> call, Throwable t) {
                Log.d("MainActivity", t.getMessage());
                MainActivity.prefConfig.displayToast(t.getMessage());
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        try {

        } catch (Exception e) {
            e.printStackTrace();
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

    //public interface onReportOutageActivityListener{

  //  }
}
