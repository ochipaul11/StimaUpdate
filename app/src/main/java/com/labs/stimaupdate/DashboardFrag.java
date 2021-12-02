package com.labs.stimaupdate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class DashboardFrag extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DashboardFragmentListener dashboardFragmentListener;
    CardView cvMyZone, cvReportOutage, cvReportStatus, cvPlannedOutage, cvLogout, cvAbout;
    private String mParam1;
    private String mParam2;
    private ProgressDialog progressDialog;

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

        Toolbar toolbarDashboard = view.findViewById(R.id.tool_bar);
        toolbarDashboard.setNavigationIcon(R.drawable.ic_profile);
        toolbarDashboard.setTitle("Stima Update");
        toolbarDashboard.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashboardFragmentListener.openMyProfileFrag();
            }
        });
        cvMyZone = view.findViewById(R.id.cvMyZone);
        cvReportOutage = view.findViewById(R.id.cvReportOutage);
        cvReportStatus = view.findViewById(R.id.cvReportStatus);
        cvPlannedOutage = view.findViewById(R.id.cvPlannedOutage);
        cvLogout = view.findViewById(R.id.cvLogOut);
        cvAbout = view.findViewById(R.id.cvAbout);

       cvPlannedOutage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(Intent.ACTION_VIEW,
                       Uri.parse("https://www.kplc.co.ke/category/view/50/planned-power-interruptions/")));
           }
       });

        cvMyZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashboardFragmentListener.openHeatMap();
            }
        });
        cvReportOutage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashboardFragmentListener.openReportOutageFrag();
            }
        });

        cvReportStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvReportStatus.animate();
                dashboardFragmentListener.openOutageReportsFrag();
            }
        });
        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setMessage("Are you sure you want to logout");
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog = ProgressDialog.show(getContext(), "Logging out...", null, true, true);
                        Backendless.UserService.logout(new AsyncCallback<Void>() {
                            @Override
                            public void handleResponse(Void response) {
                                MainActivity.prefConfig.displayToast("Logged out!");
                                dashboardFragmentListener.LogoutListener();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                MainActivity.prefConfig.displayToast("Error: " + fault.getMessage());
                                progressDialog.dismiss();
                            }
                        });
                    }
                });
                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });

        return view;
    }

    public interface DashboardFragmentListener {
        void LogoutListener();

        void openReportOutageFrag();

        void openMyProfileFrag();

        void openHeatMap();

        void openOutageReportsFrag();

        void BackFromHomeFragment();
    }
}
