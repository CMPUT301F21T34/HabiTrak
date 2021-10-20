package com.cmput301f21t34.habittrak;

import android.location.Location;

import java.io.File;
import java.util.Date;

public class Habit_Event implements Comparable<Habit_Event> {
    private String habitEventId;
    private String comment;
    private Habit habit;
    private Date completedDate;
    private Location location;
    private File photograph;
    Habit_Event(){
        this.comment="";
        this.completedDate = new Date();
        this.habit = new Habit();
        this.location = new Location("");
        this.photograph = new File("");
        this.habitEventId = "";

    }
    Habit_Event(String habitEventId, String comment, Date date, Habit habit, Location loc, File photo){
        this.photograph = photo;
        this.location = loc;
        this.habit = habit;
        this.comment = comment;
        this.completedDate = date;
        this.habitEventId = habitEventId;
    }
    // getter methods

    public String getHabitEventId() {
        return habitEventId;
    }

    public Habit getHabit() {
        return habit;
    }

    public Date getCompletedDate() {
        return completedDate;
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

    public void setHabitEventId(String habitEventId) {
        this.habitEventId = habitEventId;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
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
     * @author Dakota
     * @param habitEvent
     * @return int -1,0,1
     */
    @Override
    public int compareTo(Habit_Event habitEvent) {
        return this.completedDate.compareTo(habitEvent.getCompletedDate());
    }
}
