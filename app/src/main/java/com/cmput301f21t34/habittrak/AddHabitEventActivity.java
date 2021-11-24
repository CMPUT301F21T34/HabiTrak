package com.cmput301f21t34.habittrak;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

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
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitEvent;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

// Map libraries
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * @author Tauseef Nafee Fattah
 * @author Henry
 * Version: 1.0
 * Takes a habit and returns the new habit event
 * TODO: Fix the camera bug
 */
public class AddHabitEventActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int GALLERY_REQUEST_CODE = 105;
    Button cameraButton;
    Button galleryButton;
    Button mapButton;
    Button addButton;
    EditText commentText;
    ImageView image;
    TextView addressLine;
    public static int RESULT_CODE = 3000;
    public static final int Camera_Permission_CODE = 100;
    public static final int Camera_REQUEST_CODE = 101;
    private StorageReference mStorageRef;
    String currentPhotoPath;
    HabitEvent habitEvent;
    DatabaseManager db;
    ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    ActivityResultLauncher<Intent> galleryActivityResultLauncher;
    ActivityResultLauncher<Intent> mapActivityResultLauncher;
    private FusedLocationProviderClient fusedLocationClient;

    // intent data variables
    private Habit habit;
    private String email;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit_event);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        habitEvent = new HabitEvent();

        cameraButton = findViewById(R.id.CameraButton);
        galleryButton = findViewById(R.id.GalleryButton);
        mapButton = findViewById(R.id.mapButton);
        addButton = findViewById(R.id.addHabitEventButton);
        commentText = findViewById(R.id.Comment);
        image = findViewById(R.id.photo);
        addressLine = findViewById(R.id.addressLineText);

        db = new DatabaseManager();

        // set the completed date of the habit event
        habitEvent.setCompletedDate(Calendar.getInstance());

        // get data from clicked item
        Intent intent = getIntent();
        this.habit = intent.getParcelableExtra("HABIT");
        this.email = intent.getStringExtra("USER");
        this.position = intent.getIntExtra("position", 0);

        Log.d("USER TO UPDATE", email);
        Log.d("HABIT IN ADD EVENT", habit.getTitle());

        // the activity launcher to get an image from the gallery
        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri contentUri = result.getData().getData();
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                    Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                    image.setImageURI(contentUri);
                    habitEvent.setPhotograph(db.uploadImageToFirebase(imageFileName, contentUri, mStorageRef));
                }
            }
        });

        // the activity launcher for taking image using the phone camera
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Log.d("CAMERA", "entered camera on activity result");
                if (result.getResultCode() == Activity.RESULT_OK ) {
                    Log.d("CAMERA", "entered camera on activity result if condition");
                    File f = new File(currentPhotoPath);
                    Log.d("CAMERA","Setting the image");
                    image.setImageURI(Uri.fromFile(f));
                    Log.d("CAMERA", "Absolute url is " + Uri.fromFile(f));
                    Uri contentUri = Uri.fromFile(f);
                    Log.d("CAMERA", "ENTERING TO FIREBASE");
                    habitEvent.setPhotograph(db.uploadImageToFirebase(f.getName(), contentUri, mStorageRef));
                    Log.d("CAMERA", "Exited the FIREBASE");
                    Log.d("Edit_HAbit", "The uri is :" + habitEvent.getPhotograph());

                    Log.d("CAMERA","Entering gallery stage");

                    // save the image to the gallery
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    //Uri contentUri = Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    AddHabitEventActivity.this.sendBroadcast(mediaScanIntent);
                    // to load the image using the uri use Picasso.get().load(he.getUri()).into(image);
                    Log.d("CAMERA","Exiting gallery stage");
                }

                else {
                    Log.d("CAMERA", "Failed onActivityResult if condition");

                }

            }
        });

        mapActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    double latitude = result.getData().getDoubleExtra("latitude", 0.000000);
                    double longitude = result.getData().getDoubleExtra("longitude", 0.000000);
                    Location eventLocation = new Location("gps");
                    eventLocation.setLatitude(latitude);
                    eventLocation.setLongitude(longitude);
                    habitEvent.setLocation(eventLocation);
                    addressLine.setText("");
                    addressLine.setText(getAddress(eventLocation.getLatitude(),eventLocation.getLongitude()));
                }
                else {
                    Log.d("MAP", "Failed onActivityResult if condition");
                }

            }
        });

        // setting listeners
        cameraButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
    }

    // Handles behaviors of button clicks
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addHabitEventButton) {
            Log.d("CAMERA", "pressed add button in habit event");
            habitEvent.setComment(commentText.getText().toString());
            ArrayList<HabitEvent> currentEventList = habit.getHabitEvents();
            currentEventList.add(habitEvent);
            habit.setHabitEvents(currentEventList);
            // Propagate the changes to the database
            DatabaseManager db = new DatabaseManager();
            db.updateHabitList(email, habit);

            // Pass the result back to BaseActivity
            Intent result = new Intent();
           // result.putExtra("HABIT_EVENT", habitEvent);
            result.putExtra("HABIT", habit);
            result.putExtra("position", position);
            if(habitEvent.getPhotograph() == null){
                Log.d("ADDHABITEVENT", "base the uri is null");
            }
            else {
                Log.d("ADDHABITEVENT", "base the uri is not null");
            }
            result.putExtra("HABIT_EVENT",habitEvent);
            setResult(RESULT_CODE, result);
            Log.d("CAMERA", "ready to finish");
            this.finish();
        }
        else if (view.getId() == R.id.mapButton) {
            addressLine.setText("");
            Intent map = new Intent(view.getContext(), MapsActivity.class);
            mapActivityResultLauncher.launch(map);
        }
        else if (view.getId() == R.id.GalleryButton) {
            Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryActivityResultLauncher.launch(gallery);
        }
        else if (view.getId() == R.id.CameraButton) {
            askCameraPermission();
        }
    }

    /**
     * Asks the user for permission to use the camera
     * and if the permission has already been granted starts the process of taking picture using the camera
     */
    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(AddHabitEventActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(AddHabitEventActivity.this, new String[]{
                    Manifest.permission.CAMERA}, Camera_Permission_CODE);
        }
        else{
            dispatchTakePictureIntent();
        }
    }

    /**
     * creates the intent for taking picture with the camera and starts the activity
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d("CAMERA","entered here1");
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
            Log.d("Camera","Entered the if condition in dispatchtakePictureIntent after createImageFile");
        } catch (IOException ex) {
            Log.d("CAMERA","Dispatch take picture intent exception: " + ex.getLocalizedMessage());
        }

        // Continue only if the File was successfully created
        if (photoFile != null) {
            Log.d("CAMERA","calling here");
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            // starts the activity
            cameraActivityResultLauncher.launch(takePictureIntent);

        }
        else{
            Log.d("CAMERA","The photofile is null");
        }
    }

    /**
     * creates an image file and returns the file
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
     * Processes the permission for the camera and starts the process for taking photos with the camera
     * @param requestCode int: The request code passed in ActivityCompat.requestPermissions(android.app.Activity, String[], int)
     * @param permissions String: The requested permissions. Never null.
     * @param grantResults int: The grant results for the corresponding permissions which is either
     *                     PackageManager.PERMISSION_GRANTED or PackageManager.PERMISSION_DENIED.
     *                     Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Camera_Permission_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // granted camera permission
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permission is Required", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
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
     * returns the street address in the mentioned latitude and longitude
     * @param latitude double, the latitude of the location
     * @param longitude double, the longitude of the location
     * @return String, the address of the location
     */
    public String getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        String address = "";

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String addressLine = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            address = address + addressLine.toString() + ", " + city.toString() + ", " + state.toString()
                    + ", " + country.toString() + ", " + postalCode.toString();
        } catch (Exception e) {
            Log.d("address failed", "yep");
            e.printStackTrace();
        }
        return address;
    }
}