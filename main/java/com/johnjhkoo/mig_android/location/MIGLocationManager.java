package com.johnjhkoo.mig_android.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by JohnKoo on 7/5/16.
 */
public class MIGLocationManager {

    private static final String TAG = "MIGLocationManager";
    private Context context;
    private Location myLastLocation;

    private boolean myLocationChanged = false;

    public MIGLocationManager(Context context) {
        Log.d(TAG, ":::::: INIT ::::::");
        this.context = context;
    }

    public void startLocationService(){
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        GPSListener gpsListener = new GPSListener();
        long minTime = 60000;
        float minDistance = 0;

        try {

            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener
            );

            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener
            );

            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastLocation != null) {

                myLastLocation = lastLocation;

                Log.v(TAG, "MyLastLocation lat : " + myLastLocation.getLatitude() + " lon : " + myLastLocation.getLongitude());
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public Location getMyLastLocation(){
        return this.myLastLocation;
    }

    public boolean isMyLocationChanged(){
        return this.myLocationChanged;
    }

    private class GPSListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            if (location != myLastLocation ) {

                Log.v("GPSListener", "Location Changed to Lat : " + location.getLatitude() + " lon : " + location.getLongitude());
                myLocationChanged = true;

                myLastLocation = location;
            } else {
                Log.d("GPSListener", "Location remains the same");
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
