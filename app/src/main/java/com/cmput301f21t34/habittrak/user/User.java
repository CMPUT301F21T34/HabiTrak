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
 *
 * User to kepp track of
 *
 * @version 1.3
 * @since 2021-10-16
 * @see Habit_Event
 * @see Habit
 */
public class User implements Parcelable {

    // Attributes //

    // Any changes need to be implement in writeToParcel and Parcel constructor - Dakota

    private String username;
    
    private String password;

    private Habit_List habitList; // Habit_List extends ArrayList<Habit>

    private final String email;

    private ArrayList<Database_Pointer> followerList;
    private ArrayList<Database_Pointer> followingList;
    private ArrayList<Database_Pointer> followerReqList;
    private ArrayList<Database_Pointer> followerRequestedList;
    private ArrayList<Database_Pointer> blockList;
    private ArrayList<Database_Pointer> blockedByList;

    private String biography;

    // Constructors //

    public User(String username, String email, Habit_List habitList, ArrayList<Habit_Event> habitEventList,
                ArrayList<Database_Pointer> followerList, ArrayList<Database_Pointer> followingList,
                ArrayList<Database_Pointer> followerReqList, ArrayList<Database_Pointer> followerRequestedList,
                ArrayList<Database_Pointer> blockList, ArrayList<Database_Pointer> blockedByList,
                String biography) {
        this.email = email;
        this.username = username;
        this.habitList = habitList;
        this.followerList = followerList;
        this.followingList = followingList;
        this.followerReqList = followerReqList;
        this.followerRequestedList = followerRequestedList;
        this.blockList = blockList;
        this.blockedByList = blockedByList;
        this.biography = biography;
    }

    /**
     * New User
     *
     * Creates a User with only a username
     *
     * @author Dakota
     */
    public User(){
        this.email = "dummyEmail";
        this.username = "dummyUser";
        this.password = "12345";
        
        this.habitList = new Habit_List();
        this.followerList = new ArrayList<Database_Pointer>();
        this.followingList = new ArrayList<Database_Pointer>();
        this.followerReqList = new ArrayList<Database_Pointer>();
        this.followerRequestedList = new ArrayList<Database_Pointer>();
        this.blockList = new ArrayList<Database_Pointer>();
        this.blockedByList = new ArrayList<Database_Pointer>();
        this.biography = "";
    }
    
    /**
     * User
     *
     * Creates a User with the given email
     * User only has a username, an email and a password
     */
    public User(String email){
        this.email = email;
        this.username = "dummyUser";
        this.password = "12345";

        this.habitList = new Habit_List();
        this.followerList = new ArrayList<Database_Pointer>();
        this.followingList = new ArrayList<Database_Pointer>();
        this.followerReqList = new ArrayList<Database_Pointer>();
        this.followerRequestedList = new ArrayList<Database_Pointer>();
        this.blockList = new ArrayList<Database_Pointer>();
        this.blockedByList = new ArrayList<Database_Pointer>();
        this.biography = "";
    }

    /**
     * Parcel Constructor Class
     *
     * Constructs Habit from a parcel
     * Un-does writeToParcel method
     *
     * @author Dakota
     * @see Parcelable
     * @param parcel Parcel to construct from
     */
    public User(Parcel parcel){

        Bundle userBundle;
        userBundle = parcel.readBundle(User.class.getClassLoader());
        Log.d("UserParcelable", "Parcel Construction userName:" + userBundle.getString("username"));


        this.username = userBundle.getString("username");
        this.password = userBundle.getString("password");
        this.email = userBundle.getString("email");
        this.habitList = new Habit_List(userBundle.getParcelableArrayList("habitList"));
        this.followerList = userBundle.getParcelableArrayList("followerList");
        this.followingList = userBundle.getParcelableArrayList("followingList");
        this.followerReqList = userBundle.getParcelableArrayList("followerReqList");
        this.followerReqList = userBundle.getParcelableArrayList("followerRequestedList");
        this.blockList = userBundle.getParcelableArrayList("blockList");
        this.blockedByList = userBundle.getParcelableArrayList("blockedByList");

        this.biography = userBundle.getString("biography");




    }


    // getter methods
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public Habit_List getHabitList() {
        return habitList;
    }
    
    public ArrayList<Database_Pointer> getFollowerList() {
        return followerList;
    }
    
    public ArrayList<Database_Pointer> getFollowerReqList() {
        return followerReqList;
    }
    
    public ArrayList<Database_Pointer> getFollowingList() {
        return followingList;
    }
    
    public ArrayList<Database_Pointer> getFollowerRequestedList() {
        return followerRequestedList;
    }
    
