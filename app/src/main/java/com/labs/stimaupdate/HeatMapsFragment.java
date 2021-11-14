package com.labs.stimaupdate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeatMapsFragment extends Fragment {
    HeatMapFragLister heatMapFragLister;
    GoogleMap map;
    double longitude, latitude;


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
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12));

            addHeatMap();
        }
    };


    private void addHeatMap() {
  /*    List<LatLng> latLngs = null;

        // Get the data: latitude/longitude positions of police stations.
        try {
            latLngs = readItems(R.raw.police);


        } catch (JSONException e) {
            Toast.makeText(getContext(), "Problem reading list of locations.", Toast.LENGTH_LONG).show();
        }

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                .data(latLngs)
                .build();

        // Add a tile overlay to the map, using the heat map tile provider.
        TileOverlay overlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(provider));



   */
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Loading...", null, true, true);


        Call<List<Coordinates>> call = MainActivity.apiInterface.getCoordinates();
        call.enqueue(new Callback<List<Coordinates>>() {
            @Override
            public void onResponse(Call<List<Coordinates>> call, Response<List<Coordinates>> response) {
                MainActivity.coordinates = response.body();
                List<LatLng> latitudeLongitude = readLatLng(response.body());
                String reply = String.valueOf(response.body().get(4).getLatitude());
                MainActivity.prefConfig.displayToast(reply);
                // Create a heat map tile provider, passing it the latlngs of the police stations.
                HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                        .data(latitudeLongitude)
                        .build();

                // Add a tile overlay to the map, using the heat map tile provider.
                TileOverlay overlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Coordinates>> call, Throwable t) {
progressDialog.dismiss();
            }
        });


    }

    private List<LatLng> readItems(@RawRes int resource) throws JSONException {
        List<LatLng> result = new ArrayList<>();
        InputStream inputStream = getContext().getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            result.add(new LatLng(lat, lng));
        }
        return result;
    }

    List<LatLng> readLatLng(List<Coordinates> coordinates) {
        List<LatLng> result = new ArrayList<>();
        for (int i = 0; i < coordinates.size(); i++) {
            double lat = coordinates.get(i).getLatitude();
            double lng = coordinates.get(i).getLongitude();
            result.add(new LatLng(lat, lng));
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
