package com.labs.stimaupdate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.List;

public class HeatMapsFragment extends Fragment {
    HeatMapFragLister heatMapFragLister;
    GoogleMap map;
    double longitude, latitude;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
      /*      LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
       */

            map = googleMap;
            heatMapFragLister.getLongitudeLatitude();
            longitude = MainActivity.longitude;
            latitude = MainActivity.latitude;
            if (longitude == latitude) {
                heatMapFragLister.getLongitudeLatitude();

            } else {
                heatMapFragLister.getLongitudeLatitude();
            }

            longitude = MainActivity.longitude;
            latitude = MainActivity.latitude;
            LatLng myLocation = new LatLng(latitude, longitude);
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_json));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));

            addHeatMap();
        }
    };

    private void addHeatMap() {

        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Loading...", null, true, true);

        final int PAGESIZE = 100;
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();

        String whereClause = "restored = false";
        queryBuilder.addProperty("latitude");
        queryBuilder.addProperty("longitude");
        queryBuilder.addProperty("id");
        queryBuilder.setPageSize(PAGESIZE);
        queryBuilder.setWhereClause(whereClause);

        Backendless.Persistence.of(Report.class).find(queryBuilder, new AsyncCallback<List<Report>>() {
            private final int offset = 0;
            @Override
            public void handleResponse(List<Report> response) {
              //  int size = response.size();
                List<LatLng> latitudeLongitude = readLatLng(response);

                // Create a heat map tile provider, passing it the latlngs of the police stations.
                HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                        .data(latitudeLongitude)
                        .build();

                // Add a tile overlay to the map, using the heat map tile provider.
                TileOverlay overlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
                progressDialog.dismiss();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("ERROR: ", fault.getMessage());
                progressDialog.dismiss();
            }
        });

    }

    List<LatLng> readLatLng(List<Report> reports) {
        List<LatLng> result = new ArrayList<>();
        Log.d("SIZE: ", String.valueOf(reports.size()));
        for (int i = 0; i < reports.size(); i++) {
            double lat = reports.get(i).getLatitude();
            double lng = reports.get(i).getLongitude();
            result.add(new LatLng(lat, lng));
            Log.d("LATLNG: ID", reports.get(i).getId()+" "+lat + " " + lng);
        }
        return result;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_heat_map, container, false);
        Toolbar toolbarHeatMapFrag = view.findViewById(R.id.toolbarHeatMapFrag);

        toolbarHeatMapFrag.setTitle("My Zone");
        toolbarHeatMapFrag.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbarHeatMapFrag.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heatMapFragLister.backFromHeatMapFragToDashboard();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map2);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        heatMapFragLister = (HeatMapFragLister) activity;
    }

    public interface HeatMapFragLister {

        void getLongitudeLatitude();

        void backFromHeatMapFragToDashboard();
    }
}
