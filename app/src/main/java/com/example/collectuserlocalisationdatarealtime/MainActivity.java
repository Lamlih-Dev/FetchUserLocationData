package com.example.collectuserlocalisationdatarealtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements IGpsListner{

    //This is the request code, you can name it whatever you want !
    private static final int PERMISSION_LOCATION = 1000;

    TextView tv_location;
    Button btn_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Accessing the text view and button element that we created in our activity main file
        tv_location = findViewById(R.id.tv_location);
        btn_location = findViewById(R.id.btn_location);

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checking if the android version is higher than Android 6.0
                //Checking is the Location Permission is already granted before requesting it
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
                }else {
                    showLocation();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*
            Make sure that we are working on our location request by passing request value
            that we put with it previously
        */
        if (requestCode == PERMISSION_LOCATION) {
            //Check if the user accept the request
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showLocation();
            } else {
                /*
                    Here i used toast to inform user in case he denied the request
                    You can use dialogs instead and inform user more about what you need from this permission
                    and request it again
                */
                Toast.makeText(this, "Permission not granted !", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void showLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Check if the GPS is enabled
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //Fetching User Current Location
            tv_location.setText("Fetching Location Data...");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, this);
        } else {
            //Enable it if it's not the case
            Toast.makeText(this, "Please Enable GPS!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    //Show location as string
    private String stringifyLocation(Location location){
        return "Lat: " + location.getLatitude() + "\nLon: " + location.getLongitude();
    }

    @Override
    public void onLocationChanged(Location location) {
        //Update Location
        tv_location.setText(stringifyLocation(location));
    }

    @Override
    public void onProviderDisabled(String provider) {
        //nothing here
    }

    @Override
    public void onProviderEnabled(String provider) {
        //nothing here
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //nothing here
    }

    @Override
    public void onGpsStatusChanged(int event) {
        //nothing here
    }
}