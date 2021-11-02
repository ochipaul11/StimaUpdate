package com.labs.stimaupdate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class DashboardFrag extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DashboardFragmentListener dashboardFragmentListener;
    CardView cvMyZone, cvReportOutage, cvReportStatus, cvPlannedOutage, cvLogout, cvAbout;
    private String mParam1;
    private String mParam2;

    public DashboardFrag() {
        // Required empty public constructor
    }

    public static DashboardFrag newInstance(String param1, String param2) {
        DashboardFrag fragment = new DashboardFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        dashboardFragmentListener = (DashboardFragmentListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        cvMyZone = view.findViewById(R.id.cvMyZone);
        cvReportOutage = view.findViewById(R.id.cvReportOutage);
        cvReportStatus = view.findViewById(R.id.cvReportStatus);
        cvPlannedOutage = view.findViewById(R.id.cvPlannedOutage);
        cvLogout = view.findViewById(R.id.cvLogOut);
        cvAbout = view.findViewById(R.id.cvAbout);

        cvReportOutage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashboardFragmentListener.openReportOutageFrag();
            }
        });
        cvReportStatus.findFocus();
        cvReportStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashboardFragmentListener.openOutageReportsFrag();
            }
        });
        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashboardFragmentListener.LogoutListener();
            }
        });

        return view;
    }

    public interface DashboardFragmentListener {
        void LogoutListener();

        void openReportOutageFrag();

        void openOutageReportsFrag();

        void BackFromHomeFragment();
    }
}
