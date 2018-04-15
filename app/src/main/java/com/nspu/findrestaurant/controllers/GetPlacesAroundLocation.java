package com.nspu.findrestaurant.controllers;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.PlacesApi;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.nspu.findrestaurant.utils.FormatLocation;

/**
 * Get the Places around a location
 */
public class GetPlacesAroundLocation {
    private final GeoApiContext mGeoApiContext;

    /**
     * Constructor
     *
     * @param apiKey apikey from google api
     */
    public GetPlacesAroundLocation(String apiKey) {
        mGeoApiContext = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();

    }

    /**
     * Retrieve place around the location latLng
     *
     * @param query     The text string on which to search.
     * @param placeType The type of place to restrict the results with.
     * @param radius    The radius of the search bias.
     * @param latLng    The location of the center of the search.
     * @param callback  callback to get the data
     * @throws Throwable If no api key
     */
    public void retrieve(String query,
                         PlaceType placeType,
                         int radius,
                         LatLng latLng,
                         PendingResult.Callback<PlacesSearchResponse> callback) throws Throwable {
        try {
            PlacesApi.textSearchQuery(mGeoApiContext, query)
                    .type(placeType)
                    .radius(radius)
                    .location(FormatLocation.convert(latLng))
                    .setCallback(callback);
        } catch (Exception e) {
            throw new Throwable(e);
        }
    }
}
