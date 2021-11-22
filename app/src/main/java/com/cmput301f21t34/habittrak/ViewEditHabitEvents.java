package com.cmput301f21t34.habittrak;

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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitEvent;
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

public class ViewEditHabitEvents extends AppCompatActivity {
    private TextInputEditText comment;
    private TextView addressLine;
    private ImageView image;
    private MaterialButton galleryBtn;
    private MaterialButton cameraBtn;
    private MaterialButton saveHabitEventBtn;
    private TextView completionDateCalendar;
    public static int RESULT_CODE = 4000;
    public static final int Camera_Permission_CODE = 100;
    public static final int Camera_REQUEST_CODE = 101;
    private StorageReference mStorageRef;
    String currentPhotoPath;
    private HabitEvent returnedHabitEvent;
    private Habit habit;

    DatabaseManager db;
    ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    ActivityResultLauncher<Intent> galleryActivityResultLauncher;
    ActivityResultLauncher<Intent> mapActivityResultLauncher;


    private HabitEvent habitEvent;
    private String TAG = "EDIT_HABIT_EVENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_habit_events);

        // initializing variable
        mStorageRef = FirebaseStorage.getInstance().getReference();
        db = new DatabaseManager();
        returnedHabitEvent = new HabitEvent();

        // get data

        Intent intent = getIntent();
        this.habitEvent = intent.getParcelableExtra("HABIT_EVENT_VIEW");
        this.habit = intent.getParcelableExtra("HABIT_VIEW");
        Log.d(TAG,"The completed date of the habit event is "+ habitEvent.getComment());
        Log.d(TAG,"The habit is "+habit.getTitle());


        // get views
        comment = findViewById(R.id.comment_edit_text);
        addressLine = findViewById(R.id.address_line_edit);
        image = findViewById(R.id.photo_edit);
        galleryBtn = findViewById(R.id.gallery_button_edit);
        cameraBtn = findViewById(R.id.camera_button_edit);
        saveHabitEventBtn = findViewById(R.id.save_habit_event_edit);
        completionDateCalendar = findViewById(R.id.completion_date_calendar);
Log.d(TAG,"Got the views");

        //get data from habit event
        String commentHabitEvent = habitEvent.getComment();
        Calendar completedDate = habitEvent.getCompletedDate();
        Uri photoUri = habitEvent.getPhotograph();
        Location locationHabitEvent = habitEvent.getLocation();
Log.d(TAG,"got the data");
// setting the data to the new habit event
        returnedHabitEvent.setCompletedDate(completedDate);
        returnedHabitEvent.setPhotograph(photoUri);
        returnedHabitEvent.setComment(commentHabitEvent);
        returnedHabitEvent.setLocation(locationHabitEvent);
        Log.d(TAG,"The uri is " + returnedHabitEvent.getPhotograph());
        Log.d(TAG,"The comment is " + returnedHabitEvent.getComment());

        // set data to the fields
        if (commentHabitEvent != null){
            comment.setText(commentHabitEvent);
        }
        if (photoUri != null){
            // set the photo
            Picasso.get().load(photoUri).into(image);
        }

        if (locationHabitEvent != null){
            // set the address

            addressLine.setText(getAddress(locationHabitEvent.getLatitude(),locationHabitEvent.getLongitude()));
        }
        else{
            Log.d(TAG,"Address is not selelceted");
            addressLine.setText("No location selected");
        }
Log.d(TAG,"set the data");
        // set the date
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        completionDateCalendar.setText(format.format(completedDate.getTime()));
Log.d(TAG,"Set the dateeeee");
        // setting up the activity launchers

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
                            returnedHabitEvent.setPhotograph(db.uploadImageToFirebase(imageFileName, contentUri, mStorageRef));
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
                            returnedHabitEvent.setPhotograph(db.uploadImageToFirebase(f.getName(), contentUri, mStorageRef));
                            Log.d("CAMERA", "Exited the FIREBASE");
                            Log.d("CAMERA", "The uri is :" + returnedHabitEvent.getPhotograph());

                            Log.d("CAMERA","Entering gallery stage");

                            // save the image to the gallery
                            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            //Uri contentUri = Uri.fromFile(f);
                            mediaScanIntent.setData(contentUri);
                            ViewEditHabitEvents.this.sendBroadcast(mediaScanIntent);
                            // to load the image using the uri use Picasso.get().load(he.getUri()).into(image);
                            Log.d("CAMERA","Exiting gallery stage");
                        }

                        else {
                            Log.d("CAMERA", "Failed onActivityResult if condition");

                        }

                    }
                });
/*
// uncomment if we want allow the user to change address
// in this case a map button and uncomment the onClickListener
        mapActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    double latitude = result.getData().getDoubleExtra("latitude", 0.000000);
                    double longitude = result.getData().getDoubleExtra("longitude", 0.000000);
                    Location eventLocation = new Location("gps");
                    eventLocation.setLatitude(latitude);
                    eventLocation.setLongitude(longitude);
                    returnedHabitEvent.setLocation(eventLocation);
                    addressLine.setText("");
                    addressLine.setText(getAddress(eventLocation.getLatitude(),eventLocation.getLongitude()));
                }
                else {
                    Log.d("MAP", "Failed onActivityResult if condition");
                }

            }
        });
*/
        // handling the onclick listener
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermission();
            }
        });
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryActivityResultLauncher.launch(gallery);
            }
        });
/*
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map = new Intent(view.getContext(), MapsActivity.class);
                mapActivityResultLauncher.launch(map);
            }
        });
*/
        saveHabitEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Save button pressed");
                returnedHabitEvent.setComment(comment.getText().toString());
                // create the intent to return the habit event
                Log.d(TAG,"creating intent");
                Log.d(TAG,"The size of the habit event list is " + habit.getHabitEvents().size());
                habit.addHabitEvent(returnedHabitEvent);
                Log.d(TAG,"The size of the habit event list is " + habit.getHabitEvents().size());
                Intent result = new Intent();
                result.putExtra("HABIT_SAVE", habit);
                setResult(RESULT_CODE, result);
                ViewEditHabitEvents.this.finish();
            }
        });


    }


    /**
     * Asks the user for permission to use the camera
     * and if the permission has already been granted starts the process of taking picture using the camera
     */
    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(ViewEditHabitEvents.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ViewEditHabitEvents.this, new String[]{
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