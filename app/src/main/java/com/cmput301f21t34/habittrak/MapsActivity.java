package com.cmput301f21t34.habittrak;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.cmput301f21t34.habittrak.databinding.ActivityMapsBinding;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
// TODO: set the address in the address text view and return the location after confirm button press

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 110;
    private static final int REQUEST_CHECK_SETTINGS = 101;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationListener listener;
    private LocationManager locationManager;
    boolean locationPermissionGranted = false;
    private Location lastKnownLocation;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private final int UPDATE_INTERVAL = 10000;
    private final int FASTEST_INTERVAL = 5000;
    private static final int DEFAULT_ZOOM = 15;
    private boolean isLocationEnabled = false;
    LocationRequest locationRequest;
    Button confirmButton;
    TextView addressTextView;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lastKnownLocation = new Location(LocationManager.GPS_PROVIDER);
        getLocationPermission();

        // creating a location request object
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // checking for location settings


        // done checking location settings

        confirmButton = findViewById(R.id.confirm_button);
        addressTextView = findViewById(R.id.addressText);
        //this.finish();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null){
                    Log.d("MAPpppp","location result is null");
                    return;
                }
                for(Location location:locationResult.getLocations()){
                    Log.d("MAPppp","location is " + location.toString());
                }
                lastKnownLocation = locationResult.getLastLocation();
                Log.d("MAPpppp","The long is "+lastKnownLocation.getLongitude()+" and the lat is "+lastKnownLocation.getLatitude());
                stopLocationUpdates();
                updateLocationUI(lastKnownLocation);
                //MapsActivity.this.finish();
            }
        };

        // Setup location manager and location listener

        // what does this part do?
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
/*
        LocationProvider provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
*/
        /*
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("MAPpppp","Inside the callback");
                locationManager.removeUpdates(this);
                lastKnownLocation = location;
                updateLocationUI(lastKnownLocation);
                Log.d("MAPpppp","The lat is "+lastKnownLocation.getLatitude()+ " the long is "+ lastKnownLocation.getLongitude());
                Log.d("MAPpppp","outside the update location ui callback");
                MapsActivity.this.finish();
            }
        };


         */
        //
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MAP","The confirm button has been pressed");
                Intent result = new Intent();
                result.putExtra("latitude", lastKnownLocation.getLatitude());
                result.putExtra("longitude", lastKnownLocation.getLongitude());
                setResult(RESULT_OK, result);
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CHECK_SETTINGS){
                if (resultCode == Activity.RESULT_OK){
                    Log.d("MAPpppp","The gps has been turned on");
                    isLocationEnabled = true;
                   //getDeviceLocation();
                    startLocationUpdates();
                }
                else{
                    Log.d("MAPpppp","The gps has notttttt been turned on");
                }
        }
        else{
            Log.d("MAPpppp","different case");
        }

    }
    private void checkSettingsAndStartLocationUpdates(){
        // check location settings

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // settings are satisfied the client can initialize location requests here
                isLocationEnabled = true;
                startLocationUpdates();
                Log.d("MAPpppp","it was already turned on");
                //getDeviceLocation();
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException){
                    // location settings are not satisfied.
                    try{
                        Log.d("MAPpppp","need to turn it on");
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MapsActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    }catch(IntentSender.SendIntentException sendEx){
                        // ignore it
                        Log.d("MAPpppp","error on task on failure");
                    }
                }
            }
        });

    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
    @SuppressLint("MissingPermission")
    private void startLocationUpdates(){
        if(locationPermissionGranted){
            fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());
        }
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //getLocationPermission();

        //getDeviceLocation();
        Log.d("MAPpppp","outside device location");
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Log.d("MAPppppp","inside the onclick function");

                lastKnownLocation.setLatitude(latLng.latitude);
                lastKnownLocation.setLongitude(latLng.longitude);
                updateLocationUI(lastKnownLocation);
                Log.d("MAPpppp","Got the location " + lastKnownLocation.getLongitude() + lastKnownLocation.getLatitude());
               // addressTextView.setText("INSIDE THE ONCLICK FUNCTION lat is " + lastKnownLocation.getLatitude()+"long is "+ lastKnownLocation.getLongitude());

            }
        });
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            checkSettingsAndStartLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     *
     * Handles the result of the request for location permissions.
     * @param requestCode int: The request code passed in ActivityCompat.requestPermissions(android.app.Activity, String[], int)
     * @param permissions String: The requested permissions. Never null.
     * @param grantResults int: The grant results for the corresponding permissions which is either
     *                     PackageManager.PERMISSION_GRANTED or PackageManager.PERMISSION_DENIED.
     *                     Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    checkSettingsAndStartLocationUpdates();
                }
                else {
                    Log.d("MAPppp","location not granted");
                    getLocationPermission();
                }
            }
        }
        //updateLocationUI();
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
   /* private void getDeviceLocation(){
        String TAG = "MAPpppp";
        Log.d(TAG,"entered device location");
        if(locationPermissionGranted)
        {
            @SuppressLint("MissingPermission") Task<Location> locationTask = fusedLocationClient.getLastLocation();
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        // we have location
                        Log.d(TAG,"Last location found in onSuccess");
                        Log.d(TAG,"location is"+location.toString());
                        Log.d(TAG,"long is "+location.getLongitude());
                        Log.d(TAG,"lat is "+ location.getLatitude());
                        lastKnownLocation = location;
                        Log.d(TAG,"lastknown one +"+lastKnownLocation.toString());
                        updateLocationUI(lastKnownLocation);

                    }
                    else{
                        //location is null start requesting update
                        Log.d(TAG,"Location is null");
                    }
                }
            });
            locationTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"Onfailure is "+ e.getLocalizedMessage());
                }
            })

        }
    }
    /*
    private void getDeviceLocation() {

        String TAG = "MAPpppp";
        Criteria criteria = new Criteria();
        String bestProvider = String.valueOf(locationManager.getBestProvider(criteria,true)).toString();
        Log.d(TAG,"entered device location");

        try {
            if (locationPermissionGranted) {

                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
                Log.d("MAPpppp", "got manager");
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null) {
                    Log.d("YEP", "not null");
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    Log.d(TAG, "the lat is " + String.valueOf(latitude));
                    Log.d(TAG,"the long is " + String.valueOf(longitude));
                    lastKnownLocation.setLatitude(latitude);
                    lastKnownLocation.setLongitude(longitude);
                    Log.d(TAG,"entered device location  lastKnownLocation successful");
                    Log.d(TAG,"Lat:" + lastKnownLocation.getLatitude()+" long is "+lastKnownLocation.getLongitude());
                    updateLocationUI(lastKnownLocation);

                }
                else {
                    // location is null
                    Log.d("MAPpppp","Hopefully going into callback");
                    locationManager.requestLocationUpdates(bestProvider,1000,0,listener);
                    Log.d(TAG,"entered device location lastKnownLocation unsuccessful");
                    }

            }
        } catch (SecurityException e)  {
            Log.e("MAPpppp", "Exception: %s" + e.getMessage(), e);
        }

    }

     */
    private void setMarker(Location lastKnownLocation){
        // add marker location
        mMap.addMarker(new MarkerOptions().position(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude())));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lastKnownLocation.getLatitude(),
                        lastKnownLocation.getLongitude()), DEFAULT_ZOOM));

    }

    /**
     * updates the UI of the map and handles myLocationButton
     */
    private void updateLocationUI(Location loc) {
        Log.d("MAPpppp","inside the updatelocation ui");
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                Log.d("MAPpppp","inside location granted updatedLocationUI");
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.clear();
                setMarker(loc);
                addressTextView.setText("INSIDE THE updateLocation FUNCTION lat is " + loc.getLatitude() + " long is " + loc.getLongitude());

            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}