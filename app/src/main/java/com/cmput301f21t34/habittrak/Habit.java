package com.cmput301f21t34.habittrak;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Habit
 *
 * @author Dakota
 *
 * Habit object that a user wants to track
 *
 * @version 1.1
 * @since 2021-10-15
 * @see Habit_Event
 */
public class Habit implements Comparable<Habit>, Parcelable {

    // Attributes //

    // Any changes need to be implement in writeToParcel and Parcel constructor - Dakota


    private String title, reason;
    private Calendar startDate;
    private ArrayList<Habit_Event> habitEvents = new ArrayList<Habit_Event>();

    // Boolean array to track which day of the week
    // index 0 -> Monday, index 1 -> Tuesday, ... index 6 -> Sunday
    private ArrayList<Boolean> onDays = new ArrayList<>();
        private final int DAYS_IN_WEEK = 7; // Amount of days in week onDays Handles




    // Constructors //

    Habit(){
        this.title = ""; this.reason="";
        this.startDate = Calendar.getInstance();



    }

    Habit(String title){
        this.title = title;
        this.reason="";
        this.startDate = Calendar.getInstance();


    }

    public Habit(String title, String reason, Calendar startDate){
        this.title = title; this.reason = reason;
        this.startDate = startDate;


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
    public Habit(Parcel parcel) {
        Bundle habitBundle;
        habitBundle = parcel.readBundle(Habit.class.getClassLoader());


        this.title = habitBundle.getString("title");
        this.reason = habitBundle.getString("reason");
        this.habitEvents = habitBundle.getParcelableArrayList("habitEvents");

        // Handles Calendar
        String completedDateTimeZone = habitBundle.getString("startDateTimeZone");
        if ( completedDateTimeZone != null) {

            Calendar constructionCalendar = Calendar.getInstance();
            constructionCalendar.setTimeZone(TimeZone.getTimeZone(completedDateTimeZone));
            constructionCalendar.setTimeInMillis(habitBundle.getLong("startDateTime"));

            this.startDate = constructionCalendar;
        } else {
            this.startDate = null;
        }

        // converts boolean[] into ArrayList<Boolean>
        ArrayList<Boolean> constructionArrayList = new ArrayList<Boolean>();
        boolean[] onDaysArray = habitBundle.getBooleanArray("onDaysArray"); // array to convert from
        for (int index = 0; index < DAYS_IN_WEEK; index++){
            constructionArrayList.add(onDaysArray[index]);

        }
        this.onDays = constructionArrayList;
    }


    // Methods //


    public static final Parcelable.Creator<Habit> CREATOR = new Parcelable.Creator<Habit>() {
        @Override
        public Habit createFromParcel(Parcel in) {
            return new Habit(in);
        }

        @Override
        public Habit[] newArray(int size) {
            return new Habit[size];
        }
    };

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
     * getOnDays
     *
     * getter function for Habit to track which day of the week
     *
     * @author Henry
     * @return ArrayList
     * returns a boolean array that contains which day of the week the habit is on
     */
    public ArrayList<Boolean> getOnDays() {
        return this.onDays;
    }

    /**
     * setOnDays
     *
     * setter function for Habit onDays
     *
     * @author Henry
     * @param onDays onDays list to change Habit onDays to
     */
    public void setOnDays(ArrayList<Boolean> onDays) {
        this.onDays = onDays;
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
            if (habitEvents.get(index).getHabitEventId() == habitEventID){

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
        // min API 24 needed, need to program own sorting else wise

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

    /**
     * Code that parses Habit parameters into a Parcel
     * Needs to be updated for any changes to attributes
     * Or new attributes added
     *
     * Used to pass through intents
     *
     * @author Dakota
     * @see Parcelable
     * @see Parcel
     * @see Bundle
     * @see ClassLoader
     * @param out Parcel created
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {

        Bundle habitBundle = new Bundle(Habit.class.getClassLoader());

        habitBundle.putString("title", title);
        habitBundle.putString("reason", reason);

        // Requires Habit_Events to implement parcelable
        habitBundle.putParcelableArrayList("habitEvents", habitEvents);

        // Handles Calendar
        if (startDate != null) {
            habitBundle.putString("startDateTimeZone", startDate.getTimeZone().getID());
            habitBundle.putLong("startDateTime", startDate.getTimeInMillis());
        } else {
            habitBundle.putString("startDateTimeZone", null);
        }

        // converts ArrayList<Boolean> into boolean[]
        boolean[] onDaysArray = new boolean[DAYS_IN_WEEK];
        for (int index = 0; index < DAYS_IN_WEEK; index++){
            onDaysArray[index] = onDays.get(index).booleanValue();
        }
            // boolean[] can be passed but not ArrayList<boolean.

        habitBundle.putBooleanArray("onDaysArray", onDaysArray);
        out.writeBundle(habitBundle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private static class Creator implements Parcelable.Creator<Habit> {
        public Habit createFromParcel(Parcel in) {
            return new Habit(in);
        }

        public Habit[] newArray(int size) {
            return new Habit[size];
        }
    }
}
