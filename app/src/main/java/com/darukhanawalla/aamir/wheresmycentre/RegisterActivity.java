package com.darukhanawalla.aamir.wheresmycentre;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    LatLng address;
    public static FirebaseDatabase mDatabase;
    public static DatabaseReference mMessagesDatabaseRefrence;

    //TestCentre dj = new TestCentre(new LatLng(19.1072846,72.836916), "Dwarkadas J Sanghvi College of Engineering", "Plot No.U-15, J.V.P.D. Scheme, Bhaktivedanta Swami Marg, Vile Parle West, Mumbai, Maharashtra 400056", 10, 0);
    //TestCentre saboo = new TestCentre(new LatLng(18.9685351, 72.8310192), "M. H. Saboo Siddik College of Engineering", "8, Saboo Siddik Polytechnic Road, New Nagpada, Byculla, Mumbai, Maharashtra 400008", 15,0);
    //TestCentre vjti = new TestCentre(new LatLng(19.0222262, 72.8561856), "Veermata Jijabai Technilogical Institute", "Matunga, Mumbai, Maharashtra", 10, 0);

    public List<TestCentre> testCentres;

    Button submit;
    EditText name;
    EditText age;
    EditText aadhaar;
    EditText mobile;

    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        testCentres = new ArrayList<>();

        address = new LatLng(intent.getDoubleExtra("lat", 0), intent.getDoubleExtra("lng", 0));
        submit = findViewById(R.id.submit);

        mDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseRefrence = mDatabase.getReference();
        TextView email = findViewById(R.id.email);
        email.setText(MainActivity.userEmail);

        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        aadhaar = findViewById(R.id.aadhaar);
        mobile = findViewById(R.id.mobile);

        String xname = intent.getStringExtra("name");
        int xage = intent.getIntExtra("age",0);
        long xaadhaar = intent.getLongExtra("aadhaar",0);
        long xmobile = intent.getLongExtra("mobile",0);

        if(xname != null)
            name.setText(xname);
        if(xage != 0)
            age.setText(Integer.toString(xage));
        if(xaadhaar != 0)
            aadhaar.setText(Long.toString(xaadhaar));
        if(xmobile != 0)
            mobile.setText(Long.toString(xmobile));

        mMessagesDatabaseRefrence.child("TestCentres").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String value = dataSnapshot.getValue(String.class);
                //Log.i("Value is: ", value);
                List<XTestCentre> list = new ArrayList<>();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    list.add(child.getValue(XTestCentre.class));
                }

                for(XTestCentre i: list)
                    testCentres.add(new TestCentre(i));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.i("Fail", "fail");

            }

        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                TextView incomplete = findViewById(R.id.incomplete);

                if (name.getText().toString().equals("") || age.getText().toString().equals("") || aadhaar.getText().toString().equals("") || mobile.getText().toString().equals("") || (address.latitude == 0 && address.longitude == 0)) {
                    incomplete.setVisibility(View.VISIBLE);
                } else {
                    student = new Student(name.getText().toString(), Integer.parseInt(age.getText().toString()), MainActivity.userEmail, Long.parseLong(aadhaar.getText().toString()), address, Long.parseLong(mobile.getText().toString()));

                    XStudent xstudent = new XStudent(student);
                    String key = mMessagesDatabaseRefrence.child("Students").push().getKey();
                    student.id = key;
                    xstudent.id = key;
                    mMessagesDatabaseRefrence.child("Students").child(key).setValue(xstudent);




                    TestCentre nearest = testCentres.get(0);
                    double mindist = distance(testCentres.get(0).location, student.address);

                    for( TestCentre x: testCentres)
                    {
                        double dist = distance(x.location, student.address);
                        if(dist< mindist)
                        {
                            mindist = dist;
                            nearest = x;

                        }
                    }

                    student.testCentre = nearest;

                    mMessagesDatabaseRefrence.child("Students").child(key).child("testCentre").setValue(new XTestCentre(nearest));




                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    /*i.putExtra("name", student.name);
                    i.putExtra("id", student.id);
                    i.putExtra("centre", student.testCentre.name);
                    i.putExtra("address", student.testCentre.address);*/
                    startActivity(i);
                }
            }
        });

        Button openMap = findViewById(R.id.openMap);
        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("age", Integer.parseInt(age.getText().toString()));
                intent.putExtra("aadhaar", Long.parseLong(aadhaar.getText().toString()));
                intent.putExtra("mobile", Long.parseLong(mobile.getText().toString()));
                startActivity(intent);

            }
        });
    }


    private double distance(LatLng l1, LatLng l2) {
        double lat1 = l1.latitude, lon1 = l1.longitude, lat2 = l2.latitude, lon2 = l2.longitude;
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
