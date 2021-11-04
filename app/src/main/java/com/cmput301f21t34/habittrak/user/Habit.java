package com.cmput301f21t34.habittrak.user;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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
 * @version 2.0
 * @since 2021-10-15
 * @see Habit_Event
 */
public class Habit implements Comparable<Habit>, Parcelable {

    // Attributes //

    // Any changes need to be implement in writeToParcel and Parcel constructor - Dakota


    private int index = 0;

    private String title;
    private String reason;

    private Calendar startDate;
    private ArrayList<Habit_Event> habitEvents = new ArrayList<Habit_Event>();
    private boolean isPublic = false; // If other users can see this habit

    private On_Days onDaysObj = new On_Days();

    // Enum //

    // Constructors //

    public Habit(){
        this.title = "";
        this.reason= "";
        this.startDate = Calendar.getInstance();
        this.getOnDaysObj().setAll(
                new boolean[]{false, false, false, false, false, false, false}
        );
    }

    public Habit(String title){
        this.title = title;
        this.reason= "";
        this.startDate = Calendar.getInstance();
        this.getOnDaysObj().setAll(
                new boolean[]{false, false, false, false, false, false, false}
        );
    }

    public Habit(String title, String reason, Calendar startDate){
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        this.getOnDaysObj().setAll(
                new boolean[]{false, false, false, false, false, false, false}
        );

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

        // Handles Calendar //
        String completedDateTimeZone = habitBundle.getString("startDateTimeZone");
        if (completedDateTimeZone != null) {

            Calendar constructionCalendar = Calendar.getInstance();
            constructionCalendar.setTimeZone(TimeZone.getTimeZone(completedDateTimeZone));
            constructionCalendar.setTimeInMillis(habitBundle.getLong("startDateTime"));

            this.startDate = constructionCalendar;
        } else {
            this.startDate = null;
        }

        this.isPublic = habitBundle.getBoolean("isPublic");

        this.onDaysObj = habitBundle.getParcelable("onDaysObj");


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

    public int getIndex(){
        return this.index;

    }

    public void setIndex(int index){
        this.index = index;
    }



    /**
     * getOnDaysOnj
     *
     * gets reference to Habits On_Days object
     *
     * @author Dakota
     *
     * @return On_Days object for manipulation
     */
    public On_Days getOnDaysObj(){
        return this.onDaysObj;
    }


    @Deprecated
    public void setOnDaysObj(On_Days onDaysObj) { this.onDaysObj = onDaysObj; }


    /**
     * isHabitStart
     *
     * check if the habit is started or not
     *
     * @author Pranav
     * @return boolean is habit starts today
     */

    public boolean isHabitStart(){
        Calendar today = Calendar.getInstance();
        today.setFirstDayOfWeek(Calendar.MONDAY);
        boolean isStart = false;

        // compareTo returns 0 if time is equal
        // returns less than 0 if time but the calendar is less than the argument
        if (startDate.compareTo(today) <= 0){
            isStart = true;
        }
        return  isStart;
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
     * getHabitEvents
     *
     * @author Henry
     *
     * @return
     * Returns the habit events array list
     */
    public ArrayList<Habit_Event> getHabitEvents() {
        return this.habitEvents;
    }

    /**
     * setHabitEvents
     *
     * @author Henry
     *
     * @return
     * Sets the habit events array list
     */
    public void setHabitEvents(ArrayList<Habit_Event> habitEvents) { this.habitEvents = habitEvents; }

    /**
     * sortHabitEvents
     *
     * sorts a Habit's habit events Arraylist
     *
     * @author Dakota
     */
    public void sortHabitEvents(){

        // Sorts with Habit_Event's compareTo method
        // Sorts by date
        habitEvents.sort(Habit_Event::compareTo);
        // min API 24 needed, need to program own sorting else wise

    }

    /** makePublic
     *
     * makes the habit publicly visible
     *
     * @author Dakota
     *
     */
    public void makePublic(){
        this.isPublic = true;
    }
    /** makePrivate
     *
     * makes the habit not publicly visible
     *
     * @author Dakota
     *
     */
    public void makePrivate(){
        this.isPublic = false;
    }

    /** isPublic
     *
     * checks if the habit is public
     *
     * @author Dakota
     *
     * @return true if the habit is public, false if not
     */
    public boolean isPublic(){
        return this.isPublic;
    }

    /** isPrivate
     *
     * checks if the habit is private
     *
     * @author Dakota
     *
     * @return true if the habit is private, false if not
     */
    public boolean isPrivate(){
        return !this.isPublic;
    }


    /**
     * compareTo
     *
     * Uses Habit index for comparison in sorting
     *
     * @author Dakota
     * @param habit Habit object to compare to
     * @return int -1,0,1
     */
    @Override
    public int compareTo(Habit habit) {

        if (this.getIndex() > habit.getIndex()){
            return 1;
        } else if (this.getIndex() < habit.getIndex()){
            return -1;
        }
        return 0;

    }


    /**
     * hashCode
     *
     * returns an int hash of this object based on its title and reason
     * for hashing and comparison
     *
     * @author Dakota
     * @return int Hash
     */
    @Override
    public int hashCode(){
        return this.title.hashCode() * this.reason.hashCode();
    }


    /**
     * equals
     *
     * Compares an object with this Habit to see if they are equal
     * They are equal if they are both Habit classes, with the same title and reason
     *
     * @author Dakota
     * @param object Object to compare
     * @return boolean true if they are equal, false elsewise
     */
    @Override
    public boolean equals(Object object){
        String TAG = "HabitEquals";

        Log.d(TAG, "entered equals");

        if (object.getClass() == Habit.class){

            if( ( ((Habit) object).getTitle() == this.getTitle() )
                    && ( ((Habit) object).getReason() == this.getReason() ) ){
                return true;
            }
        }


        return false;

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
     * @param parcel Parcel created
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {


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

        habitBundle.putParcelable("onDaysObj", onDaysObj);

        habitBundle.putBoolean("isPublic", isPublic);

        parcel.writeBundle(habitBundle);
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