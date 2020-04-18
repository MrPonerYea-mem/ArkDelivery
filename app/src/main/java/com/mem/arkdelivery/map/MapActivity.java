package com.mem.arkdelivery.map;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mem.arkdelivery.R;

import java.util.HashMap;
import java.util.Map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    Button btnExit;
    GoogleMap map;
    String[] city = {"Москва пункт 1", "Санкт-Петербург пункт 1", "Архангельск пункт 1", "Москва пункт 2", "Санкт-Петербург пункт 2", "Архангельск пункт 2"};
    Map<String, double[]> position = new HashMap<>();
    String country;
    double[] xy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        position.put("Архангельск пункт 1", new double[]{64.568188, 40.541283});
        position.put("Архангельск пункт 2", new double[]{64.547283, 40.574413});
        position.put("Москва пункт 1", new double[]{55.790853, 37.886669});
        position.put("Москва пункт 2", new double[]{55.785460, 37.305313});
        position.put("Санкт-Петербург пункт 1", new double[]{59.894129, 30.515632});
        position.put("Санкт-Петербург пункт 2", new double[]{59.849351, 30.028356});
//        MapFragment mapFragment = (MapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                            .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        Bundle arguments = getIntent().getExtras();

        country = arguments.getString("country");
        xy = position.get(country);

        btnExit = (Button) findViewById(R.id.buttonExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap mapp) {
        map = mapp;
        // DO WHATEVER YOU WANT WITH GOOGLEMAP
        LatLng latLng1 = new LatLng(64.568188, 40.541283);
        LatLng latLng2 = new LatLng(64.547283, 40.574413);
        LatLng latLng3 = new LatLng(55.790853, 37.886669);
        LatLng latLng4 = new LatLng(55.785460, 37.305313);
        LatLng latLng5 = new LatLng(59.894129, 30.515632);
        LatLng latLng6 = new LatLng(59.849351, 30.028356);
        map.addMarker(new MarkerOptions().position(latLng1).title("Архнгельск пункт 1"));
        map.addMarker(new MarkerOptions().position(latLng2).title("Архангельск пункт 2"));
        map.addMarker(new MarkerOptions().position(latLng3).title("Москва пункт 1"));
        map.addMarker(new MarkerOptions().position(latLng4).title("Москва пункт 2"));
        map.addMarker(new MarkerOptions().position(latLng5).title("Санкт-Петербург пункт 1"));
        map.addMarker(new MarkerOptions().position(latLng6).title("Санкт-Петербург 2"));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(xy[0], xy[1]))
                .zoom(40)
                .tilt(20)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(new LatLng(
//                xy[0], xy[1]));
//        map.animateCamera(cameraUpdate);
        //map.moveCamera(CameraUpdateFactory.newLatLng(latLng1));

//        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        map.setMyLocationEnabled(true);
//        map.setTrafficEnabled(true);
//        map.setIndoorEnabled(true);
//        map.setBuildingsEnabled(true);
//        map.getUiSettings().setZoomControlsEnabled(true);
    }
}
