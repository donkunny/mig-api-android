package com.johnjhkoo.mig_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by JohnKoo on 6/25/16.
 */
public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googlemap_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) throws SecurityException {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(37.387398, 126.724762)).title("Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.387398, 126.724762), 15));
//        googleMap.setMyLocationEnabled(true);
    }
}
