package com.darukhanawalla.aamir.wheresmycentre;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN = 1;

    public static String userEmail;

    boolean flag;

    public static FirebaseDatabase mDatabase;
    public static DatabaseReference mMessagesDatabaseRefrence;

    TextView dname;
    TextView did;
    TextView dcentre;
    TextView daddress;

    XStudent current;

    //List<Student> liststudents;
    List<XStudent> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser auth = firebaseAuth.getCurrentUser();
                if (auth != null) {
                    //user signed in
                    //Log.i("name", auth.getDisplayName());
                    userEmail = auth.getEmail();
                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setLogo(R.drawable.googlemaps)
                                    .setTosUrl("https://superapp.example.com/terms-of-service.html")
                                    .setPrivacyPolicyUrl("https://superapp.example.com/privacy-policy.html")
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())).build(),

                            RC_SIGN_IN);
                }
            }
        };

        flag =  true;
        mDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseRefrence = mDatabase.getReference();

        dname = findViewById(R.id.dname);
        did = findViewById(R.id.did);
        dcentre = findViewById(R.id.dcentre);
        daddress = findViewById(R.id.daddress);

        /*mMessagesDatabaseRefrence.child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot data: dataSnapshot.getChildren())
                {
                    if(data.child("email").getValue().equals(userEmail))
                    {
                        flag = true;
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        //liststudents = new ArrayList<>();
        list = new ArrayList<>();

        mMessagesDatabaseRefrence.child("Students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String value = dataSnapshot.getValue(String.class);
                //Log.i("Value is: ", value);

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    list.add(child.getValue(XStudent.class));
                }

                for(XStudent x: list)
                {
                    if(x.email.equals(userEmail))
                    {
                        flag = false;
                        current = x;
                        Log.i(userEmail, x.email);
                        break;
                    }
                }

                if (flag)
                {
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                }
                else
                {
            /*Intent i = getIntent();
            dname.setText(i.getStringExtra("name"));
            did.setText(i.getStringExtra("id"));
            dcentre.setText(i.getStringExtra("centre"));
            daddress.setText(i.getStringExtra("address"));*/

                    dname.setText(current.name);
                    did.setText(current.id);
                    dcentre.setText(current.testCentre.name);
                    daddress.setText(current.testCentre.address);


                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.i("Fail", "fail");

            }

        });






    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {


            mAuth.removeAuthStateListener(mAuthStateListener);

        }
    }

    public void sn(View view) {
        AuthUI.getInstance().signOut(this);
    }
}