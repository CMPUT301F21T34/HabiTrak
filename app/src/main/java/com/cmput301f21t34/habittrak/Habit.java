package com.cmput301f21t34.habittrak;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

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
     * getter function for Habit title
     *
     * @author Dakota
     * @return String
     */
    public String getTitle(){
        return this.title;
    }

    /**
     * setTitle
     *
     * setter function for Habit title
     *
     * @author Dakota
     * @param title String to change Habits title to
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * getReason
     *
     * getter function for Habit reason
     *
     * @author Dakota
     * @return String
     */
    public String getReason(){
        return this.reason;
    }

    /**
     * setReason
     *
     * setter function for Habit reason
     *
     * @author Dakota
     * @param reason String to change Habits reason to
     */
    public void setReason(String reason){
        this.reason = reason;
    }

    /**
     * getStartDate
     *
     * getter function for Habit start date
     *
     * @author Dakota
     * @return Calendar
     */
    public Calendar getStartDate(){
        return this.startDate;
    }

    /**
     * setStartDate
     *
     * setter function for Habit start date
     *
     * @author Dakota
     * @param startDate Calendar to change Habits start date to
     */
    public void setStartDate(Calendar startDate){
        this.startDate = startDate;
    }


    /**
     * addHabitEvent
     *
     * adds a Habit_Event object to Habit's habit events
     *
     * @param habitEvent Habit_Event to be added
     * @return boolean true if added
     */
    public boolean addHabitEvent(Habit_Event habitEvent){
        return habitEvents.add(habitEvent);
    }


    /**
     * removeHabitEvent
     *
     * removes a specific Habit_Event object from Habit's habit events
     *
     * @author Dakota
     * @param habit Habit_Event object to be removed
     * @return boolean true if removed
     */
    public boolean removeHabitEvent(Habit_Event habit){
        return habitEvents.remove(habit);
    }

    /**
     * removeHabitEvent
     *
     * removes a Habit_Event using its String id from Habit's habit events
     *
     * @author Dakota
     * @param habitEventID String habit event id
     * @return boolean true if a Habit_Event was removed
     * @deprecated use removeHabitEvent(Habit_Event) instead
     */
    public boolean removeHabitEvent(String habitEventID){

        boolean removed = false; // keeps track if a Habit_Event has been removed

        // iterates through habitEvents
        for (int index = 0; index < habitEvents.size(); index++){

            // checks for matching habitEventID
            if (habitEvents.get(index).getHabit_event_id() == habitEventID){

                habitEvents.remove(index); // removes if found
                removed = true;

                break;
            }
        }

        return removed;
    }

    /**
     * sortHabitEvents
     *
     * sorts a Habit's habit events Arraylist
     *
     * @author Dakota
     */
    public void sortHabitEvents(){

        // Sorts with Habit_Event's compareTo method
        habitEvents.sort(Habit_Event::compareTo);
        // min API 24 needed

    }


    /**
     * compareTo
     *
     * Uses Habit startDate for comparison in sorting
     *
     * @author Dakota
     * @param habit Habit object to compare to
     * @return int -1,0,1
     */
    @Override
    public int compareTo(Habit habit) {

        return this.startDate.getTime().compareTo(habit.getStartDate().getTime());

    }
}
