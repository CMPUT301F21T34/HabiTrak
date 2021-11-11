package com.cmput301f21t34.habittrak;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.User;
import com.cmput301f21t34.habittrak.fragments.AllHabitsFragment;
import com.cmput301f21t34.habittrak.fragments.EventsFragment;
import com.cmput301f21t34.habittrak.fragments.ProfileFragment;
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

    final String TAG = "Base_Activity";
    // Result codes from activity
    public static final int RESULT_NEW_HABIT = 1000;
    public static final int RESULT_EDIT_HABIT = 2000;

    /**
     * Function called when activity is created
     * @param savedInstanceState savedInstances
     */


    //TODO: Explicitly make attributes private
    NavigationBarView bottomNav;
    User mainUser;// = new User(); // Creates dummy user for testing purposes
    // navigation fragments
    TodayListFragment todayFrag;
    ProfileFragment profileFrag;
    EventsFragment eventsFrag;
    AllHabitsFragment allHabitsFrag;

    MaterialButton addHabitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Gets Intents //
        Intent intent = getIntent();

        this.mainUser = intent.getParcelableExtra("mainUser"); // Gets mainUser from intent

        addHabitButton = findViewById(R.id.base_add_habit_button);

        // Initializes Fragments //
        todayFrag = new TodayListFragment(mainUser);
        profileFrag = new ProfileFragment(mainUser);
        eventsFrag = new EventsFragment(mainUser);
        allHabitsFrag = new AllHabitsFragment(mainUser);

        // Sets up Nav Bar //
        bottomNav = findViewById(R.id.bottom_nav); // Sets Nav to bottom nav res
        bottomNav.setOnItemSelectedListener(this);  // Sets listener to this class
        bottomNav.setSelectedItemId(R.id.navbar_menu_today); // Sets initial selected item

        // add habit listener
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: remove this and change it back to habit activity
                // for testing AddHabitEventsActivity
                /*Intent remove =new Intent(BaseActivity.this,AddHabitEventActivity.class);
                startActivity(remove);

                 */

                Intent intent = new Intent(view.getContext(), AddHabitActivity.class);
                addHabitActivityLauncher.launch(intent);
            }
        });

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

                Intent intent = new Intent(getBaseContext(), SocialActivity.class);
                intent.putExtra("mainUser", mainUser); // passes mainUser through intent
                startActivity(intent);
                bottomNav.setSelectedItemId(R.id.navbar_menu_today);
                addHabitButton.setVisibility(View.VISIBLE);

                return false;
        }

        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // result from add habit activity
        if (resultCode == RESULT_NEW_HABIT) {
            Habit newHabit = intent.getParcelableExtra("newHabit");
            Log.d(TAG, "adding new habit: " + newHabit.getTitle());
            mainUser.getHabitList().add(newHabit);
            Log.d(TAG, "!newHabit Absolute Index: " + String.valueOf(newHabit.getIndex()));
            Log.d(TAG, "new habit: " + mainUser.getHabit(mainUser.getHabitList().size() - 1).getTitle());
            Log.d(TAG, "size: " + String.valueOf(mainUser.getHabitList().size()));
            todayFrag.refreshTodayFragment(); // refresh view
        }
        // result from view/edit habit activity
        else if (resultCode == RESULT_EDIT_HABIT){
            Habit habit = intent.getParcelableExtra("HABIT");
            int position = intent.getIntExtra("position", 0);
            mainUser.replaceHabit(position, habit);
            todayFrag.refreshTodayFragment();
            allHabitsFrag.refreshAllFragment();
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    // activity result launcher for add habit activity
    ActivityResultLauncher<Intent> addHabitActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
            }
    );


}