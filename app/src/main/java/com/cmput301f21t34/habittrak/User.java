package com.cmput301f21t34.habittrak;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User {
    private String username;
    ArrayList<Habit> habit_list;
    ArrayList<Habit_Event> habit_event_list;
    ArrayList<User> follower_list;
    ArrayList<User> following_list;
    ArrayList<User> follower_req_list;
    User(String username, ArrayList<Habit> habit_list, ArrayList<Habit_Event> habit_event_list,
         ArrayList<User> follower_list, ArrayList<User> following_list, ArrayList<User> follower_req_list){
        this.username=username;
        this.habit_list=habit_list;
        this.habit_event_list = habit_event_list;
        this.follower_list = follower_list;
        this.following_list = following_list;
        this.follower_req_list = follower_req_list;
    }
    User(){
        this.username="";
        this.habit_list=new ArrayList<Habit>();
        this.habit_event_list = new ArrayList<Habit_Event>();
        this.follower_list = new ArrayList<User>();
        this.following_list = new ArrayList<User>();
        this.follower_req_list = new ArrayList<User>();
    }
    //getter methods
    public String getUsername() {
        return username;
    }
    public ArrayList<Habit> getHabit_list() {
        return habit_list;
    }
    public ArrayList<Habit_Event> getHabit_event_list() {
        return habit_event_list;
    }
    public ArrayList<User> getFollower_list() {
        return follower_list;
    }
    public ArrayList<User> getFollower_req_list() {
        return follower_req_list;
    }
    public ArrayList<User> getFollowing_list() {
        return following_list;
    }
    //setter methods
    public void setUsername(String username) {
        this.username = username;
    }

    public void setHabit_list(ArrayList<Habit> habit_list) {
        this.habit_list = habit_list;
    }

    public void setHabit_event_list(ArrayList<Habit_Event> habit_event_list) {
        this.habit_event_list = habit_event_list;
    }

    public void setFollower_list(ArrayList<User> follower_list) {
        this.follower_list = follower_list;
    }

    public void setFollowing_list(ArrayList<User> following_list) {
        this.following_list = following_list;
    }

    public void setFollower_req_list(ArrayList<User> follower_req_list) {
        this.follower_req_list = follower_req_list;
    }
    // add methods have to adjust the database
    public void addHabit(Habit habit){
        this.habit_list.add(habit);
    }
    public void addHabit_Event(Habit_Event habit_event){
        this.habit_event_list.add(habit_event);
    }
    public void addFollower(User new_follower){
        this.follower_list.add(new_follower);
    }
    public void add_follower_req(User new_follow_req){
        this.follower_req_list.add(new_follow_req);
    }
    public void add_following(User new_following){
        this.following_list.add(new_following);
    }
    // remove methods
    public boolean removeHabit(Habit habit){
        int index= this.habit_list.size();
        for(int i = 0; i < this.habit_list.size();i++){
            if (habit_list.get(i).getTitle().equals(habit.getTitle())){
                index = i;
            }
        }
        if (index != this.habit_list.size()){
            this.habit_list.remove(index);
            return true;
        }
        else{
            return false;
        }
    }
    public boolean removeHabit_Event(Habit_Event habit_event){
        int index= this.habit_event_list.size();
        for(int i = 0; i < this.habit_event_list.size();i++){
            if (habit_event_list.get(i).getHabit_event_id().equals(habit_event.getHabit_event_id())){
                index = i;
            }
        }
        if (index != this.habit_event_list.size()){
            this.habit_event_list.remove(index);
            return true;
        }
        else{
            return false;
        }
    }
    public boolean removeFollower(User follower){
        int index = this.follower_list.size();
        for(int i =0; i < this.follower_list.size();i++){
            if(this.follower_list.get(i).getUsername().equals(follower.getUsername())){
                index = i;
            }
        }
        if(index != this.follower_list.size()){
            this.follower_list.remove(index);
            return true;
        }
        else{
            return false;
        }
    }
    public boolean removeFollowing(User follower){
        int index = this.following_list.size();
        for(int i =0; i < this.following_list.size();i++){
            if(this.following_list.get(i).getUsername().equals(follower.getUsername())){
                index = i;
            }
        }
        if(index != this.following_list.size()){
            this.following_list.remove(index);
            return true;
        }
        else{
            return false;
        }
    }
    public boolean removeFollower_req(User follower){
        int index = this.follower_req_list.size();
        for(int i =0; i < this.follower_req_list.size();i++){
            if(this.follower_req_list.get(i).getUsername().equals(follower.getUsername())){
                index = i;
            }
        }
        if(index != this.follower_req_list.size()){
            this.follower_req_list.remove(index);
            return true;
        }
        else{
            return false;
        }
    }
    // replace methods not sure if i need it or not
    public void replaceHabit(int index, Habit habit){
        this.habit_list.set(index,habit);
    }
    public void replaceHabit_Event(int index, Habit_Event habit_event){
        this.habit_event_list.set(index,habit_event);
    }
}

