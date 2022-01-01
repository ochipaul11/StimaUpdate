package com.labs.stimaupdate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class FieldAdminReportsFragment extends Fragment {
    ProgressDialog progressDialog;
    Toolbar toolbarFieldAdminReports;
    FieldAdminReportsFragmentListener fieldAdminReportsFragmentListener;
    PieChart chart;
    TextView tvBtnFieldAminNatureOfOutage, tvBtnFieldAdminScopeofOutage;
    List<Report> reports = new ArrayList<>();

    public FieldAdminReportsFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        fieldAdminReportsFragmentListener = (FieldAdminReportsFragmentListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_admin_reports, container, false);

        toolbarFieldAdminReports = view.findViewById(R.id.toolbarFieldAdminReports);
        toolbarFieldAdminReports.setTitle("My Reports");
        toolbarFieldAdminReports.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbarFieldAdminReports.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldAdminReportsFragmentListener.backToFieldAdminDashboardFromReportsFrag();
            }
        });

        chart = view.findViewById(R.id.chartNewAndRestoredCases);

        getChart();

        tvBtnFieldAdminScopeofOutage = view.findViewById(R.id.tvScopeOfOutage);
        tvBtnFieldAminNatureOfOutage = view.findViewById(R.id.tvNatureOfOutage);

        tvBtnFieldAdminScopeofOutage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldAdminReportsFragmentListener.openFieldAdminScopeOfOutage();

            }
        });

        tvBtnFieldAminNatureOfOutage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldAdminReportsFragmentListener.openFieldAdminNatureOfOutage();
            }
        });

        return view;
    }

    private void getChart() {
        progressDialog = ProgressDialog.show(getContext(), "Loading chart...", null, true, true);
        Backendless.Data.of(Report.class).find(new AsyncCallback<List<Report>>() {
            @Override
            public void handleResponse(List<Report> response) {
                reports = response;
                progressDialog.dismiss();
                Log.d("Responce count.............", String.valueOf(response.size()));


                int restored = 0;
                int activecase = 0;
                for (int i = 0; i < reports.size(); i++) {

                    if (response.get(i).isRestored()) {
                        restored = restored + 1;
                        Log.d("Restored.............", String.valueOf(restored));

                    } else {
                        activecase = activecase + 1;
                        Log.d("Active cases.............", String.valueOf(activecase));

                    }
                }
                Log.d("Restored", String.valueOf(restored));
                Log.d("Responce count.............", String.valueOf(reports.size()));

                String[] labels = {"Restored", "Active Cases"};
                int[] figures = {restored, activecase};
                List<PieEntry> pieEntries = new ArrayList<>();

                for (int i = 0; i < labels.length; i++) {
                    pieEntries.add(new PieEntry(figures[i], labels[i]));
                    Log.d("Entry........", String.valueOf(figures[i]));
                }

                PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
                pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                PieData data = new PieData(pieDataSet);
                pieDataSet.setValueTextSize(16.0F);
                pieDataSet.setValueFormatter(new DefaultValueFormatter(0));

                Description description = new Description();
                description.setText("ACTIVE AND RESTORED CASES");
                chart.setDescription(description);
                chart.setData(data);
                chart.animateXY(2000, 2000);
                chart.setEntryLabelTextSize(16.0F);
                chart.invalidate();

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                MainActivity.prefConfig.displayToast("Error: " + fault.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    public interface FieldAdminReportsFragmentListener {
        void backToFieldAdminDashboardFromReportsFrag();

        void openFieldAdminScopeOfOutage();

        void openFieldAdminNatureOfOutage();
    }
}