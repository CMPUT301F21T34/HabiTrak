package com.cmput301f21t34.habittrak;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import com.cmput301f21t34.habittrak.auth.Auth;
import com.cmput301f21t34.habittrak.auth.LoginFragment;
import com.cmput301f21t34.habittrak.user.User;

/**
 * MainActivity
 *
 * Starting point of the app
 * TODO: figure out how to save the login state of the user
 * TODO : Testing that the database returns the correct on days object
 *
 */
public class MainActivity extends AppCompatActivity{

    private Auth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = new Auth(null,this);
        User mainUser = null;


        if (savedInstanceState != null) {
            // get users credentials
            // validate credentials with database
            // populate mainUser
            // move to main menu
        } else {
            // get user to login
            goToLogin();
        }

    }

    private void goToLogin(){
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.login_fragment_container, new LoginFragment(null))
                .commit();
    }

}

