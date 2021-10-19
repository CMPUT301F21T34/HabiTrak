package com.cmput301f21t34.habittrak;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

/**
 * BaseActivity
 *
 * @author Pranav
 *
 * Base Acitivity after logging in.
 * Hold the topbar, bottomnav bar and the base fragments
 *
 * @version 1.0
 * @since 2021-10-16
 */

public class BaseActivity extends AppCompatActivity {

    /**
     * Function called when activity is created
     * @param savedInstanceState savedInstances
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_base);



        // setting the controller for bottom nav bar
        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNav, navController);


    }

    /**
     * onBackPressed
     * Overriding the back button to minimize the app instead of going to login screen
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true); // minimizes the screen instead of going to login screen
    }

}