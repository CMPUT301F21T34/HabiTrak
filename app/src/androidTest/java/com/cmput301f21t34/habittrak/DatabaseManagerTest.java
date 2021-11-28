package com.cmput301f21t34.habittrak;

import static org.junit.Assert.*;

import android.util.Log;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitEvent;
import com.cmput301f21t34.habittrak.user.HabitList;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for testing Habit Objects
 *
 * @author Henry
 * @version 1.0
 * @since 2021-11-03
 * @see DatabaseManager
 */
public class DatabaseManagerTest {

    private static DatabaseManager dm;
    private static FirebaseFirestore db;

    @Before
    public void setUp() {
        dm = new DatabaseManager();
        db = dm.getDatabase();
    }

    /**
     * createNewUser
     *
     * Tests if createNewUser correctly adds a new user with the given attributes to the database
     */
    @Test
    public void createNewUserTest() {
        String email = "test@gmail.com";
        String username = "testUser";
        dm.createNewUser(email, username);
        try {
            DocumentReference docref = db.collection("users").document("test@gmail.com");
            Task<DocumentSnapshot> task = docref.get();
            while (!task.isComplete()) ;
            DocumentSnapshot document = task.getResult();
            if (document.getData() != null) {
                assertEquals(username, document.get("Username"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        dm.deleteUser("test@gmail.com");
    }

    /**
     * updateHabitTest
     *
     * Tests of updateHabitList properly propagates the changes in habit list to the database
     */
    @Test
    public void updateHabitListTest() {
        String email = "test@gmail.com";
        String username = "testUser";
        dm.createNewUser(email, username);
        Habit habit1 = new Habit("walk dog");
        Habit habit2 = new Habit("read book");

        HabitList habitList = new HabitList();
        habitList.add(habit1);
        habitList.add(habit2);
        dm.updateHabitList(email, habitList);

        User user = dm.getUser(email);
        Habit habitFromDB = user.getHabitList().get(0);

        assertEquals(2, user.getHabitList().size());
        assertEquals(habit1.getTitle(), habitFromDB.getTitle());

        // Add new habit at the front and test again
        Habit habit3 = new Habit("test habit");
        habitList.add(0, habit3);
        dm.updateHabitList(email, habitList);

        user = dm.getUser(email);
        habitFromDB = user.getHabitList().get(0);
        assertEquals(3, user.getHabitList().size());
        assertEquals(habit3.getTitle(), habitFromDB.getTitle());

        // Add event to habit3 and perform test
        HabitEvent event = new HabitEvent();
        event.setComment("test event");
        ArrayList<HabitEvent> eventList = new ArrayList<>();
        eventList.add(event);
        habit3.setHabitEvents(eventList);
        habitList.replace(habit3);
        dm.updateHabitList(email, habitList);

        user = dm.getUser(email);
        HabitEvent eventFromDB = user.getHabitList().get(0).getHabitEvents().get(0);
        assertEquals(habit3.getHabitEvents().get(0).getComment(), eventFromDB.getComment());

        // Delete habit3 and perform test
        habitList.remove(habit3);
        dm.updateHabitList(email, habitList);

        user = dm.getUser(email);
        habitFromDB = user.getHabitList().get(0);
        assertEquals(2, user.getHabitList().size());
        assertEquals(habit1.getTitle(), habitFromDB.getTitle());

        // Delete test user after test is done
        dm.deleteUser(email);
    }
}

