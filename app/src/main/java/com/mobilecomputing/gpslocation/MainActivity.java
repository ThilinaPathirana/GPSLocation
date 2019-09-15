package com.mobilecomputing.gpslocation;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener{

    public  static final int RequestPermissionCode  = 1 ;
    Button buttonStart, buttonStop;
    TextView textViewLongitude, textViewLatitude , status ;
    Context context;
    Location location;
    LocationManager locationManager ;
    boolean GpsStatus = false ;
    Criteria criteria ;
    String Holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EnableRuntimePermission();

        buttonStart = (Button)findViewById(R.id.button2);
        buttonStop = (Button)findViewById(R.id.button3);

        textViewLongitude = (TextView)findViewById(R.id.textView);
        textViewLatitude = (TextView)findViewById(R.id.textView2);
        status = (TextView)findViewById(R.id.textView3);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        Holder = locationManager.getBestProvider(criteria, false);

        context = getApplicationContext();

        CheckGpsStatus();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckGpsStatus();

                if(GpsStatus == true) {
                    if (Holder != null) {
                        if (ActivityCompat.checkSelfPermission(
                                MainActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                &&
                                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }

                        location = locationManager.getLastKnownLocation(Holder);
                        locationManager.requestLocationUpdates(Holder, 1000, 2, MainActivity.this);

                        Toast.makeText(MainActivity.this, "Updating Started.......!!!", Toast.LENGTH_LONG).show();
                        status.setText("Updating");
                        status.setTextColor(Color.parseColor("#50D050"));
                    }
                }else {

                    Toast.makeText(MainActivity.this, "Please Enable GPS First", Toast.LENGTH_LONG).show();

                }
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                locationManager.removeUpdates(MainActivity.this);
                Toast.makeText(MainActivity.this, "Updating Stopped.......!!!", Toast.LENGTH_LONG).show();
                status.setText("Stopped");
                status.setTextColor(Color.parseColor("#DF0101"));
            }
        });


    }

    @Override
    public void onLocationChanged(Location location) {

        textViewLongitude.setText("Longitude:" + location.getLongitude());
        textViewLatitude.setText("Latitude:" + location.getLatitude());
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

    public void CheckGpsStatus(){

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION))
        {

            Toast.makeText(MainActivity.this,"ACCESS_FINE_LOCATION permission allows us to Access GPS in app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this,"Permission Granted, Now your application can access GPS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(MainActivity.this,"Permission Canceled, Now your application cannot access GPS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


}
