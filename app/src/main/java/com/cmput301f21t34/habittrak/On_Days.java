package com.cmput301f21t34.habittrak;

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

//TODO, implement parcelable
public class On_Days {



    private boolean mon, tue, wed, thu, fri, sat, sun; // booleans for each day of the week

    // gets constants from Calendar class
    private final int MON = Calendar.MONDAY,
            TUE = Calendar.TUESDAY,
            WED = Calendar.WEDNESDAY,
            THU = Calendar.THURSDAY,
            FRI = Calendar.FRIDAY,
            SAT = Calendar.SATURDAY,
            SUN = Calendar.SUNDAY;


    // initializes On_Days by default to all true
    On_Days(){
        this.mon = true;
        this.tue = true;
        this.wed = true;
        this.thu = true;
        this.fri = true;
        this.sat = true;
        this.sun = true;
    }


    /** get
     *
     * gets the boolean value of a particular day
     *
     * @author Dakota
     *
     * @throws IllegalArgumentException must use Day object
     * @param day Day.DAY that you want to get (Ex. Day.MON)
     * @return boolean val of particular day;
     */
    public boolean get(int day){

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
     * @throws IllegalArgumentException must use Day object
     * @param day Day.DAY that you want to set true (Ex. Day.MON)
     */
    public void setTrue(int day){

        switch (day){
            case MON: mon = true;
            case TUE: tue = true;
            case WED: wed = true;
            case THU: thu = true;
            case FRI: fri = true;
            case SAT: sat = true;
            case SUN: sun = true;
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
     * @throws IllegalArgumentException must use Day object
     * @param day Day.DAY that you want to set false (Ex. Day.MON)
     */
    public void setFalse(int day){

        switch (day){
            case MON: mon = true;
            case TUE: tue = true;
            case WED: wed = true;
            case THU: thu = true;
            case FRI: fri = true;
            case SAT: sat = true;
            case SUN: sun = true;
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
     * @param startOfWeek the day you want for the start of the week
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
            default: throw new IllegalArgumentException("must use Day enum object with On_Days.getAll()");

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



    public void setAll(boolean[] array, Day startOfWeek ){

        if (array.length != 7){
            throw new IllegalArgumentException("boolean[] argument of setAll must be of length 7");
        }

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



}


