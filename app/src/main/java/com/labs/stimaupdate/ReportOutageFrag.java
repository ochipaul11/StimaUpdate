package com.labs.stimaupdate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

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
    private ProgressDialog progressDialog;

    public ReportOutageFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_report_outage, container, false);
        Toolbar toolbarReportOutage = view.findViewById(R.id.toolbarReportOutageFrag);

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
        dpScopes.setThreshold(1); //how many characters required to load suggestion spinner

        arrayAdapter_complaintNature = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                arrayList_complaintNature);
        dpComplaintNature.setAdapter(arrayAdapter_complaintNature);
        dpComplaintNature.setThreshold(1);

        return view;
    }

    private void reportOutage() {
        progressDialog = ProgressDialog.show(getContext(), "Reporting Outage...", null, true, true);
   /*     reportOutageActivityListener.getLongitudeLatitude();
        accountNumber = etAccountNumber.getInputType();
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
        report.setMeteraccountid(accountNumber);
*/
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


        /*
        Contact contact = new Contact();
        contact.setName("Paul");
        contact.setEmail(email);
        contact.setNumber("0798124123");
        contact.setUserEmail("34340jhfhdf@gmail.com");

        Backendless.Persistence.save(contact, new AsyncCallback<Contact>() {
            @Override
            public void handleResponse(Contact response) {
                Log.d("Mainactivity",response.toString());
                progressDialog.dismiss();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("Mainactivity","Error: "+fault.toString());
                progressDialog.dismiss();
            }
        });


         */

        /*
        Meter meter = new Meter();
        meter.setMeterNumber(3451909);
        meter.setTown("Ruaka");
        meter.setCounty("Kiambu");

        Backendless.Persistence.save(meter, new AsyncCallback<Meter>() {
            @Override
            public void handleResponse(Meter response) {
                Log.d("Mainactivity",response.toString());
progressDialog.dismiss();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("Mainactivity","Error: "+fault.toString());
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

    public interface ReportOutageActivityListener {
        void getLongitudeLatitude();

        void backFromReportOutageToDashboard();

    }
}
