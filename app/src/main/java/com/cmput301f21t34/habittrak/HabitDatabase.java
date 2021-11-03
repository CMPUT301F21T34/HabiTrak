package com.cmput301f21t34.habittrak;

import com.cmput301f21t34.habittrak.user.Habit_Event;
import com.cmput301f21t34.habittrak.user.On_Days;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * HabitDatabase
 * @author Henry
 * @version 1.0.0
 * Same as Habit but with a different implementation for onDaysObj (On_Days/boolean[] -> ArrayList<Booleam>)
 * This is what we store for habit on the database
 */
public class HabitDatabase {
    private int index = 0;
    private String title, reason;
    private Calendar startDate;
    private ArrayList<Habit_Event> habitEvents;
    private boolean isPublic;
    private ArrayList<Boolean> onDaysObj;

    public HabitDatabase(){
        this.index = 0;
        this.title = "";
        this.reason= "";
        this.startDate = Calendar.getInstance();
        this.habitEvents = new ArrayList<Habit_Event>();
        this.isPublic = false;
        this.onDaysObj = new ArrayList<Boolean>();
    }

    public String getTitle(){ return this.title; }
    public void setTitle(String title){ this.title = title; }

    public String getReason(){ return this.reason; }
    public void setReason(String reason){ this.reason = reason; }

    public Calendar getStartDate(){ return this.startDate; }
    public void setStartDate(Calendar startDate){ this.startDate = startDate; }

    public int getIndex(){ return this.index; }
    public void setIndex(int index){ this.index = index; }

    public ArrayList<Habit_Event> getHabitEvents(){ return this.habitEvents; }
    public void setHabitEvents(ArrayList<Habit_Event> habitEvents) { this.habitEvents = habitEvents; }

    public ArrayList<Boolean> getOnDaysObj(){ return this.onDaysObj; }
    public void setOnDaysObj(On_Days onDays) {
        ArrayList<Boolean> onDaysToSet = new ArrayList<>();
        boolean[] onDaysArray = onDays.getAll();
        for (int i = 0; i < onDaysArray.length; i++) {
            onDaysToSet.add((Boolean) onDaysArray[i]);
        }
        this.onDaysObj = onDaysToSet;
    }
}
