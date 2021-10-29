package com.cmput301f21t34.habittrak.user;

import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Habit_Event
 *
 *
 * @author Dakota
 *
 * Habit object that a user wants to track
 *
 * @version 1.0
 * @since 2021-10-16
 * @see Habit
 */
public class Habit_Event implements Comparable<Habit_Event>, Parcelable {

    // Attributes //

    // Any changes need to be implement in writeToParcel and Parcel constructor - Dakota

    @Deprecated
    private String habitEventId; // Not sure if this is needed
    private String comment;
    @Deprecated
    private Habit habit; // Not sure if this is needed since Habit_Events must belong in a Habit
    private Calendar completedDate;
    private Location location;
    private File photograph;

    // redundant constructor
    @Deprecated
    public Habit_Event() {
        this.comment= "";
        this.completedDate = Calendar.getInstance();
        this.habit = new Habit();
        this.location = new Location("");
        this.photograph = new File("");
        this.habitEventId = "";

    }
    public Habit_Event(String habitEventId, String comment, Calendar date, Habit habit, Location loc, File photo){
        this.photograph = photo;
        this.location = loc;
        this.habit = habit;
        this.comment = comment;
        this.completedDate = date;
        this.habitEventId = habitEventId;
    }


    /**
     * Parcel Constructor Class
     *
     * Constructs Habit_Event from a parcel
     * Un-does writeToParcel method
     *
     * @author Dakota
     * @see Parcelable
     * @param parcel Parcel to construct from
     */
    public Habit_Event(Parcel parcel){

        Bundle habitEventBundle;
        habitEventBundle = parcel.readBundle(Habit_Event.class.getClassLoader());

        this.habitEventId = habitEventBundle.getString("habitEventId");
        this.habit = habitEventBundle.getParcelable("habit");
        this.comment = habitEventBundle.getString("comment");

        String completedDateTimeZone = habitEventBundle.getString("completedDateTimeZone");
        if ( completedDateTimeZone != null) {

            Calendar constructionCalendar = Calendar.getInstance();
            constructionCalendar.setTimeZone(TimeZone.getTimeZone(completedDateTimeZone));
            constructionCalendar.setTimeInMillis(habitEventBundle.getLong("completedDateTime"));

            this.completedDate = constructionCalendar;
        } else {

            this.completedDate = null;
        }

        this.location = habitEventBundle.getParcelable("location");

        this.photograph = new File(habitEventBundle.getString("photograph"));


        }


    // getter methods

    public static final Parcelable.Creator<Habit_Event> CREATOR = new Parcelable.Creator<Habit_Event>() {
        @Override
        public Habit_Event createFromParcel(Parcel in) {
            return new Habit_Event(in);
        }

        @Override
        public Habit_Event[] newArray(int size) {
            return new Habit_Event[size];
        }
    };

    public String getHabitEventId() {
        return habitEventId;
    }

    public Habit getHabit() {
        return habit;
    }

    public Calendar getCompletedDate() {
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

    public void setCompletedDate(Calendar completedDate) {
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
        return this.completedDate.compareTo(habitEvent.completedDate);
    }

    // Parcelable Implementation Code  Start //

    /**
     * Code that parses Habit_Event parameters into a Parcel
     * Needs to be updated for any changes to attributes
     * Or new attributes added
     *
     * Used to pass through intents
     *
     * @author Dakota
     * @see Parcelable
     * @see Parcel
     * @see Bundle
     * @param out Parcel created
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {


        Bundle habitEventBundle = new Bundle(Habit_Event.class.getClassLoader());

        habitEventBundle.putString("habitEventId", habitEventId);
        habitEventBundle.putString("comment", comment);
        habitEventBundle.putParcelable("habit", habit);




        // Handles Calendar
        if (completedDate != null) {
            habitEventBundle.putString("completedDateTimeZone", completedDate.getTimeZone().getID());
            habitEventBundle.putLong("completedDateTime", completedDate.getTimeInMillis());

        } else {
            habitEventBundle.putString("completedDateTimeZone", null);
        }

        habitEventBundle.putParcelable("location", location);

        // Handles photograph
        habitEventBundle.putString("photograph", photograph.getPath());



        out.writeBundle(habitEventBundle);
    }




    @Override
    public int describeContents() {
        return 0;
    }

    private static class Creator implements Parcelable.Creator<Habit_Event> {
        public Habit_Event createFromParcel(Parcel source) {
            return new Habit_Event(source);
        }

        public Habit_Event[] newArray(int size) {
            return new Habit_Event[size];
        }
    }

    // Parcelable Implementation Code  End //
}
