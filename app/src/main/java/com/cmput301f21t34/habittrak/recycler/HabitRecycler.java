package com.cmput301f21t34.habittrak.recycler;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitList;

import java.util.ArrayList;

/**
 * HabitRecycler class to handle HabitRecyclerAdapter and implement
 * ItemTouchHelper.SimpleCallback (for dragging habits) for the recycler
 * Used in TodayListFragment, AllHabitsFragment and Events Fragment for display habits
 *
 * @author Dakota
 * @author Pranav
 * @see HabitRecyclerAdapter
 */
public class HabitRecycler {

    // data list
    private ArrayList<Habit> displayHabits;
    private HabitList habits;
    // recycler data
    private HabitRecyclerAdapter adapter;
    private final RecyclerView recyclerView;
    // ItemTouchHelper for dragging rows
    private ItemTouchHelper.SimpleCallback simpleCallback;
    // boolean for locking the drag feature
    private final boolean locked = false;

    /**
     * Standard Constructor for a recyclable view of habits.
     *
     * @param recyclerView  The RecyclerView to use
     * @param layoutManager The Layout Manger from the activity to use
     * @param displayHabits An ArrayList<Habit> for using as a display list
     * @param habits        A HabitList for getting the Habits from
     * @author Dakota
     */
    public HabitRecycler(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, ArrayList<Habit> displayHabits, HabitList habits) {

        // Sets up two list of habits, the ones to display and the HabitList of all habits
        this.displayHabits = displayHabits;
        this.habits = habits;

        // Sets up recycler view and its layout manger
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Standard Constructor for a recyclable view of habits with lock boolean
     * to pass
     *
     * @param recyclerView  The RecyclerView to use
     * @param layoutManager The Layout Manger from the activity to use
     * @param displayHabits An ArrayList<Habit> for using as a display list
     * @param habits        A HabitList for getting the Habits from
     * @param locked        A boolean who if is true, locks the recycler view
     * @author Dakota
     */
    public HabitRecycler(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, ArrayList<Habit> displayHabits, HabitList habits, boolean locked) {
        // Sets up two list of habits, the ones to display and the HabitList of all habits
        this.displayHabits = displayHabits;
        this.habits = habits;

        // Sets up recycler view and its layout manger
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * recycler
     * <p>
     * Handles the recycler views logic, swapping the indices
     * in the view, and the indices of the habits involved,
     *
     * @param locked boolean If true then locks the recycler view
     * @author Dakota
     * @author Pranav
     */
    private ItemTouchHelper.SimpleCallback recycler(boolean locked) {
        /**
         * Touch Helper for dragging habits
         *
         * onMove swaps the position in the adapter
         */
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START
                        | ItemTouchHelper.END, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                if (fromPosition < toPosition && toPosition < displayHabits.size()) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Habit habit1 = displayHabits.get(i);
                        Habit habit2 = displayHabits.get(i + 1);

                        // We swap the habits in the main list rather then the views particular list
                        habits.swap(habit1.getIndex(), habit2.getIndex());

                        // Then we swap them in the display so that the for loop can continue before refreshing displayHabits
                        displayHabits.set(i, habit2);
                        displayHabits.set(i + 1, habit1);
                    }
                } else if (toPosition >= 0) {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Habit habit1 = displayHabits.get(i);
                        Habit habit2 = displayHabits.get(i - 1);

                        // We swap the habits in the main list rather then the views particular list
                        habits.swap(habit1.getIndex(), habit2.getIndex());

                        // Then we swap them in the display so that the for loop can continue before refreshing displayHabits
                        displayHabits.set(i, habit2);
                        displayHabits.set(i - 1, habit1);
                    }
                }
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

                return true;
            }

            @Override
            public boolean isLongPressDragEnabled() {
                // is locked is true, then long press drag is disabled and user
                // can't use recycler view
                return !locked;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // nothing function function
            }
        };
        return simpleCallback;
    }

    /**
     * notifyDataSetChanged
     * <p>
     * Allows user to notify the adapter that the data set has changed
     *
     * @author Dakota
     */
    @SuppressLint("NotifyDataSetChanged")
    public void notifyDataSetChanged() {
        this.adapter.notifyDataSetChanged();
    }

    /**
     * Allows to set adapter.
     *
     * @param adapter TodayHabitRecyclerAdapter to set
     * @author Dakota
     */
    public void setAdapter(HabitRecyclerAdapter adapter) {
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);

        this.simpleCallback = recycler(locked);

        // Sets up touch handling with the recycler view
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * setRecyclerVisibility.
     * <p>
     * function to hide the recycler view
     *
     * @param view boolean value
     */
    public void setRecyclerVisibility(boolean view) {
        if (view)
            recyclerView.setVisibility(View.VISIBLE);
        else
            recyclerView.setVisibility(View.GONE);
    }
}
