package com.example.helpinghands.model;

import android.location.LocationListener;
import android.os.Bundle;

public class Location implements LocationListener {

    double latitude;
    double longitude;


    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public void onLocationChanged(android.location.Location location) {

        latitude=location.getLatitude();
        longitude=location.getLongitude();
        new Location(latitude,longitude);
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
