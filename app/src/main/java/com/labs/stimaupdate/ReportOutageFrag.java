package com.labs.stimaupdate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class ReportOutageFrag extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextInputLayout tilScope;
    AutoCompleteTextView dpScopes, dpComplaintNature;
    ArrayList<String> arrayList_scope;
    ArrayList<String> arrayList_complaintNature;
    ArrayAdapter<String> arrayAdapter_scope;

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

        tilScope = view.findViewById(R.id.tilScope);
        dpComplaintNature = view.findViewById(R.id.dpComplaintNature);
        dpScopes = view.findViewById(R.id.dpScopes);

        arrayList_scope = new ArrayList<>();
        arrayList_scope.add("House");
        arrayList_scope.add("Estate");
        arrayList_scope.add("Flat");
        arrayList_scope.add("Club");
        arrayList_scope.add("Hospital");

        arrayAdapter_scope = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                arrayList_scope);
        dpScopes.setAdapter(arrayAdapter_scope);
        dpScopes.setThreshold(1); //how many characters required to load suggestion spinner
        return view;
    }
}
