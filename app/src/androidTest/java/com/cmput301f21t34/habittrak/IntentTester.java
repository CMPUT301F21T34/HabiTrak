package com.cmput301f21t34.habittrak;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

import android.os.Parcel;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.Habit_Event;
import com.cmput301f21t34.habittrak.user.Habit_List;
import com.cmput301f21t34.habittrak.user.On_Days;
import com.cmput301f21t34.habittrak.user.User;


import java.util.ArrayList;
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
    /*
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

    }

    @Test
    public void addHabitTest(){
        //Click login to login with dummyUser
        //TODO: On complete implementation, will have to login for real
        onView(withId(R.id.login_button))
                .perform(click());
        //Check to make sure we're on the intended activity, then click the add habit button
        intended(hasComponent(BaseActivity.class.getName()));
        onView(withId(R.id.today_add_habit_button))
                .perform(click());
    }
     */
}
