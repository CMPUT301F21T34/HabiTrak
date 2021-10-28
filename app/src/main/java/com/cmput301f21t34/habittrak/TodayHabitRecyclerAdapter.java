package com.cmput301f21t34.habittrak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * TodayHabitRecyclerAdapter
 *
 * @author Pranav
 *
 * Adapter for recycler view
 *
 * @version 1.0
 * @since 2020-10-27
 */

public class TodayHabitRecyclerAdapter extends RecyclerView.Adapter<TodayHabitRecyclerAdapter.ViewHolder>{

    private ArrayList<Habit> habits;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView habitName;
        private final TextView habitDesc;
        private final CheckBox checkBox;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            habitName = (TextView) view.findViewById(R.id.today_listview_habit_name);
            habitDesc = (TextView) view.findViewById(R.id.today_listview_habit_desc);
            checkBox = (CheckBox) view.findViewById(R.id.today_listview_checkbox);
        }

        public TextView getHabitDesc() {
            return habitDesc;
        }

        public TextView getHabitName() {
            return habitName;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param data ArrayList<Habit> containing the data to populate views to be used
     * by RecyclerView.
     */
    public TodayHabitRecyclerAdapter(ArrayList<Habit> data){
        habits = data;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_row, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Habit habit = habits.get(position);

        viewHolder.getHabitName().setText(habit.getTitle());
        viewHolder.getHabitDesc().setText(habit.getReason());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return habits.size();
    }

}
