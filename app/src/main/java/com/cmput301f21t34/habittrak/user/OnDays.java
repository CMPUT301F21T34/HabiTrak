package com.cmput301f21t34.habittrak.user;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;

/** On_Days
 *
 * On_Days object for representing which weekdays a habit is on
 *
 * @author Dakota
 * @since 2021-10-25
 * @version 1
 *
 */
public class OnDays implements Parcelable {

    private boolean mon, tue, wed, thu, fri, sat, sun; // booleans for each day of the week

    // gets constants from Calendar class
    public final int MON = Calendar.MONDAY,
            TUE = Calendar.TUESDAY,
            WED = Calendar.WEDNESDAY,
            THU = Calendar.THURSDAY,
            FRI = Calendar.FRIDAY,
            SAT = Calendar.SATURDAY,
            SUN = Calendar.SUNDAY;

    // initializes On_Days by default to all true
    public OnDays() {
        this.mon = true;
        this.tue = true;
        this.wed = true;
        this.thu = true;
        this.fri = true;
        this.sat = true;
        this.sun = true;
    }

    /**
     * Construct an On_Days object from ArrayList<Boolean>
     * Needed for Database
     * @author Henry
     */
    public OnDays(ArrayList<Boolean> onDaysArray) {
        setAll(new boolean[] {
                onDaysArray.get(0),
                onDaysArray.get(1),
                onDaysArray.get(2),
                onDaysArray.get(3),
                onDaysArray.get(4),
                onDaysArray.get(5),
                onDaysArray.get(6),
        });
    }

    // Constructing from a parcel
    public OnDays(Parcel parcel) {
        Bundle onDaysBundle = parcel.readBundle(this.getClass().getClassLoader()); // get bundle
        boolean[] onDays = onDaysBundle.getBooleanArray("onDays");

        setAll(onDays);
    }

    /** get
     *
     * gets the boolean value of a particular day
     *
     * @author Dakota
     *
     * @throws IllegalArgumentException must use int constants (EX. MON)
     * @param day int day constant that you want to get (Ex. MON))
     * @return boolean val of particular day;
     */
    public boolean get(int day) {
        switch (day){
            case MON: return mon;
            case TUE: return tue;
            case WED: return wed;
            case THU: return thu;
            case FRI: return fri;
            case SAT: return sat;
            case SUN: return sun;
            default: throw new IllegalArgumentException("must use Calendar int constant object " +
                    "with On_Days.get(). \nEx. Calendar.MONDAY");
        }
    }

    /** setTrue
     *
     * sets a particular day true
     *
     * @author Dakota
     *
     * @throws IllegalArgumentException must use int constants (EX. MON)
     * @param day int day constant that you want to set true (Ex. MON))
     */
    public void setTrue(int day) {
        set(day, true);
    }

    /** setFalse
     *
     * sets a particular day false
     *
     * @author Dakota
     *
     * @throws IllegalArgumentException must use int constants (EX. MON)
     * @param day int day constant that you want to set false (Ex. MON))
     */
    public void setFalse(int day) {
        set(day, false);
    }

    /**
     * sets a given day to a given boolean
     *
     * @author Dakota
     * @param day int day to be changed
     * @param bool boolean to change it to
     */
    public void set(int day, boolean bool) {
        switch (day){
            case MON: mon = bool; break;
            case TUE: tue = bool; break;
            case WED: wed = bool; break;
            case THU: thu = bool; break;
            case FRI: fri = bool; break;
            case SAT: sat = bool; break;
            case SUN: sun = bool; break;
            default: throw new IllegalArgumentException("must use Calendar int constant object " +
                    "with On_Days.setTrue(). \nEx. Calendar.MONDAY");
        }
    }

    /** getAll
     *
     * gets an array of all days
     *
     * @author Dakota
     *
     * @param startOfWeek int constant day you want for the start of the week
     * @return boolean[7] of the days
     */
    public boolean[] getAll(int startOfWeek) {
        boolean[] allDays = getAll();

        // + 1 shifts the array to the left
        allDays = shift(allDays, getShift(startOfWeek), + 1);
        return allDays;

    }

    /** getAll
     *
     * gets an array of all days starting on Monday
     *
     * @author Dakota
     *
     * @return boolean[7] of the days starting on Monday
     */
    public boolean[] getAll() {
        return new boolean[] {
                get(MON),
                get(TUE),
                get(WED),
                get(THU),
                get(FRI),
                get(SAT),
                get(SUN),
        };
    }

    /** setAll
     *
     * sets the days On_Day is on
     *
     * @author Dakota
     *
     * @param array boolean[7] array of values to set
     * @param startOfWeek int constant day that array starts on Ex. MONDAY
     * @throws IllegalArgumentException array boolean[] must be of boolean[7]
     */
    public void setAll(boolean[] array, int startOfWeek ) {
        if (array.length != 7) {
            throw new IllegalArgumentException("boolean[] argument of setAll must be of length 7");
        }

        boolean[] allDays;

        // - 1 shifts the array to the right
        allDays = shift(array, getShift(startOfWeek), - 1);

        setAll(allDays);
    }

