package com.cmput301f21t34.habittrak;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import com.cmput301f21t34.habittrak.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * MainActivity
 * <p>
 * Starting point of the app
 * TODO : Testing that the database returns the correct on days object
 */
public class MainActivity extends AppCompatActivity implements Utilities {

    // Get shared prefs
    private FirebaseUser fUser;
    private User mainUser;
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Log In State
        db = new DatabaseManager();


        fUser = FirebaseAuth.getInstance().getCurrentUser();

        if (fUser != null) {
            String email = fUser.getEmail();
            mainUser = db.getUser(email);
            goToBaseActivity(this, mainUser);

        } else {
            // get user to login
            goToLogin(this);
        }
    }
}

