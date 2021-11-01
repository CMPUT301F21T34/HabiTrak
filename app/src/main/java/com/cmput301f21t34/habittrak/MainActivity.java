package com.cmput301f21t34.habittrak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;


import com.cmput301f21t34.habittrak.user.Database_Pointer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import com.cmput301f21t34.habittrak.user.User;

import java.util.ArrayList;

/**
 * MainActivity
 *
 * Starting point of the app
 * TODO: figure out how to save the login state of the user
 */
public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        User mainUser = null;

        if (savedInstanceState != null) {
            // get users credentials
            // validate credentials with database
            // populate mainUser
            // move to main menu
        } else {
            // get user to login
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.login_fragment_container, new LoginFragment(mainUser))
                    .commit();
        }*/
        DatabaseManager db = new DatabaseManager();
        User user = db.getUser("pog@gmail.com");
        String TAG ="Checkchek";

        Log.d(TAG,user.getUsername());
        Log.d(TAG,user.getFollowingList().get(1).getEmail());
    }
}
