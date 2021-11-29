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
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
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
import com.cmput301f21t34.habittrak.user.HabitEvent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * activity for viewing and editing habit event
 * User can edit the location, picture and the comment of the habit event
 *
 * @author Aron
 * @version 1.0
 */
public class ViewEditHabitEventActivity extends AppCompatActivity {

    // result variables
    public static int RESULT_CODE = 4000;
    public static final int CAMERA_PERMISSION_CODE = 100;
    // views
    private TextInputEditText comment;
    private TextView addressLine;
    private ImageView image;
    private MaterialButton galleryBtn;
    private MaterialButton cameraBtn;
    private MaterialButton saveHabitEventBtn;
    private TextView completionDateCalendar;
    private MapView mapView;
    private GoogleMap map;
    // other variables
    private StorageReference mStorageRef;
    String currentPhotoPath;
    private HabitEvent returnedHabitEvent;
    private DatabaseManager db;
    // launcher variables
    ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    ActivityResultLauncher<Intent> galleryActivityResultLauncher;


    // intent data variables
    private HabitEvent habitEvent;
    private int habitPosition;
    private int eventPosition;

    private final String TAG = "EDIT_HABIT_EVENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_habit_events);

        // toolbar
        Toolbar toolbar = findViewById(R.id.view_edit_habit_event_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initializing variable
        mStorageRef = FirebaseStorage.getInstance().getReference();
        db = new DatabaseManager();
        returnedHabitEvent = new HabitEvent();

        // get habit event data from intent
        Intent intent = getIntent();
        this.habitEvent = intent.getParcelableExtra("HABIT_EVENT_VIEW");
        this.habitPosition = intent.getIntExtra("position", 0);
        this.eventPosition = intent.getIntExtra("event_position", 0);
        String title = intent.getStringExtra("HABIT_TITLE");
        toolbar.setTitle(title + "'s event");

        // get views
        comment = findViewById(R.id.comment_edit_text);
        addressLine = findViewById(R.id.address_line_edit);
        image = findViewById(R.id.photo_edit);
        galleryBtn = findViewById(R.id.gallery_button_edit);
        cameraBtn = findViewById(R.id.camera_button_edit);
        saveHabitEventBtn = findViewById(R.id.save_habit_event_edit);
        completionDateCalendar = findViewById(R.id.completion_date_calendar);
        mapView = (MapView) findViewById(R.id.view_event_map_view);
        mapView.onCreate(Bundle.EMPTY);

        //get data from habit event
        String commentHabitEvent = habitEvent.getComment();
        Calendar completedDate = habitEvent.getCompletedDate();
        Uri photoUri;

        if (habitEvent.getPhotograph() != null) {
            photoUri = Uri.parse(habitEvent.getPhotograph());
        } else {
            photoUri = null;
        }

        Location locationHabitEvent = habitEvent.getLocation();
        mapView.getMapAsync(googleMap -> {
            map = googleMap;
            LatLng latLng = new LatLng(40.7143528, -74.0059731);
            if (locationHabitEvent != null){
                latLng = new LatLng(locationHabitEvent.getLatitude(), locationHabitEvent.getLongitude());
                mapView.setVisibility(View.VISIBLE);
            }
            map.addMarker(new MarkerOptions().position(latLng));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        });

        // setting the data to the new habit event
        returnedHabitEvent.setCompletedDate(completedDate);
        returnedHabitEvent.setPhotograph(photoUri);
        returnedHabitEvent.setComment(commentHabitEvent);
        returnedHabitEvent.setLocation(locationHabitEvent);

        // set comment
        if (commentHabitEvent != null) {
            comment.setText(commentHabitEvent);
        }

        // set photo
        if (photoUri != null) {
            Picasso.get().load(photoUri).into(image);
        }

        // set address
        if (locationHabitEvent != null) {
            addressLine.setText(getAddress(locationHabitEvent.getLatitude(), locationHabitEvent.getLongitude()));
        } else {

            addressLine.setText("No location selected");
        }

        // set date
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        completionDateCalendar.setText(format.format(completedDate.getTime()));

        // setting up activity launchers
        // the activity launcher to get an image from the gallery
        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri contentUri = result.getData().getData();
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);

                        image.setImageURI(contentUri);
                        returnedHabitEvent.setPhotograph(db.uploadImageToFirebase(imageFileName, contentUri, mStorageRef));
                    }
                });
        // the activity launcher for taking image using the phone camera
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        File f = new File(currentPhotoPath);
                        image.setImageURI(Uri.fromFile(f));
                        Uri contentUri = Uri.fromFile(f);
                        returnedHabitEvent.setPhotograph(db.uploadImageToFirebase(f.getName(), contentUri, mStorageRef));
                        // save the image to the gallery
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        //Uri contentUri = Uri.fromFile(f);
                        mediaScanIntent.setData(contentUri);
                        ViewEditHabitEventActivity.this.sendBroadcast(mediaScanIntent);

                    } else {

                    }
                });

        // handling the onclick listener
        cameraBtn.setOnClickListener(view -> askCameraPermission());
        galleryBtn.setOnClickListener(view -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryActivityResultLauncher.launch(gallery);
        });
        // handling the save button click
        saveHabitEventBtn.setOnClickListener(view -> {

            if (!comment.getText().toString().equals(""))
                returnedHabitEvent.setComment(comment.getText().toString());
            // create the intent to return the habit event

            Intent result = new Intent();
            result.putExtra("HABIT_EVENT", returnedHabitEvent);
            result.putExtra("event_position", eventPosition);
            setResult(RESULT_CODE, result);
            this.finish();
        });
    }


    /**
     * askCameraPermission
     * <p>
     * Asks the user for permission to use the camera
     * and if the permission has already been granted starts the process of taking picture using the camera
     */
    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(ViewEditHabitEventActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ViewEditHabitEventActivity.this, new String[]{
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

        }
        // Continue only if the File was successfully created
        if (photoFile != null) {

            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            // starts the activity
            cameraActivityResultLauncher.launch(takePictureIntent);
        } else {

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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
