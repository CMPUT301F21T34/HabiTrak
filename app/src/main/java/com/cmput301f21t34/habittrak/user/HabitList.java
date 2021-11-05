package com.cmput301f21t34.habittrak.user;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import java.util.ArrayList;

/**
 * HabitList
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
public class HabitList extends ArrayList<Habit> {

    final String TAG = "HabitList";

    public HabitList(){
        super();
    }

    public HabitList(ArrayList<Habit> arrayList){
        this.addAll(arrayList);
    }

    public HabitList(Parcel parcel) {

        // Gets the bundle
        Bundle habitListBundle = parcel.readBundle(HabitList.class.getClassLoader());

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

        // Makes sure list isn't empty
        if (size > 0) {
            // Makes sure our index is in bounds
            if (start < 0) {
                start = 0;
            }

            for (int index = start; index < size; index++) {
                this.get(index).setIndex(index);
            }
        }
    }

    public void reOrder(){

        Log.d(TAG, "\n==sorting==");

        for(int i = 0; i < this.size(); i++){
            Log.d(TAG, "\nBefore Sort! Index: " + String.valueOf(i)
                    + " = Habit: " + String.valueOf(this.get(i).getTitle()));
        }

        this.sort(Habit::compareTo);

        for(int i = 0; i < this.size(); i++){
            Log.d(TAG, "\nAfter Sort! Index: " + String.valueOf(i)
                    + " = Habit: " + String.valueOf(this.get(i).getTitle()));
        }

        Log.d(TAG, "\n====end====");
    }


    public void swap(int index1, int index2){



        Log.d(TAG, "index1: " + String.valueOf(index1));
        Log.d(TAG, "index2: " + String.valueOf(index2));




        // Swaps //
        Habit habit1 = this.get(index1);
        Habit habit2 = this.get(index2);

        Log.d(TAG, "Habit1: " + String.valueOf(habit1.getTitle()));
        Log.d(TAG, "Habit2: " + String.valueOf(habit2.getTitle()));


        int habit1Index = habit1.getIndex();
        int habit2Index = habit2.getIndex();

        Log.d(TAG, "!!habit1Index: " + String.valueOf(habit1Index));
        Log.d(TAG, "!!habit2Index: " + String.valueOf(habit2Index));

        Log.d(TAG, "Habit1 Absolute Index: " + String.valueOf(habit1Index));
        Log.d(TAG, "Habit2 Absolute Index: " + String.valueOf(habit2Index));

        habit1.setIndex(habit2Index);
        habit2.setIndex(habit1Index);

        Log.d(TAG, "!!habit1Index: " + String.valueOf(habit1Index));
        Log.d(TAG, "!!habit2Index: " + String.valueOf(habit2Index));

        Log.d(TAG, "Habit1 Absolute Index: " + String.valueOf(habit1.getIndex()));
        Log.d(TAG, "Habit2 Absolute Index: " + String.valueOf(habit2.getIndex()));

        this.reOrder();



        //this.set(index1, this.get(index2));
        Log.d(TAG, "Habit now at index1: " + String.valueOf(this.get(index1).getTitle()));

        //this.set(index2, swap);
        Log.d(TAG, "Habit now at index2: " + String.valueOf(this.get(index2).getTitle()));

        // Updates index
        //this.get(index1).setIndex(index1);
        //this.get(index2).setIndex(index2);
    }


    @Override
    public void add(int index, Habit habit){

        habit.setIndex(index); // Sets habit index

        // Calls super add
        super.add(index, habit);
        this.saveOrder(index); // Saves order for all index above the inserted element
    }

    /**
     * addTracking
     *
     * adds a habit to the list but does not update
     * the habits indexes or any other
     *
     * Used when tracking a subset from a larger HabitList
     *
     * @param habit Habit to be added for tracking
     */
    public void addTracking(Habit habit){


        // Calls super add
        super.add(habit);

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
            Log.e("HabitList", "Index Mismatch:" + "\n\t index expected: "
                    + String.valueOf(index)
                    + "\n\t index retrieved: "
                    + String.valueOf(this.get(index).getIndex())
                    + "\n\t will attempt to continue.");

            success = super.remove(toRemove);
            this.saveOrder();

            return success;

        }





    }

}
