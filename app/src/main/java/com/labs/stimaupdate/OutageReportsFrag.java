package com.labs.stimaupdate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class OutageReportsFrag extends Fragment {
    private OutageReportsListener outageReportsListener;

    public OutageReportsFrag() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_outage_reports, container, false);

        final Toolbar outageReportsToolbar = view.findViewById(R.id.toolbar_outage_reports);
        outageReportsToolbar.setTitle("Outage Reports");

        ListView lvReportsList = view.findViewById(R.id.lvAllReports);
        lvReportsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ReportStatusFrag reportStatusFrag = new ReportStatusFrag();
                Bundle bundle = new Bundle();
                bundle.putInt("reportNumber",MainActivity.reportStatuses.get(i).getReportNumber());
                bundle.putString("outageReceived",MainActivity.reportStatuses.get(i).getOutageReceived());
                bundle.putString("technicianOnSIte",MainActivity.reportStatuses.get(i).getTechnicianOnSite());

                reportStatusFrag.setArguments(bundle);
                outageReportsListener.openReportStatusFrag(reportStatusFrag);


            }
        });
        outageReportsList();
        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        outageReportsListener = (OutageReportsListener) activity;
    }

    private void outageReportsList(){

    }

    public interface OutageReportsListener {
        void openReportStatusFrag(ReportStatusFrag i);

        void backFromOutageReportsFrag();
    }
}
