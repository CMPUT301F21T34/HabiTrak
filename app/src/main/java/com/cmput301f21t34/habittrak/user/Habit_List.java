package com.cmput301f21t34.habittrak.user;

import android.util.Log;

import androidx.annotation.Nullable;

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
public class Habit_List extends ArrayList<Habit> {

    public Habit_List(){
        super();
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
       habit.setIndex(this.size()); // Sets habit index to last index

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



}
