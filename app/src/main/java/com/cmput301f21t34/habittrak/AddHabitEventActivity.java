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
import java.util.Date;


/**
 * @author Tauseef Nafee Fattah
 * Version: 1.0
 * TODO: Add the gallery functionality, the map (to get address) and need to set the completed date
 */
public class AddHabitEventActivity extends AppCompatActivity {

    public static final int GALLERY_REQUEST_CODE = 105;
    Button cameraButton;
    Button galleryButton;
    Button mapButton;
    Button addButton;
    EditText commentText;
    ImageView image;
    public static int RESULT_CODE = 2000;
    public static final int Camera_Permission_CODE = 100;
    public static final int Camera_REQUEST_CODE = 101;
    private StorageReference mStorageRef;
    String currentPhotoPath;
    HabitEvent habitEvent;
    DatabaseManager db;
    ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    ActivityResultLauncher<Intent> galleryActivityResultLauncher;
    private FusedLocationProviderClient fusedLocationClient;

    // intent data variables
    private Habit habit;

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
        db = new DatabaseManager();

        // get data

        Intent intent = getIntent();
        this.habit = intent.getParcelableExtra("HABIT");
        // the activity to get an image from the gallery
        galleryActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
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
        cameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    File f = new File(currentPhotoPath);
                    image.setImageURI(Uri.fromFile(f));
                    Log.d("CAMERA", "Absolute url is " + Uri.fromFile(f));
                    Uri contentUri = Uri.fromFile(f);
                    Log.d("CAMERA", "ENTERING TO FIREBASE");
                    habitEvent.setPhotograph(db.uploadImageToFirebase(f.getName(), contentUri, mStorageRef));
                    Log.d("CAMERA", "The uri is :" + habitEvent.getPhotograph());
                    Log.d("CAMERA", "Exited the FIREBASE");
Log.d("CAMERA","Entering gallery stage");
                    // save it to the gallery
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

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermission();
            }
        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(gallery, GALLERY_REQUEST_CODE);
                galleryActivityResultLauncher.launch(gallery);
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    Intent map = new Intent(view.getContext(), MapsActivity.class);
                                    // putExtra here
                                    startActivity(map);
                                }
                            }
                        });*/
                Intent map = new Intent(view.getContext(), MapsActivity.class);
                startActivity(map);
            }
        });

        // returning the habit event object after the user pressed the add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // return the habit event object back
                habitEvent.setComment(commentText.getText().toString());
                habit.getHabitEvents().add(habitEvent);
                Intent result = new Intent();
                result.putExtra("HABIT",habit);
                setResult(RESULT_CODE, result);
                finish();
            }
        });
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
     * creates the intent for taking picture with the camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d("CAMERA","entered here1");

        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
        }

        // Continue only if the File was successfully created
        if (photoFile != null) {
            Log.d("CAMERA","calling here");
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            // starts the activity

            //startActivityForResult(takePictureIntent, Camera_REQUEST_CODE);
            cameraActivityResultLauncher.launch(takePictureIntent);

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



}