    public ArrayList<Database_Pointer> getBlockList() {
        return blockList;
    }
    
    public ArrayList<Database_Pointer> getBlockedByList() {
        return blockedByList;
    }
    
    public String getBiography() {
        return biography;
    }


    // Database can modify these methods below //

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public void setHabitList(Habit_List habitList) {
        this.habitList = habitList;
    }


        // Might not want to allow these //
    public void setFollowerList(ArrayList<Database_Pointer> followerList) {
        this.followerList = followerList;
    }

    public void setFollowingList(ArrayList<Database_Pointer> followingList) {
        this.followingList = followingList;
    }

    public void setFollowerReqList(ArrayList<Database_Pointer> followerReqList) {
        this.followerReqList = followerReqList;
    }

    public void setFollowerRequestedList(ArrayList<Database_Pointer> followerRequestedList) {
        this.followerRequestedList = followerRequestedList;
    }

    public void setBlockList(ArrayList<Database_Pointer> blockList) {
        this.blockList = blockList;
    }

    public void setBlockedByList(ArrayList<Database_Pointer> blockedByList) {
        this.blockedByList = blockedByList;
    }
    
    public void setBiography(String biography) {
        this.biography = biography;
    }
    // add methods have to adjust the database

    
    public void addHabit(Habit habit){
        this.habitList.add(habit);
    }

    /**
     * removeHabit
     *
     * removes a given habit from the habitList
     *
     * @param habit Habit to remove
     * @return boolean true if succeeded, false else wise
     */
    public boolean removeHabit(Habit habit){
        return this.habitList.remove(habit);
    }


    public Habit getHabit(int index){
        return this.habitList.get(index);
    }





    public void addFollower(Database_Pointer newFollower){

        //TODO: Database Implementation
        this.followerList.add(newFollower);
    }

    public void addFollowerReq(Database_Pointer newFollowReq){

        //TODO: Database Implementation
        this.followerReqList.add(newFollowReq);
    }

    public void addFollowing(Database_Pointer newFollowing){

        //TODO: Database Implementation
        this.followingList.add(newFollowing);
    }

    /** getEmail
     *
     * gets Email
     *
     * @author Dakota
     * @return String email
     */
    public String getEmail(){
        return this.email;
    }







    // replace methods not sure if i need it or not
    public void replaceHabit(int index, Habit habit){
        this.habitList.set(index,habit);
    }



    // These implement Parcelable for being passed through an intent

    /**
     * Apart of Parcelable implementation, does nothing but is required
     * @author Dakota
     * @return int 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * writeToParcel
     *
     * Writes all the attributes to the parcel out
     * @author Dakota
     * @see Parcelable
     * @see Parcel
     * @see Bundle
     * @see ClassLoader
     * @param parcel Parcel to be create
     * @param flags int, idk not important but required
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {


        Bundle userBundle = new Bundle(this.getClass().getClassLoader());


        userBundle.putString("username", username);

        Log.d("UserParcelable", "Parcel Writer userName:" + userBundle.getString("username"));
        
        userBundle.putString("password", password);

        userBundle.putString("biography", biography);

        userBundle.putString("email", this.getEmail());

        // requires Habit to implement Parcelable
        userBundle.putParcelableArrayList("habitList", (ArrayList<Habit>)habitList);


        userBundle.putParcelableArrayList("followerList", followerList);
        userBundle.putParcelableArrayList("followingList", followingList);
        userBundle.putParcelableArrayList("followerReqList", followerReqList);
        userBundle.putParcelableArrayList("followerRequestedList", followerRequestedList);
        userBundle.putParcelableArrayList("blockList", blockList);
        userBundle.putParcelableArrayList("blockedByList", blockedByList);

        parcel.writeBundle(userBundle); // writes bundle to parcel

    }

    // Creates User from parcel
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel in) {

            return new User(in);
        }

        @Override
        public User[] newArray(int size) {

            return new User[size];
        }
    };


    public boolean removeFollower(Database_Pointer follower) {
        boolean success = false;
        success = this.followerList.removeIf(database_pointer -> {
            if (database_pointer.getEmail() == follower.getEmail()){ return true; }
            return false;
        });

        return success;
    }

    public boolean removeFollowerReq(Database_Pointer followReq) {
        boolean success = false;
        success = this.followerReqList.removeIf(database_pointer -> {
            if (database_pointer.getEmail() == followReq.getEmail()){ return true; }
            return false;
        });

        return success;
    }

    public boolean removeFollowing(Database_Pointer following) {
        boolean success = false;
        success = this.followingList.removeIf(database_pointer -> {
            if (database_pointer.getEmail() == following.getEmail()){ return true; }
            return false;
        });

        return success;
    }
}

