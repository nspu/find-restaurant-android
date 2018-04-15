package com.nspu.findrestaurant.views;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.PendingResult;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.nspu.findrestaurant.R;
import com.nspu.findrestaurant.controllers.GetPlacesAroundLocation;
import com.nspu.findrestaurant.utils.FormatLocation;

/**
 * Not the time to implement phone number
 * I m 2 activities because LoadingActivity and MapsActivity are not implicitly linked.
 * For example i would have used fragment to display more information about restaurant in MapsActivity
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final int REQUEST_LOCATION_PERMISSION = 100;

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private SupportMapFragment mMapFragment;
    private GetPlacesAroundLocation mRetrievePlace;

    //I m not use ProgressDialog usually but it provide a waiting dialog quickly
    private ProgressDialog mProgressRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mRetrievePlace = new GetPlacesAroundLocation(getString(R.string.google_api_key));
        startProgressDialog();
        locationPermission();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Location currentLocation;
        LatLng currentLatLng;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.permission_gps_refused, Toast.LENGTH_SHORT).show();
        } else {
            currentLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            mMap = googleMap;

            configureMap(currentLatLng);
            try {
                mRetrievePlace.retrieve("", PlaceType.RESTAURANT, 14, currentLatLng, mResultCallback);
            } catch (Throwable throwable) {
                Toast.makeText(this, R.string.impossible_get_restaurant, Toast.LENGTH_SHORT).show();
                stopProgressDialog();
            }
        }
    }

    /**
     * Configure the display of the map
     *
     * @param latLng location to center the map
     */
    private void configureMap(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
    }

    /**
     * Check permission for access to the location
     * If permitted display call mapAsync
     */
    private void locationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //if no permission, ask for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            mMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMapFragment.getMapAsync(this);
                } else {
                    Toast.makeText(this, R.string.permission_gps_refused, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void startProgressDialog() {
        mProgressRestaurant = ProgressDialog.show(this, getString(R.string.loading), getString(R.string.get_restaurant));
    }

    private void stopProgressDialog() {
        if (mProgressRestaurant != null) {
            mProgressRestaurant.cancel();
            mProgressRestaurant = null;
        }
    }

    /**
     * Callback of pending result
     */
    private final PendingResult.Callback<PlacesSearchResponse> mResultCallback = new PendingResult.Callback<PlacesSearchResponse>() {
        @Override
        public void onResult(final PlacesSearchResponse result) {
            //need to be executed in ui thread because the callback come from an another thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (PlacesSearchResult placesSearchResult : result.results) {
                        MapsActivity.this.mMap.addMarker(new MarkerOptions().position(FormatLocation.convert(placesSearchResult.geometry.location)).title(placesSearchResult.name));
                    }
                    stopProgressDialog();
                }
            });
        }

        @Override
        public void onFailure(Throwable e) {
            //need to be executed in ui thread because the callback come from an another thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //TODO process error
                    Toast.makeText(MapsActivity.this, R.string.request_error, Toast.LENGTH_LONG).show();
                    stopProgressDialog();
                }
            });
        }
    };
}