    /** setAll
     *
     * sets the days On_Day with an array starting on Monday
     *
     * @author Dakota
     * @param onDays boolean[7] array of values to set
     */
    public void setAll(boolean[] onDays) {
        set(MON, onDays[0]);
        set(TUE, onDays[1]);
        set(WED, onDays[2]);
        set(THU, onDays[3]);
        set(FRI, onDays[4]);
        set(SAT, onDays[5]);
        set(SUN, onDays[6]);
    }

    /** isOnDay
     *
     * returns if an On_Day object is on today
     *
     * @author Dakota
     * @return boolean true if On_Day is on today
     * @throws IllegalStateException impossible state to reach, default switch case.
     */
    public boolean isOnDay() {
        // Calls isOnDay with today's date
        return isOnDay(Calendar.getInstance());
    }

    /**
     * returns if it is an OnDay for a particular date
     * @param date Calendar that we want to check
     * @return boolean true if it is an On Day, false elsewise
     * @throws IllegalStateException impossible state to reach, default switch case.
     */
    public boolean isOnDay(Calendar date) {
        date.setFirstDayOfWeek(Calendar.SUNDAY);
        int currentDayOfWeek = date.get(Calendar.DAY_OF_WEEK);

        System.out.println(String.valueOf(currentDayOfWeek) + " mon: " + String.valueOf(MON - 1));

        switch (currentDayOfWeek){
            case MON + 3: return get(MON);
            case TUE + 3: return get(TUE);
            case WED + 3: return get(WED);
            case THU - 4: return get(THU);
            case FRI - 4: return get(FRI);
            case SAT - 4: return get(SAT);
            case SUN + 3: return get(SUN);
            default: throw new IllegalStateException("Current day does not exist!?!");

        }
    }

    /**
     * gets the amount a boolean array needs to be shifted to change its start
     * day from monday to startOfWeek
     *
     * @author Dakota
     * @param startOfWeek Int Calendar constant of startOfWeek to shift to
     * @return int amount to shift
     */
    private int getShift(int startOfWeek) {

        int shift = 0;

        switch (startOfWeek) {
            case MON:
                break; // If monday then no need to shift
            case SUN:
                shift++; // shift 6 to the left
            case SAT:
                shift++; // shift 5 to the left
            case FRI:
                shift++; //   .
            case THU:
                shift++; //   .
            case WED:
                shift++; //   .
            case TUE:
                shift++; // shift 1 to the left
                break;
            default:
                throw new IllegalArgumentException("must use Calendar day constant " +
                        "with On_Days.getAll()" +
                        "\nEx. Calendar.MONDAY.");

        }

        return shift;

    }

    /**
     * shifts a boolean array left or right
     *
     * @author Dakota
     * @param array boolean[] to be shifted
     * @param shift amount to shift
     * @param direction +1 to shift left, -1 to shift right
     * @return boolean[] that has been shifted
     */
    private boolean[] shift(boolean[] array, int shift, int direction) {

        if ( direction != -1 && direction != 1) {
            throw new IllegalArgumentException(
                    "direction must be -1 or 1! Given was: " + String.valueOf(direction)
            );
        }

        if (array == null) {
            throw new NullPointerException("array passed to shiftLeft is null");
        } else if (shift >= array.length) {
            throw new IllegalArgumentException("shift passed to shiftLeft is greater then array length");
        }

        int maxIndex = array.length - 1;

        // for each time we want to shift it
        for (;shift > 0; shift--) {
            // for each pair of [index] [index - 1] swap
            for (int index = 0; index <= maxIndex; index++) {
                boolean swap = array[index];
                if (index == 0){ // if we are at index 0, loop back to max index
                    array[index] = array[maxIndex];
                    array[maxIndex] = swap;
                } else {
                    array[index] = array[index + direction];
                    array[index + direction] = swap;
                }
            }
        }
        return array;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        Bundle onDaysBundle = new Bundle(this.getClass().getClassLoader()); // Create a new bundle
        onDaysBundle.putBooleanArray // Writes the values as a bool array
                ("onDays",new boolean[]{mon, tue, wed, thu, fri, sat, sun});
        parcel.writeBundle(onDaysBundle); // Writes to parcel
    }

    // Creates User from parcel
    public static final Parcelable.Creator<OnDays> CREATOR = new Parcelable.Creator<OnDays>() {
        @Override
        public OnDays createFromParcel(Parcel in) {

            return new OnDays(in);
        }
        @Override
        public OnDays[] newArray(int size) {

            return new OnDays[size];
        }
    };
}

