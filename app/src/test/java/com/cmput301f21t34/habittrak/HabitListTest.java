package com.cmput301f21t34.habittrak;

import android.location.Location;
import android.util.Log;

import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitEvent;
import com.cmput301f21t34.habittrak.user.HabitList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Class for testing HabitList
 *
 * @author Henry
 * @version 1.0
 * @since 2021-11-23
 * @see HabitList
 * @see Habit
 */

public class HabitListTest {
    private HabitList mockHabitList() {
        return new HabitList();
    }

    @Test
    public void addTest() {
        HabitList mockList = mockHabitList();
        assertEquals(0, mockList.size());

        Habit habit1 = new Habit("read book");
        Habit habit2 = new Habit("walk dog");
        Habit habit3 = new Habit("hydrate");

        // Add a habit then check size and verify added habit
        mockList.add(habit1);
        assertEquals(1, mockList.size()); // size
        assertEquals(habit1, mockList.get(0)); // added habit

        // Add by index test
        mockList = mockHabitList();
        mockList.add(habit1);
        mockList.add(habit2);
        assertEquals(2, mockList.size());

        mockList.add(1, habit3);
        assertEquals(3, mockList.size());
        assertEquals(habit2, mockList.get(2));
        assertEquals(habit3, mockList.get(1));
        assertEquals(habit1, mockList.get(0));
    }

    @Test
    public void deleteTest() {
        HabitList mockList = mockHabitList();
        assertEquals(0, mockList.size());

        Habit habit1 = new Habit("read book");
        Habit habit2 = new Habit("walk dog");

        // Test remove by index
        mockList.add(habit1);
        assertEquals(1, mockList.size());
        mockList.add(habit2);
        assertEquals(2, mockList.size());
        mockList.remove(1);  // remove habit2
        assertEquals(1, mockList.size());
        assertEquals(habit1, mockList.get(0));
        mockList.remove(0);  // remove habit1
        assertEquals(0, mockList.size());

        // Test remove by object
        mockList.add(habit1);
        assertEquals(1, mockList.size());
        mockList.add(habit2);
        assertEquals(2, mockList.size());
        mockList.remove(habit2);  // remove habit2
        assertEquals(1, mockList.size());
        assertEquals(habit1, mockList.get(0));
        mockList.remove(habit1);  // remove habit1
        assertEquals(0, mockList.size());
    }

    @Test
    public void replaceTest() {
        HabitList mockList = mockHabitList();
        Habit habit1 = new Habit("read book");
        Habit habit2 = new Habit("walk dog");
        mockList.add(habit1);
        mockList.add(habit2);
        assertEquals(2, mockList.size());

        // New habit objects that have the same indices as habit1 and habit2 but different titles
        Habit habit3 = new Habit("not read book");
        habit3.setIndex(habit1.getIndex());
        assertEquals(0, habit3.getIndex());
        Habit habit4 = new Habit("not walk dog");
        habit4.setIndex(habit2.getIndex());
        assertEquals(1, habit4.getIndex());

        assertEquals(habit1, mockList.get(0));  // before
        mockList.replace(habit3);
        assertEquals(habit3, mockList.get(0));  // after

        assertEquals(habit2, mockList.get(1));  // before
        mockList.replace(habit4);
        assertEquals(habit4, mockList.get(1));  // after

    }

    @Test
    public void swapTest() {
        HabitList mockList = mockHabitList();
        Habit habit1 = new Habit("read book");
        Habit habit2 = new Habit("walk dog");
        Habit habit3 = new Habit("hydrate");

        mockList.add(habit1);
        mockList.add(habit2);
        mockList.add(habit3);
        assertEquals(3, mockList.size());

        // Before swap
        assertEquals(habit1, mockList.get(0));
        assertEquals(habit2, mockList.get(1));
        assertEquals(habit3, mockList.get(2));

        // After swap
        mockList.swap(0, 2);
        assertEquals(habit3, mockList.get(0));
        assertEquals(habit1, mockList.get(2));
        assertEquals(habit2, mockList.get(1));
    }
}
