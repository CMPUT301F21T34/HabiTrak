package com.cmput301f21t34.habittrak;

import org.junit.Test;

import static org.junit.Assert.*;

import android.location.Location;
import android.os.Parcel;
import android.util.Log;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Class for testing Parcelable Objects
 *
 * @author Dakota
 * @author Henry
 * @version 1.0
 * @since 2021-10-22
 * @see android.os.Parcelable
 * @see User
 * @see Habit
 * @see Habit_Event
 */
// TODO: Assert similarity for location and file
public class ParcelableUnitTest {

    // Testing Parcelability for User

    /**
     * Tests if {@link User} is properly parceled and unpacked
     *
     * @author Dakota
     */
    @Test
    public void testParcelableUser(){


        User testUser = getTestUser();

        Parcel testParcel = Parcel.obtain(); // init a parcel
        testUser.writeToParcel(testParcel, 0); // populate testParcel

        testParcel.setDataPosition(0); // reset data position after writing to read

        User parceledUser = new User(testParcel); // construct from parcel

        // Asserts Similarity
        assertEquals(testUser.getUsername(), parceledUser.getUsername());
        assertEquals(testUser.getHabitList().get(0).getTitle(),
                parceledUser.getHabitList().get(0).getTitle()); // Compares Title

        // Since they are not the same object we need to compare their actual attributes
        assertEquals(testUser.getHabitList().get(1).getStartDate(),
                parceledUser.getHabitList().get(1).getStartDate()); // Compares Dates

    }

    /**
     * getTestUser
     *
     * helper class that creates a user for testing
     * @return User for testing
     */
    private User getTestUser(){

        return new User("testUser",
                getTestHabitList(),
                null,
                null,
                null,
                null);

    }

    /**
     * getTestHabitList
     *
     * helper class that creates a habit list for testing
     * @return ArrayList\<Habit\> for testing
     */
    private ArrayList<Habit> getTestHabitList(){
        boolean[] onDays = new boolean[]{false, false, false, false, false, false, false};
        ArrayList<Habit> habitList = new ArrayList<>();
        habitList.add(new Habit("hab1"));
        habitList.add(new Habit("hab2", "reason", Calendar.getInstance(), onDays));

        return  habitList;

    }

    // Testing Parcelability for Habit

    @Test
    public void testParcelableHabit(){

        boolean[] onDays = new boolean[]{true, false, false, false, false, false, false};
        Habit testHabit = new Habit("title", "reason", Calendar.getInstance(), onDays);
        Habit_Event event = new Habit_Event();
        testHabit.addHabitEvent(event);

        // Create empty parcels to populate
        Parcel testParcel = Parcel.obtain();

        // Populate testParcel
        testHabit.writeToParcel(testParcel, 0);
        testParcel.setDataPosition(0); // reset data position after writing to read

        Habit parceledHabit = new Habit(testParcel); // construct from parcel

        // Asserts Similarity
        assertEquals(testHabit.getTitle(), parceledHabit.getTitle()); // compare title
        assertEquals(testHabit.getReason(), parceledHabit.getReason()); // compare reason
        assertEquals(testHabit.getStartDate(), parceledHabit.getStartDate()); // compare startDate
        assertArrayEquals(testHabit.getOnDays(), parceledHabit.getOnDays()); // compare onDays

        // Similarity for habit event list in testHabit
        assertEquals(testHabit.getHabitEvents().get(0).getHabitEventId(), parceledHabit.getHabitEvents().get(0).getHabitEventId());
        assertEquals(testHabit.getHabitEvents().get(0).getComment(), parceledHabit.getHabitEvents().get(0).getComment());
        assertEquals(testHabit.getHabitEvents().get(0).getCompletedDate(), parceledHabit.getHabitEvents().get(0).getCompletedDate());
    }

    // Testing Parcelability for Habit_Event

    @Test
    public void testParcelableHabitEvent(){

        Habit_Event testHabitEvent = new Habit_Event();
        // Create empty parcels to populate
        Parcel testParcel = Parcel.obtain();

        // Populate testParcel
        testHabitEvent.writeToParcel(testParcel, 0);
        testParcel.setDataPosition(0); // reset data position after writing to read

        Habit_Event parceledHabitEvent = new Habit_Event(testParcel); // construct from parcel

        // Asserts Similarity
        assertEquals(testHabitEvent.getHabitEventId(), parceledHabitEvent.getHabitEventId());
        assertEquals(testHabitEvent.getComment(), parceledHabitEvent.getComment());
        assertEquals(testHabitEvent.getCompletedDate(), parceledHabitEvent.getCompletedDate());
    }
}
