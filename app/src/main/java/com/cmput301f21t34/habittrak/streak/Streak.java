package com.cmput301f21t34.habittrak.streak;

import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitEvent;

import java.util.ArrayList;
import java.util.Calendar;

public class Streak {

    Habit habit;
    Calendar bestStreakDate;
    Calendar bestStreakDateEnd;
    Calendar currentStreakDate;
    Calendar currentStreakDateEnd;


    public Streak(Habit habit){
        this.habit = habit;

        // null checks
        if (habit.getBestStreakDate() == null) {
            this.bestStreakDate = null;
        } else {
            this.bestStreakDate = (Calendar) habit.getBestStreakDate().clone(); // clone as to not effect
        }
        if (habit.getCurrentStreakDate() == null){
            this.currentStreakDate = null;
        } else {
            this.currentStreakDate = (Calendar) habit.getCurrentStreakDate().clone();
        }
        if (habit.getCurrentStreakDateEnd() == null){
            this.currentStreakDateEnd = Calendar.getInstance();
        } else {
            this.currentStreakDateEnd = habit.getCurrentStreakDateEnd();
        }

    }


    /**
     * Checks going backwards from firstDay, the longest streak
     *
     * @author Dakota
     * @param firstDay Calendar of the first day to check (usually today)
     * @return int streak
     */
    private int refreshStreak(Calendar firstDay){

        // Set the starting day
        Calendar day = Calendar.getInstance();
        day.setTime(firstDay.getTime());

        int streak = 0; // Initialize our running count

        ArrayList<HabitEvent> events = habit.getHabitEvents(); // Get events to check

        // Make sure events are sorted
        events.sort(HabitEvent::compareTo);

        boolean notCompleted = true;
        while (notCompleted){

            // If the particular day we are checking is a day that the habit is active
            if (habit.getOnDaysObj().isOnDay(day)){

                // Checks if the current day has a corresponding event
                int index = 0; // Must init before loop for use outside of loop
                for (; index < events.size(); index++){

                    HabitEvent event = events.get(index); // event to check

                    // Compare the event day and current day we are checking (Only compare year, month, day)
                    int comparison = new TimeIgnoringComparator().compare(event.getCompletedDate(), day);

                    if ( comparison == 0){ // If days are the same
                        // Then the habit was completed that day
                        streak++;
                        this.currentStreakDate = event.getCompletedDate(); // update current streak date
                        break;

                    }
                    // Checks if we are missing a day, excluding the current day
                    else if (
                            (comparison < 0) // event we are checking completed before the current day we are checking
                                    &&
                            (new TimeIgnoringComparator().compare(Calendar.getInstance(), day) != 0) // it in not today
                    ) {
                        // We are missing an event aka the streak was broken
                        notCompleted = false;
                        break;

                    }

                    // else check the next event

                }

                if (index >= events.size()){
                    // Ran out of events last search
                    notCompleted = false;
                }

            }
            // decrement the day by one a check that day
            day.add(Calendar.DATE, -1);

        }

        return streak;

    }

    private boolean currentDayCheck(){
        Calendar today = Calendar.getInstance();

        if (habit.getOnDaysObj().isOnDay(today)){
            ArrayList<HabitEvent> events = habit.getHabitEvents();
            events.sort(HabitEvent::compareTo);

            for (int index = 0; index < events.size(); index ++) {
                int comparison = new TimeIgnoringComparator().compare(today, events.get(index).getCompletedDate());
                if (comparison == 0) {
                    // Then an event was completed today
                    return true;
                }
            }

        }

        return false;
    }


    /**
     * refreshes the Habits streak
     *
     * @author Dakota
     */
    public void refreshStreak(){

        // If the CurrentStreakDateEnd is before March 2021 then consider it a test
        // We will start from that day instead of the current date
        // This is so we can test the code handling over a year change without possible running 364 loops
        TimeIgnoringComparator comparator = new TimeIgnoringComparator();
        Calendar testDate = Calendar.getInstance();
        testDate.set(2021, 02, 28);

        if (comparator.compare(currentStreakDateEnd,testDate) == -1){
            // We are testing, don't update DateEnd
        } else {
            currentStreakDateEnd = Calendar.getInstance(); // set to today
        }

        int currentStreak; // Actual current streak (can be bigger than 30 days)

        currentStreak = refreshStreak(currentStreakDateEnd); // get the current streak

        // Fix currentStreakDateEnd to be an onDay instead of today

        while(!habit.getOnDaysObj().isOnDay(currentStreakDateEnd)){ // if it is not an onDay
            currentStreakDateEnd.add(Calendar.DAY_OF_YEAR, -1); // increments the day down by 1
        } // Until it is an onDay

        boolean completedToday = currentDayCheck();

        if (completedToday){
            currentStreak += 1;
        }

        // Only update best streak if needed
        if (habit.getBestStreak() <= currentStreak){
            this.bestStreakDate = this.currentStreakDate;
            this.bestStreakDateEnd = currentStreakDateEnd;
            habit.setBestStreak(currentStreak);
            habit.setBestStreakDateEnd(this.bestStreakDateEnd);
            habit.setBestStreakDate(this.bestStreakDate);
        }


        habit.setCurrentStreakDate(this.currentStreakDate); // Probably not used for anything
        habit.setCurrentStreak(currentStreak % 30); // We only show out of 30 days


    }
}
