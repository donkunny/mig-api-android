package com.johnjhkoo.mig_android.frangments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.johnjhkoo.mig_android.MapListActivity;
import com.johnjhkoo.mig_android.R;
import com.johnjhkoo.mig_android.location.MIGLocationManager;
import com.johnjhkoo.mig_android.pojos.MIGEventVOWithDist;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by JohnKoo on 6/27/16.
 */
public class MapListFragment extends ListFragment {

    private static final String TAG = "MapListFragment";

    ArrayList<MIGEventVOWithDist> data;
    MapListArrayAdapter adapter;
    MapListActivity mapListActivity;
    MIGLocationManager locationManager;


    public MapListFragment newInstance(MapListActivity activity, ArrayList<MIGEventVOWithDist> list) {
        Log.e(TAG, "newInstance()");
        Log.i(TAG, list.size() + "");

        this.mapListActivity = activity;
        this.data = list;
        adapter = new MapListArrayAdapter(activity, 0, list);

        this.setListAdapter(adapter);

        return this;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.e(TAG, "::::: onActivityCreated :::::");
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG, "::::: onCreateView :::::");
//        mapListActivity = (MapListActivity)getActivity();
//        View view = inflater.inflate(R.layout.maplist_fragment, container, false);

        locationManager = new MIGLocationManager(mapListActivity);
        locationManager.startLocationService();

        return super.onCreateView(inflater,container,savedInstanceState);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mapListActivity.changeLocation(data.get(position));
    }

    private class MapListArrayAdapter extends ArrayAdapter<MIGEventVOWithDist> {

        Context context;
        int resource;
        private ArrayList<MIGEventVOWithDist> list;


        public MapListArrayAdapter(Context context, int resource, ArrayList<MIGEventVOWithDist> list) {
            super(context, resource, list);
            this.context = context;
            this.resource = resource;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public MIGEventVOWithDist getItem(int position) {
            return list.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if ( v == null) {
                v = LayoutInflater.from(context).inflate(R.layout.maplist_list_item_layout, null);
            }

            MIGEventVOWithDist vo = list.get(position);

            if (vo != null) {
                TextView title = (TextView) v.findViewById(R.id.maplist_list_item_layout_title);
                title.setText(vo.getTitle());

                DecimalFormat df = new DecimalFormat("0.0");
                String formated = df.format(vo.getDistance());

                TextView distance = (TextView) v.findViewById(R.id.maplist_list_item_layout_distance);
                distance.setText(formated + " km");
            }

            return v;
        }
    }




}
