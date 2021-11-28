package com.cmput301f21t34.habittrak;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.os.SystemClock;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.cmput301f21t34.habittrak.user.Habit;

import java.util.Calendar;

public class IntentTester {

    // Rule to set, or else all intent checks will fail
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void signUpTest(){
        //Click signup button
        onView(withId(R.id.signup_button))
                .perform(click());

        //Type in new signup information
        onView(withId(R.id.signup_email_edit_text))
                .perform(typeText("iTestEmail@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.signup_username_edit_text))
                .perform(typeText("iTestUser"), closeSoftKeyboard());
        onView(withId(R.id.signup_password_edit_text))
                .perform(typeText("iTestPass"), closeSoftKeyboard());

        //Click the signup button
        onView(withId(R.id.signup_signup_button))
                .perform(click());

        //Logout to standardize the steps for the tests
        SystemClock.sleep(3000);
        intended(hasComponent(BaseActivity.class.getName()));
        onView(withId(R.id.navbar_menu_profile))
                .perform(click());
        onView(withId(R.id.logout))
                .perform(click());

        // Delete the test user
        new DatabaseManager().deleteUser("iTestEmail@gmail.com");
    }

    @Test
    public void addHabitTest(){
        //Enter login information
        onView(withId(R.id.username_edit_text))
                .perform(typeText("rajabi@ualberta.ca"), closeSoftKeyboard());
        onView(withId(R.id.password_edit_text))
                .perform(typeText("password"), closeSoftKeyboard());
        //Click login to login
        onView(withId(R.id.login_button))
                .perform(click());
        SystemClock.sleep(2000);
        //Check to make sure we're on the intended activity, then click the add habit button
        intended(hasComponent(BaseActivity.class.getName()));
        onView(withId(R.id.base_add_habit_button))
                .perform(click());
        intended(hasComponent(AddHabitActivity.class.getName()));
        //Fill out the new habit form
        onView(withId(R.id.habit_name_edit_text))
                .perform(typeText("Breakfast"), closeSoftKeyboard());
        onView(withId(R.id.habit_reason_edit_text))
                .perform(typeText("Most important meal of the day"), closeSoftKeyboard());
        onView(withId(R.id.star_date_button))
                .perform(click());
        Espresso.pressBack();
        onView(withId(R.id.tuesday_button))
                .perform(click());
        onView(withId(R.id.thursday_button))
                .perform(click());
        onView(withId(R.id.save_habit))
                .perform((click()));
        intended(hasComponent(BaseActivity.class.getName()));
        //Logout to standardize the steps for the tests
        onView(withId(R.id.navbar_menu_profile))
                .perform(click());
        onView(withId(R.id.logout))
                .perform(click());
    }

    @Test
    public void habitAddedTest(){
        //Enter login information
        onView(withId(R.id.username_edit_text))
                .perform(typeText("rajabi@ualberta.ca"), closeSoftKeyboard());
        onView(withId(R.id.password_edit_text))
                .perform(typeText("password"), closeSoftKeyboard());
        //Click login to login
        onView(withId(R.id.login_button))
                .perform(click());
        SystemClock.sleep(2000);
        //Check to make sure we're on the intended activity, then click the add habit button
        intended(hasComponent(BaseActivity.class.getName()));
        //Build the stub for AddHabitActivity to return
        Intent resultData = new Intent();
        String name = "Breakfast";
        String reason = "Most important meal of the day!";
        Calendar calendar = Calendar.getInstance();
        Habit testHabit = new Habit(name, reason, calendar);
        resultData.putExtra("newHabit", testHabit);
        ActivityResult result = new ActivityResult(BaseActivity.RESULT_NEW_HABIT, resultData);
        //Call the AddHabitActivity to return the stub
        intending(toPackage("com.cmput301f21t34.habittrak.AddHabitActivity"))
                .respondWith(result);
        //Check to see if the new habit was added
        onView(withId(R.id.navbar_menu_habits))
                .perform(click());
        //Logout to standardize the steps for the tests
        onView(withId(R.id.navbar_menu_profile))
                .perform(click());
        onView(withId(R.id.logout))
                .perform(click());
        //TODO: for part 4, make sure we look into the actual list to see if the habit was added
    }

    @Test
    public void profileFragTest(){
        //Enter login information
        onView(withId(R.id.username_edit_text))
                .perform(typeText("rajabi@ualberta.ca"), closeSoftKeyboard());
        onView(withId(R.id.password_edit_text))
                .perform(typeText("password"), closeSoftKeyboard());
        //Click login to login
        onView(withId(R.id.login_button))
                .perform(click());
        SystemClock.sleep(2000);
        //Check to make sure we're on the intended activity, then click to profile fragment
        intended(hasComponent(BaseActivity.class.getName()));
        onView(withId(R.id.navbar_menu_profile))
                .perform(click());
        //Attempt to edit the biography of the user and confirm
        onView(withId(R.id.editBio))
                .perform(typeText("Intent testing!"), closeSoftKeyboard());
        onView(withId(R.id.confirmer))
                .perform(click());
        //Log out, and check to see if we went back to the login page
        onView(withId(R.id.logout))
                .perform(click());
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void socialFragUITest(){
        //Enter login information
        onView(withId(R.id.username_edit_text))
                .perform(typeText("rajabi@ualberta.ca"), closeSoftKeyboard());
        onView(withId(R.id.password_edit_text))
                .perform(typeText("password"), closeSoftKeyboard());
        //Click login to login
        onView(withId(R.id.login_button))
                .perform(click());
        SystemClock.sleep(2000);
        //Check to make sure we're on the intended activity, then click on the social button
        intended(hasComponent(BaseActivity.class.getName()));
        onView(withId(R.id.navbar_menu_social))
                .perform(click());
        //Click around to make sure we can navigate through
        onView(withContentDescription("Following"))
                .perform(click());
        onView(withContentDescription("Followers"))
                .perform(click());
        onView(withContentDescription("Requests"))
                .perform(click());
        onView(withContentDescription("Search"))
                .perform(click());
        //Logout to standardize the steps for the tests
        onView(withId(R.id.navbar_menu_profile))
                .perform(click());
        onView(withId(R.id.logout))
                .perform(click());
        //TODO: maybe put in searching? Can the emulator connect to the internet to search?
    }
}
