package com.darukhanawalla.aamir.wheresmycentre;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by aamir on 28/1/18.
 */

public class Student {

    public String name;
    public int age;
    public String email;
    public long aadhaar;
    public LatLng address;
    public TestCentre testCentre;
    public String id;
    public long phone;

    public Student() {
    }

    public Student(String name, int age, String email, long aadhaar, LatLng address, long phone) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.aadhaar = aadhaar;
        this.address = address;
        this.phone = phone;
        testCentre = new TestCentre();
    }

    public Student(XStudent xyz)
    {
        this.name = xyz.name;
        this.age = xyz.age;
        this.email = xyz.email;
        this.aadhaar = xyz.aadhaar;
        this.address = new LatLng(xyz.lat, xyz.lng);
        this.phone = xyz.phone;
        testCentre = new TestCentre(xyz.testCentre);
    }

}
