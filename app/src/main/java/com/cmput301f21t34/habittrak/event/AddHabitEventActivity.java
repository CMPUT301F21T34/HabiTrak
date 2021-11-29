package com.cmput301f21t34.habittrak.event;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitEvent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.gms.maps.MapView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Tauseef Nafee Fattah
 * @author Henry
 * Version: 2.0
 * Takes a habit and returns the new habit event
 */
public class AddHabitEventActivity extends AppCompatActivity implements View.OnClickListener {

    // Static Variables //
    public static int RESULT_CODE = 3000;
    public static final int CAMERA_PERMISSION_CODE = 100;

    // View Variables //
    private TextInputEditText commentText;
    private ImageView image;
    private MapView mapView;
    private GoogleMap mMap;
    private TextView noPhotoText;
    private TextView addressLine;

    // Data Variables //
    private StorageReference mStorageRef;
    private String currentPhotoPath;
    private HabitEvent habitEvent;
    private DatabaseManager db;
    private Habit habit;

    // Launchers //
    ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    ActivityResultLauncher<Intent> galleryActivityResultLauncher;
    ActivityResultLauncher<Intent> mapActivityResultLauncher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit_event);

        // set toolbar
        Toolbar toolbar = findViewById(R.id.add_habit_event_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // getting the firebase storage
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // getting the Views
        // views
        Button cameraButton = findViewById(R.id.CameraButton);
        Button galleryButton = findViewById(R.id.GalleryButton);
        Button mapButton = findViewById(R.id.mapButton);
        Button addButton = findViewById(R.id.addHabitEventButton);
        commentText = findViewById(R.id.Comment);
        image = findViewById(R.id.photo);
        addressLine = findViewById(R.id.addressLineText);
        noPhotoText = findViewById(R.id.add_no_photo_text);

        // set visibility
        image.setVisibility(View.GONE);
        noPhotoText.setVisibility(View.VISIBLE);

        // setting up map view
        mapView = (MapView) findViewById(R.id.add_event_map_view);
        mapView.onCreate(Bundle.EMPTY);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;
                LatLng ny = new LatLng(40.7143528, -74.0059731);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ny, 15));
            }
        });
        MapsInitializer.initialize(this);
        mapView.setClickable(false);

        // events data
        habitEvent = new HabitEvent();
        db = new DatabaseManager();

        // set the completed date of the habit event
        habitEvent.setCompletedDate(Calendar.getInstance());

        // get habit data from intent
        Intent intent = getIntent();
        this.habit = intent.getParcelableExtra("HABIT");

        // the activity launcher to get an image from the gallery
        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri contentUri = result.getData().getData();
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                        noPhotoText.setVisibility(View.GONE);
                        image.setVisibility(View.VISIBLE);
                        image.setImageURI(contentUri);
                        habitEvent.setPhotograph(db.uploadImageToFirebase(imageFileName, contentUri, mStorageRef));
                    }
                });

        // the activity launcher for taking image using the phone camera
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        File f = new File(currentPhotoPath);
                        noPhotoText.setVisibility(View.GONE);
                        image.setVisibility(View.VISIBLE);
                        image.setImageURI(Uri.fromFile(f));
                        Uri contentUri = Uri.fromFile(f);
                        habitEvent.setPhotograph(db.uploadImageToFirebase(f.getName(), contentUri, mStorageRef));
                        // save the image to the gallery
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        mediaScanIntent.setData(contentUri);
                        AddHabitEventActivity.this.sendBroadcast(mediaScanIntent);
                    }
                });

        // the activity launcher for starting map activity
        mapActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                if (result.getData().getBooleanExtra("permission", false)) {
                    double latitude = result.getData().getDoubleExtra("latitude", 0.000000);
                    double longitude = result.getData().getDoubleExtra("longitude", 0.000000);
                    Location eventLocation = new Location("gps");
                    eventLocation.setLatitude(latitude);
                    eventLocation.setLongitude(longitude);
                    habitEvent.setLocation(eventLocation);
                    addressLine.setText("");
                    addressLine.setText(getAddress(eventLocation.getLatitude(), eventLocation.getLongitude()));
                    // set up the map
                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(latLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mapView.setVisibility(View.VISIBLE);
                }
            }
        });
        // setting listeners
        cameraButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
    }

    /**
     * onClick
     * <p>
     * Handles behavior of the onClick() method for all buttons in AddHabitEventActivity
     *
     * @param view current view
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addHabitEventButton) {

            // Listener for add habit event button
            habitEvent.setComment(Objects.requireNonNull(commentText.getText()).toString());
            ArrayList<HabitEvent> currentEventList = habit.getHabitEvents();
            currentEventList.add(habitEvent);
            habit.setHabitEvents(currentEventList);

            // Pass the result back to BaseActivity
            Intent result = new Intent();
            result.putExtra("HABIT", habit);
            result.putExtra("HABIT_EVENT", habitEvent);
            setResult(RESULT_CODE, result);
            this.finish();

        } else if (view.getId() == R.id.mapButton) {
            // Listener for add location button
            addressLine.setText("");
            Intent map = new Intent(view.getContext(), MapsActivity.class);
            mapActivityResultLauncher.launch(map);

        } else if (view.getId() == R.id.GalleryButton) {
            // Listener for add from gallery button
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryActivityResultLauncher.launch(gallery);

        } else if (view.getId() == R.id.CameraButton) {
            // Listener for add from camera button
            askCameraPermission();
        }
    }

    /**
     * askCameraPermission
     * <p>
     * Asks the user for permission to use the camera
     * and if the permission has already been granted starts the process of taking picture using the camera
     */
    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(AddHabitEventActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddHabitEventActivity.this, new String[]{
                    Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    /**
     * dispatchTakePictureIntent
     * <p>
     * creates the intent for taking picture with the camera and starts the activity
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();

        } catch (IOException ex) {
            Toast.makeText(this, "Error creating file", Toast.LENGTH_SHORT);
        }

        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            // starts the activity
            cameraActivityResultLauncher.launch(takePictureIntent);
        }
    }

    /**
     * createImageFile
     * <p>
     * creates an image file and returns the file
     *
     * @return File returns the image file
     * @throws IOException could throw permission denied exception
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * onRequestPermissionsResult
     * <p>
     * Processes the permission for the camera and starts the process for taking photos with the camera
     *
     * @param requestCode  int: The request code passed in ActivityCompat.requestPermissions(android.app.Activity, String[], int)
     * @param permissions  String: The requested permissions. Never null.
     * @param grantResults int: The grant results for the corresponding permissions which is either
     *                     PackageManager.PERMISSION_GRANTED or PackageManager.PERMISSION_DENIED.
     *                     Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // granted camera permission
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permission is Required", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * getFileExt
     *
     * @param contentUri Uri, the image uri
     * @return String, returns the extension of the image file
     */
    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    /**
     * getAddress
     * <p>
     * returns the street address in the mentioned latitude and longitude
     *
     * @param latitude  double, the latitude of the location
     * @param longitude double, the longitude of the location
     * @return String, the address of the location
     */
    public String getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addressList;
        geocoder = new Geocoder(this, Locale.getDefault());
        String address = "";
        // Get address using geocoder
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
            address = addressList.get(0).getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
