package com.cmput301f21t34.habittrak;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitEvent;
import com.cmput301f21t34.habittrak.user.HabitList;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Class for testing Habit Objects
 *
 * @author Henry
 * @author Tauseef
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
     * <p>
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

        // Delete test user after test is done
        dm.deleteUser(email);
    }

    /**
     * getAllUserTest
     *
     * Tests if getAllUsers correctly get all emails from the database
     */
    @Test
    public void getAllUsersTest() {
        String email1 = "test1@gmail.com";
        String username1 = "testUser1";
        dm.createNewUser(email1, username1);
        String email2 = "test2@gmail.com";
        String username2 = "testUser2";
        dm.createNewUser(email2, username2);

        ArrayList<String> userNames = dm.getAllUsers();
        assertEquals(email1, userNames.get(userNames.size() - 2));
        assertEquals(email2, userNames.get(userNames.size() - 1));

        // Delete test user after test is done
        dm.deleteUser(email1);
        dm.deleteUser(email2);
    }

    /**
     * getUserNameTest
     *
     * Tests if getUserName correctly get the username of the user from the database
     */
    @Test
    public void getUserNameTest(){
        String email = "test@gmail.com";
        String username = "testUser";
        User user = new User(email);
        user.setUsername(username);
        dm.createNewUser(email, username);
        String name = dm.getUserName(user.getEmail());
        assertEquals(user.getUsername(), name);

        // Delete test user after test is done
        dm.deleteUser(email);
    }

    /**
     * getUserTest
     *
     * Tests if getUser correctly get the user from the database
     */
    @Test
    public void getUserTest() {
        String email = "test@gmail.com";
        String username = "testUser";
        String bio = "testing";
        dm.createNewUser(email, username);
        dm.updateBio(email, bio);

        // Populate habit list
        Habit habit1 = new Habit("walk dog");
        Habit habit2 = new Habit("read book");

        HabitList habitList = new HabitList();
        habitList.add(habit1);
        habitList.add(habit2);
        dm.updateHabitList(email, habitList);

        HabitEvent event = new HabitEvent();
        event.setComment("test event");
        ArrayList<HabitEvent> eventList = new ArrayList<>();
        eventList.add(event);
        habit1.setHabitEvents(eventList);
        habitList.replace(habit1);
        dm.updateHabitList(email, habitList);

        // Populate social lists
        String email2 = "test2@gmail.com";
        String username2 = "testUser2";
        dm.createNewUser(email2, username2);

        // User 1 is blocking user 2
        dm.updateBlock(email2, email, false);
        // User 1 is sending a follow request to user 2
        dm.updateFollowRequest(email2, email, false);
        // User 1 is following user 2
        dm.updateFollow(email2, email, false);

        // Get user from the db then test
        // Wait for a few seconds to ensure data is updated to firestore
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

        User user = dm.getUser(email);
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(bio, user.getBiography());
        assertEquals(0, user.getFollowerList().size());
        assertEquals(1, user.getFollowingList().size());
        assertEquals(0, user.getFollowerReqList().size());
        assertEquals(1, user.getFollowingReqList().size());
        assertEquals(0, user.getBlockedByList().size());
        assertEquals(1, user.getBlockList().size());

        HabitList habitListFromDB = user.getHabitList();
        assertEquals(2, habitListFromDB.size());
        assertEquals(habit1.getTitle(), habitListFromDB.get(0).getTitle());
        assertEquals(habit2.getTitle(), habitListFromDB.get(1).getTitle());

        assertEquals(event.getComment(), habitListFromDB.get(0).getHabitEvents().get(0).getComment());

        // Delete test user after test is done
        dm.deleteUser(email);
        dm.deleteUser(email2);
    }

    /**
     * getUserBioTest
     *
     * Tests if getUserBio correctly get the biography of the user from the database
     */
    @Test
    public void getUserBioTest() {
        String email = "test@gmail.com";
        String username = "testUser";
        String biography = "testing";
        User user = new User(email);
        user.setUsername(username);
        user.setBiography(biography);
        dm.createNewUser(email, username);  // add user to the db
        dm.updateBio(user.getEmail(),user.getBiography());

        String bio = dm.getUserBio(user.getEmail());  // get bio from the db
        assertEquals(user.getBiography(),bio);

        // Delete test user after test is done
        dm.deleteUser("test@gmail.com");
    }

    /**
     * isUniqueEmailTest
     *
     * Tests if isUniqueEmail checks uniqueness of email in the database
     */
    @Test
    public void isUniqueEmailTest() {
        // Testing an email that's not in the db
        assertEquals(true, dm.isUniqueEmail("testing@email.com"));

        // Add email to db then test
        String email = "test@gmail.com";
        String username = "testUser";
        User user = new User(email);
        dm.createNewUser(email, username);
        assertEquals(false, dm.isUniqueEmail(user.getEmail()));

        // Delete test user after test is done
        dm.deleteUser("test@gmail.com");
    }

    /**
     * updateHabitTest
     * <p>
     * Tests if updateHabitList properly propagates the changes in habit list to the database
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

        // Wait for a few seconds to ensure data is updated to firestore
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

        User user = dm.getUser(email);
        Habit habitFromDB = user.getHabitList().get(0);

        assertEquals(2, user.getHabitList().size());
        assertEquals(habit1.getTitle(), habitFromDB.getTitle());

        // Add new habit at the front and test again
        Habit habit3 = new Habit("test habit");
        habitList.add(0, habit3);
        dm.updateHabitList(email, habitList);

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

        user = dm.getUser(email);
        habitFromDB = user.getHabitList().get(0);
        assertEquals(3, user.getHabitList().size());
        assertEquals(habit3.getTitle(), habitFromDB.getTitle());

        // Add event to habit3 and perform test
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

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

    /**
     * updateBlockTest
     *
     * Tests if updateBlock properly propagates the changes in block relation to the database
     */
    @Test
    public void updateBlockTest() {
        String email1 = "test1@gmail.com";
        String username1 = "testUser1";
        dm.createNewUser(email1, username1);

        String email2 = "test2@gmail.com";
        String username2 = "testUser2";
        dm.createNewUser(email2, username2);

        // User 1 is blocking user 2
        dm.updateBlock(email2, email1, false);

        // Wait for a few seconds to ensure data is updated to firestore
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

        User user1 = dm.getUser(email1);
        User user2 = dm.getUser(email2);

        assertEquals(user1.getBlockList().get(0), user2.getEmail());
        assertEquals(user2.getBlockedByList().get(0), user1.getEmail());

        // Delete test user after test is done
        dm.deleteUser(email1);
        dm.deleteUser(email2);
    }

    /**
     * updateFollowTest
     *
     * Tests if updateFollow properly propagates the changes in follow relation to the database
     */
    @Test
    public void updateFollowTest() {
        String email1 = "test1@gmail.com";
        String username1 = "testUser1";
        dm.createNewUser(email1, username1);

        String email2 = "test2@gmail.com";
        String username2 = "testUser2";
        dm.createNewUser(email2, username2);

        // User 1 is following user 2
        dm.updateFollow(email2, email1, false);

        // Wait for a few seconds to ensure data is updated to firestore
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

        User user1 = dm.getUser(email1);
        User user2 = dm.getUser(email2);

        assertEquals(user1.getFollowingList().get(0), user2.getEmail());
        assertEquals(user2.getFollowerList().get(0), user1.getEmail());

        // Delete test user after test is done
        dm.deleteUser(email1);
        dm.deleteUser(email2);
    }

    /**
     * updateFollowRequestTest
     *
     * Tests if updateFollowTest properly propagates the changes in request relation to the database
     */
    @Test
    public void updateFollowRequestTest() {
        String email1 = "test1@gmail.com";
        String username1 = "testUser1";
        dm.createNewUser(email1, username1);

        String email2 = "test2@gmail.com";
        String username2 = "testUser2";
        dm.createNewUser(email2, username2);

        // User 1 is sending follow request to user 2
        dm.updateFollowRequest(email2, email1, false);

        // Wait for a few seconds to ensure data is updated to firestore
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

        User user1 = dm.getUser(email1);
        User user2 = dm.getUser(email2);

        assertEquals(user1.getFollowingReqList().get(0), user2.getEmail());
        assertEquals(user2.getFollowerReqList().get(0), user1.getEmail());

        // Delete test user after test is done
        dm.deleteUser(email1);
        dm.deleteUser(email2);
    }

    /**
     * updateBioTest
     *
     * Tests if updateBio properly propagates the changes in biography to the database
     */
    @Test
    public void updateBioTest() {
        String email = "test@gmail.com";
        String username = "testUser";
        dm.createNewUser(email, username);

        String bio = "bing chilling";
        dm.updateBio(email, bio);

        // Wait for a few seconds to ensure data is updated to firestore
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

        User user = dm.getUser(email);
        assertEquals(bio, user.getBiography());

        // Delete test user after test is done
        dm.deleteUser(email);
    }

    /**
     * updateUsernameTest
     *
     * Tests if updateUsername properly propagates the changes in username to the database
     */
    @Test
    public void updateUsernameTest() {
        String email = "test@gmail.com";
        String username = "testUser";
        dm.createNewUser(email, username);

        String name = "bing chilling";
        dm.updateUsername(email, name);

        // Wait for a few seconds to ensure data is updated to firestore
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

        User user = dm.getUser(email);
        assertEquals(name, user.getUsername());

        // Delete test user after test is done
        dm.deleteUser(email);
    }
}