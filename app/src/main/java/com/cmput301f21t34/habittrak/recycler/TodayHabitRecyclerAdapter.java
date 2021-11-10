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
import com.cmput301f21t34.habittrak.user.HabitEvent;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.Calendar;

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
    private boolean viewCheckbox = true;

    public interface HabitClickListener{
        void onItemClick(View view, int position);
        void menuButtonOnClick(View view,  int position);
        void checkBoxOnClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView habitName;
        private final TextView habitDesc;
        private final MaterialCheckBox checkBox;
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
            checkBox = (MaterialCheckBox) view.findViewById(R.id.today_listview_checkbox);
            menuButton = (ImageButton) view.findViewById(R.id.habit_menu);
            menuButton.setOnClickListener(this);
            checkBox.setOnClickListener(this);

        }

        /**
         * listener function for the button and checkbox in the recycler.
         *
         * @param view view of the clicked button, checkbox or the recycler view
         */
        @Override
        public void onClick(final View view) {
            if (view.getId() == R.id.habit_menu) {
                habitClickListener.menuButtonOnClick(view, getAdapterPosition());
            } else if (view.getId() == R.id.today_listview_checkbox) {
                habitClickListener.checkBoxOnClick(view, getAdapterPosition());
            } else {
                habitClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        /**
         *
         * habit reason getter function.
         * @return return the textview of Habit reason
         */
        public TextView getHabitDesc() {
            return habitDesc;
        }

        /**
         *
         * habit name getter function.
         * @return return the textview of Habit name
         */
        public TextView getHabitName() {
            return habitName;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        /**
         *
         * set the visibility fo the checkbox depending where the recycler is being used.
         * @author Pranav
         *
         * @param isVisible true if visible else false
         */
        public void checkBoxVisibility(final boolean isVisible) {
            if (isVisible) {
                checkBox.setVisibility(View.VISIBLE);
            } else {
                checkBox.setVisibility(View.INVISIBLE);
            }

        }

        /**
         *
         * sets the checkbox state to checked and un clickable.
         * @author Pranav
         *
         */
        public void checkCheckbox() {
            checkBox.setChecked(true);
            checkBox.setEnabled(false);
            Log.d("Adapter", "check checkbox");
        }

    }

    /**
     *
     * function to set the click listener.
     * @param habitClickListener interface for onItemClick
     */
    public void setHabitClickListener(final HabitClickListener habitClickListener) {
        TodayHabitRecyclerAdapter.habitClickListener = habitClickListener;
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param data ArrayList<Habit> containing the data to populate views
     * @param viewCheckbox to show the checkbox or not
     */
    public TodayHabitRecyclerAdapter(final ArrayList<Habit> data, final boolean viewCheckbox) {
        habits = data;
        this.viewCheckbox = viewCheckbox;
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
        // set texts
        viewHolder.getHabitName().setText(habit.getTitle());
        viewHolder.getHabitDesc().setText(habit.getReason());
        // set checkbox visibility
        viewHolder.checkBoxVisibility(viewCheckbox);
        // set checkbox value if checkbox is visible
        if (viewCheckbox) {
            Calendar date = Calendar.getInstance();
            ArrayList<HabitEvent> events = habit.getHabitEvents();
            for (int i = 0; i < events.size(); i++) {
                Log.d("Adapter", Integer.toString(events.size()));
                if (sameDay(events.get(i).getCompletedDate(), date)) {
                    viewHolder.checkCheckbox();
                    break;
                }
            }
        }
    }

    /**
     * check if the calendar dates occur on the same day.
     * @param d1 calender instance
     * @param d2 another calender instance to compare
     * @return true if day is same in both calendar
     */
    public boolean sameDay(final Calendar d1, final Calendar d2) {
        return d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)
                && d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH)
                && d1.get(Calendar.DAY_OF_MONTH) == d2.get(Calendar.DAY_OF_MONTH);
    }


    @Override
    public int getItemCount() {
        return habits.size();
    }

}
