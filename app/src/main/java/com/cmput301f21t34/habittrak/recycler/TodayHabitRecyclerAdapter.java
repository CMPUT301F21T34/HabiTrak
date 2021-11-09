package com.cmput301f21t34.habittrak.recycler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.user.Habit;

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
    private static HabitClickListener habitClickListener;

    public interface HabitClickListener{
        void onItemClick(View view, int position);
        void menuButtonOnClick(View view,  int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView habitName;
        private final TextView habitDesc;
        private final CheckBox checkBox;
        private final ImageButton menuButton;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            habitName = (TextView) view.findViewById(R.id.today_listview_habit_name);
            habitDesc = (TextView) view.findViewById(R.id.today_listview_habit_desc);
            checkBox = (CheckBox) view.findViewById(R.id.today_listview_checkbox);
            menuButton = (ImageButton) view.findViewById(R.id.habit_menu);
            menuButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.habit_menu){
                habitClickListener.menuButtonOnClick(view, getAdapterPosition());
            }
            else {
                habitClickListener.onItemClick(view, getAdapterPosition());
            }
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
     * setHabitClickListener
     *
     * function to set the click listener
     * @param habitClickListener interface for onItemClick
     */
    public void setHabitClickListener(HabitClickListener habitClickListener) {
        TodayHabitRecyclerAdapter.habitClickListener = habitClickListener;
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
