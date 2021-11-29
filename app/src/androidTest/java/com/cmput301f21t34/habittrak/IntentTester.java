package com.cmput301f21t34.habittrak;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityResult;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cmput301f21t34.habittrak.HabitActivity.AddHabitActivity;
import com.cmput301f21t34.habittrak.HabitActivity.ViewEditHabitActivity;
import com.cmput301f21t34.habittrak.event.AddHabitEventActivity;
import com.cmput301f21t34.habittrak.event.MapsActivity;
import com.cmput301f21t34.habittrak.event.ViewEditHabitEventActivity;
import com.cmput301f21t34.habittrak.event.ViewHabitEvents;
import com.cmput301f21t34.habittrak.user.Habit;

import java.util.Calendar;

/**
 * A custom view action that handles the clicks inside an item of the recycler view
 * Class was implemented from - "https://stackoverflow.com/questions/28476507/using-espresso-to-click-view-inside-recyclerview-item"
 *
 */
class MyViewAction {

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

}
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

    /**
     * Tests the signUp activity
     * A new user is created using the signup page and then it is deleted at the end of the method
     */
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

    /**
     * tests the addHabitActivity.
     * At result a habit is added on the user's habit list. Also the habit frequency is tuesday,thursday
     */
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

    /**
     * checks to see whether the habit has been added
     */
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
        intending(toPackage("com.cmput301f21t34.habittrak.HabitActivity.AddHabitActivity"))
                .respondWith(result);
        //Check to see if the new habit was added
        onView(withId(R.id.navbar_menu_habits))
                .perform(click());
        //Logout to standardize the steps for the tests
        onView(withId(R.id.navbar_menu_profile))
                .perform(click());
        onView(withId(R.id.logout))
                .perform(click());
    }

    /**
     * tests the profile Frag UI
     * At result the bio of the user will be edited
     */
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

    /**
     * tests the social Frags UI
     * clicks on the tabs of the social section and see the whole lists in those tabs
     */
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
        onView(withContentDescription("Following"))
                .perform(click());
        //Logout to standardize the steps for the tests
        onView(withId(R.id.navbar_menu_profile))
                .perform(click());
        onView(withId(R.id.logout))
                .perform(click());
    }

    /**
     *  tests the view habit test
     *  clicks on the habit tab in navigation bar and sees the whole habit list
     */
    @Test
    public void viewHabitTest(){
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
        //Go to all habits menu
        onView(withId(R.id.navbar_menu_habits))
                .perform(click());
        //Click on a habit
        SystemClock.sleep(2000);
    }

    /**
     * tests add habit event activity.
     * At result the user creates the habit event of the habit in the 0th index
     */
    @Test
    public void addHabitEventTest(){
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

        // click the item on the recycler view
        onView(withId(R.id.today_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.today_listview_checkbox)));
        // check to make sure we are on the intended activity, then fill the new habit event form
        intended(hasComponent(AddHabitEventActivity.class.getName()));
        // fill the habit event form
        onView(withId(R.id.Comment))
                .perform(typeText("testing Habit Event"), closeSoftKeyboard());
        onView((withId(R.id.mapButton))).perform(click());
        // check to make sure that we're on the map activity
        intended(hasComponent(MapsActivity.class.getName()));
        // wait for the location to load
        SystemClock.sleep(10000);
        // ending the add map  activity
        onView(withId(R.id.confirm_button)).perform(click());

        // check to make sure that we're on the add habit event activity
        intended(hasComponent(AddHabitEventActivity.class.getName()));
        // take care of camera button

        // take care of gallery button
        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_PICK),hasData(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        Resources resources = InstrumentationRegistry.getInstrumentation().getContext().getResources();
        Uri imageUri = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        resources.getResourcePackageName(R.drawable.ic_launcher_background) + '/' +
                        resources.getResourceTypeName(R.drawable.ic_launcher_background) + '/' +
                        resources.getResourceEntryName(R.drawable.ic_launcher_background)
        );
        Intent resultIntent = new Intent();

        resultIntent.setData(imageUri);

        ActivityResult activityResult = new ActivityResult(Activity.RESULT_OK,resultIntent);
        intending(expectedIntent).respondWith(activityResult);
        onView(withId(R.id.GalleryButton)).perform(click());
        intended(expectedIntent);

        // exit the add habit activity
        onView(withId(R.id.addHabitEventButton)).perform(click());
        intended(hasComponent(BaseActivity.class.getName()));
        // click camera and gallery
        //Logout to standardize the steps for the tests
        onView(withId(R.id.navbar_menu_profile))
                .perform(click());
        onView(withId(R.id.logout))
                .perform(click());

    }

    /**
     * tests the viewEditHabitEvent activity.
     * At result it edits the 1st event of the habit at position index 0 and then the user logs out
     */
    @Test
    public void ViewEditHabitEventTest(){
        //Enter login information
        onView(withId(R.id.username_edit_text))
                .perform(typeText("rajabi@ualberta.ca"), closeSoftKeyboard());
        onView(withId(R.id.password_edit_text))
                .perform(typeText("password"), closeSoftKeyboard());
        //Click login to login
        onView(withId(R.id.login_button))
                .perform(click());
        SystemClock.sleep(2000);
        //Check to make sure we're on the intended activity,
        intended(hasComponent(BaseActivity.class.getName()));
        // go to the events fragment
        onView(withId(R.id.navbar_menu_events)).perform(click());
        //TODO:FIX the event recycler view id change it with the appropriate one
        onView(withId(R.id.events_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // check to see we're in the intended activity
        intended(hasComponent(ViewHabitEvents.class.getName()));
        onView(withId(R.id.events_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // check to see we're in the intended activity
        intended(hasComponent(ViewEditHabitEventActivity.class.getName()));
        SystemClock.sleep(2000);
        onView(withId(R.id.comment_edit_text))
                .perform(typeText("testing Edit Habit Event"), closeSoftKeyboard());
        // take care of camera and gallery

        // take care of gallery button
        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_PICK),hasData(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        Resources resources = InstrumentationRegistry.getInstrumentation().getContext().getResources();
        Uri imageUri = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        resources.getResourcePackageName(R.drawable.ic_launcher_background) + '/' +
                        resources.getResourceTypeName(R.drawable.ic_launcher_background) + '/' +
                        resources.getResourceEntryName(R.drawable.ic_launcher_background)
        );
        Intent resultIntent = new Intent();

        resultIntent.setData(imageUri);

        ActivityResult activityResult = new ActivityResult(Activity.RESULT_OK,resultIntent);
        intending(expectedIntent).respondWith(activityResult);
        onView(withId(R.id.gallery_button_edit)).perform(click());
        intended(expectedIntent);


        // exit the view edit habit event activity
        onView(withId(R.id.save_habit_event_edit)).perform(click());
        intended((hasComponent(ViewHabitEvents.class.getName())));
        // get back to base activity
        Espresso.pressBack();
        intended(hasComponent(BaseActivity.class.getName()));
        //Logout to standardize the steps for the tests
        onView(withId(R.id.navbar_menu_profile))
                .perform(click());
        onView(withId(R.id.logout))
                .perform(click());
    }

    /**
     * tests the view edit habit Activity.
     * At result the habit at index 0 (1st position) is edited and the user logs out.
     */
    @Test
    public void ViewEditHabitActivityTest(){
        //Enter login information
        onView(withId(R.id.username_edit_text))
                .perform(typeText("rajabi@ualberta.ca"), closeSoftKeyboard());
        onView(withId(R.id.password_edit_text))
                .perform(typeText("password"), closeSoftKeyboard());
        //Click login to login
        onView(withId(R.id.login_button))
                .perform(click());
        SystemClock.sleep(2000);
        //Check to make sure we're on the intended activity,
        intended(hasComponent(BaseActivity.class.getName()));
        onView(withId(R.id.navbar_menu_habits)).perform(click());
        onView(withId(R.id.all_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // check to make sure we're at the view edit habit activity
        intended(hasComponent(ViewEditHabitActivity.class.getName()));
        // edit the habit activity
        onView(withId(R.id.view_habit_name_edit_text))
                .perform(typeText("testing edit habit activity"), closeSoftKeyboard());
        onView(withId(R.id.view_habit_reason_edit_text))
                .perform(typeText("testing edit habit activity reason"), closeSoftKeyboard());
        onView(withId(R.id.view_start_date_button))
                .perform(click());
        Espresso.pressBack();
        onView(withId(R.id.view_save_habit))
                .perform((click()));
        intended(hasComponent(BaseActivity.class.getName()));
        //Logout to standardize the steps for the tests
        onView(withId(R.id.navbar_menu_profile))
                .perform(click());
        onView(withId(R.id.logout))
                .perform(click());



    }

}
