package com.cmput301f21t34.habittrak.user;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

/**
 * User
 *
 * @author Dakota
 * <p>
 * User to kepp track of
 * @version 1.3
 * @see HabitEvent
 * @see Habit
 * @since 2021-10-16
 */
public class User implements Parcelable {
    // Any changes need to be implement in writeToParcel and Parcel constructor - Dakota

    // Attributes ----------------------------------------------------------------------------------
    // Creates User from parcel
    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // Password does not need to be parse and may be eventually removed
    // TODO: Make password final in version after merge with Database
    private final String email;
    // TODO: Make username final in version after merge with Database
    private String username;
    private String password;
    private HabitList habitList; // Habit_List extends ArrayList<Habit>
    // Lists of users' UUIDS (emails)
    private final ArrayList<String> followerList;     // Users that follow this user
    private final ArrayList<String> followingList;    // Users this user follows
    private final ArrayList<String> followingReqList; // Users that this user requested to follow
    private final ArrayList<String> followerReqList;  // Users that requested to follow this user
    private final ArrayList<String> blockList;        // Users that this user blocked
    private final ArrayList<String> blockedByList;    // Users that blocked this user
    private String biography;
    // Attributes ----------------------------------------------------------------------------------

    // Constructors --------------------------------------------------------------------------------
    public User(
            String username,
            String password,
            String email,
            String biography,
            HabitList habitList,
            ArrayList<String> followerList,
            ArrayList<String> followingList,
            ArrayList<String> followingReqList,
            ArrayList<String> followerReqList,
            ArrayList<String> blockList,
            ArrayList<String> blockedByList) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.biography = biography;
        this.habitList = habitList;
        this.followerList = followerList;
        this.followingList = followingList;
        this.followingReqList = followingReqList;
        this.followerReqList = followerReqList;
        this.blockList = blockList;
        this.blockedByList = blockedByList;
    }

    /**
     * basic constructor with bare minimum info
     *
     * @param username String the Users username
     * @param password String the Users password (for auth)
     * @param email    String the Users email (for identification)
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.biography = "";
        this.habitList = new HabitList();
        this.followerList = new ArrayList<>();
        this.followingList = new ArrayList<>();
        this.followingReqList = new ArrayList<>();
        this.followerReqList = new ArrayList<>();
        this.blockList = new ArrayList<>();
        this.blockedByList = new ArrayList<>();
    }

    /**
     * Parcel Constructor Class
     * <p>
     * Constructs Habit from a parcel
     * Un-does writeToParcel method
     *
     * @param parcel Parcel to construct from
     * @author Dakota
     * @see Parcelable
     */
    public User(Parcel parcel) {
        Bundle userBundle;
        userBundle = parcel.readBundle(User.class.getClassLoader());

        // Strings
        this.username = userBundle.getString("username");
        this.password = userBundle.getString("password");
        this.email = userBundle.getString("email");
        this.biography = userBundle.getString("biography");

        // HabitList from ArrayList<Habit>
        this.habitList = new HabitList(userBundle.getParcelableArrayList("habitList"));

        // Lists of UUIDs
        this.followerList = userBundle.getStringArrayList("followerList");
        this.followingList = userBundle.getStringArrayList("followingList");
        this.followingReqList = userBundle.getStringArrayList("followingReqList");
        this.followerReqList = userBundle.getStringArrayList("followerReqList");
        this.blockList = userBundle.getStringArrayList("blockList");
        this.blockedByList = userBundle.getStringArrayList("blockedByList");
    }

    /**
     * New User
     * <p>
     * Creates a User with only a username
     *
     * @author Dakota
     */
    public User() {
        this.email = "dummyEmail";
        this.username = "dummyUser";
        this.password = "12345";
        this.habitList = new HabitList();
        this.followerList = new ArrayList<>();
        this.followingList = new ArrayList<>();
        this.followingReqList = new ArrayList<>();
        this.followerReqList = new ArrayList<>();
        this.blockList = new ArrayList<>();
        this.blockedByList = new ArrayList<>();
        this.biography = "";
    }

    /**
     * User
     * <p>
     * Creates a User with the given email
     * User only has a username, an email and a password
     */
    public User(String email) {
        this.email = email;
        this.username = "dummyUser";
        this.password = "12345";
        this.habitList = new HabitList();
        this.followerList = new ArrayList<>();
        this.followingList = new ArrayList<>();
        this.followingReqList = new ArrayList<>();
        this.followerReqList = new ArrayList<>();
        this.blockList = new ArrayList<>();
        this.blockedByList = new ArrayList<>();
        this.biography = "";
    }
    // Constructors --------------------------------------------------------------------------------

    // Getters and Setters -------------------------------------------------------------------------
    // User details
    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getPassword() {
        return password;
    }

    /**
     * @param password String The Users password
     * @author Dakota
     * @author Henry
     * @deprecated assign password in constructor
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * getEmail
     * <p>
     * gets Email
     *
     * @return String email
     * @author Dakota
     */
    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Social Lists
    public ArrayList<String> getBlockList() {
        return (ArrayList<String>) blockList.clone();
    }

    public ArrayList<String> getBlockedByList() {
        return (ArrayList<String>) blockedByList.clone();
    }

    public ArrayList<String> getFollowerList() {
        return (ArrayList<String>) followerList.clone();
    }

    public ArrayList<String> getFollowingList() {
        return (ArrayList<String>) followingList.clone();
    }

    public ArrayList<String> getFollowerReqList() {
        return (ArrayList<String>) followerReqList.clone();
    }

    public ArrayList<String> getFollowingReqList() {
        return (ArrayList<String>) followingReqList.clone();
    }

    // Habits
    public Habit getHabit(int index) {
        return this.habitList.get(index);
    }

    public HabitList getHabitList() {
        return habitList;
    }

    public void setHabitList(HabitList habitList) {
        this.habitList = habitList;
    }
    // Getters and Setters -------------------------------------------------------------------------

    // Adders and Removers -------------------------------------------------------------------------
    // Social
    /**
     * addBlock
     * adds a blockee to this user's blockList if not already present
     *
     * @param UUID String, the UUID of the user to add
     * @author Kaaden
     */
    public void addBlock(String UUID) {
        if (!blockList.contains(UUID)) {
            blockList.add(UUID);
        }
    }

    /**
     * removeBlock
     * Remove all occurrences of a blockee in this user's blockList
     *
     * @param UUID String, the UUID of the user(s) to remove
     * @return boolean, true if any removals occurred, false otherwise
     */
    public boolean removeBlock(String UUID) {
        return blockList.removeIf(blockee -> blockee.equals(UUID));
    }

    /**
     * addFollower
     * adds a follower to this user's followerList if not already present
     *
     * @param UUID String, the UUID of the user to add
     * @author Kaaden
     */
    public void addFollower(String UUID) {
        if (!followerList.contains(UUID)) {
            followerList.add(UUID);
        }
    }

    /**
     * removeFollower
     * Remove all occurrences of a follower in this user's followerList
     *
     * @param UUID String, the UUID of the user(s) to remove
     * @return boolean, true if any removals occurred, false otherwise
     */
    public boolean removeFollower(String UUID) {
        return followerList.removeIf(follower -> follower.equals(UUID));
    }

    /**
     * addFollowing
     * adds a followee to this user's followingList if not already present
     *
     * @param UUID String, UUID of the user to add
     * @author Kaaden
     */
    public void addFollowing(String UUID) {
        if (!followingList.contains(UUID)) {
            followingList.add(UUID);
        }
    }

    /**
     * removeFollowing
     * Remove all occurrences of a followee in this user's followingList
     *
     * @param UUID String, the UUID of the user(s) to remove
     * @return boolean, true if any removals occurred, false otherwise
     */
    public boolean removeFollowing(String UUID) {
        return followingList.removeIf(followee -> followee.equals(UUID));
    }

    /**
     * addFollowerReq
     * adds a follow-requester to this user's followerReqList if not already present
     *
     * @param UUID String, the UUID of the user to add
     * @author Kaaden
     */
    public void addFollowerReq(String UUID) {
        if (!followerReqList.contains(UUID)) {
            followerReqList.add(UUID);
        }
    }

    /**
     * removeFollowerReq
     * Remove all occurrences of a follow-requester in this user's followerReqList
     *
     * @param UUID String, the UUID of the user(s) to remove
     * @return boolean, true if any removals occurred, false otherwise
     */
    public boolean removeFollowerReq(String UUID) {
        return followerReqList.removeIf(followRequester -> followRequester.equals(UUID));
    }

    /**
     * addFollowingReq
     * adds a follow-requestee to this user's followingReqList if not already present
     *
     * @param UUID String, the UUID of the user to add
     * @author Kaaden
     */
    public void addFollowingReq(String UUID) {
        if (!followingReqList.contains(UUID)) {
            followingReqList.add(UUID);
        }
    }

    /**
     * removeFollowingReq
     * Remove all occurrences of a follow-requestee in this user's followingReqList
     *
     * @param UUID String, the UUID of the user(s) to remove
     * @return boolean, true if any removals occurred, false otherwise
     */
    public boolean removeFollowingReq(String UUID) {
        return followingReqList.removeIf(followRequestee -> followRequestee.equals(UUID));
    }

    // Habits
    public void addHabit(Habit habit) {
        this.habitList.add(habit);
    }

    /**
     * removeHabit
     * <p>
     * removes a given habit from the habitList
     *
     * @param habit Habit to remove
     * @return boolean true if succeeded, false else wise
     */
    public boolean removeHabit(Habit habit) {
        return this.habitList.remove(habit);
    }

    // Parcelable Implementation -------------------------------------------------------------------
    /**
     * writeToParcel
     * <p>
     * Writes all the attributes to the parcel out
     *
     * @param parcel Parcel to be create
     * @param flags  int, idk not important but required
     * @author Dakota
     * @see Parcelable
     * @see Parcel
     * @see Bundle
     * @see ClassLoader
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        Bundle userBundle = new Bundle(this.getClass().getClassLoader());

        userBundle.putString("username", username);

        Log.d("UserParcelable",
                "Parcel Writer userName:" + userBundle.getString("username"));

        userBundle.putString("password", password);

        userBundle.putString("biography", biography);

        userBundle.putString("email", this.getEmail());

        userBundle.putString("password", password);

        userBundle.putString("biography", biography);

        userBundle.putString("email", this.getEmail());

        // requires Habit to implement Parcelable
        userBundle.putParcelableArrayList("habitList", habitList);

        userBundle.putStringArrayList("followerList", followerList);
        userBundle.putStringArrayList("followingList", followingList);
        userBundle.putStringArrayList("followingReqList", followingReqList);
        userBundle.putStringArrayList("followerReqList", followerReqList);
        userBundle.putStringArrayList("blockList", blockList);
        userBundle.putStringArrayList("blockedByList", blockedByList);

        parcel.writeBundle(userBundle); // writes bundle to parcel
    }

    /**
     * Apart of Parcelable implementation, does nothing but is required
     *
     * @return int 0
     * @author Dakota
     */
    @Override
    public int describeContents() {
        return 0;
    }
}
