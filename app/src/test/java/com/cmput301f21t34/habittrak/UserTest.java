package com.cmput301f21t34.habittrak;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.Habit_Event;
import com.cmput301f21t34.habittrak.user.User;

/**
 * Class for testing User Objects
 *
 * @author Henry
 * @version 1.0
 * @since 2021-10-22
 * @see User
 * @see Habit
 * @see Habit_Event
 */

public class UserTest {
    // No tests for getters or setters
    /**
     * mockUser
     *
     * @author Henry
     *
     * @return Habit
     * Returns a new Habit object to perform tests on
     */
    private User mockUser() {
        User mockUser = new User("Main_User");
        return mockUser;
    }

    /**
     * addHabitTest
     * Tests if habit is correctly added to the user
     *
     * @author Henry
     */
    @Test
    public void addHabitTest() {
        User user = mockUser();

        // Add new habit then check size
        assertEquals(0, user.getHabitList().size());
        Habit habit1 = new Habit("habit1");
        user.addHabit(habit1);
        assertEquals(1, user.getHabitList().size());
        Habit habit2 = new Habit("habit2");
        user.addHabit(habit2);
        assertEquals(2, user.getHabitList().size());

        // Compare added habit object
        assertEquals(habit1, user.getHabitList().get(0));
        assertEquals(habit2, user.getHabitList().get(1));
    }

    /**
     * removeHabitTest
     * Tests if habit is properly removed from user.habitList
     *
     * @author Henry
     */
    @Test
    public void removeHabitTest() {
        User user = mockUser();

        // Populate habit list of a new user
        Habit habit1 = new Habit("habit1");
        user.addHabit(habit1);
        Habit habit2 = new Habit("habit2");
        user.addHabit(habit2);

        // Remove habit1 then check size
        assertTrue(user.removeHabit(habit1));
        assertEquals(1, user.getHabitList().size());
        assertEquals(habit2, user.getHabitList().get(0)); // habit at index 0 should now be habit2
        // Remove habit2, size should be 0
        assertTrue(user.removeHabit(habit2));
        assertEquals(0, user.getHabitList().size());
    }

    /**
     * addFollowerTest
     * Tests if follower is correctly added to the user
     *
     * @author Henry
     */
    @Test
    public void addFollowerTest() {
        User user = mockUser();

        // Add new follower then check size
        assertEquals(0, user.getFollowerList().size());
        User follower1 = new User("follower1");
        user.addFollower(follower1);
        assertEquals(1, user.getFollowerList().size());
        User follower2 = new User("follower2");
        user.addFollower(follower2);
        assertEquals(2, user.getFollowerList().size());

        // Compare added follower (user object)
        assertEquals(follower1, user.getFollowerList().get(0));
        assertEquals(follower2, user.getFollowerList().get(1));
    }

    /**
     * removeFollowerTest
     * Tests if follower is correctly removed from user.followerList
     *
     * @author Henry
     */
    @Test
    public void removeFollowerTest() {
        User user = mockUser();

        // Populate follower list of a new user
        User follower1 = new User("follower1");
        user.addFollower(follower1);
        User follower2 = new User("follower2");
        user.addFollower(follower2);

        // Remove follower1 then check size
        assertTrue(user.removeFollower(follower1));
        assertEquals(1, user.getFollowerList().size());
        assertEquals(follower2, user.getFollowerList().get(0)); // follower at index 0 should now be follower2
        // Remove follower2, size should be 0
        assertTrue(user.removeFollower(follower2));
        assertEquals(0, user.getFollowerList().size());
    }

    /**
     * addFollowerReqTest
     * Tests if followerReq is correctly added to the user
     *
     * @author Henry
     */
    @Test
    public void addFollowerReqTest() {
        User user = mockUser();

        // Add new followerreq then check size
        assertEquals(0, user.getFollowerReqList().size());
        User followerreq1 = new User("followerreq1");
        user.addFollowerReq(followerreq1);
        assertEquals(1, user.getFollowerReqList().size());
        User followerreq2 = new User("followerreq2");
        user.addFollowerReq(followerreq2);
        assertEquals(2, user.getFollowerReqList().size());

        // Compare added followerreq (user object)
        assertEquals(followerreq1, user.getFollowerReqList().get(0));
        assertEquals(followerreq2, user.getFollowerReqList().get(1));
    }

    /**
     * removeFollowerReqTest
     * Tests if followerreq is correctly removed from user.followerReqList
     *
     * @author Henry
     */
    @Test
    public void removeFollowerReqTest() {
        User user = mockUser();

        // Populate followerreq list of a new user
        User followerreq1 = new User("followerreq1");
        user.addFollowerReq(followerreq1);
        User followerreq2 = new User("followerreq2");
        user.addFollowerReq(followerreq2);

        // Remove followerreq1 then check size
        assertTrue(user.removeFollowerReq(followerreq1));
        assertEquals(1, user.getFollowerReqList().size());
        assertEquals(followerreq2, user.getFollowerReqList().get(0)); // followerreq at index 0 should now be followerreq2
        // Remove followerreq2, size should be 0
        assertTrue(user.removeFollowerReq(followerreq2));
        assertEquals(0, user.getFollowerReqList().size());
    }
    /**
     * addFollowingTest
     * Tests if following is correctly added to the user
     *
     * @author Henry
     */
    @Test
    public void addFollowingTest() {
        User user = mockUser();

        // Add new following then check size
        assertEquals(0, user.getFollowingList().size());
        User following1 = new User("following1");
        user.addFollowing(following1);
        assertEquals(1, user.getFollowingList().size());
        User following2 = new User("following2");
        user.addFollowing(following2);
        assertEquals(2, user.getFollowingList().size());

        // Compare added following (user object)
        assertEquals(following1, user.getFollowingList().get(0));
        assertEquals(following2, user.getFollowingList().get(1));
    }

    /**
     * removeFollowingTest
     * Tests if following is correctly removed from user.followingList
     *
     * @author Henry
     */
    @Test
    public void removeFollowingTest() {
        User user = mockUser();

        // Populate following list of a new user
        User following1 = new User("following1");
        user.addFollowing(following1);
        User following2 = new User("following2");
        user.addFollowing(following2);

        // Remove following1 then check size
        assertTrue(user.removeFollowing(following1));
        assertEquals(1, user.getFollowingList().size());
        assertEquals(following2, user.getFollowingList().get(0)); // following at index 0 should now be following2
        // Remove following2, size should be 0
        assertTrue(user.removeFollowing(following2));
        assertEquals(0, user.getFollowingList().size());
    }
    /**
     * replaceHabitTest() for potential replaceHabitTest() method
     */
    @Test
    public void replaceHabitTest() {

    }
}
