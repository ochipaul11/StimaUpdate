package com.labs.stimaupdate;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import androidx.appcompat.app.AlertDialog;
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
import java.util.List;

public class FieldAdminMapsFragment extends Fragment {

    OnFieldAdminMapsFragListener onFieldAdminMapsFragListener;
    double longitude, latitude;
    GoogleMap map;
    List<String> courseofActionList;
    ProgressDialog progressDialog;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @SuppressLint("PotentialBehaviorOverride")
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            onFieldAdminMapsFragListener.getLongitudeLatitude();
            longitude = MainActivity.longitude;
            latitude = MainActivity.latitude;
  /*
            if (longitude == 0) {
                onFieldAdminMapsFragListener.getLongitudeLatitude();
                Log.d("FIELDADMINMAPSFRAG Latitude and Longitude***************************", String.valueOf(longitude + " " + latitude));
            } else {
                onFieldAdminMapsFragListener.getLongitudeLatitude();
                Log.d("FIELDADMINMAPSFRAG ELSE Latitude and Longitude***************************", String.valueOf(longitude + " " + latitude));
 }

   */

            LatLng myLocation = new LatLng(latitude, longitude);
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_json));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));

            addPointsOnMap();

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {

                    courseofActionList = new ArrayList<>();
                    String title = marker.getTitle();
                    int customerId = Integer.parseInt(title.substring(title.lastIndexOf("#") + 1));

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
                            Log.d("COURSE OF ACTION**************************8",courseofActionList.toString());
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create();
                    return false;
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
        String whereClause = "restored = false";

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
                    String customerNumber = String.valueOf(response.get(i).getId());
                    String scope = response.get(i).getScope();
                    String nature = response.get(i).getNature();
                    LatLng name = new LatLng(lat, lng);

                    map.addMarker(new MarkerOptions()
                            .position(name)
                            .title("CUSTOMER #" + response.indexOf(response.get(i)))
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