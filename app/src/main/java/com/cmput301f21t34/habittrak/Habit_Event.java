package com.cmput301f21t34.habittrak;

import android.location.Location;

import java.io.File;
import java.util.Date;

public class Habit_Event implements Comparable<Habit_Event> {
    private String habit_event_id;
    private String comment;
    private Habit habit;
    private Date completed_date;
    private Location location;
    private File photograph;
    Habit_Event(){
        this.comment="";
        this.completed_date = new Date();
        this.habit = new Habit();
        this.location = new Location("");
        this.photograph = new File("");
        this.habit_event_id = "";

    }
    Habit_Event(String habit_event_id, String comment,Date date,Habit habit,Location loc, File photo){
        this.photograph = photo;
        this.location = loc;
        this.habit = habit;
        this.comment = comment;
        this.completed_date = date;
        this.habit_event_id = habit_event_id;
    }
    // getter methods

    public String getHabit_event_id() {
        return habit_event_id;
    }

    public Habit getHabit() {
        return habit;
    }

    public Date getCompleted_date() {
        return completed_date;
    }

    public File getPhotograph() {
        return photograph;
    }

    public Location getLocation() {
        return location;
    }

    public String getComment() {
        return comment;
    }
    //setter methods

    public void setHabit_event_id(String habit_event_id) {
        this.habit_event_id = habit_event_id;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCompleted_date(Date completed_date) {
        this.completed_date = completed_date;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setPhotograph(File photograph) {
        this.photograph = photograph;
    }

    /**
     * compareTo
     *
     * Uses Habit_Events date to compare when sorting
     *
     * @author Dakota Kryzanowski
     * @param habitEvent
     * @return int -1,0,1
     */
    @Override
    public int compareTo(Habit_Event habitEvent) {
        return this.completed_date.compareTo(habitEvent.getCompleted_date());
    }
}
