package com.chandrasaha.makanyuk.Adapter;

/**
 * Created by Chandra Saha on 12/1/2015.
 */
public class MarkerAdapter {
    int id;
    com.google.android.gms.maps.model.Marker marker;

    public MarkerAdapter(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public com.google.android.gms.maps.model.Marker getMarker() {
        return marker;
    }

    public void setMarker(com.google.android.gms.maps.model.Marker marker) {
        this.marker = marker;
    }
}
