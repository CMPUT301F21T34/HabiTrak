package com.cmput301f21t34.habittrak;

import org.junit.Test;

import static org.junit.Assert.*;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.Calendar;


public class ParcelableUnitTest {

    @Test
    public void testParcelableUser(){


        User testUser = getTestUser();

        Parcel testParcel = Parcel.obtain(); // init a parcel
        testUser.writeToParcel(testParcel, 0); // populate testParcel

        testParcel.setDataPosition(0); // reset data position after writing to read

        User parceledUser = new User(testParcel); // construct from parcel

        // Asserts Similarity
        assertEquals(testUser.getUsername(), parceledUser.getUsername());
        assertEquals(testUser.getHabitList().get(0).getTitle(), parceledUser.getHabitList().get(0).getTitle());
        assertEquals(testUser.getHabitList().get(1).getStartDate(), parceledUser.getHabitList().get(1).getStartDate());

    }

    public User getTestUser(){

        return new User("testUser",
                getTestHabitList(),
                null,
                null,
                null,
                null);

    }

    public ArrayList<Habit> getTestHabitList(){
        ArrayList<Habit> habitList = new ArrayList<>();
        habitList.add(new Habit("hab1"));
        habitList.add(new Habit("hab2", "reason", Calendar.getInstance()));


        return  habitList;

    }

}
