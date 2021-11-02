package com.cmput301f21t34.habittrak.user;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Habit_List
 *
 * @author Dakota
 *
 * Extention of ArrayList to utilize specific index attribute of Habit in ordering
 *
 * @version 1.0
 * @since 2021-10-16
 * @see ArrayList
 * @see Habit
 */
public class Habit_List extends ArrayList<Habit> implements Parcelable {

    public Habit_List(){
        super();
    }

    protected Habit_List(Parcel parcel) {

        // Gets the bundle
        Bundle habitListBundle = parcel.readBundle(Habit_List.class.getClassLoader());

        // Gets the arraylist
        ArrayList<Habit> habitArrayList = habitListBundle.getParcelableArrayList("habitList");

        // Adds all in the array list to this
        this.addAll(habitArrayList);

    }


    public void saveOrder(){
        int size = this.size();

        for (int index = 0; index < size; index++){
            this.get(index).setIndex(index);
        }

    }

    public void saveOrder(int start){
        int size = this.size();

        for (int index = start; index < size; index++){
            this.get(index).setIndex(index);
        }

    }

    public void reOrder(){
        this.sort(Habit::compareTo);
    }


    public void swap(int index1, int index2){

        // Swaps //
        Habit swap = this.get(index1);
        this.set(index1, this.get(index2));
        this.set(index2, swap);

        // Updates index
        this.get(index1).setIndex(index1);
        this.get(index2).setIndex(index2);
    }

    @Override
    public void add(int index, Habit habit){

        habit.setIndex(index); // Sets habit index

        // Calls super add
        super.add(index, habit);
        this.saveOrder(index); // Saves order for all index above the inserted element
    }


    @Override
    public boolean add(Habit habit){

        boolean success;
        success = super.add(habit); // Calls super add
       habit.setIndex(this.size() - 1); // Sets habit index to last index

       return success;
    }

    @Override
    public Habit remove(int index){
        Habit removed = super.remove(index);
        this.saveOrder(index - 1); // Updates all indices affected

        return removed;
    }

    public boolean remove( Habit toRemove ){


        boolean success;
        int index = toRemove.getIndex();
        if (this.get(index) == toRemove){
            this.remove(index);
            return true;

        } else {
            // index mismatch //
            Log.e("Habit_List", "Index Mismatch:" + "\n\t index expected: "
                    + String.valueOf(index)
                    + "\n\t index retrieved: "
                    + String.valueOf(this.get(index).getIndex())
                    + "\n\t will attempt to continue.");

            success = super.remove(toRemove);
            this.saveOrder();

            return success;

        }





    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        // Casts Habit_List to ArrayList for parceling
        ArrayList<Habit> habitList = this;

        Bundle habitListBundle = new Bundle(this.getClass().getClassLoader());
        habitListBundle.putParcelableArrayList("habitList", habitList);

        parcel.writeBundle(habitListBundle);

    }

    public static final Creator<Habit_List> CREATOR = new Creator<Habit_List>() {
        @Override
        public Habit_List createFromParcel(Parcel in) {
            return new Habit_List(in);
        }

        @Override
        public Habit_List[] newArray(int size) {
            return new Habit_List[size];
        }
    };
}
