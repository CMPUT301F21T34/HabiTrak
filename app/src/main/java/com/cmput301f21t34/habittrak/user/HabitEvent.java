package com.cmput301f21t34.habittrak.user;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.cmput301f21t34.habittrak.Utilities;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * HabitEvent
 *
 *
 * @author Dakota
 *
 * Habit object that a user wants to track
 *
 * @version 2.0
 * @since 2021-10-16
 * @see Habit
 */
public class HabitEvent implements Comparable<HabitEvent>, Parcelable, Utilities {

    // Attributes //

    // Any changes need to be implement in writeToParcel and Parcel constructor - Dakota
    private String comment;

    private Calendar completedDate;
    private Location location;
    private String photograph;

    public HabitEvent() {
        this.comment= "";
        this.completedDate = Calendar.getInstance();
        this.location = null;
        this.photograph = null;
    }

    public HabitEvent(String comment, Calendar date, Location loc, Uri photo){
        // Null check on photo (allows it to be null for testing purposes)
        if (photo == null){
            this.photograph = null;
        } else {
            this.photograph = photo.toString();
        }

        this.location = loc;
        this.comment = comment;
        this.completedDate = date;
    }

    /**
     * Parcel Constructor Class
     *
     * Constructs HabitEvent from a parcel
     * Un-does writeToParcel method
     *
     * @author Dakota
     * @see Parcelable
     * @param parcel Parcel to construct from
     */
    public HabitEvent(Parcel parcel){
        Bundle habitEventBundle;
        habitEventBundle = parcel.readBundle(HabitEvent.class.getClassLoader());

        this.comment = habitEventBundle.getString("comment");

        this.completedDate = calendarParcelConstructor(habitEventBundle, "completedDate");


        Location parcelLocation = habitEventBundle.getParcelable("location");
        if (parcelLocation != null) {
            this.location = habitEventBundle.getParcelable("location");
        } else {
            this.location = null;
        }

        // Sets path
        String photographPath = habitEventBundle.getString("photograph");
        if (photographPath != null) {
            this.photograph = habitEventBundle.getString("photograph");
        } else {
            this.photograph = null;
        }
    }

    // getter methods
    public static final Parcelable.Creator<HabitEvent> CREATOR = new Parcelable.Creator<HabitEvent>() {
        @Override
        public HabitEvent createFromParcel(Parcel in) {
            return new HabitEvent(in);
        }

        @Override
        public HabitEvent[] newArray(int size) {
            return new HabitEvent[size];
        }
    };

    public Calendar getCompletedDate() {
        return completedDate;
    }

    public String getPhotograph() {
        return this.photograph;
    }

    public Location getLocation() {
        return location;
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCompletedDate(Calendar completedDate) {
        this.completedDate = completedDate;
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public void setPhotograph(Uri photograph) {
        if (photograph != null) {
            this.photograph = photograph.toString();
        }
        else {
            this.photograph = null;
        }
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
    public int compareTo(HabitEvent habitEvent) {
        return -this.completedDate.compareTo(habitEvent.completedDate);
    }

    // Parcelable Implementation Code  Start //
    /**
     * Code that parses HabitEvent parameters into a Parcel
     * Needs to be updated for any changes to attributes
     * Or new attributes added
     *
     * Used to pass through intents
     *
     * @author Dakota
     * @see Parcelable
     * @see Parcel
     * @see Bundle
     * @param parcel Parcel created
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        Bundle habitEventBundle = new Bundle(HabitEvent.class.getClassLoader());

        habitEventBundle.putString("comment", comment);

        putCalendarInBundle(habitEventBundle, completedDate, "completedDate");

        if (location != null) {
            habitEventBundle.putParcelable("location", location);
        } else {
            habitEventBundle.putParcelable("location", null);
        }

        if (photograph != null) {
            String photographpath = photograph;
            Log.d("EDIT_HAb","the path is "+photographpath);

            // Handles photograph
            habitEventBundle.putString("photograph", photographpath);
        } else {
            habitEventBundle.putString("photograph", null);
        }
        parcel.writeBundle(habitEventBundle);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    private static class Creator implements Parcelable.Creator<HabitEvent> {
        public HabitEvent createFromParcel(Parcel source) {
            return new HabitEvent(source);
        }

        public HabitEvent[] newArray(int size) {
            return new HabitEvent[size];
        }
    }
    // Parcelable Implementation Code  End //
}