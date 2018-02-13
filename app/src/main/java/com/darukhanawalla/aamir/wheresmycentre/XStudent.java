package com.darukhanawalla.aamir.wheresmycentre;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by aamir on 28/1/18.
 */

public class XStudent {

    public String name;
    public int age;
    public String email;
    public long aadhaar;
    public double lat;
    public double lng;
    public XTestCentre testCentre;
    public String id;
    public long phone;

    public XStudent() {
    }

    public XStudent(String name, int age, String email, long aadhaar, double lat, double lng, long phone) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.aadhaar = aadhaar;
        this.lat = lat;
        this.lng = lng;
        this.phone = phone;
        testCentre = new XTestCentre();
    }

    public XStudent(Student xyz)
    {
        this.name = xyz.name;
        this.age = xyz.age;
        this.email =xyz.email;
        this.aadhaar = xyz.aadhaar;
        this.lat = xyz.address.latitude;
        this.lng = xyz.address.longitude;
        this.phone = xyz.phone;
        testCentre = new XTestCentre(xyz.testCentre);
    }
}
