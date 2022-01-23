package com.labs.stimaupdate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FieldAdminDashboardFragment extends Fragment {
    Toolbar toolbarFieldAdminDashboard;
    TextView tvNewOutages, tvSolvedCases;
    ProgressDialog progressDialog;
    FieldAdminDashboardFragListener fieldAdminDashboardFragListener;

    public FieldAdminDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_admin_dashboard, container, false);

        toolbarFieldAdminDashboard = view.findViewById(R.id.toolbarFieldAdminDashboard);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbarFieldAdminDashboard);
        //  toolbarFieldAdminDashboard.setTitle("STIMA UPDATE");
        TextView fieldAdminDashboardTitle = view.findViewById(R.id.fieldAdminDashboard_toolbar_title);
        fieldAdminDashboardTitle.setText("Stima Update");

        tvNewOutages = view.findViewById(R.id.tvNewOutages);
        tvSolvedCases = view.findViewById(R.id.tvSolvedCases);

        Date date = Calendar.getInstance().getTime();
        String afterFormatDate = new SimpleDateFormat("MM-dd-yyyy").format(date);
        String WHERECLAUSE = "created >= '" + afterFormatDate + "'";

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();

        queryBuilder.addProperty("objectId");
        queryBuilder.setWhereClause(WHERECLAUSE);

        Backendless.Data.of(Report.class).getObjectCount(queryBuilder, new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {
                Log.d("COUNT new outages...................", response.toString());
                Log.d("DATE STRING..............", afterFormatDate);
                tvNewOutages.setText(response.toString());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("Error: ", fault.toString());
            }
        });
        Date date2 = Calendar.getInstance().getTime();
        String afterFormatDate2 = new SimpleDateFormat("MM-dd-yyyy").format(date2);
        String WHERECLAUSE2 = "restoredDate >= '" + afterFormatDate2 + "'";
        DataQueryBuilder queryBuilder2 = DataQueryBuilder.create();

        queryBuilder2.addProperty("objectId");
        queryBuilder2.setWhereClause(WHERECLAUSE2);

        Backendless.Data.of(Report.class).getObjectCount(queryBuilder2, new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer response) {

                tvSolvedCases.setText(response.toString());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("Error: ", fault.toString());
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.field_admin_dashboard_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            SpannableString spannable = new SpannableString(menu.getItem(i).getTitle().toString());
            spannable.setSpan(new ForegroundColorSpan(R.color.colorPrimaryText), 0, spannable.length(), 0);
            menuItem.setTitle(spannable);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuGenerateReports:
                fieldAdminDashboardFragListener.openFieldAdminReports();
                return true;
            case R.id.menuMyProfile:
                fieldAdminDashboardFragListener.openFieldAdmnProfileFragment();
                return true;
            case R.id.menuLogOut:

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
                                fieldAdminDashboardFragListener.performFieldAdminLogout();
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
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        fieldAdminDashboardFragListener = (FieldAdminDashboardFragListener) activity;
    }

    public interface FieldAdminDashboardFragListener {
        void performFieldAdminLogout();

        void openFieldAdmnProfileFragment();

        void openFieldAdminReports();
    }
}