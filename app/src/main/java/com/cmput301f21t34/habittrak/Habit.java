package com.cmput301f21t34.habittrak;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Habit
 *
 * @author Dakota
 *
 * Habit object that a user wants to track
 *
 * @version 1.0
 * @since 2021-10-15
 * @see Habit_Event, User
 */
public class Habit implements Comparable<Habit> {

    // Attributes //


    private String title, reason;
    private Calendar startDate;
    private ArrayList<Habit_Event> habitEvents = new ArrayList<Habit_Event>();

    // need to track which days of the week


    // Constructors //

    Habit(){
        this.title = ""; this.reason="";
        this.startDate = Calendar.getInstance();
    }

    Habit(String title, String reason, Calendar startDate){
        this.title = title; this.reason = reason;
        this.startDate = startDate;
    }


    // Methods //


    /**
     * getTitle
     *
     * @author Dakota
     *
     * getter function for Habit title
     *
     * @return String
     */
    public String getTitle(){
        return this.title;
    }

    /**
     * setTitle
     *
     * @author Dakota
     *
     * setter function for Habit title
     *
     * @param title String to change Habits title to
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * getReason
     *
     * @author Dakota
     *
     * getter function for Habit reason
     *
     * @return String
     */
    public String getReason(){
        return this.reason;
    }

    /**
     * setReason
     *
     * @author Dakota
     *
     * setter function for Habit reason
     *
     * @param reason String to change Habits reason to
     */
    public void setReason(String reason){
        this.reason = reason;
    }

    /**
     * getStartDate
     *
     * @author Dakota
     *
     * getter function for Habit start date
     *
     * @return Calendar
     */
    public Calendar getStartDate(){
        return this.startDate;
    }

    /**
     * setStartDate
     *
     * @author Dakota
     *
     * setter function for Habit start date
     *
     * @param startDate Calendar to change Habits start date to
     */
    public void setStartDate(Calendar startDate){
        this.startDate = startDate;
    }


    public boolean addHabitEvent(Habit_Event habitEvent){
        return habitEvents.add(habitEvent);
    }

    public Habit_Event getHabitEvent(int index){
        return habitEvents.get(index);
    }

    public Habit_Event removeHabitEvent(int index){
        return habitEvents.remove(index);
    }

    public boolean removeHabitEvent(Habit_Event habit){
        return habitEvents.remove(habit);
    }


    @Override
    public int compareTo(Habit habit) {

        return this.startDate.getTime().compareTo(habit.getStartDate().getTime());

    }
}
