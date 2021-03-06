package com.cmput301f21t34.habittrak.recycler;

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
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * TodayHabitRecyclerAdapter
 *
 * @author Pranav
 * <p>
 * Adapter for recycler view
 * @version 1.0
 * @since 2020-10-27
 */
public class HabitRecyclerAdapter extends RecyclerView.Adapter<HabitRecyclerAdapter.ViewHolder> {

    // data list
    private ArrayList<Habit> habits;
    // listener
    private static HabitClickListener habitClickListener;
    // boolean to show the checkbox or not
    private boolean viewCheckbox;

    // interface for implementing listener functions
    public interface HabitClickListener {
        void onItemClick(View view, int position);

        void menuButtonOnClick(View view, int position);

        void checkBoxOnClick(View view, int position);
    }

    /**
     * view holder for HabitRecyclerAdapter
     * holds all the layout views of the recycler
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // views
        private final TextView habitName;
        private final TextView habitDesc;
        private final MaterialCheckBox checkBox;
        private final ImageButton menuButton;
        private final LinearProgressIndicator progressBar;
        private final TextView streak;

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
            progressBar = (LinearProgressIndicator) view.findViewById(R.id.habit_progress);
            streak = (TextView) view.findViewById(R.id.habit_streak);
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
                // listener for menu button
                habitClickListener.menuButtonOnClick(view, getAdapterPosition());
            } else if (view.getId() == R.id.today_listview_checkbox) {
                // listener for checkbox
                habitClickListener.checkBoxOnClick(view, getAdapterPosition());
            } else {
                // listener for the recycler view row
                habitClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        /**
         * habit reason getter function.
         *
         * @return return the textview of Habit reason
         */
        public TextView getHabitDesc() {
            return habitDesc;
        }

        /**
         * habit name getter function.
         *
         * @return return the textview of Habit name
         */
        public TextView getHabitName() {
            return habitName;
        }

        /**
         * habit checkbox getter function
         *
         * @return return the Checkbox
         */
        public CheckBox getCheckBox() {
            return checkBox;
        }

        /**
         * set the visibility fo the checkbox depending where the recycler is being used.
         *
         * @param isVisible true if visible else false
         * @author Pranav
         */
        public void checkBoxVisibility(final boolean isVisible) {
            if (isVisible) {
                checkBox.setVisibility(View.VISIBLE);
            } else {
                checkBox.setVisibility(View.INVISIBLE);
            }
        }

        /**
         * setProgress.
         * <p>
         * set the progress bar value
         *
         * @param progress int value of the habit streak
         */
        public void setProgress(int progress) {
            progressBar.setProgress((progress * 100) / 30);
            String text = Integer.toString(progress) + "/30";
            streak.setText(text);
        }

        /**
         * makes the checkbox state true and un-clickable.
         *
         * @author Pranav
         */
        public void checkCheckbox() {
            checkBox.setChecked(true);
            checkBox.setEnabled(false);
        }
    }

    /**
     * function to set the click listener.
     *
     * @param habitClickListener interface for onItemClick
     */
    public void setHabitClickListener(final HabitClickListener habitClickListener) {
        HabitRecyclerAdapter.habitClickListener = habitClickListener;
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param data         ArrayList<Habit> containing the data to populate views
     * @param viewCheckbox to show the checkbox or not
     */
    public HabitRecyclerAdapter(final ArrayList<Habit> data, final boolean viewCheckbox) {
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
                if (sameDay(events.get(i).getCompletedDate(), date)) {
                    viewHolder.checkCheckbox();
                    break;
                }
            }
        }
        // set progress bar
        viewHolder.setProgress(habit.getCurrentStreak());
    }

    /**
     * check if the calendar dates occur on the same day.
     *
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
