package com.cmput301f21t34.habittrak.user;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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


public class On_Days implements Parcelable {



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
    public On_Days(){
        this.mon = true;
        this.tue = true;
        this.wed = true;
        this.thu = true;
        this.fri = true;
        this.sat = true;
        this.sun = true;
    }

    // Constructing from a parcel
    public On_Days(Parcel parcel){

        Bundle onDaysBundle = parcel.readBundle(this.getClass().getClassLoader()); // get bundle
        boolean[] onDays = onDaysBundle.getBooleanArray("onDays");

        this.mon = onDays[0];
        this.tue = onDays[1];
        this.wed = onDays[2];
        this.thu = onDays[3];
        this.fri = onDays[4];
        this.sat = onDays[5];
        this.sun = onDays[6];

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
    public boolean get(int day){
        Log.d("On_Days", "int passed: " + String.valueOf(day));

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
    public void setTrue(int day){
        Log.d("On_Days", "int passed: " + String.valueOf(day)
        + "int expected (for monday): " + String.valueOf(MON));

        switch (day){
            case MON: mon = true; break;
            case TUE: tue = true; break;
            case WED: wed = true; break;
            case THU: thu = true; break;
            case FRI: fri = true; break;
            case SAT: sat = true; break;
            case SUN: sun = true; break;
            default: throw new IllegalArgumentException("must use Calendar int constant object " +
                "with On_Days.setTrue(). \nEx. Calendar.MONDAY");
        }


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
    public void setFalse(int day){

        switch (day){
            case MON: mon = false; break;
            case TUE: tue = false; break;
            case WED: wed = false; break;
            case THU: thu = false; break;
            case FRI: fri = false; break;
            case SAT: sat = false; break;
            case SUN: sun = false; break;
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
    public boolean[] getAll(int startOfWeek){

        boolean[] allDays = new boolean[]{get(MON), get(TUE), get(WED), get(THU), get(FRI), get(SAT), get(SUN)};
        int shift = 0;

        switch (startOfWeek){
            case MON: break; // If monday then no need to shift
            case SUN: shift++; // shift 6 to the left
            case SAT: shift++; // shift 5 to the left
            case FRI: shift++; //   .
            case THU: shift++; //   .
            case WED: shift++; //   .
            case TUE: shift++; // shift 1 to the left
                shiftLeft(allDays, shift); // execute shift
                break;
            default: throw new IllegalArgumentException("must use int day constant " +
                    "with On_Days.getAll()" +
                    "\nEx. MONDAY");

        }

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
    public boolean[] getAll(){

        return getAll(MON);

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
    public void setAll(boolean[] array, int startOfWeek ){

        if (array.length != 7){
            throw new IllegalArgumentException("boolean[] argument of setAll must be of length 7");
        }

        int shift = 0;
        boolean[] allDays = array;

        // Makes sure array starts on monday for handling
        switch (startOfWeek) {
            case MON:
                break; // If monday then no need to shift
            case SUN: shift++; // shift 6 to the left
            case SAT: shift++; // shift 5 to the left
            case FRI: shift++; //   .
            case THU: shift++; //   .
            case WED: shift++; //   .
            case TUE: shift++; // shift 1 to the left
                allDays = shiftRight(array, shift); // execute shift
                break;
            default:
                throw new IllegalArgumentException("must use Calendar day constant " +
                        "with On_Days.getAll()" +
                        "\nEx. Calendar.MONDAY.");
        }

        this.mon = allDays[0];
        this.tue = allDays[1];
        this.wed = allDays[2];
        this.thu = allDays[3];
        this.fri = allDays[4];
        this.sat = allDays[5];
        this.sun = allDays[6];

    }

    /** setAll
     *
     * sets the days On_Day with an array starting on Monday
     *
     * @author Dakota
     *
     * @param array boolean[7] array of values to set
     */
    public void setAll(boolean[] array){
        setAll(array, MON);
    }

    /** isOnDay
     *
     * returns if an On_Day object is on today
     *
     * @return boolean true if On_Day is on today
     * @throws IllegalStateException impossible state to reach, default switch case.
     */
    public boolean isOnDay(){

        int currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        switch (currentDayOfWeek){
            case MON: return get(MON);
            case TUE: return get(TUE);
            case WED: return get(WED);
            case THU: return get(THU);
            case FRI: return get(FRI);
            case SAT: return get(SAT);
            case SUN: return get(SUN);
            default: throw new IllegalStateException("Current day does not exist!?!");

        }


    }

    /**
     * shiftLeft
     *
     * shifts a boolean array to the left
     *
     * @author Dakota
     *
     * @throws NullPointerException array can't be null
     * @throws IllegalArgumentException shift can't be greater than array length
     * @param array the array to be shifted
     * @param shift the amount of times to shift to the left
     * @return boolean[] shifted
     */
    private boolean[] shiftLeft(boolean[] array, int shift){
        // Worst case O(n^2) Best case O(n) pretty slow

        if (array == null){
            throw new NullPointerException("array passed to shiftLeft is null");
        } else if (shift >= array.length) {
            throw new IllegalArgumentException("shift passed to shiftLeft is greater then array length");
        }

        int maxIndex = array.length - 1;

        // for each time we want to shift it
        for (;shift > 0; shift--){

            // for each pair of [index] [index + 1] swap
            for (int index = 0; index <= maxIndex; index++){

                boolean swap = array[index];

                if (index == maxIndex){ // if we are at max index, loop back to index 0
                    array[index] = array[0];
                    array[0] = swap;
                } else {
                    array[index] = array[index + 1];
                    array[index + 1] = swap;
                }

            }
        }

        return array;

    }

    /**
     * shiftRight
     *
     * shifts a boolean array to the right
     *
     * @author Dakota
     *
     * @throws NullPointerException array can't be null
     * @throws IllegalArgumentException shift can't be greater than array length
     * @param array the array to be shifted
     * @param shift the amount of times to shift to the right
     * @return boolean[] shifted
     */
    private boolean[] shiftRight(boolean[] array, int shift){
        // Worst case O(n^2) Best case O(n) pretty slow

        if (array == null){
            throw new NullPointerException("array passed to shiftLeft is null");
        } else if (shift >= array.length) {
            throw new IllegalArgumentException("shift passed to shiftLeft is greater then array length");
        }

        int maxIndex = array.length - 1;

        // for each time we want to shift it
        for (;shift > 0; shift--){

            // for each pair of [index] [index - 1] swap
            for (int index = 0; index <= maxIndex; index++){

                boolean swap = array[index];

                if (index == 0){ // if we are at index 0, loop back to max index
                    array[index] = array[maxIndex];
                    array[maxIndex] = swap;
                } else {
                    array[index] = array[index - 1];
                    array[index - 1] = swap;
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
        onDaysBundle.putBooleanArray // Writes the vals as a bool array
                ("onDays",new boolean[]{mon, tue, wed, thu, fri, sat, sun});
        parcel.writeBundle(onDaysBundle); // Writes to parcel

    }

    // Creates User from parcel
    public static final Parcelable.Creator<On_Days> CREATOR = new Parcelable.Creator<On_Days>() {

        @Override
        public On_Days createFromParcel(Parcel in) {

            return new On_Days(in);
        }

        @Override
        public On_Days[] newArray(int size) {

            return new On_Days[size];
        }
    };
}


