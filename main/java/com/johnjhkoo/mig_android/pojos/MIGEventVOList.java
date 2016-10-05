package com.johnjhkoo.mig_android.pojos;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by JohnKoo on 7/5/16.
 */
public class MIGEventVOList extends ArrayList<MIGEventVOWithDist> {

    private static final String TAG = "MIGEventVOList";

    double lat1 = 0;
    double lon1 = 0;
    double lat2 = 0;
    double lon2 = 0;

    public MIGEventVOList() {
        super();
    }

    public MIGEventVOList(List<MIGEventVOWithDist> collection) {
        super(collection);

        for (int i=0; i<collection.size(); i++) {
            double voLat = collection.get(i).getLatitude();
            double voLon = collection.get(i).getLatitude();

            if (i == 0) {
                lat1 = voLat;
                lat2 = voLat;
                lon1 = voLon;
                lon2 = voLon;
            } else {
                if (voLat < lat1) {
                    lat1 = voLat;
                } else if (voLat > lat2) {
                    lat2 = voLat;
                }

                if (voLon < lon1) {
                    lon1 = voLon;
                } else if (voLon > lon2) {
                    lon2 = voLon;
                }
            }

            Log.v(TAG, "lat1 : " + lat1 + " lon1 : " + lon1 + " lat2 : " + lat2 + " lon2 : " + lon2);

        }
    }

    public double getLat1() {
        return lat1;
    }

    public double getLon1() {
        return lon1;
    }

    public double getLat2() {
        return lat2;
    }

    public double getLon2() {
        return lon2;
    }
}
