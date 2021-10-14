package com.labs.stimaupdate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportOutageFrag extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextInputLayout tilScope;
    AutoCompleteTextView dpScopes, dpComplaintNature;
    ArrayList<String> arrayList_scope;
    ArrayList<String> arrayList_complaintNature;
    ArrayAdapter<String> arrayAdapter_scope;
    ArrayAdapter<String> arrayAdapter_complaintNature;
    TextInputEditText etAccountNumber;
    Button btnReportComplaint;

    public ReportOutageFrag() {
        // Required empty public constructor
    }

    public static ReportOutageFrag newInstance(String param1, String param2) {
        ReportOutageFrag fragment = new ReportOutageFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

        final int meterAccount = Integer.parseInt(etAccountNumber.getText().toString());
        btnReportComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Account> call = MainActivity.apiInterface.checkMeterExists(meterAccount);
                call.enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        if (response.body().getResponse().equals("ok")) {

                        } else {
                            etAccountNumber.setError("Meter number does not exist!");
                        }
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        MainActivity.prefConfig.displayToast(t.getMessage());
                    }
                });
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
}
