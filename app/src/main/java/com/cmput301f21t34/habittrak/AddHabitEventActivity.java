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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cmput301f21t34.habittrak.user.HabitEvent;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

    Button cameraBtn;
    Button galleryBtn;
    Button addBtn;
    EditText commentText;
    ImageView image;
    public static final int Camera_Permission_CODE = 100;
    public static final int Camera_REQUEST_CODE = 101;
    private StorageReference mStorageRef;
    String currentPhotoPath;
    HabitEvent habitEvent;
    DatabaseManager db;
    ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit_event);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        habitEvent = new HabitEvent();
        cameraBtn = findViewById(R.id.CameraButton);
        galleryBtn = findViewById(R.id.GalleryButton);
        addBtn = findViewById(R.id.addHabitEventButton);
        commentText = findViewById(R.id.Comment);
        image = findViewById(R.id.photo);
        db = new DatabaseManager();

        // the activity launcher for taking image using the phone camera
        cameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                    File f = new File(currentPhotoPath);
                    image.setImageURI(Uri.fromFile(f));
                    Log.d("CAMERA","Absolute url is " + Uri.fromFile(f));
                    Uri contentUri = Uri.fromFile(f);
                    Log.d("CAMERA","ENTERING TO FIREBASE");
                    habitEvent.setPhotograph(db.uploadImageToFirebase(f.getName(),contentUri,mStorageRef));
                    Log.d("CAMERA", "The uri is :" + habitEvent.getPhotograph());
                    Log.d("CAMERA","Exited the FIREBASE");

                    // to load the image using the uri use Picasso.get().load(he.getUri()).into(image);
                }
                else{
                    Log.d("CAMERA","Failed onActivityResult if condition");

                }

            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermission();
            }
        });
        // returning the habit event object after the user pressed the add button
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // return the habit event object back
                habitEvent.setComment(commentText.getText().toString());
                Intent intent = new Intent(AddHabitEventActivity.this,BaseActivity.class);
                intent.putExtra("addHabitEvent",habitEvent);
                setResult(Activity.RESULT_OK,intent);
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
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

}