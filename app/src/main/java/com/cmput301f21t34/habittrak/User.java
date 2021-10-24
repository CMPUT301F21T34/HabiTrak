package com.cmput301f21t34.habittrak;


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
 * @version 1.1
 * @since 2021-10-16
 * @see Habit_Event
 * @see Habit
 */
public class User implements Parcelable {

    // Attributes //

    // Any changes need to be implement in writeToParcel and Parcel constructor - Dakota

    private String username;
    ArrayList<Habit> habitList;
    ArrayList<User> followerList;
    ArrayList<User> followingList;
    ArrayList<User> followerReqList;

    // Constructors //

    User(String username, ArrayList<Habit> habitList, ArrayList<Habit_Event> habitEventList,
         ArrayList<User> followerList, ArrayList<User> followingList, ArrayList<User> followerReqList){
        this.username = username;
        this.habitList = habitList;
        this.followerList = followerList;
        this.followingList = followingList;
        this.followerReqList = followerReqList;
    }

    /**
     * New User
     *
     * Creates a User with only a username
     *
     * @author Dakota
     * @param username String is new users new username
     */
    User(String username){
        this.username = username;

        this.habitList = new ArrayList<Habit>();
        this.followerList = new ArrayList<User>();
        this.followingList = new ArrayList<User>();
        this.followerReqList = new ArrayList<User>();
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
    User(Parcel parcel){

        Bundle userBundle;
        userBundle = parcel.readBundle(User.class.getClassLoader());

        Log.d("UserParcelable", "Parcel Construction userName:" + userBundle.getString("username"));

        this.username = userBundle.getString("username");
        this.habitList = userBundle.getParcelableArrayList("habitList");
        this.followerList = userBundle.getParcelableArrayList("followerList");
        this.followingList = userBundle.getParcelableArrayList("followingList");
        this.followerReqList = userBundle.getParcelableArrayList("followerReqList");


    }


    //getter methods
    public String getUsername() {
        return username;
    }
    public ArrayList<Habit> getHabitList() {
        return habitList;
    }

    public ArrayList<User> getFollowerList() {
        return followerList;
    }
    public ArrayList<User> getFollowerReqList() {
        return followerReqList;
    }
    public ArrayList<User> getFollowingList() {
        return followingList;
    }
    //setter methods
    public void setUsername(String username) {
        this.username = username;
    }

    public void setHabitList(ArrayList<Habit> habitList) {
        this.habitList = habitList;
    }


    public void setFollowerList(ArrayList<User> followerList) {
        this.followerList = followerList;
    }

    public void setFollowingList(ArrayList<User> followingList) {
        this.followingList = followingList;
    }

    public void setFollowerReqList(ArrayList<User> followerReqList) {
        this.followerReqList = followerReqList;
    }
    // add methods have to adjust the database
    public void addHabit(Habit habit){
        this.habitList.add(habit);
    }

    public void addFollower(User newFollower){
        this.followerList.add(newFollower);
    }
    public void addFollowerReq(User newFollowReq){
        this.followerReqList.add(newFollowReq);
    }
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


    public boolean removeFollower(User follower){
        int index = this.followerList.size();
        for(int i = 0; i < this.followerList.size(); i++){
            if(this.followerList.get(i).getUsername().equals(follower.getUsername())){
                index = i;
            }
        }
        if(index != this.followerList.size()){
            this.followerList.remove(index);
            return true;
        }
        else{
            return false;
        }
    }
    public boolean removeFollowing(User follower){
        int index = this.followingList.size();
        for(int i = 0; i < this.followingList.size(); i++){
            if(this.followingList.get(i).getUsername().equals(follower.getUsername())){
                index = i;
            }
        }
        if(index != this.followingList.size()){
            this.followingList.remove(index);
            return true;
        }
        else{
            return false;
        }
    }
    public boolean removeFollowerReq(User follower){
        int index = this.followerReqList.size();
        for(int i = 0; i < this.followerReqList.size(); i++){
            if(this.followerReqList.get(i).getUsername().equals(follower.getUsername())){
                index = i;
            }
        }
        if(index != this.followerReqList.size()){
            this.followerReqList.remove(index);
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
        userBundle.putParcelableArrayList("followerList",followerList);
        userBundle.putParcelableArrayList("followingList",followingList);
        userBundle.putParcelableArrayList("followerReqList",followerReqList);

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

