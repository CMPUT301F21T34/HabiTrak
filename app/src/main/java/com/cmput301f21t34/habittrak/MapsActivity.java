package com.cmput301f21t34.habittrak;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.cmput301f21t34.habittrak.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Locale;
// TODO: set the address in the address text view and return the location after confirm button press

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 110;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LatLng loc;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationListener listener;
    private LocationManager locationManager;
    boolean locationPermissionGranted = false;
    private Location lastKnownLocation;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private final int UPDATE_INTERVAL = 30000;
    private final int FASTEST_INTERVAL = 5000;
    private static final int DEFAULT_ZOOM = 15;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private CameraPosition cameraPosition;

    LocationRequest locationRequest;
    Button confirmButton;
    TextView addressTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }*/
        // creating a location request object



        locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        confirmButton = findViewById(R.id.confirm_button);
        addressTextView = findViewById(R.id.addressText);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Setup location manager and location listener
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationProvider provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastKnownLocation = location;
                updateLocationUI();
            }
        };
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
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Log.d("MAP","inside the onclick function");
                lastKnownLocation = new Location(LocationManager.GPS_PROVIDER);
                lastKnownLocation.setLatitude(latLng.latitude);
                lastKnownLocation.setLongitude(latLng.longitude);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude())));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(lastKnownLocation.getLatitude(),
                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                Log.d("MAP","Got the location " + lastKnownLocation.getLongitude() + lastKnownLocation.getLatitude());
                addressTextView.setText("INSIDE THE ONCLICK FUNCTION");

            }
        });
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
   /* @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }*/
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
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {

        String TAG = "MAP";
        try {
            if (locationPermissionGranted) {
                Log.d(TAG,"entered device location");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
                Log.d("YEP", "got manager");
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    Log.d("YEP", "not null");
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    lastKnownLocation = new Location("gps");
                    Log.d("lat", String.valueOf(latitude));
                    Log.d("long", String.valueOf(longitude));
                    lastKnownLocation.setLatitude(latitude);
                    lastKnownLocation.setLongitude(longitude);

                    // Add marker for current location
                    Log.d(TAG,"entered device location  lastKnownLocation successful");
                    mMap.addMarker(new MarkerOptions().position(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude())));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(lastKnownLocation.getLatitude(),
                                    lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                }
                else {
                    Log.d(TAG,"entered device location lastKnownLocation unsuccessful");
                    mMap.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                    mMap.addMarker(new MarkerOptions().position(defaultLocation));
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                }
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private String getAddress(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE,
                    LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            "\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.d("Address",
                        "" + strReturnedAddress.toString());
            } else {
                Log.d("Address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Address", "Canont get Address!");
        }
        return strAdd;
    }



   /* private void setupMap() {

        Log.d("MAP","Still in setupMap");

        Task<Location>  task = fusedLocationProviderClient.getLastLocation();
        while(!task.isComplete());
        if (task.isSuccessful()){
            Location location = task.getResult();
            if (location != null){
                Log.d("MAP","Entered location onsuccess");
                loc = new LatLng( location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(loc).title("Marker in Current Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                Log.d("MAP","the location is " + location.getLongitude() + location.getLatitude());

            }

        }
        else{
            Log.d("MAP","Task is not successful");
        }
        }*/

    /**
     * updates the UI of the map and handles myLocationButton
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
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