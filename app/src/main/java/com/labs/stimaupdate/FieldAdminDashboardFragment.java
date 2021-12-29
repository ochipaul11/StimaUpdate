package com.labs.stimaupdate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
        toolbarFieldAdminDashboard.setTitle("STIMA UPDATE");

        tvNewOutages = view.findViewById(R.id.tvNewOutages);
        tvSolvedCases = view.findViewById(R.id.tvSolvedCases);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.field_admin_dashboard_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        for(int i = 0; i<menu.size(); i++){
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