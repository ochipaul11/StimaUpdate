package com.labs.stimaupdate;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class FieldAdminDashboardFragment extends Fragment {
    Toolbar toolbarFieldAdminDashboard;
    TextView tvNewOutages, tvSolvedCases;
    ProgressDialog progressDialog;

    public FieldAdminDashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_admin_dashboard, container, false);

        toolbarFieldAdminDashboard = view.findViewById(R.id.toolbarFieldAdminDashboard);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbarFieldAdminDashboard);
        toolbarFieldAdminDashboard.setTitle("STIMA UPDATE: Field Admin");

        tvNewOutages = view.findViewById(R.id.tvNewOutages);
        tvSolvedCases = view.findViewById(R.id.tvSolvedCases);

        return view;
    }
}