package com.cmput301f21t34.habittrak;

import android.location.Location;
import android.net.Uri;

import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitEvent;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Class for testing HabitEvent Objects
 *
 * @author Henry
 * @version 1.0
 * @since 2021-10-22
 * @see Habit
 * @see HabitEvent
 */
public class HabitEventTest {
    /**
     * mockHabitEvent()
     *
     * @author Henry
     *
     * @return Habit
     * Returns a new Habit object to perform tests on
     */
    private HabitEvent mockHabitEvent() {
        String id = "173";
        String comment = "I read book 1";
        Habit habit = new Habit("Read Book");
        Location loc = new Location("");
        Uri photo = null;
        Calendar completeDate = new GregorianCalendar(2021, 6, 1);
        HabitEvent mockHabitEvent = new HabitEvent(comment, completeDate, loc, photo);
        return mockHabitEvent;
    }
}
