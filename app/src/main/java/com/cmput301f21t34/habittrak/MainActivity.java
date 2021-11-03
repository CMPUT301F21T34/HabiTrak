package com.cmput301f21t34.habittrak;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cmput301f21t34.habittrak.user.User;

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

        User mainUser = new User();

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

        }

    }
}
