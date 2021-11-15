package com.labs.stimaupdate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class ReportOutageFrag extends Fragment implements LocationListener {

    double longitude, latitude;
    String email, scope, nature, address;
    int accountNumber;
    ReportOutageActivityListener reportOutageActivityListener;
    LocationManager locationManager;
    private TextInputLayout tilScope;
    private AutoCompleteTextView dpScopes, dpComplaintNature;
    private ArrayList<String> arrayList_scope;
    private ArrayList<String> arrayList_complaintNature;
    private ArrayAdapter<String> arrayAdapter_scope;
    private ArrayAdapter<String> arrayAdapter_complaintNature;
    private TextInputEditText etAccountNumber;
    private Button btnReportComplaint;
    private ProgressDialog progressDialog;
    private MeterAccount meterAccount;
    private BackendlessUser consumer;

    public ReportOutageFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_report_outage, container, false);
        Toolbar toolbarReportOutage = view.findViewById(R.id.toolbarReportOutageFrag);
        reportOutageActivityListener.getLongitudeLatitude();


        etAccountNumber = view.findViewById(R.id.etAccountNumber);
        tilScope = view.findViewById(R.id.tilScope);
        dpComplaintNature = view.findViewById(R.id.dpComplaintNature);
        dpScopes = view.findViewById(R.id.dpScopes);
        btnReportComplaint = view.findViewById(R.id.btnReportComplaint);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbarReportOutage);

        toolbarReportOutage.setTitle("Report Outage");
        toolbarReportOutage.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbarReportOutage.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportOutageActivityListener.backFromReportOutageToDashboard();
            }
        });
        btnReportComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reportOutageActivityListener.getLongitudeLatitude();

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
        arrayList_complaintNature.add("Lines on Ground");
        arrayList_complaintNature.add("Fire");
        arrayList_complaintNature.add("Meter Damaged");
        arrayList_complaintNature.add("Tree Branches on Poweline");
        arrayList_complaintNature.add("Faulty Transformer");

        arrayAdapter_scope = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                arrayList_scope);
        dpScopes.setAdapter(arrayAdapter_scope);
        //how many characters required to load suggestion spinner
        dpScopes.setThreshold(1);
        arrayAdapter_complaintNature = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                arrayList_complaintNature);
        dpComplaintNature.setAdapter(arrayAdapter_complaintNature);
        dpComplaintNature.setThreshold(1);

        return view;
    }

    private void reportOutage() {
        progressDialog = ProgressDialog.show(getContext(), "Reporting Outage...", null, true, true);
        reportOutageActivityListener.getLongitudeLatitude();


        email = MainActivity.prefConfig.readEmail();
        scope = dpScopes.getText().toString();
        nature = dpComplaintNature.getText().toString();
        longitude = MainActivity.longitude;
        latitude = MainActivity.latitude;
        address = MainActivity.address;

        Report report = new Report();

        report.setAddress(address);
        report.setLatitude(latitude);
        report.setLongitude(longitude);
        report.setScope(scope);
        report.setNature(nature);


        // DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        // queryBuilder.setWhereClause(whereClause);

        // queryBuilder.setGroupBy("name");
        //  DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        //List<String> relations = new ArrayList<String>();
        //relations.add( "meterAccountNumber" );
        accountNumber = Integer.parseInt(etAccountNumber.getText().toString());

        String whereClause = "accountNumber = '" + accountNumber + "'";

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);

        Backendless.Data.of(MeterAccount.class).find(queryBuilder, new AsyncCallback<List<MeterAccount>>() {
            @Override
            public void handleResponse(List<MeterAccount> response) {
                if (response.size() == 0) {
                    etAccountNumber.setError("Incorrect meter Account number");
                    progressDialog.dismiss();
                } else {

                    meterAccount = response.get(0);
                    consumer = MainActivity.backendlessUser;
                    //                   progressDialog.dismiss();

                    report.setConsumerId(consumer);
                    report.setMeterAccountId(meterAccount);

                    Backendless.Data.of(Report.class).deepSave(report, new AsyncCallback<Report>() {
                        @Override
                        public void handleResponse(Report response) {
                            MainActivity.prefConfig.displayToast("Outage successfully reported!");
                            progressDialog.dismiss();

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.d("MAINACTIVITY   ", fault.toString());
                            progressDialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                progressDialog.dismiss();
                Log.d("MAINACTIVITY", "Error: " + fault.toString());
            }
        });
        /*
                Backendless.Data.of(Report.class).find(queryBuilder, new AsyncCallback<List<Report>>() {
            @Override
            public void handleResponse(List<Report> response) {
                MainActivity.prefConfig.displayToast("Outage reported Successfully!");
                progressDialog.dismiss();

                for (int i = 0; i < response.size(); i++) {
                    Log.d("MainActivity", "id: " + response.get(i).getId());
                    Log.d("MainActivity", "meterAccountId: " + response.get(i).getMeterAccountId());
                    Log.d("MainActivity", "consumerId: " + response.get(i).getConsumerId());
                    Log.d("MainActivity", "scope: " + response.get(i).getScope());
                    Log.d("MainActivity", "nature: " + response.get(i).getNature());
                    Log.d("MainActivity", "longitude: " + response.get(i).getLongitude());
                    Log.d("MainActivity", "latitude: " + response.get(i).getLatitude());
                    Log.d("MainActivity", "Address: " + response.get(i).getAddress());
                    Log.d("MainActivity", "restored: " + response.get(i).isRestored());
                    Log.d("MainActivity", "technicianOnSite: " + response.get(i).isTechnicianOnSite());
                    Log.d("MainActivity", "restoredDate: " + response.get(i).getRestoredDate());

                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                MainActivity.prefConfig.displayToast("Error!");
                Log.d("Mainactivity", "Error: " + fault.getMessage());
                progressDialog.dismiss();
            }
        });

         */
/*
        Backendless.Persistence.save(report, new AsyncCallback<Report>() {
            @Override
            public void handleResponse(Report response) {
                MainActivity.prefConfig.displayToast("Outage reported Successfully!");
              progressDialog.dismiss();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                MainActivity.prefConfig.displayToast("Error!");
                Log.d("Mainactivity", "Error: " + fault.toString());
                progressDialog.dismiss();
            }
        });


 */
        /*
        MeterAccount metersAccount = new MeterAccount();
        metersAccount.setAccountNumber("123456789");
        metersAccount.setAccountType("Domestic");
        metersAccount.setCounty("Kiambu");
        metersAccount.setTown("Ruaka");

        Backendless.Persistence.save(metersAccount, new AsyncCallback<MeterAccount>() {
            @Override
            public void handleResponse(MeterAccount response) {
                Log.d("Mainactivity",response.toString());
                progressDialog.dismiss();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("Mainactivity",fault.toString());
                progressDialog.dismiss();
            }
        });


         */
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        reportOutageActivityListener = (ReportOutageActivityListener) activity;
    }

    @Override
    public void onLocationChanged(Location location) {
    /*    if (location != null) {
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

     */
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

    public interface ReportOutageActivityListener {
        void getLongitudeLatitude();

        void backFromReportOutageToDashboard();

    }
}
