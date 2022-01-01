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

public class FieldAdminScopeOfOutageFragment extends Fragment {

    private AutoCompleteTextView dpYears, dpMonths;
    private ProgressDialog progressDialog;
    private PieChart chart;
    private Button btnFieldAdminScopeOfOutageGene;
    private TextView tvScopeOfOutageHouse, tvScopeOfOutageEstate, tvScopeOfOutageFlat, tvScopeOfOutageClub, tvScopeOfOutageHospital;
    private Toolbar toolbarFieldAdminScopeOfOutage;
    private FieldAdminScopreOfOutageListener fieldAdminScopreOfOutageListener;

    public FieldAdminScopeOfOutageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_admin_scope_of_outage, container, false);

        toolbarFieldAdminScopeOfOutage = view.findViewById(R.id.toolbarFieldAdminScopeOfOutage);
        //   ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbarFieldAdminScopeOfOutage);
        toolbarFieldAdminScopeOfOutage.setTitle("Scope of Outage");
        toolbarFieldAdminScopeOfOutage.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbarFieldAdminScopeOfOutage.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldAdminScopreOfOutageListener.backToFieldAdminReportsFromScopeOfOutage();
            }
        });

        dpYears = view.findViewById(R.id.dpYears);
        dpMonths = view.findViewById(R.id.dpMonths);

        tvScopeOfOutageHouse = view.findViewById(R.id.tvnatureOfOutageNoSupply);
        tvScopeOfOutageEstate = view.findViewById(R.id.tvNatureOfOutageIrregularSupply);
        tvScopeOfOutageClub = view.findViewById(R.id.tvNatureOfOutageLinesOnGround);
        tvScopeOfOutageFlat = view.findViewById(R.id.tvNatureOfOutageFire);
        tvScopeOfOutageHospital = view.findViewById(R.id.tvScopeOfOutageHospital);

        chart = view.findViewById(R.id.pcScopeOfOutage);

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

        dpYears.setAdapter(arrayAdapterYears);
        dpYears.setThreshold(1);

        dpMonths.setAdapter(arrayAdapterMonths);
        dpMonths.setThreshold(1);

        btnFieldAdminScopeOfOutageGene = view.findViewById(R.id.btnFieldAdminScopeOfOutageGenerateReport);
        btnFieldAdminScopeOfOutageGene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChart();
            }
        });

        return view;
    }

    private void getChart() {
        progressDialog = ProgressDialog.show(getContext(), "Loading chart...", null, true, true);

        String year = dpYears.getText().toString();
        String month = dpMonths.getText().toString();
//created >= '12/12/2021'  AND created <= '12/14/2021'
        String WHERECLAUSE = "created >= '" + month + "/01/" + year + "' AND created <= '" + month + "/30/" + year + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(WHERECLAUSE);

        Backendless.Data.of(Report.class).find(queryBuilder, new AsyncCallback<List<Report>>() {
            @Override
            public void handleResponse(List<Report> response) {
                int house = 0, estate = 0, club = 0, hospital = 0, flat = 0;

                for (int i = 0; i < response.size(); i++) {
                    if (response.get(i).getScope().matches("House")) {
                        house = house + 1;
                    } else if (response.get(i).getScope().matches("Flat")) {
                        flat = flat + 1;
                    } else if (response.get(i).getScope().matches("Estate")) {
                        estate = estate + 1;
                    } else if (response.get(i).getScope().matches("Club")) {
                        club = club + 1;
                    } else if (response.get(i).getScope().matches("Hospital")) {
                        hospital = hospital + 1;
                    }

                }

                int total = hospital + house + flat + estate + club;

                tvScopeOfOutageHouse.setText(String.format("%.1f", (double) house / total * 100.0));
                tvScopeOfOutageEstate.setText(String.format("%.1f", (double) estate / total * 100.0));
                tvScopeOfOutageFlat.setText(String.format("%.1f", (double) flat / total * 100.0));
                tvScopeOfOutageClub.setText(String.format("%.1f", (double) club / total * 100.0));
                tvScopeOfOutageHospital.setText(String.format("%.1f", (double) hospital / total * 100.0));

                String[] labels = {"House", "Flat", "Estate", "Club", "Hospital"};
                int[] figures = {house, flat, estate, club, hospital};
                progressDialog.dismiss();
                List<PieEntry> pieEntries = new ArrayList<>();

                for (int i = 0; i < labels.length; i++) {
                    pieEntries.add(new PieEntry(figures[i], labels[i]));
                    Log.d("Entry........", String.valueOf(figures[i]));
                }
                PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
                pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                pieDataSet.setValueTextSize(16.0F);
                pieDataSet.setValueFormatter(new DefaultValueFormatter(0));
                PieData data = new PieData(pieDataSet);

                Description description = new Description();
                description.setText("SCOPE OF OUTAGE");
                chart.setDescription(description);
                chart.setData(data);
                chart.animateXY(1000, 1000);
                chart.setEntryLabelTextSize(16.0F);
                chart.invalidate();

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                progressDialog.dismiss();
                MainActivity.prefConfig.displayToast(fault.getMessage());
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        fieldAdminScopreOfOutageListener = (FieldAdminScopreOfOutageListener) activity;
    }

    public interface FieldAdminScopreOfOutageListener {
        void backToFieldAdminReportsFromScopeOfOutage();
    }
}