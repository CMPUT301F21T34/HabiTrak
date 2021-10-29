package com.cmput301f21t34.habittrak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.cmput301f21t34.habittrak.fragments.AllHabitsFragment;
import com.cmput301f21t34.habittrak.fragments.EventsFragment;
import com.cmput301f21t34.habittrak.fragments.ProfileFragment;
import com.cmput301f21t34.habittrak.fragments.SocialFragment;
import com.cmput301f21t34.habittrak.fragments.TodayListFragment;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;

//TODO: Rename BaseActivity to a more suitable name

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


    //TODO: Explicitly make attributes private
    NavigationBarView bottomNav;
    User mainUser;

    TodayListFragment todayFrag;
    SocialFragment socialFrag;
    ProfileFragment profileFrag;
    EventsFragment eventsFrag;
    AllHabitsFragment allHabitsFrag;

    MaterialButton addHabitButton;


    public static final int RESULT_NEW_HABIT = 1000; // Custom Activity Result





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Gets Intents //
        Intent intent = getIntent();

        this.mainUser = intent.getParcelableExtra("mainUser"); // Gets mainUser from intent
        addHabitButton = findViewById(R.id.base_add_habit_button);
        Log.d("mainUser", "in BaseActivity mainUser: " + mainUser.getUsername());


        // Initializes Fragments //
        todayFrag = new TodayListFragment(mainUser);
        socialFrag = new SocialFragment(mainUser);
        profileFrag = new ProfileFragment(mainUser);
        eventsFrag = new EventsFragment(mainUser);
        allHabitsFrag = new AllHabitsFragment(mainUser);



        // Sets up Nav Bar //
        bottomNav = findViewById(R.id.bottom_nav); // Sets Nav to bottom nav res
        bottomNav.setOnItemSelectedListener(this);  // Sets listener to this class
        bottomNav.setSelectedItemId(R.id.navbar_menu_today); // Sets initial selected item






        



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
                addHabitButton.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, todayFrag).commit();
                return true;
            case R.id.navbar_menu_events:
                addHabitButton.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, eventsFrag).commit();
                return true;
            case R.id.navbar_menu_habits:
                addHabitButton.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, allHabitsFrag).commit();
                return true;
            case R.id.navbar_menu_profile:
                addHabitButton.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, profileFrag).commit();
                return true;
            case R.id.navbar_menu_social:
                addHabitButton.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, socialFrag).commit();
                return true;
        }

        return false;
    }


    //TODO:
    // implement public function in BaseActivity to handle
    // launching AddHabitActivity and refreshing views

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_NEW_HABIT) {
            Habit newHabit = intent.getParcelableExtra("newHabit");
            mainUser.addHabit(newHabit);
            todayFrag.refreshHabitList(); // refresh view


        }

        super.onActivityResult(requestCode, resultCode, intent);
    }
}