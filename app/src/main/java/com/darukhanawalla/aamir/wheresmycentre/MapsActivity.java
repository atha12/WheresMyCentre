package com.darukhanawalla.aamir.wheresmycentre;

import android.content.Context;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private EditText locationSearch;
    LatLng finalAddress;

    String xname;
    int xage;
    long xaadhaar;
    long xmobile;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                centerMapOnLocation(lastKnownLocation, "Current location");

            }


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        xname = intent.getStringExtra("name");
        xage = intent.getIntExtra("age",0);
        xaadhaar = intent.getLongExtra("aadhaar",0);
        xmobile = intent.getLongExtra("mobile",0);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public void Done(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("lat", finalAddress.latitude);
        intent.putExtra("lng", finalAddress.longitude);
        intent.putExtra("name", xname);
        intent.putExtra("age", xage);
        intent.putExtra("aadhaar", xaadhaar);
        intent.putExtra("mobile", xmobile);
        startActivity(intent);
    }

    public void onMapSearch(View view) {

        String location = locationSearch.getText().toString();
        List<Address> addressList = null;
        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            finalAddress = latLng;
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng dj = new LatLng(19.1072846, 72.836916);
        mMap.addMarker(new MarkerOptions().position(dj).title("DJ Sanghvi"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dj));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                finalAddress = latLng;

                String address = "";

                try {

                    List<Address> listAddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                    if (listAddresses != null && listAddresses.size() > 0) {

                        if (listAddresses.get(0).getThoroughfare() != null) {

                            if (listAddresses.get(0).getSubThoroughfare() != null) {

                                address += listAddresses.get(0).getSubThoroughfare() + " ";

                            }

                            address += listAddresses.get(0).getThoroughfare();

                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (address == "") {

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");

                    address = sdf.format(new Date());

                }

                locationSearch.setText(address);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(address));

            }
        });
        locationSearch = findViewById(R.id.editText);

        finalAddress = dj;

        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                centerMapOnLocation(location, "Current Location");
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

    }

    private void centerMapOnLocation(Location location, String title) {

        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());

        mMap.clear();

        if(title != "Current Location")
        {
            mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));
    }

}

