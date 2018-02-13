package com.darukhanawalla.aamir.wheresmycentre;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by aamir on 28/1/18.
 */

public class XTestCentre {

    public double lat;
    public double lng;
    public String name;
    public String address;
    public int seats;
    public int allocated;

    public XTestCentre() {
        lat = 0;
        lng = 0;
        name = "";
        address = "";
    }

    public XTestCentre(double lat, double lng, String name, String address, int seats, int allocated) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.address = address;
        this.seats = seats;
        this.allocated = allocated;
    }

    public XTestCentre(TestCentre abc)
    {
        this.lat = abc.location.latitude;
        this.lng = abc.location.longitude;
        this.name = abc.name;
        this.address = abc.address;
        this.seats = abc.seats;
        this.allocated = abc.allocated;
    }
}
