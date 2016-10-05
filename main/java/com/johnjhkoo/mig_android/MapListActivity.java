package com.johnjhkoo.mig_android;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.johnjhkoo.mig_android.dao.MIGRetroDAO;
import com.johnjhkoo.mig_android.frangments.MapListFragment;
import com.johnjhkoo.mig_android.location.MIGLocationManager;
import com.johnjhkoo.mig_android.pojos.MIGEventVO;
import com.johnjhkoo.mig_android.pojos.MIGEventVOWithDist;
import com.wunderlist.slidinglayer.SlidingLayer;

import java.util.ArrayList;
import java.util.List;


public class MapListActivity extends AppCompatActivity implements OnMapReadyCallback, OnMyLocationButtonClickListener, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = "MapListActivity";


    private SupportMapFragment mapFragment;
    private FloatingActionButton floatingActionButton;
    private SlidingLayer slidingLayer;

    private GoogleMap map;

    ArrayList<MIGEventVOWithDist> data;
    ArrayList<Marker> markers;

    MIGLocationManager locationManager;
    Location myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "::::: onCreate initiated :::::");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_list);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maplist_map);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.maplist_floating_button);

        locationManager = new MIGLocationManager(this);
        locationManager.startLocationService();

        myLocation = new Location("test");
        myLocation.setLatitude(37.447327);
        myLocation.setLongitude(126.653891);

        mapFragment.getMapAsync(this);

        slidingLayer = (SlidingLayer) findViewById(R.id.maplist_sliding_layer);
        slidingLayer.setStickTo(SlidingLayer.STICK_TO_BOTTOM);

        new DAOAsyncTask().execute();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slidingLayer.isClosed()) {
                    slidingLayer.openLayer(true);
                } else {
                    slidingLayer.closeLayer(true);
                }

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        } else {
            map.setMyLocationEnabled(true);
        }

        map.setOnMyLocationButtonClickListener(this);
        map.setOnInfoWindowClickListener(this);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        Intent i = new Intent(getApplicationContext(), DetailActivity.class);
        i.putExtra("detail_info", data.get(markers.indexOf(marker)).getId());

        startActivity(i);

    }

    public void changeLocation(MIGEventVO vo) {
        slidingLayer.closeLayer(true);

        String placeName = vo.getPlace();
        LatLng loc = new LatLng(vo.getLatitude(), vo.getLongitude());

//        map.addMarker(new MarkerOptions().position(loc).title(placeName));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
    }

    public void markLocations(ArrayList<MIGEventVOWithDist> list) {

        double lat1 = 0, lon1 = 0, lat2 = 0, lon2 =0;
        markers = new ArrayList<>();

        for (int i=0; i<list.size();i++) {

            double iLat = list.get(i).getLatitude();
            double iLon = list.get(i).getLongitude();
            if (i == 0) {
                lat1 = iLat;
                lon1 = iLon;
                lat2 = iLat;
                lon2 = iLon;
            } else {
                if (iLat < lat1) {
                    lat1 = iLat;
                } else if (iLat > lat2) {
                    lat2 = iLat;
                }

                if (iLon < lon1) {
                    lon1 = iLon;
                } else if (iLon > lon2) {
                    lon2 = iLon;
                }
            }


            String placeName = list.get(i).getPlace();
            LatLng loc = new LatLng(list.get(i).getLatitude(), list.get(i).getLongitude());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title(list.get(i).getTitle());
            markerOptions.position(loc);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_pin_40x40));

            markers.add(map.addMarker(markerOptions));
        }

        Log.d(TAG, "lat1 : " + lat1 + " lon1 : " + lon1 + " lat2 : " + lat2 + " lon2 " + lon2);

        LatLngBounds bounds = new LatLngBounds(new LatLng(lat1, lon1), new LatLng(lat2, lon2));

        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,50));

    }

    @Override
    public boolean onMyLocationButtonClick() {

        return false;
    }



    class DAOAsyncTask extends AsyncTask<Location, Void, List<MIGEventVOWithDist>> {

        @Override
        protected void onPreExecute() {
            Log.e("DAOAsyncTask", "::::::  onPreExecute() ::::::");
            if (locationManager.getMyLastLocation() != null) {
                myLocation = locationManager.getMyLastLocation();
            }
            Log.e(TAG, myLocation.toString());
        }

        @Override
        protected List<MIGEventVOWithDist> doInBackground(Location... params) {

            MIGRetroDAO dao = new MIGRetroDAO();

//            Log.v("DAOAsyncTask", "MyLocation lat : " + myLocation.getLatitude() + " lon : " + myLocation.getLongitude());

            List<MIGEventVOWithDist> list = dao.getPlaces(myLocation.getLatitude(), myLocation.getLongitude(), 10);

            return list;
        }

        @Override
        protected void onPostExecute(List<MIGEventVOWithDist> list) {

            data = new ArrayList<>();
            data.addAll(list);

            markLocations(data);

            MapListFragment mapList = new MapListFragment().newInstance(MapListActivity.this,data);

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.add(R.id.maplist_sliding_layer_fragcontainer, mapList);
            ft.commit();

        }
    }

}
