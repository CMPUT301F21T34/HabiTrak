package com.cmput301f21t34.habittrak;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;



public class User implements Parcelable {
    private String username;
    ArrayList<Habit> habitList;
    ArrayList<Habit_Event> habitEventList;
    ArrayList<User> followerList;
    ArrayList<User> followingList;
    ArrayList<User> followerReqList;

    User(String username, ArrayList<Habit> habitList, ArrayList<Habit_Event> habitEventList,
         ArrayList<User> followerList, ArrayList<User> followingList, ArrayList<User> followerReqList){
        this.username=username;
        this.habitList = habitList;
        this.habitEventList = habitEventList;
        this.followerList = followerList;
        this.followingList = followingList;
        this.followerReqList = followerReqList;
    }

    /**
     * New empty user
     *
     * @author Dakota
     * @param username username String is new users new username
     */
    User(String username){
        this.username = username;
        this.habitList = new ArrayList<Habit>();
        this.habitEventList = new ArrayList<Habit_Event>();
        this.followerList = new ArrayList<User>();
        this.followingList = new ArrayList<User>();
        this.followerReqList = new ArrayList<User>();
    }

    User(Parcel parcel){

        // order matters
        this.username = parcel.readString();
        this.habitList = parcel.readArrayList(null);
        this.habitEventList = parcel.readArrayList(null);
        this.followerList = parcel.readArrayList(null);
        this.followingList = parcel.readArrayList(null);
        this.followerReqList = parcel.readArrayList(null);

    }


    //getter methods
    public String getUsername() {
        return username;
    }
    public ArrayList<Habit> getHabitList() {
        return habitList;
    }
    public ArrayList<Habit_Event> getHabitEventList() {
        return habitEventList;
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

    public void setHabitEventList(ArrayList<Habit_Event> habitEventList) {
        this.habitEventList = habitEventList;
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
    public void addHabitEvent(Habit_Event habitEvent){
        this.habitEventList.add(habitEvent);
    }
    public void addFollower(User newFollower){
        this.followerList.add(newFollower);
    }
    public void addFollowerReq(User newFollowReq){
        this.followerReqList.add(newFollowReq);
    }
    public void add_following(User newFollowing){
        this.followingList.add(newFollowing);
    }
    // remove methods
    public boolean removeHabit(Habit habit){
        int index= this.habitList.size();
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
    public boolean removeHabitEvent(Habit_Event habitEvent){
        int index= this.habitEventList.size();
        for(int i = 0; i < this.habitEventList.size(); i++){
            if (habitEventList.get(i).getHabitEventId().equals(habitEvent.getHabitEventId())){
                index = i;
            }
        }
        if (index != this.habitEventList.size()){
            this.habitEventList.remove(index);
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
    public void replaceHabit_Event(int index, Habit_Event habitEvent){
        this.habitEventList.set(index,habitEvent);
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
     * @see "https://stackoverflow.com/questions/7042272/how-to-properly-implement-parcelable-with-an-"arraylistparcelable"
     * @param out Parcel to be create
     * @param flags int, idk not important but required
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeString(username);
        out.writeList(habitList);
        out.writeList(habitEventList);
        out.writeList(followerList);
        out.writeList(followingList);
        out.writeList(followerReqList);
    }

    // Creates User from parcel
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        /**
         * createFromParcel
         *
         * creates User from parcel
         *
         * @author Dakota
         * @param in Parcel to create User from
         * @return User from parcel
         */
        @Override
        public User createFromParcel(Parcel in) {

            return new User(in);
        }

        /**
         * newArray
         *
         * needed for creating User from parcel but unimportant
         *
         * @author Dakota
         * @param size of User[] array
         * @return User[]
         */
        @Override
        public User[] newArray(int size) {

            return new User[size];
        }
    };



}

