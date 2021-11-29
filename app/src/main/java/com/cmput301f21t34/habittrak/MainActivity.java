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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Log In State
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        if (fUser != null) {
            goToBaseActivity(this);

        } else {
            // get user to login
            goToLogin(this);
        }
    }
}

