package com.cmput301f21t34.habittrak.user;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * User
 *
 * @author Dakota
 *
 * User to kepp track of
 *
 * @version 1.1
 * @since 2021-10-16
 * @see Habit_Event
 * @see Habit
 */
public class User extends Database_Pointer implements Serializable {

    // Attributes //

    // Any changes need to be implement in writeToParcel and Parcel constructor - Dakota

    private String username;
    private String password;

    private ArrayList<Habit> habitList;

    private ArrayList<Database_Pointer> followerList;
    private ArrayList<Database_Pointer> followingList;
    private ArrayList<Database_Pointer> followerReqList;
    private ArrayList<Database_Pointer> followerRequestedList;
    private ArrayList<Database_Pointer> blockList;
    private ArrayList<Database_Pointer> blockedByList;

    String biography;

    // Constructors //

    public User(String username, String password, String email, ArrayList<Habit> habitList, ArrayList<Habit_Event> habitEventList,
                ArrayList<Database_Pointer> followerList, ArrayList<Database_Pointer> followingList, ArrayList<Database_Pointer> followerReqList,
                ArrayList<Database_Pointer> followerRequestedList, ArrayList<Database_Pointer> blockList, ArrayList<Database_Pointer> blockedByList,
                String biography){

        super(email);
        this.username = username;
        this.password = password;
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
        super("dummyEmail");
        this.username = "dummyUser";
        this.password = "123456789";
        this.habitList = new ArrayList<Habit>();
        this.followerList = new ArrayList<Database_Pointer>();
        this.followingList = new ArrayList<Database_Pointer>();
        this.followerReqList = new ArrayList<Database_Pointer>();
        this.followerRequestedList = new ArrayList<Database_Pointer>();
        this.blockList = new ArrayList<Database_Pointer>();
        this.blockedByList = new ArrayList<Database_Pointer>();
        this.biography = "";
    }

    /**
     * New User
     *
     * Creates a new User object with only an email
     * @param email
     */
    public User(String email){
        super(email);
        this.username = "dummyUser" + email;
        this.password = "123456789";

        this.habitList = new ArrayList<Habit>();
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
        super("broken");


        /* Not Working Currently
        Bundle userBundle;
        userBundle = parcel.readBundle(User.class.getClassLoader());

        Log.d("UserParcelable", "Parcel Construction userName:" + userBundle.getString("username"));

        this.username = userBundle.getString("username");
        this.habitList = userBundle.getParcelableArrayList("habitList");
        this.followerList = userBundle.getParcelableArrayList("followerList");
        this.followingList = userBundle.getParcelableArrayList("followingList");
        this.followerReqList = userBundle.getParcelableArrayList("followerReqList");
        this.biography = userBundle.getString("biography");

         */


    }


    // Getter methods
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public ArrayList<Habit> getHabitList() {
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
        return followerReqList;
    }
    public ArrayList<Database_Pointer> getBlockList() {
        return followerList;
    }
    public ArrayList<Database_Pointer> getBlockByList() {
        return followingList;
    }
    public String getBiography() {
        return biography;
    }

    // Setter methods
    // Database can modify these //

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHabitList(ArrayList<Habit> habitList) {
        this.habitList = habitList;
    }


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



    @Deprecated
    public void addFollower(User newFollower){

        this.followerList.add(newFollower);
    }
    @Deprecated
    public void addFollowerReq(User newFollowReq){
        this.followerReqList.add(newFollowReq);
    }
    @Deprecated
    public void addFollowing(User newFollowing){
        this.followingList.add(newFollowing);
    }



    // remove methods
    public boolean removeHabit(Habit habit){
        int index = this.habitList.size();
        for(int i = 0; i < this.habitList.size(); i++){
            if (habitList.get(i).getTitle().equals(habit.getTitle())){
                index = i;
            }
        }
        if (index != this.habitList.size()){
            this.habitList.remove(index);
            return true;
        }
        else{
            return false;
        }
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
    /*
    @Override
    public int describeContents() {
        return 0;
    }*/

    /**
     * writeToParcel
     *
     * Writes all the attributes to the parcel out
     * @author Dakota
     * @see Parcelable
     * @see Parcel
     * @see Bundle
     * @see ClassLoader
     * @param out Parcel to be create
     * @param flags int, idk not important but required
     */
    /*
    @Override
    public void writeToParcel(Parcel out, int flags) {

        Bundle userBundle = new Bundle(this.getClass().getClassLoader());


        userBundle.putString("username", username);
        Log.d("UserParcelable", "Parcel Writer userName:" + userBundle.getString("username"));

        // requires Habit to implement Parcelable
        userBundle.putParcelableArrayList("habitList", habitList);

        // requires User to implement Parcelable (which is what this code dose)
        // Broken
        userBundle.putParcelableArrayList("followerList", (ArrayList<? extends Parcelable>) followerList);
        userBundle.putParcelableArrayList("followingList", (ArrayList<? extends Parcelable>) followingList);
        userBundle.putParcelableArrayList("followerReqList", (ArrayList<? extends Parcelable>) followerReqList);

        out.writeBundle(userBundle); // writes bundle to parcel

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
    };*/
}

