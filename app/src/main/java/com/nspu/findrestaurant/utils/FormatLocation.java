package com.nspu.findrestaurant.utils;

public class FormatLocation {
    static public com.google.android.gms.maps.model.LatLng convert(com.google.maps.model.LatLng latLng) {
        return new com.google.android.gms.maps.model.LatLng(latLng.lat, latLng.lng);
    }

    static public com.google.maps.model.LatLng convert(com.google.android.gms.maps.model.LatLng latLng) {
        return new com.google.maps.model.LatLng(latLng.latitude, latLng.longitude);
    }
}
