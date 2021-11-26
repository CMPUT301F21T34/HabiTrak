package com.cmput301f21t34.habittrak.user;

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
 * @version 2.0
 * @since 2021-10-15
 * @see HabitEvent
 */
public class Habit implements Comparable<Habit>, Parcelable {

    // Attributes //

    // Any changes need to be implement in writeToParcel and Parcel constructor - Dakota

    // Not to be added to DB
    private int currentStreak = 0; // For display
    private int bestStreak = 0; // For display
    private Calendar bestStreakDateEnd;
    private Calendar currentStreakDateEnd;


    // To Be Added To DB
    private int index = 0;
    private Calendar bestStreakDate;
    private Calendar currentStreakDate; // We could also just figure this out without db.

    private String title;
    private String reason;

    private Calendar startDate;
    private ArrayList<HabitEvent> habitEvents = new ArrayList<HabitEvent>();
    private boolean isPublic = false; // If other users can see this habit

    private OnDays onDaysObj = new OnDays();

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

        this.index = habitBundle.getInt("index");
        this.title = habitBundle.getString("title");
        this.reason = habitBundle.getString("reason");
        this.habitEvents = habitBundle.getParcelableArrayList("habitEvents");

        this.index = habitBundle.getInt("index");

        // Handles Calendar //
        // Handles Start Day Calendar //
        String completedDateTimeZone = habitBundle.getString("startDateTimeZone");
        if (completedDateTimeZone != null) {

            Calendar constructionCalendar = Calendar.getInstance();
            constructionCalendar.setTimeZone(TimeZone.getTimeZone(completedDateTimeZone));
            constructionCalendar.setTimeInMillis(habitBundle.getLong("startDateTime"));

            this.startDate = constructionCalendar;
        } else {
            this.startDate = null;
        }


        // Handles Best Streak Calendar //
        String bestStreakDateTimeZone = habitBundle.getString("bestStreakDateTimeZone");
        if (bestStreakDateTimeZone != null) {

            Calendar constructionCalendar = Calendar.getInstance();
            constructionCalendar.setTimeZone(TimeZone.getTimeZone(bestStreakDateTimeZone));
            constructionCalendar.setTimeInMillis(habitBundle.getLong("bestStreakDateTime"));

            this.bestStreakDate = constructionCalendar;
        } else {
            this.bestStreakDate = null;
        }

        // Handles Current Streak Calendar //
        String currentStreakDateTimeZone = habitBundle.getString("currentStreakDateTimeZone");
        if (currentStreakDateTimeZone != null) {

            Calendar constructionCalendar = Calendar.getInstance();
            constructionCalendar.setTimeZone(TimeZone.getTimeZone(currentStreakDateTimeZone));
            constructionCalendar.setTimeInMillis(habitBundle.getLong("currentStreakDateTime"));

            this.currentStreakDate = constructionCalendar;
        } else {
            this.currentStreakDate = null;
        }

        // Handles Current Streak End Calendar //
        String currentStreakDateEndTimeZone = habitBundle.getString("currentStreakDateEndTimeZone");
        if (currentStreakDateEndTimeZone != null) {

            Calendar constructionCalendar = Calendar.getInstance();
            constructionCalendar.setTimeZone(TimeZone.getTimeZone(currentStreakDateEndTimeZone));
            constructionCalendar.setTimeInMillis(habitBundle.getLong("currentStreakDateEndTime"));

            this.currentStreakDateEnd = constructionCalendar;
        } else {
            this.currentStreakDateEnd = null;
        }

        // Handles Best Streak End Calendar //
        String bestStreakDateEndTimeZone = habitBundle.getString("bestStreakDateEndTimeZone");
        if (bestStreakDateEndTimeZone != null) {

            Calendar constructionCalendar = Calendar.getInstance();
            constructionCalendar.setTimeZone(TimeZone.getTimeZone(bestStreakDateEndTimeZone));
            constructionCalendar.setTimeInMillis(habitBundle.getLong("bestStreakDateEndTime"));

            this.bestStreakDateEnd = constructionCalendar;
        } else {
            this.bestStreakDateEnd = null;
        }

        // streak ints

        this.currentStreak = habitBundle.getInt("currentStreak");
        this.bestStreak = habitBundle.getInt("bestStreak");


        this.isPublic = habitBundle.getBoolean("isPublic");

