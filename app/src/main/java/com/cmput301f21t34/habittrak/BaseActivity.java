package com.cmput301f21t34.habittrak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.cmput301f21t34.habittrak.fragments.AllHabitsFragment;
import com.cmput301f21t34.habittrak.fragments.EventsFragment;
import com.cmput301f21t34.habittrak.fragments.ProfileFragment;
import com.cmput301f21t34.habittrak.fragments.SocialFragment;
import com.cmput301f21t34.habittrak.fragments.TodayListFragment;


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

public class BaseActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    /**
     * Function called when activity is created
     * @param savedInstanceState savedInstances
     */


    NavigationBarView bottomNav;
    User mainUser;

    TodayListFragment todayFrag;
    SocialFragment socialFrag;
    ProfileFragment profileFrag;
    EventsFragment eventsFrag;
    AllHabitsFragment allHabitsFrag;


    public static final int RESULT_NEW_HABIT = 1000; // Custom Activity Result





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Intent intent = getIntent();
        this.mainUser = intent.getParcelableExtra("mainUser"); // Gets mainUser from intent

        Log.d("mainUser", "in BaseActivity mainUser: " + mainUser.getUsername());


        todayFrag = new TodayListFragment(mainUser);
        socialFrag = new SocialFragment(mainUser);
        profileFrag = new ProfileFragment(mainUser);
        eventsFrag = new EventsFragment(mainUser);
        allHabitsFrag = new AllHabitsFragment(mainUser);



        //set up toolbar
        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_base);


         */

        // Sets up Nav Bard
        bottomNav = findViewById(R.id.bottom_nav); // Sets Nav to bottom nav

        bottomNav.setOnItemSelectedListener(this);  // Sets listener to this
        bottomNav.setSelectedItemId(R.id.navbar_menu_today); // Sets inital selected item



        // Creates a mainUser bundle
        //Bundle mainUserBundle = new Bundle();
        //mainUserBundle.putParcelable("mainUser", mainUser);


        // Adds any new habits







        /*
        // setting the controller for bottom nav bar
        NavHostFragment navHostFragment = (NavHostFragment) // NavHostFragment extends Fragment, it is what holds each fragment
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment); // nav_host_frag is activity_main.xml is what hosts each frag
        NavController navController = navHostFragment.getNavController();
        navController.setGraph(R.id.nav_graph, mainUserBundle);


         */


        



    }

    /**
     * onBackPressed
     * Overriding the back button to minimize the app instead of going to login screen
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true); // minimizes the screen instead of going to login screen
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navbar_menu_today:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, todayFrag).commit();
                return true;
            case R.id.navbar_menu_events:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, eventsFrag).commit();
                return true;
            case R.id.navbar_menu_habits:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, allHabitsFrag).commit();
                return true;
            case R.id.navbar_menu_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, profileFrag).commit();
                return true;
            case R.id.navbar_menu_social:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, socialFrag).commit();
                return true;
        }

        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_NEW_HABIT) {
            Habit newHabit = intent.getParcelableExtra("newHabit");
            mainUser.addHabit(newHabit);
            todayFrag.refreshHabitList();


        }

        super.onActivityResult(requestCode, resultCode, intent);
    }
}