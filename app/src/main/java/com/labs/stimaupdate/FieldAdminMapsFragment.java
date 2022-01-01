package com.labs.stimaupdate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FieldAdminMapsFragment extends Fragment {

    OnFieldAdminMapsFragListener onFieldAdminMapsFragListener;
    double longitude, latitude;
    GoogleMap map;
    List<String> courseofActionList;
    ProgressDialog progressDialog;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        @SuppressLint("PotentialBehaviorOverride")
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            onFieldAdminMapsFragListener.getLongitudeLatitude();
            longitude = MainActivity.longitude;
            latitude = MainActivity.latitude;

            LatLng myLocation = new LatLng(latitude, longitude);
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_json));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));

            addPointsOnMap();
            map.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
                @Override
                public void onInfoWindowLongClick(@NonNull Marker marker) {

                    Log.d("INFORMATION WINDOW LONG CLICK", "********************************ACTIVE");

                    courseofActionList = new ArrayList<>();
                    String title = marker.getTitle();
                    // int customerId = Integer.parseInt(title.substring(title.lastIndexOf("#") + 1));
                    int customerId = Integer.parseInt(title.substring(0, 1));

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Update outage report");
                    builder.setMultiChoiceItems(R.array.lsCourseOfAction, null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                            String[] checked = getResources().getStringArray(R.array.lsCourseOfAction);
                            if (b) {
                                courseofActionList.add(checked[i]);

                            } else courseofActionList.remove(checked[i]);
                        }
                    });
                    builder.setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String action = "";

                            if (courseofActionList.size() == 1 && courseofActionList.contains("Reporting to site")) {
                                Log.d("IF********************CustomerId: ", String.valueOf(MainActivity.reports.get(customerId).getId()));
                                MainActivity.reports.get(customerId).setTechnicianOnSite(true);
                                Log.d("IF******************** CustomerId String: ", String.valueOf(customerId));

                                Date currentTime = Calendar.getInstance().getTime();
                                Log.d("Date:************************************", currentTime.toString());
//restoredDate = new SimpleDateFormat("EEE MMM dd").format(MainActivity.reports.get(i).getRestoredDate());

                                Backendless.Data.of(Report.class).save(MainActivity.reports.get(customerId), new AsyncCallback<Report>() {
                                    @Override
                                    public void handleResponse(Report response) {
                                        MainActivity.prefConfig.displayToast("Report successfully updated");
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        MainActivity.prefConfig.displayToast("Error: " + fault.getMessage());
                                        Log.d("FIELD ADMIN REPORT UPDATE:********************", fault.toString());
                                    }
                                });
                            } else if (courseofActionList.size() == 1 && courseofActionList.contains("Electricity restored")) {
                                MainActivity.reports.get(customerId).setRestored(true);
                                MainActivity.reports.get(customerId).setTechnicianOnSite(true);
                                MainActivity.reports.get(customerId).setRestoredDate(Calendar.getInstance().getTime());

                                Backendless.Data.of(Report.class).save(MainActivity.reports.get(customerId), new AsyncCallback<Report>() {
                                    @Override
                                    public void handleResponse(Report response) {
                                        MainActivity.prefConfig.displayToast("Report successfully updated");
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        MainActivity.prefConfig.displayToast("Error: " + fault.getMessage());
                                        Log.d("FIELD ADMIN REPORT UPDATE:********************", fault.toString());
                                    }
                                });
                                addPointsOnMap();
                                Log.d("IF ELSE 1********************", String.valueOf(MainActivity.reports.get(customerId).isRestored()));
                            } else if (courseofActionList.size() == 2 && courseofActionList.contains("Electricity restored") && courseofActionList.contains("Reporting to site")) {
                                MainActivity.reports.get(customerId).setRestored(true);
                                MainActivity.reports.get(customerId).setTechnicianOnSite(true);
                                MainActivity.reports.get(customerId).setRestoredDate(Calendar.getInstance().getTime());
                                Log.d("IF ELSE 2********************", MainActivity.reports.get(customerId).toString());

                                Backendless.Data.of(Report.class).save(MainActivity.reports.get(customerId), new AsyncCallback<Report>() {
                                    @Override
                                    public void handleResponse(Report response) {
                                        MainActivity.prefConfig.displayToast("Report successfully updated");
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        MainActivity.prefConfig.displayToast("Error: " + fault.getMessage());
                                        Log.d("FIELD ADMIN REPORT UPDATE:********************", fault.toString());
                                    }
                                });
                                addPointsOnMap();
                            } else {
                                MainActivity.prefConfig.displayToast("Error while capturing your input");
                            }

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }
            });
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        onFieldAdminMapsFragListener = (OnFieldAdminMapsFragListener) activity;
    }

    private void addPointsOnMap() {
        final int PAGESIZE = 100;
        String whereClause = "restored = FALSE";

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setPageSize(PAGESIZE);

        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setSortBy("created DESC");

        progressDialog = ProgressDialog.show(getContext(), "Loading Map...", null, true, true);

        Backendless.Persistence.of(Report.class).find(queryBuilder, new AsyncCallback<List<Report>>() {
            @Override
            public void handleResponse(List<Report> response) {
                MainActivity.reports = response;

                int i = 0;
                while (i < response.size()) {

                    Double lat = response.get(i).getLatitude();
                    Double lng = response.get(i).getLongitude();
                    String scope = response.get(i).getScope();
                    String nature = response.get(i).getNature();
                    LatLng name = new LatLng(lat, lng);

                    map.addMarker(new MarkerOptions()
                            .position(name)
                            .title(response.indexOf(response.get(i)) + ": CUSTOMER #" + response.get(i).getId())
                            .snippet("Experiencing " + nature + " nature of outage in " + scope));
                    i++;
                }

                progressDialog.dismiss();

            }

            @Override
            public void handleFault(BackendlessFault fault) {

                progressDialog.dismiss();
                Log.d("FIELDADMIN MAPS FRAG**************************", fault.toString());
                MainActivity.prefConfig.displayToast("Error: " + fault.getMessage());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_field_admin_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public interface OnFieldAdminMapsFragListener {
        void getLongitudeLatitude();
    }
}