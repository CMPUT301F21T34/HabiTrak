package com.cmput301f21t34.habittrak;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cmput301f21t34.habittrak.auth.Auth;
import com.cmput301f21t34.habittrak.fragments.AllHabitsFragment;
import com.cmput301f21t34.habittrak.fragments.EventsFragment;
import com.cmput301f21t34.habittrak.fragments.ProfileFragment;
import com.cmput301f21t34.habittrak.fragments.SocialFragment;
import com.cmput301f21t34.habittrak.fragments.TodayListFragment;
import com.cmput301f21t34.habittrak.streak.Streak;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitEvent;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

//TODO: Rename BaseActivity to a more suitable name

/**
 * BaseActivity
 *
 * @author Pranav
 * <p>
 * Base Acitivity after logging in.
 * Hold the topbar, bottomnav bar and the base fragments
 * @version 1.0
 * @since 2021-10-16
 */
public class BaseActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    // Result codes from activity
    public static final int RESULT_NEW_HABIT = 1000;
    public static final int RESULT_EDIT_HABIT = 2000;
    public static final int RESULT_NEW_HABIT_EVENT = 3000;
    public static final int RESULT_HABIT_EVENTS = 5000;
    DatabaseManager db = new DatabaseManager();

    final String TAG = "Base_Activity";
    //TODO: Explicitly make attributes private
    NavigationBarView bottomNav;
    User mainUser;      // Creates dummy user for testing purposes
    TodayListFragment todayFrag;
    ProfileFragment profileFrag;
    EventsFragment eventsFrag;
    AllHabitsFragment allHabitsFrag;
    SocialFragment socialFrag;
    MaterialButton addHabitButton;
    ActivityResultLauncher<Intent> addHabitActivityLauncher = registerForActivityResult(
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

        // Gets Intents
        Intent intent = getIntent();
        mainUser = intent.getParcelableExtra("mainUser");      // Gets mainUser from intent



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

        
        refreshHabitStreak();

    }

    @Override
    public void onResume() {
        super.onResume();

        // Updates the mainUser, even if they are already logged in
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser == null){
            //TODO: Send to main

        }
        mainUser = db.getUser(fUser.getEmail());


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
     * refreshes all habit streaks
     *
     * @author Dakota
     */
    private void refreshHabitStreak() {
        // Refreshes all habit streaks //

        ArrayList<Habit> habits = (ArrayList<Habit>) mainUser.getHabitList(); // cast for simple iteration

        for (int index = 0; index < habits.size(); index++){
            Streak streak = new Streak(habits.get(index)); // set a Streak class to modify each habit
            streak.refreshStreak(); // refreshes each streak
        }
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
            Log.d(TAG, "adding new habit: " + newHabit.getTitle());
            mainUser.getHabitList().add(newHabit);
            Log.d(TAG, "!newHabit Absolute Index: " + String.valueOf(newHabit.getIndex()));
            Log.d(TAG, "new habit: " + mainUser.getHabit(mainUser.getHabitList().size() - 1).getTitle());
            Log.d(TAG, "size: " + String.valueOf(mainUser.getHabitList().size()));
            todayFrag.refreshTodayFragment(); // refresh view

            // TODO: Change the email to uuid in the next line
            db.updateHabitList(mainUser.getEmail(),mainUser.getHabitList());
            // TODO: Update all events list
        }
        // result from view/edit habit activity
        else if (resultCode == RESULT_EDIT_HABIT) {
            Habit habit = intent.getParcelableExtra("HABIT");
            int position = intent.getIntExtra("position", 0); // useless
            mainUser.getHabitList().replace(habit);
            todayFrag.refreshTodayFragment();
            allHabitsFrag.refreshAllFragment();

            // TODO: Change the email to uuid in the next line
            db.updateHabitList(mainUser.getEmail(),mainUser.getHabitList());
            // TODO: Update all events list

        }
        // result from add habit event activity
        else if (resultCode == RESULT_NEW_HABIT_EVENT) {
            HabitEvent habitEvent = intent.getParcelableExtra("HABIT_EVENT");

            Habit habit = intent.getParcelableExtra("HABIT");
            int position = intent.getIntExtra("position", 0); // useless
//            habit.incrementStreak();
            mainUser.getHabitList().replace(habit);
            // Propagate the changes to the database

            // TODO: Change the email to uuid in the next line
            db.updateHabitList(mainUser.getEmail(),mainUser.getHabitList());
            // TODO: Update all events list
        }
        // result from view habit events activity
        else if (resultCode == RESULT_HABIT_EVENTS){
            Habit habit = intent.getParcelableExtra("HABIT");
            int position = intent.getIntExtra("position", 0); // useless
            mainUser.getHabitList().replace(habit);

            // TODO: Change the email to uuid in the next line
            db.updateHabitList(mainUser.getEmail(),mainUser.getHabitList());
            // TODO: Update all events list
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }
}
