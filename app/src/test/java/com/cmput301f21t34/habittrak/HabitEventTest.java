package com.cmput301f21t34.habittrak;

import android.location.Location;

import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.Habit_Event;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Class for testing Habit_Event Objects
 *
 * @author Henry
 * @version 1.0
 * @since 2021-10-22
 * @see Habit
 * @see Habit_Event
 */
// TODO: Used for future testing if more methods (apart from getters and setters) are added to Habit_Event

public class HabitEventTest {
    // No tests for getters or setters

    /**
     * mockHabitEvent()
     *
     * @author Henry
     *
     * @return Habit
     * Returns a new Habit object to perform tests on
     */
    private Habit_Event mockHabitEvent() {
        String id = "173";
        String comment = "I read book 1";
        Habit habit = new Habit("Read Book");
        Location loc = new Location("");
        File photo = new File("");
        Calendar completeDate = new GregorianCalendar(2021, 6, 1);
        Habit_Event mockHabitEvent = new Habit_Event(id, comment, completeDate, habit, loc, photo);
        return mockHabitEvent;
    }
}
