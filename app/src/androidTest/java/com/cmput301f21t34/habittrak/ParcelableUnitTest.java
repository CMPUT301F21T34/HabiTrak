package com.cmput301f21t34.habittrak;

import org.junit.Test;

import static org.junit.Assert.*;

import android.os.Parcel;

import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitEvent;
import com.cmput301f21t34.habittrak.user.HabitList;
import com.cmput301f21t34.habittrak.user.OnDays;
import com.cmput301f21t34.habittrak.user.User;


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
 * @see HabitEvent
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
        User testUser = new User("DummyUser","dummy@email.com");

        HabitList testHabits = getTestHabitList();
        for (int index = 0; index < testHabits.size(); index++){
            testUser.getHabitList().add(testHabits.get(index));
        }
        return testUser;
    }

    /**
     * getTestHabitList
     *
     * helper class that creates a habit list for testing
     * @return ArrayList\<Habit\> for testing
     */
    private HabitList getTestHabitList(){
        boolean[] onDays = new boolean[]{false, false, false, false, false, false, false};
        HabitList habitList = new HabitList();
        habitList.add(new Habit("hab1"));
        habitList.add(new Habit("hab2","res2",Calendar.getInstance()));
        return habitList;

    }

    // Testing Parcelability for Habit
    /**
     * Tests if {@link Habit} is properly parceled and unpacked
     *
     * @author Henry
     */
    @Test
    public void testParcelableHabit(){

        boolean[] onDays = new boolean[]{true, false, false, false, false, false, false};
        Habit testHabit = new Habit("tit", "res", Calendar.getInstance());
        HabitEvent testEvent = new HabitEvent();
        testHabit.addHabitEvent(testEvent);

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
        assertArrayEquals(testHabit.getOnDaysObj().getAll(), parceledHabit.getOnDaysObj().getAll()); // compare onDays

        // Similarity for habit event list in testHabit
        assertEquals(testHabit.getHabitEvents().get(0).getComment(), parceledHabit.getHabitEvents().get(0).getComment());
        assertEquals(testHabit.getHabitEvents().get(0).getComment(), parceledHabit.getHabitEvents().get(0).getComment());
        assertEquals(testHabit.getHabitEvents().get(0).getCompletedDate(), parceledHabit.getHabitEvents().get(0).getCompletedDate());
    }

    // Testing Parcelability for HabitEvent
    /**
     * Tests if {@link HabitEvent} is properly parceled and unpacked
     */
    @Test
    public void testParcelableHabitEvent() {
        HabitEvent testHabitEvent = new HabitEvent();
        // Create empty parcels to populate
        Parcel testParcel = Parcel.obtain();

        // Populate testParcel
        testHabitEvent.writeToParcel(testParcel, 0);
        testParcel.setDataPosition(0); // reset data position after writing to read

        HabitEvent parceledHabitEvent = new HabitEvent(testParcel); // construct from parcel

        // Asserts Similarity
        assertEquals(testHabitEvent.getComment(), parceledHabitEvent.getComment());
        assertEquals(testHabitEvent.getCompletedDate(), parceledHabitEvent.getCompletedDate());
    }

    @Test
    public void testParcelableOnDays() {
        OnDays onDays = new OnDays();
        onDays.setAll(new boolean[]{true, false, true, false, true, true, true});

        Parcel testParcel = Parcel.obtain();
        onDays.writeToParcel(testParcel, 0);
        testParcel.setDataPosition(0);

        OnDays parceledOnDays = new OnDays(testParcel);

        assertEquals(onDays.getAll()[1], parceledOnDays.getAll()[1]);
    }
}
