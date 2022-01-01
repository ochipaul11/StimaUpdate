package com.labs.stimaupdate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class FieldAdminNatureOfOutageReportFragment extends Fragment {

    AutoCompleteTextView dpYear, dpMonth;
    ProgressDialog progressDialog;
    PieChart chart;
    Button btnFieldAdminNatureOfOutageGenerateReport;
    TextView tvnatureOfOutageNoSupply, tvNatureOfOutageIrregularSupply, tvNatureOfOutageFire, tvNatureOfOutageLinesOnGround,
            tvNatureOfOutageFaultyTransfomer, tvNatureOfOutageMeterDamaged;
    Toolbar toolbarFieldAdminNatureOfOutage;
    FieldAdminNatureOfOutageReportFragListener fieldAdminNatureOfOutageReportFragListener;

    public FieldAdminNatureOfOutageReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        fieldAdminNatureOfOutageReportFragListener =
                (FieldAdminNatureOfOutageReportFragListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_admin_nature_of_outage_report, container, false);

        toolbarFieldAdminNatureOfOutage = view.findViewById(R.id.toolbarFieldAdminNatureOfOutage);
        toolbarFieldAdminNatureOfOutage.setTitle("Nature of Outage");
        toolbarFieldAdminNatureOfOutage.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbarFieldAdminNatureOfOutage.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldAdminNatureOfOutageReportFragListener.backToFieldAdminReportsFromNatureOfOutageReports();
            }
        });

        dpYear = view.findViewById(R.id.dpYearsNatureofOutage);
        dpMonth = view.findViewById(R.id.dpMonthsNatureOfOutage);

        tvNatureOfOutageFaultyTransfomer = view.findViewById(R.id.tvNatureOfOutageFaultyTransfomer);
        tvNatureOfOutageFire = view.findViewById(R.id.tvNatureOfOutageFire);
        tvNatureOfOutageIrregularSupply = view.findViewById(R.id.tvNatureOfOutageIrregularSupply);
        tvNatureOfOutageMeterDamaged = view.findViewById(R.id.tvNatureOfOutageMeterDamaged);
        tvnatureOfOutageNoSupply = view.findViewById(R.id.tvnatureOfOutageNoSupply);
        tvNatureOfOutageLinesOnGround = view.findViewById(R.id.tvNatureOfOutageLinesOnGround);

        chart = view.findViewById(R.id.pcNatureOfOutage);

        ArrayList<String> arrayListYears = new ArrayList<>();
        ArrayAdapter<String> arrayAdapterYears = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item, arrayListYears);

        ArrayList<String> arrayListMonths = new ArrayList<>();
        ArrayAdapter<String> arrayAdapterMonths = new ArrayAdapter<>(getContext(),
                R.layout.support_simple_spinner_dropdown_item, arrayListMonths);

        arrayListYears.add("2020");
        arrayListYears.add("2021");
        arrayListYears.add("2022");

        arrayListMonths.add("01");
        arrayListMonths.add("02");
        arrayListMonths.add("03");
        arrayListMonths.add("04");
        arrayListMonths.add("05");
        arrayListMonths.add("06");
        arrayListMonths.add("07");
        arrayListMonths.add("08");
        arrayListMonths.add("09");
        arrayListMonths.add("10");
        arrayListMonths.add("11");
        arrayListMonths.add("12");

        dpYear.setAdapter(arrayAdapterYears);
        dpYear.setThreshold(1);

        dpMonth.setAdapter(arrayAdapterMonths);
        dpMonth.setThreshold(1);

        btnFieldAdminNatureOfOutageGenerateReport = view.findViewById(R.id.btnFieldAdminNatureOfOutageGenerateReport);
        btnFieldAdminNatureOfOutageGenerateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChart();
            }
        });

        return view;
    }

    void getChart() {
        progressDialog = ProgressDialog.show(getContext(), "Loading chart...", null, true, true);
        String year = dpYear.getText().toString();
        String month = dpMonth.getText().toString();

        String WHERECLAUSE = "created >= '" + month + "/01/" + year + "' AND created <= '" + month + "/30/" + year + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(WHERECLAUSE);

        Backendless.Data.of(Report.class).find(queryBuilder, new AsyncCallback<List<Report>>() {
            @Override
            public void handleResponse(List<Report> response) {

                int noSupply = 0, irregularSupply = 0, linesOnGround = 0, fire = 0, meterDamaged = 0, faultyTransformer = 0;

                for (int i = 0; i < response.size(); i++) {
                    if (response.get(i).getNature().matches("No Supply")) {
                        noSupply = noSupply + 1;
                    } else if (response.get(i).getNature().matches("Irregular Supply")) {
                        irregularSupply = irregularSupply + 1;
                    } else if (response.get(i).getNature().matches("Lines on Ground")) {
                        linesOnGround = linesOnGround + 1;
                    } else if (response.get(i).getNature().matches("Fire")) {
                        fire = fire + 1;
                    } else if (response.get(i).getNature().matches("Meter Damaged")) {
                        meterDamaged = meterDamaged + 1;
                    } else if (response.get(i).getNature().matches("Faulty Transformer")) {
                        faultyTransformer = faultyTransformer + 1;
                    }
                }
                int total = noSupply + irregularSupply + linesOnGround + fire + meterDamaged + faultyTransformer;

                tvNatureOfOutageFaultyTransfomer.setText(String.format("%.1f", (double) faultyTransformer / total * 100.0));
                tvNatureOfOutageFire.setText(String.format("%.1f", (double) fire / total * 100.0));
                tvNatureOfOutageIrregularSupply.setText(String.format("%.1f", (double) irregularSupply / total * 100.0));
                tvNatureOfOutageLinesOnGround.setText(String.format("%.1f", (double) linesOnGround / total * 100.0));
                tvNatureOfOutageMeterDamaged.setText(String.format("%.1f", (double) meterDamaged / total * 100.0));
                tvnatureOfOutageNoSupply.setText(String.format("%.1f", (double) noSupply / total * 100.0));

                progressDialog.dismiss();

                String[] labels = {"Fire", "Irregular Suply", "Faulty Transformer", "Lines on Ground", "Meter Damaged", "No Supply"};
                int[] figures = {fire, irregularSupply, faultyTransformer, linesOnGround, meterDamaged, noSupply};

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
                description.setText("NATURE OF OUTAGE");
                chart.setDescription(description);
                chart.setData(data);
                chart.animateXY(2000, 2000);
                chart.setEntryLabelTextSize(16.0F);
                chart.setEntryLabelColor(Color.BLACK);
                chart.invalidate();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                progressDialog.dismiss();
                MainActivity.prefConfig.displayToast(fault.getMessage());
            }
        });

    }

    public interface FieldAdminNatureOfOutageReportFragListener {
        void backToFieldAdminReportsFromNatureOfOutageReports();
    }
}