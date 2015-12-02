package com.fabricesalmon.adet_map;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service implements LocationListener {
    public static String CS_ACTION = "com.fabricesalmon.adet_map.NEW_LOCATION";
    public static String CS_LATITUDE = "Latitude";
    public static String CS_LONGITUDE = "Longitude";
    private final String ms_TAG = this.getClass().getSimpleName();
    private LocationManager m_LocationManager = null;
    private Boolean is_Debugging = true;

    @Override
    public void onCreate() {
        super.onCreate();

        if (is_Debugging && BuildConfig.DEBUG) Log.i(ms_TAG, "onCreate .....");

        // Creating a criteria object to retrieve provider
        Criteria l_Criteria = new Criteria();
        l_Criteria.setAccuracy(Criteria.ACCURACY_FINE);
        l_Criteria.setAltitudeRequired(false);
        l_Criteria.setBearingRequired(false);
        l_Criteria.setCostAllowed(true);
        l_Criteria.setPowerRequirement(Criteria.POWER_LOW);

        m_LocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        // Getting the name of the best provider
        String ls_Provider = m_LocationManager.getBestProvider(l_Criteria, false);
        if (null != ls_Provider) {
            m_LocationManager.requestLocationUpdates(ls_Provider, 10000, 0, this);

            // Getting Current Location
            Location l_Location = m_LocationManager.getLastKnownLocation(ls_Provider);

            if (null != l_Location) {
                if (is_Debugging && BuildConfig.DEBUG) Log.i(ms_TAG, "getLastKnownLocation .....");

                Broadcast(l_Location);
            }
        }
        else m_LocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        if (is_Debugging && BuildConfig.DEBUG) Log.i(ms_TAG, "onBind .....");

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        if (is_Debugging && BuildConfig.DEBUG) Log.i(ms_TAG, "onStartCommand .....");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (is_Debugging && BuildConfig.DEBUG) Log.i(ms_TAG, "onDestroy .....");

        if (null != m_LocationManager) m_LocationManager.removeUpdates(this);

        super.onDestroy();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

        if (is_Debugging && BuildConfig.DEBUG) Log.i(ms_TAG, "onStatusChanged .....");
    }

    @Override
    public void onProviderEnabled(String provider) {

        if (is_Debugging && BuildConfig.DEBUG) Log.i(ms_TAG, "onProviderEnabled .....");
    }

    @Override
    public void onProviderDisabled(String provider) {

        if (is_Debugging && BuildConfig.DEBUG) Log.i(ms_TAG, "onProviderDisabled .....");
    }

    @Override
    public void onLocationChanged(Location l_Location) {

        if (is_Debugging && BuildConfig.DEBUG) Log.i(ms_TAG, "onLocationChanged .....");
        Broadcast(l_Location);
    }

    private void Broadcast(Location l_Location) {
        Intent l_Intent = new Intent(CS_ACTION);

        l_Intent.putExtra(CS_LATITUDE, String.valueOf(l_Location.getLatitude()));
        l_Intent.putExtra(CS_LONGITUDE, String.valueOf(l_Location.getLongitude()));

        sendBroadcast(l_Intent);
    }
}