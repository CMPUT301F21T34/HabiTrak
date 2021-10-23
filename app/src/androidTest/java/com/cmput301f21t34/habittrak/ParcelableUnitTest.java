package com.cmput301f21t34.habittrak;

import org.junit.Test;

import static org.junit.Assert.*;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Class for testing Parcelable Objects
 *
 * @author Dakota
 * @version 1.0
 * @since 2021-10-22
 * @see android.os.Parcelable
 * @see User
 * @see Habit
 * @see Habit_Event
 */
public class ParcelableUnitTest {

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
        ArrayList<Habit> habitList = new ArrayList<>();
        habitList.add(new Habit("hab1"));
        habitList.add(new Habit("hab2", "reason", Calendar.getInstance()));


        return  habitList;

    }

}
