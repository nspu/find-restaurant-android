package com.nspu.findrestaurant.utils;


import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Just an example of unit test
 * Both of classes have equals() implemented
 */
public class FormatLocationTest {
    @Test
    public void convert() {
        com.google.android.gms.maps.model.LatLng latLngGms = new com.google.android.gms.maps.model.LatLng(1, 1);
        com.google.maps.model.LatLng latLngInternal = new  com.google.maps.model.LatLng(1,1);

        assertEquals(FormatLocation.convert(latLngGms), latLngInternal);
        assertEquals(FormatLocation.convert(latLngInternal), latLngGms);
    }
}