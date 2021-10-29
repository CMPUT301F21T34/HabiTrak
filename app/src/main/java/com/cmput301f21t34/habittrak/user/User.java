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
public class User extends Database_Pointer implements Parcelable {

    // Attributes //

    // Any changes need to be implement in writeToParcel and Parcel constructor - Dakota

    private String username;

    Habit_List habitList; // Habit_List extends ArrayList<Habit>

    ArrayList<Database_Pointer> followerList;
    ArrayList<Database_Pointer> followingList;
    ArrayList<Database_Pointer> followerReqList;

    String biography;

    // Constructors //

    public User(String username, String email, Habit_List habitList, ArrayList<Habit_Event> habitEventList,
         ArrayList<Database_Pointer> followerList, ArrayList<Database_Pointer> followingList, ArrayList<Database_Pointer> followerReqList, String biography){
        super(email);
        this.username = username;
        this.habitList = habitList;
        this.followerList = followerList;
        this.followingList = followingList;
        this.followerReqList = followerReqList;
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

        this.habitList = new Habit_List();
        this.followerList = new ArrayList<Database_Pointer>();
        this.followingList = new ArrayList<Database_Pointer>();
        this.followerReqList = new ArrayList<Database_Pointer>();
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


    //getter methods
    public String getUsername() {
        return username;
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
    public String getBiography() {
        return biography;
    }


    // Database can modify these methods below //

    public void setUsername(String username) {
        this.username = username;
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



    public void setBiography(String biography) {
        this.biography = biography;
    }
    // add methods have to adjust the database


    public void addHabit(Habit habit){
        this.habitList.add(habit);
    }

    public void removeHabit(Habit habit){
        this.habitList.remove(habit);
    }

    public void removeHabit(int index){
        this.habitList.remove(index);
    }

    public Habit getHabit(int index){
        return this.habitList.get(index);
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
     * @param out Parcel to be create
     * @param flags int, idk not important but required
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {

        Bundle userBundle = new Bundle(this.getClass().getClassLoader());


        userBundle.putString("username", username);
        Log.d("UserParcelable", "Parcel Writer userName:" + userBundle.getString("username"));

        // requires Habit to implement Parcelable
        userBundle.putParcelableArrayList("habitList", habitList);

        // requires User to implement Parcelable (which is what this code dose)
        /* Broken
        userBundle.putParcelableArrayList("followerList", (ArrayList<? extends Parcelable>) followerList);
        userBundle.putParcelableArrayList("followingList", (ArrayList<? extends Parcelable>) followingList);
        userBundle.putParcelableArrayList("followerReqList", (ArrayList<? extends Parcelable>) followerReqList);
         */
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
    };



}

