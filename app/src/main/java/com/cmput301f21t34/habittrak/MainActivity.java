package com.cmput301f21t34.habittrak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.cmput301f21t34.habittrak.auth.LoginFragment;
import com.cmput301f21t34.habittrak.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * MainActivity
 *
 * Starting point of the app
 * TODO: figure out how to save the login state of the user
 * TODO : Testing that the database returns the correct on days object
 *
 */
public class MainActivity extends AppCompatActivity implements Utilities{

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