        this.onDaysObj = habitBundle.getParcelable("onDaysObj");


    }


    // Methods //


    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public void setBestStreak(int bestStreak) {
        this.bestStreak = bestStreak;
    }

    public int getCurrentStreak(){
        return this.currentStreak;
    }

    public int getBestStreak(){
        return this.bestStreak;
    }

    public Calendar getBestStreakDateEnd(){
        return this.bestStreakDateEnd;
    }

    public void setBestStreakDateEnd(Calendar calendar){
        this.bestStreakDateEnd = calendar;
    }

    public Calendar getCurrentStreakDateEnd(){
        return this.currentStreakDateEnd;
    }

    public void setCurrentStreakDateEnd(Calendar calendar){
        this.currentStreakDateEnd = calendar;
    }

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
     * gets reference to Habits OnDays object
     *
     * @author Dakota
     *
     * @return OnDays object for manipulation
     */
    public OnDays getOnDaysObj(){
        return this.onDaysObj;
    }


    @Deprecated
    public void setOnDaysObj(OnDays onDaysObj) { this.onDaysObj = onDaysObj; }


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
     * adds a HabitEvent object to Habit's habit events
     *
     * @param habitEvent HabitEvent to be added
     * @return boolean true if added
     */

    public boolean addHabitEvent(HabitEvent habitEvent){
        return habitEvents.add(habitEvent);
    }


    /**
     * removeHabitEvent
     *
     * removes a specific HabitEvent object from Habit's habit events
     *
     * @author Dakota
     * @param habit HabitEvent object to be removed
     * @return boolean true if removed
     */
    public boolean removeHabitEvent(HabitEvent habit){
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
    public ArrayList<HabitEvent> getHabitEvents() {
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
    public void setHabitEvents(ArrayList<HabitEvent> habitEvents) { this.habitEvents = habitEvents; }

    /**
     * sortHabitEvents
     *
     * sorts a Habit's habit events Arraylist
     *
     * @author Dakota
     */
    public void sortHabitEvents(){

        // Sorts with HabitEvent's compareTo method
        // Sorts by date
        habitEvents.sort(HabitEvent::compareTo);
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
        habitBundle.putInt("index", index);
        // Requires Habit_Events to implement parcelable
        habitBundle.putParcelableArrayList("habitEvents", habitEvents);

        // Handles Calendar
        if (startDate != null) {
            habitBundle.putString("startDateTimeZone", startDate.getTimeZone().getID());
            habitBundle.putLong("startDateTime", startDate.getTimeInMillis());
        } else {
            habitBundle.putString("startDateTimeZone", null);
        }

        // Handles Best Streak Calendar
        if (bestStreakDate != null) {
            habitBundle.putString("bestStreakDateTimeZone", bestStreakDate.getTimeZone().getID());
            habitBundle.putLong("bestStreakDateTime", bestStreakDate.getTimeInMillis());
        } else {
            habitBundle.putString("bestStreakDateTimeZone", null);
        }
        // Handles Current Streak Calendar
        if (currentStreakDate != null) {
            habitBundle.putString("currentStreakDateTimeZone", currentStreakDate.getTimeZone().getID());
            habitBundle.putLong("currentStreakDateTime", currentStreakDate.getTimeInMillis());
        } else {
            habitBundle.putString("currentStreakDateTimeZone", null);
        }

        // Handles Current Streak End Calendar
        if (currentStreakDateEnd != null) {
            habitBundle.putString("currentStreakDateEndTimeZone", currentStreakDateEnd.getTimeZone().getID());
            habitBundle.putLong("currentStreakDateEndTime", currentStreakDateEnd.getTimeInMillis());
        } else {
            habitBundle.putString("currentStreakDateEndTimeZone", null);
        }

        // Handles Best Streak End Calendar
        if (bestStreakDateEnd != null) {
            habitBundle.putString("bestStreakDateEndTimeZone", bestStreakDateEnd.getTimeZone().getID());
            habitBundle.putLong("bestStreakDateEndTime", bestStreakDateEnd.getTimeInMillis());
        } else {
            habitBundle.putString("bestStreakDateEndTimeZone", null);
        }

        // Streak Ints
        habitBundle.putInt("currentStreak", currentStreak);
        habitBundle.putInt("bestStreak", bestStreak);

        habitBundle.putParcelable("onDaysObj", onDaysObj);

        habitBundle.putBoolean("isPublic", isPublic);

        parcel.writeBundle(habitBundle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Calendar getBestStreakDate() {
        return bestStreakDate;
    }

    public void setBestStreakDate(Calendar bestStreakDate) {
        this.bestStreakDate = bestStreakDate;
    }

    public Calendar getCurrentStreakDate() {
        return currentStreakDate;
    }

    public void setCurrentStreakDate(Calendar currentStreakDate) {
        this.currentStreakDate = currentStreakDate;
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