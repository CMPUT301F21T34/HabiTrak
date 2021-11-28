package com.cmput301f21t34.habittrak.streak;

import java.util.Calendar;
import java.util.Comparator;


/**
 * TimeIgnoringComparator
 *
 * Compares two calendar dates, by the day not the hour or minute.
 * @author Dakota
 * @version 1.0
 * @since 2021-11-23
 * @see //https://stackoverflow.com/questions/1439779/how-to-compare-two-dates-without-the-time-portion
 */

/**
 * Compares two Calendars excluding time
 */
public class TimeIgnoringComparator implements Comparator<Calendar> {
    public int compare(Calendar c1, Calendar c2) {
        if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR)) // Compares Years
            return c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        if (c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH)) // Compares Months
            return c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        return c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH); // Compares Days
    }
}