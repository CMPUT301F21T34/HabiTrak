package com.cmput301f21t34.habittrak;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import android.location.Location;
import android.net.Uri;

import com.cmput301f21t34.habittrak.streak.Streak;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitEvent;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Class for testing Habit Objects
 *
 * @author Henry
 * @author Dakota
 * @version 1.0
 * @since 2021-10-22
 * @see Habit
 * @see HabitEvent
 */
public class HabitTest {

    /**
     * mockHabit
     *
     * @return Habit
     * Returns a new Habit object to perform tests on
     */
    private Habit mockHabit() {
        String title = "Read book";
        String reason = "Book good";
        // do this on Monday, Wednesday and Friday
        boolean[] onDays = {true, false, true, false, true, false, false};
        // use specific start date for future testing
        Calendar startDate = new GregorianCalendar(2021, 1, 1);
        Habit mockHabit = new Habit(title, reason, startDate);
        mockHabit.getOnDaysObj().setAll(onDays);
        return mockHabit;
    }

    /**
     * isOnDayTest
     *
     * Tests if isOnDay() correctly compares today and the day(s) of the week to complete a habit
     */
    @Test
    public void isOnDayTest() {
        Habit habit = mockHabit();
        Calendar cal = Calendar.getInstance();
        boolean flag = false;

        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY ||
                cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY ||
                cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
            flag = true;
        // flag and isOnDay() should both return true if today is Monday, Wednesday or Friday
        // and both return false otherwise

        // Notes: Change Calendar.MONDAY/WEDNESDAY/FRIDAY to desired week day if onDays changes
        assertEquals(flag,habit.getOnDaysObj().isOnDay());
    }

    /**
     * addHabitEventTest
     *
     * Tests if habit_event is properly added to habit.habitEvents
     */
    @Test
    public void addHabitEventTest() {
        Habit habit = mockHabit();
        HabitEvent event1 = new HabitEvent();
        Location loc = new Location("");
        Uri photo = null;
        Calendar completeDate = new GregorianCalendar(2021, 6, 1);

        HabitEvent event2 = new HabitEvent("event2", completeDate, loc, photo);


        // Check size
        assertEquals(0, habit.getHabitEvents().size());
        habit.addHabitEvent(event1);
        assertEquals(1, habit.getHabitEvents().size());
        habit.addHabitEvent(event2);
        assertEquals(2, habit.getHabitEvents().size());

        // Compare added habit events
        assertEquals(event1, habit.getHabitEvents().get(0));
        assertEquals(event2, habit.getHabitEvents().get(1));
    }

    /**
     * addHabitEventTest
     *
     * Tests if habit_event is properly removed from habit.habitEvents
     */
    @Test
    public void removeHabitEventTest() {
        Habit habit = mockHabit();
        HabitEvent event1 = new HabitEvent();
        Location loc = new Location("");
        Uri photo = null;
        Calendar completeDate = new GregorianCalendar(2021, 6, 1);
        HabitEvent event2 = new HabitEvent("comment",
                completeDate, loc, photo);

        // Check size and add habit_event to habit's habit_event list
        assertEquals(0, habit.getHabitEvents().size());
        assertTrue(habit.addHabitEvent(event1));
        assertEquals(1, habit.getHabitEvents().size());
        assertTrue(habit.addHabitEvent(event2));
        assertEquals(2, habit.getHabitEvents().size());

        // Remove and check size again
        // remove by id (deprecated)
        assertTrue(habit.removeHabitEvent(event1));
        assertEquals(1, habit.getHabitEvents().size());
        // since event 1 is removed, event at index 0 should be event 2
        assertEquals(event2, habit.getHabitEvents().get(0)); // compare id
        // remove by habit_event object
        assertTrue(habit.removeHabitEvent(event2));
        assertEquals(0, habit.getHabitEvents().size());
    }

    /**
     * habitStreakTest
     *
     * Tests the streak implementation for habit objects
     */
    @Test
    public void habitStreakTest() {
        System.out.println("habitStreak");
        // Init habit
        Habit habit = mockHabit();
        habit.getOnDaysObj().setAll(new boolean[]{
                false,
                true,
                false,
                false,
                true,
                false,
                false
        });

        Calendar startDay  = Calendar.getInstance();
        startDay.set(2020, 12, 28);
        habit.setStartDate(startDay);

        // Defaults
        Location loc = new Location("");
        System.out.println(":Creating Events");
        // Create some events

        Calendar event0Day = Calendar.getInstance();
        event0Day.set(2020, 12, 29);

        HabitEvent event0 = new HabitEvent("event0", event0Day, loc, null); // null photo since URI is not mocked

        Calendar event1Day = Calendar.getInstance();
        event1Day.set(2021, 1, 1);

        HabitEvent event1 = new HabitEvent("event1", event1Day, loc, null);

        Calendar event2Day = Calendar.getInstance();
        event2Day.set(2021, 1, 5);

        HabitEvent event2 = new HabitEvent("event2", event2Day, loc, null);

        Calendar event3Day = Calendar.getInstance();
        event3Day.set(2021, 1, 8);

        HabitEvent event3 = new HabitEvent("event3", event3Day, loc, null);

        // Add Events //
        System.out.println(":Adding Events");

        habit.addHabitEvent(event0); habit.addHabitEvent(event1); habit.addHabitEvent(event2); habit.addHabitEvent(event3);

        System.out.println(":Testing Events");

        // Asserts //
        assertEquals(0, habit.getCurrentStreak());

        // Refresh streak //
        Calendar testDay = Calendar.getInstance();
        testDay.set(2021, 1, 10);
        habit.setCurrentStreakDateEnd(testDay);

        Streak streak = new Streak(habit);
        streak.refreshStreak();

        // Asserts //
        assertEquals(4, habit.getCurrentStreak());
        assertEquals(4, habit.getBestStreak());
        assertEquals(29, habit.getCurrentStreakDate().get(Calendar.DATE));

        // Removes middle event //
        habit.removeHabitEvent(event2);

        // Asserts //
        streak.refreshStreak();
        assertEquals(1, habit.getCurrentStreak());
        assertEquals(4, habit.getBestStreak());
        assertEquals(29, habit.getBestStreakDate().get(Calendar.DATE));
        assertEquals(8, habit.getBestStreakDateEnd().get(Calendar.DATE));

        // Removes latest event //
        habit.removeHabitEvent(event3);

        streak.refreshStreak();

        // Asserts //
        assertEquals(0, habit.getCurrentStreak());
    }
}
