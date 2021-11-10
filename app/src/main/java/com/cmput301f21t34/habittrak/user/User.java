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

    // Attributes //

    // Any changes need to be implement in writeToParcel and Parcel constructor
    // - Dakota

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
    private ArrayList<String> followerList;     // Users that follow this user
    private ArrayList<String> followingList;    // Users this user follows
    private ArrayList<String> followingReqList; // Users that this user requested to follow
    //TODO: make these attributes Parcelable
    private ArrayList<String> followerReqList;  // Users that requested to follow this user
    private ArrayList<String> blockList;        // Users that this user blocked
    private ArrayList<String> blockedByList;    // Users that blocked this user

    // Constructors //
    private String biography;

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
            ArrayList<String> blockedByList

    ) {

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


        // Strings //
        this.username = userBundle.getString("username");
        this.password = userBundle.getString("password");
        this.email = userBundle.getString("email");
        this.biography = userBundle.getString("biography");


        // HabitList from ArrayList<Habit>

        this.habitList = new HabitList(
                userBundle.getParcelableArrayList("habitList")
        );

        // DatabasePointer Lists //
        this.followerList = userBundle.getStringArrayList("followerList");
        this.followingList = userBundle.getStringArrayList("followingList");
        this.followingReqList = userBundle.getStringArrayList("followingReqList");
        this.followingReqList = userBundle.getStringArrayList("followerReqList");
        this.blockList = userBundle.getStringArrayList("blockList");
        this.blockedByList = userBundle.getStringArrayList("blockedByList");


    }

    // getter methods
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public HabitList getHabitList() {
        return habitList;
    }

    public void setHabitList(HabitList habitList) {

        this.habitList = habitList;

    }

    public ArrayList<String> getFollowerList() {
        return followerList;
    }

    // Might not want to allow these //
    public void setFollowerList(ArrayList<String> followerList) {
        this.followerList = followerList;
    }

    public ArrayList<String> getFollowingReqList() {
        return followingReqList;
    }


    // Database can modify these methods below //

    public void setFollowingReqList(ArrayList<String> followingReqList) {
        this.followingReqList = followingReqList;
    }

    public ArrayList<String> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(ArrayList<String> followingList) {
        this.followingList = followingList;
    }

    public ArrayList<String> getFollowerReqList() {
        return followerReqList;
    }

    public void setFollowerReqList(ArrayList<String> followerReqList) {
        this.followerReqList = followerReqList;
    }

    public ArrayList<String> getBlockList() {
        return blockList;
    }

    public void setBlockList(ArrayList<String> blockList) {
        this.blockList = blockList;
    }

    public ArrayList<String> getBlockedByList() {
        return blockedByList;
    }

    public void setBlockedByList(ArrayList<String> blockedByList) {
        this.blockedByList = blockedByList;
    }

    public String getBiography() {
        return biography;
    }
    // add methods have to adjust the database

    public void setBiography(String biography) {
        this.biography = biography;
    }

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

    public Habit getHabit(int index) {
        return this.habitList.get(index);
    }

    public void addFollower(String newFollower) {
        //TODO: Database Implementation
        this.followerList.add(newFollower);
    }

    public void addFollowerReq(String newFollowReq) {
        //TODO: Database Implementation
        this.followingReqList.add(newFollowReq);
    }

    public void addFollowing(String newFollowing) {
        //TODO: Database Implementation
        this.followingList.add(newFollowing);
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


    // These implement Parcelable for being passed through an intent

    // replace methods not sure if i need it or not
    public void replaceHabit(int index, Habit habit) {
        this.habitList.set(index, habit);
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

        Log.d("UserParcelable", "Parcel Writer userName:" + userBundle.getString("username"));

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

    public boolean removeFollower(String follower) {
        return this.followerList.removeIf(UUID -> UUID.equals(follower));
    }

    public boolean removeFollowerReq(String followReq) {
        return this.followingReqList.removeIf(UUID -> UUID.equals(followReq));
    }

    public boolean removeFollowing(String following) {
        return this.followingList.removeIf(UUID -> UUID.equals(following));
    }
}
