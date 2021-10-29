package com.labs.stimaupdate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportOutageFrag extends Fragment {

    double longitude, latitude = 0.00;
    String email, scope, nature, address;
    int accountNumber;
    ReportOutageActivityListener reportOutageActivityListener;
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

        View view = inflater.inflate(R.layout.fragment_report_outage, container, false);

        etAccountNumber = view.findViewById(R.id.etAccountNumber);
        tilScope = view.findViewById(R.id.tilScope);
        dpComplaintNature = view.findViewById(R.id.dpComplaintNature);
        dpScopes = view.findViewById(R.id.dpScopes);
        btnReportComplaint = view.findViewById(R.id.btnReportComplaint);

        btnReportComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reportOutageActivityListener.getLatitudeLogitude();

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
        dpScopes.setThreshold(1); //how many characters required to load suggestion spinner

        arrayAdapter_complaintNature = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                arrayList_complaintNature);
        dpComplaintNature.setAdapter(arrayAdapter_complaintNature);
        dpComplaintNature.setThreshold(1);

        return view;
    }

    private void reportOutage() {
        reportOutageActivityListener.getLatitudeLogitude();
        accountNumber = Integer.parseInt(etAccountNumber.getText().toString());
        email = MainActivity.prefConfig.readEmail();
        scope = dpScopes.getText().toString();
        nature = dpComplaintNature.getText().toString();
        longitude = MainActivity.longitude;
        latitude = MainActivity.latitude;
        address = MainActivity.address;


        Call<Report> call = MainActivity.apiInterface.reportAnOutage(accountNumber, email, scope, nature, longitude, latitude, address);
        call.enqueue(new Callback<Report>() {
            @Override
            public void onResponse(Call<Report> call, Response<Report> response) {
                if (response.body().getResponse().equals("ok")) {
                    MainActivity.prefConfig.displayToast("Outage Reported Successfully!");
                } else if (response.body().getResponse().equals("account Number does not exist")) {
                    MainActivity.prefConfig.displayToast("Account Number does not exist!");
                } else if (response.body().getResponse().equals("error from system")) {
                    MainActivity.prefConfig.displayToast("Error from the system!");
                } else {
                    MainActivity.prefConfig.displayToast("Did not collect location data!");
                }
            }

            @Override
            public void onFailure(Call<Report> call, Throwable t) {
                MainActivity.prefConfig.displayToast(t.getMessage());
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        reportOutageActivityListener = (ReportOutageActivityListener) activity;
    }

    public interface ReportOutageActivityListener {
        void getLatitudeLogitude();

    }
}
