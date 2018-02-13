package com.darukhanawalla.aamir.wheresmycentre;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by aamir on 28/1/18.
 */

public class TestCentre {

    public LatLng location;
    public String name;
    public String address;
    public int seats;
    public int allocated;

    public TestCentre() {
        location = new LatLng(0, 0);
        name = "";
        address = "";
    }

    public TestCentre(XTestCentre abc)
    {
        this.location = new LatLng(abc.lat, abc.lng);
        this.name = abc.name;
        this.address = abc.address;
        this.seats = abc.seats;
        this.allocated = abc.allocated;
    }

    public TestCentre(LatLng location, String name, String address, int seats, int allocated) {
        this.location = location;
        this.name = name;
        this.address = address;
        this.seats = seats;
        this.allocated = allocated;
    }

}
