package com.cmput301f21t34.habittrak;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cmput301f21t34.habittrak.HabitActivity.AddHabitActivity;
import com.cmput301f21t34.habittrak.fragments.AllHabitsFragment;
import com.cmput301f21t34.habittrak.fragments.EventsFragment;
import com.cmput301f21t34.habittrak.fragments.ProfileFragment;
import com.cmput301f21t34.habittrak.fragments.SocialFragment;
import com.cmput301f21t34.habittrak.fragments.TodayListFragment;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitEvent;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;


/**
 * BaseActivity
 *
 * @author Pranav
 * <p>
 * Base Activity after logging in.
 * Hold the topbar, bottomnav bar and the base fragments
 * Also used for updated the user data to firestore
 * @version 1.0
 * @since 2021-10-16
 */
public class BaseActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, Utilities {

    // Result codes from activity
    public static final int RESULT_NEW_HABIT = 1000;
    public static final int RESULT_EDIT_HABIT = 2000;
    public static final int RESULT_NEW_HABIT_EVENT = 3000;
    public static final int RESULT_HABIT_EVENTS = 5000;
    DatabaseManager db = new DatabaseManager();

    // views
    private NavigationBarView bottomNav;
    private User mainUser;      // Creates dummy user for testing purposes
    private TodayListFragment todayFrag;
    private ProfileFragment profileFrag;
    private EventsFragment eventsFrag;
    private AllHabitsFragment allHabitsFrag;
    private SocialFragment socialFrag;
    private MaterialButton addHabitButton;
    private ActivityResultLauncher<Intent> addHabitActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
            }
    );

    /**
     * Function called when activity is created
     *
     * @param savedInstanceState savedInstances
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Get main User.
        if (mainUser == null) {
            mainUser = getMainUser(this);
        }

        // update the streak variables of the user habits
        refreshHabitStreak(mainUser);

        // button views
        addHabitButton = findViewById(R.id.base_add_habit_button);

        // Initializes Fragments
        todayFrag = new TodayListFragment(mainUser);
        profileFrag = new ProfileFragment(mainUser);
        eventsFrag = new EventsFragment(mainUser);
        allHabitsFrag = new AllHabitsFragment(mainUser);
        socialFrag = new SocialFragment(mainUser);

        // Sets up Nav Bar
        bottomNav = findViewById(R.id.bottom_nav);              // Sets Nav to bottom nav res
        bottomNav.setOnItemSelectedListener(this);              // Sets listener to this class
        bottomNav.setSelectedItemId(R.id.navbar_menu_today);    // Sets initial selected item
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshHabitStreak(mainUser);

        // add habit listener
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddHabitActivity.class);
                addHabitActivityLauncher.launch(intent);
            }
        });
    }

    /**
     * function is ran just before the application is close.
     * Used to updated the firestore with the latest data
     */
    @Override
    public void onPause() {
        super.onPause();
        // Update before we are ever terminated (or unfocused)
        updateHabitListDB(mainUser);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // result from add habit activity
        if (resultCode == RESULT_NEW_HABIT) {
            Habit newHabit = intent.getParcelableExtra("newHabit");
            mainUser.getHabitList().add(newHabit);
            todayFrag.refreshTodayFragment(); // refresh view
            allHabitsFrag.refreshAllFragment();
        }
        // result from view/edit habit activity
        else if (resultCode == RESULT_EDIT_HABIT) {
            Habit habit = intent.getParcelableExtra("HABIT");
            mainUser.getHabitList().replace(habit);
            todayFrag.refreshTodayFragment();
            allHabitsFrag.refreshAllFragment();
        }
        // result from add habit event activity
        else if (resultCode == RESULT_NEW_HABIT_EVENT) {
            Habit habit = intent.getParcelableExtra("HABIT");
            mainUser.getHabitList().replace(habit);
        }
        // result from view habit events activity
        else if (resultCode == RESULT_HABIT_EVENTS) {
            Habit habit = intent.getParcelableExtra("HABIT");
            mainUser.getHabitList().replace(habit);
        }

        // Update Database after results
        updateHabitListDB(mainUser);

        super.onActivityResult(requestCode, resultCode, intent);
    }
}
