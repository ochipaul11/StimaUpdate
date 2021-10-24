package com.labs.stimaupdate;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class ReportOutageFrag extends Fragment  {

    private FusedLocationProviderClient client;
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
 /*       if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
*/
        View view = inflater.inflate(R.layout.fragment_report_outage, container, false);

        etAccountNumber = view.findViewById(R.id.etAccountNumber);

        tilScope = view.findViewById(R.id.tilScope);
        dpComplaintNature = view.findViewById(R.id.dpComplaintNature);
        dpScopes = view.findViewById(R.id.dpScopes);
        btnReportComplaint = view.findViewById(R.id.btnReportComplaint);

client = LocationServices.getFusedLocationProviderClient(getContext());

        btnReportComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
getLongitudeLatitude();


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

    private void getLongitudeLatitude() {
        client.getLastLocation().addOnSuccessListener((Activity) getContext(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

            }
        });
    }


    //public interface onReportOutageActivityListener{

  //  }
}
