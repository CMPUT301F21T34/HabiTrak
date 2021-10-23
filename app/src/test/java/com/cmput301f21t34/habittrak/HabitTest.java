package com.cmput301f21t34.habittrak;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class HabitTest {
    // mock Habit object to perform test on
    private Habit mockHabit() {
        String title = "Read book";
        String reason = "Book good";
        // do this on Monday, Wednesday and Saturday
        boolean[] onDays = {true, false, true, false, false, false, false};
        // use specific start date for future testing
        Calendar startDate = new GregorianCalendar(2021, 1, 1);
        Habit mockHabit = new Habit(title, reason, startDate, onDays);
        return mockHabit;
    }
}
