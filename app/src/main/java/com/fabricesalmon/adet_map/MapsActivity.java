package com.fabricesalmon.adet_map;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private final String ms_TAG = this.getClass().getSimpleName();
    private GoogleMap m_GoogleMap;
    private Boolean is_Debugging = true;

    private BroadcastReceiver m_LocationReceiver = new BroadcastReceiver() {
         private Boolean is_Debugging = true;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == m_GoogleMap) Log.i(ms_TAG, "m_GoogleMap is null");
            else {
                Bundle l_Bundle = intent.getExtras();
                double latitude = Double.parseDouble(l_Bundle.getString(LocationService.CS_LATITUDE));
                double longitude = Double.parseDouble(l_Bundle.getString(LocationService.CS_LONGITUDE));

                MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps ");

                m_GoogleMap.addMarker(marker);
            }
        } // end onReceive
    }; /* end BroadcastReceiver */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (is_Debugging && BuildConfig.DEBUG) Log.i(ms_TAG, "onCreate .....");

        setContentView(R.layout.activity_maps);

        Intent l_Intent = new Intent(this, LocationService.class);
        startService(l_Intent);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (is_Debugging && BuildConfig.DEBUG) Log.i(ms_TAG, "onMapReady .....");

        m_GoogleMap = googleMap;

        m_GoogleMap.setMyLocationEnabled(true);
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (BuildConfig.DEBUG) Log.i(ms_TAG, "On Start .....");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (BuildConfig.DEBUG) Log.i(ms_TAG, "On Restart .....");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (is_Debugging && BuildConfig.DEBUG) Log.i(ms_TAG, "onResume .....");

        IntentFilter l_IntentFilter = new IntentFilter();
        l_IntentFilter.addAction(LocationService.CS_ACTION);
        registerReceiver(m_LocationReceiver, l_IntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (is_Debugging && BuildConfig.DEBUG) Log.i(ms_TAG, "onPause .....");

        unregisterReceiver(m_LocationReceiver);

    }

    @Override
    protected void onStop() {
        if (BuildConfig.DEBUG) Log.i(ms_TAG, "On Stop .....");

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (BuildConfig.DEBUG) Log.i(ms_TAG, "On Destroy .....");

        super.onDestroy();
    }
}
