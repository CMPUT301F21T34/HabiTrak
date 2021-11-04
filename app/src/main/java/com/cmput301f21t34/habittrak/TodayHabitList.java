package com.cmput301f21t34.habittrak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301f21t34.habittrak.user.Habit;

import java.util.ArrayList;

/**
 * TodayHabitList
 *
 * customList for ListView of Today Fragment
 * @version 1.0
 * @since 2021-10-19
 * @see Habit
 */
public class TodayHabitList extends ArrayAdapter<Habit> {

    // attributes
    private ArrayList<Habit> habits;
    private Context context;
    TextView habitName;
    TextView habitDesc;
    CheckBox habitBox;

    // constructor
    public TodayHabitList(Context context, ArrayList<Habit> habits){
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
    }

    /**
     * Set the view of the list
     * @param position position of the habit in the list
     * @param convertView view to be returned
     * @param parent ViewGroup of the list
     * @return view
     */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.recycler_view_row, parent, false);
        }

        Habit habit = habits.get(position);

        habitName = view.findViewById(R.id.today_listview_habit_name);
        habitDesc = view.findViewById(R.id.today_listview_habit_desc);

        habitName.setText(habit.getTitle());
        habitDesc.setText(habit.getReason());

        return view;
    }
}
