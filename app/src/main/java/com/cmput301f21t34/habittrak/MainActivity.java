package com.cmput301f21t34.habittrak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;


import com.cmput301f21t34.habittrak.user.Database_Pointer;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.Habit_Event;
import com.cmput301f21t34.habittrak.user.Habit_List;
import com.cmput301f21t34.habittrak.user.On_Days;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import com.cmput301f21t34.habittrak.user.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * MainActivity
 *
 * Starting point of the app
 * TODO: figure out how to save the login state of the user
 * TODO : Testing that the database returns the correct on days object
 *
 */
public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        User mainUser = null;
        if (savedInstanceState != null) {
            // get users credentials
            // validate credentials with database
            // populate mainUser
            // move to main menu
        } else {
            // get user to login
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.login_fragment_container, new LoginFragment(mainUser))
                    .commit();
        }

        Habit_Event event1 = new Habit_Event();
        event1.setComment("yep");
        Habit_Event event2 = new Habit_Event();
        Habit habit1 = new Habit();
        habit1.addHabitEvent(event1);
        habit1.addHabitEvent(event2);

        Habit_Event event3 = new Habit_Event();
        event1.setComment("go");
        Habit_Event event4 = new Habit_Event();
        event1.setComment("g0");
        Habit habit2 = new Habit();
        habit2.addHabitEvent(event3);
        habit2.addHabitEvent(event4);

        Habit_List habits = new Habit_List();
        habits.add(habit1);
        habits.add(habit2);
        User user = new User("pogo@gmail.com");
        user.setUsername("pogo");
        user.setHabitList(habits);
        DatabaseManager db = new DatabaseManager();
        db.createNewUser(user.getEmail(), user.getUsername(), user.getPassword(), "", user.getHabitList());
         */

        DatabaseManager db = new DatabaseManager();
        User user = db.getUser("pogo@gmail.com");
        Habit_List hl =  user.getHabitList();
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = hl.get(0).getHabitEvents().get(1).getCompletedDate();
        fmt.setCalendar(cal);

        Log.d("contains",Integer.toString(hl.size()));
        Log.d("contains",fmt.format(cal.getTime()));
//        Log.d("contains",( hl.get(0).getHabitEvents().get(0).getCompletedDate().toString()));
    }
}
