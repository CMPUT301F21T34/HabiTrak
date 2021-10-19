package com.cmput301f21t34.habittrak;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
            Start the login fragment if no saved data from previous logins.
        */
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.login_fragment_container, new LoginFragment())
                    .commit();
        }

        /*
            Add Logic if the user is already logged in before
         */
    }
}
