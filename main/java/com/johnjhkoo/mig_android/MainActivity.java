package com.johnjhkoo.mig_android;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.johnjhkoo.mig_android.adapters.MIGListAdapter;
import com.johnjhkoo.mig_android.context.MIGUtils;
import com.johnjhkoo.mig_android.dao.MIGApiDAO;
import com.johnjhkoo.mig_android.pojos.MIGEventVO;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Tag for Logcat
    private static final String TAG = "MainActivity";

    // UI components
    ListView listView;

    private MIGListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main_map);
        mapFragment.getMapAsync(this);

        adapter = new MIGListAdapter(this, new ArrayList<MIGEventVO>());
        listView = (ListView) findViewById(R.id.main_list_view);
        listView.setAdapter(adapter);

        new AsyncListViewLoader().execute("");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) throws SecurityException {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(37.387398, 126.724762)).title("Marker"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.387398, 126.724762), 15));

        googleMap.setMyLocationEnabled(true);
    }

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<MIGEventVO>> {
        private final ProgressBar pb = new ProgressBar(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<MIGEventVO> list) {
            super.onPostExecute(list);
            pb.setVisibility(View.GONE);
            adapter.setEventList(list);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected List<MIGEventVO> doInBackground(String... params) {
            MIGApiDAO dao = new MIGApiDAO();
            JsonObject obj = dao.getData();
            JsonArray arr = MIGUtils.getParser().parse(obj.get("data").getAsString()).getAsJsonArray();
            Log.d(TAG, String.valueOf(arr.size()));

            return MIGUtils.migJsonArrayToApiDataVOList(arr);
        }
    }
}
