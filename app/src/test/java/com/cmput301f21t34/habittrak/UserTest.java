package com.cmput301f21t34.habittrak;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitEvent;
import com.cmput301f21t34.habittrak.user.User;

import org.junit.jupiter.api.Test;

/**
 * Class for testing User Objects
 *
 * @author Henry
 * @version 1.0
 * @see User
 * @see Habit
 * @see HabitEvent
 * @since 2021-10-22
 */
public class UserTest {
    // No tests for getters or setters
    // YET! TODO: tests for getters and setters.
    // TODO finish javadoc if needed in testing files

    /**
     * mockUser
     *
     * @return Habit
     * Returns a new Habit object to perform tests on
     * @author Henry
     */
    private User mockUser() {
        return new User();
    }

    private String getMockFol1() {
        return "fol1";
    }

    private String getMockFol2() {
        return "fol2";
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

        // Adds mock follower to user
        String follower1 = getMockFol1();
        user.addFollower(follower1);
        String follower2 = getMockFol2();
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

        // Adds mock follower to user
        String follower1 = getMockFol1();
        user.addFollower(follower1);
        String follower2 = getMockFol2();
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
        assertEquals(0, user.getFollowingReqList().size());

        // Adds mock follower to user
        String follower1 = getMockFol1();
        user.addFollowerReq(follower1);

        assertEquals(1, user.getFollowingReqList().size());

        String follower2 = getMockFol2();
        user.addFollowerReq(follower2);

        assertEquals(2, user.getFollowingReqList().size());

        // Compare added followerreq (user object)

        assertEquals(follower1, user.getFollowingReqList().get(0));
        assertEquals(follower2, user.getFollowingReqList().get(1));
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
        String follower1 = getMockFol1();
        user.addFollowerReq(follower1);
        String follower2 = getMockFol2();
        user.addFollowerReq(follower2);

        // Remove followerreq1 then check size
        assertTrue(user.removeFollowerReq(follower1));
        assertEquals(1, user.getFollowingReqList().size());
        assertEquals(follower2, user.getFollowingReqList().get(0)); // followerreq at index 0 should now be followerreq2
        // Remove followerreq2, size should be 0
        assertTrue(user.removeFollowerReq(follower2));
        assertEquals(0, user.getFollowingReqList().size());
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

        String follower1 = getMockFol1();
        user.addFollowing(follower1);

        assertEquals(1, user.getFollowingList().size());

        String follower2 = getMockFol2();
        user.addFollowing(follower2);

        assertEquals(2, user.getFollowingList().size());

        // Compare added following (user object)

        assertEquals(follower1, user.getFollowingList().get(0));
        assertEquals(follower2, user.getFollowingList().get(1));
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
        String follower1 = getMockFol1();
        user.addFollowing(follower1);
        String follower2 = getMockFol2();
        user.addFollowing(follower2);

        // Remove following1 then check size
        assertTrue(user.removeFollowing(follower1));
        assertEquals(1, user.getFollowingList().size());
        assertEquals(follower2, user.getFollowingList().get(0)); // following at index 0 should now be following2
        // Remove following2, size should be 0
        assertTrue(user.removeFollowing(follower2));
        assertEquals(0, user.getFollowingList().size());
    }

    /**
     * replaceHabitTest() for potential replaceHabitTest() method
     */
    @Test
    public void replaceHabitTest() {

    }
}
