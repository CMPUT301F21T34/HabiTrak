package com.cmput301f21t34.habittrak;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.cmput301f21t34.habittrak.auth.LoginFragment;
import com.cmput301f21t34.habittrak.streak.Streak;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Utilities
 *
 * @author Dakota
 *
 * General methods used in various locations in the app
 *
 * @version 1.0
 * @since 2021-11-27
 */

public interface Utilities {

    DatabaseManager db = new DatabaseManager();

    /**
     * updates the database with a user's habit list
     *
     * @author Dakota
     * @param user User to update in database
     */
    default void updateHabitListDB(User user){

        db.updateHabitList(user.getEmail(), user.getHabitList());

    }

    /**
     * refreshes all habit streaks
     *
     * @author Dakota
     */
    default void refreshHabitStreak(User user) {
        // Refreshes all habit streaks //
        ArrayList<Habit> habits = (ArrayList<Habit>) user.getHabitList(); // cast for simple iteration

        for (int index = 0; index < habits.size(); index++){
            Streak streak = new Streak(habits.get(index)); // set a Streak class to modify each habit
            streak.refreshStreak(); // refreshes each streak
        }
    }

    /**
     * Go to login page from an activity
     *
     * @author Dakota
     * @param activity Activity context to execute from (usually 'this')
     */
    default void goToLogin(AppCompatActivity activity) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.login_fragment_container, new LoginFragment(null))
                .commit();
    }

    /**
     * Go to login page from a fragment
     *
     * @author Dakota
     * @param activity Activity context to execute from (usually 'this')
     */
    default void goToLogin(FragmentActivity activity) {
        LoginFragment loginFragment = new LoginFragment(null);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_fragment_container, loginFragment, "loginFrag")
                .commit();
    }



    /**
     * Go to Base Activity
     *
     * @author Dakota
     * @param activity Activity context to execute from (usually 'this')
     * @param user User to pass into base activity
     */
    default void goToBaseActivity(Activity activity, User user) {
        Intent intent = new Intent(activity, BaseActivity.class);

        intent.putExtra("mainUser", user);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * Go to Main Activity
     *
     * @author Dakota
     * @param activity Activity context to execute from (usually 'this')
     */
    default void goToMainActivity(Activity activity){
        // Send user back to main activity
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * Gets the main user from database.
     *
     * @author Dakota
     * @param activity Activity context to execute from (usually 'this')
     * @return User from database using FirebaseUser
     */
    default User getMainUser(Activity activity) {
        // Updates the mainUser, even if they are already logged in
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser == null){
            goToMainActivity(activity);
        }
        return db.getUser(fUser.getEmail());
    }

    // For Parcelables //

    /**
     * Safely gets Calendar's from bundle
     *
     * @author Dakota
     * @param bundle bundle to get calendar from
     * @param key String key that the Calendar was stored in
     *            The TimeZone key used should be: key + "TimeZone" as is assumed
     * @return Calendar which can be null
     */
    default Calendar calendarParcelConstructor(Bundle bundle, String key){
        String timeZoneKey = key + "TimeZone";

        String timeZone = bundle.getString(timeZoneKey);
        if (timeZone != null){

            Calendar constructionCalendar = Calendar.getInstance();
            constructionCalendar.setTimeZone(TimeZone.getTimeZone(timeZone));
            constructionCalendar.setTimeInMillis(bundle.getLong(key));
            return constructionCalendar;
        }

        return null;

    }

    /**
     * Puts a Calendar into a Bundle with a given key
     *
     * @author Dakota
     * @param bundle Bundle to use
     * @param calendar Calendar to put in bundle
     * @param key String key to use (should be exact same as variables name case sensitive)
     */
    default void putCalendarInBundle(Bundle bundle, Calendar calendar, String key){
        if (calendar != null) {
            bundle.putString(key + "TimeZone", calendar.getTimeZone().getID());
            bundle.putLong(key, calendar.getTimeInMillis());
        } else {
            bundle.putString(key + "TimeZone", null);
        }
    }

}
