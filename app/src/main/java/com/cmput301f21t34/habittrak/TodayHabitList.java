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

import java.util.ArrayList;

public class TodayHabitList extends ArrayAdapter<Habit> {
    private ArrayList<Habit> habits;
    private Context context;

    public TodayHabitList(Context context, ArrayList<Habit> habits){
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.habi_today_listview_content, parent, false);
        }

        Habit habit = habits.get(position);

        TextView habitName = view.findViewById(R.id.today_listview_habit_name);
        TextView habitDesc = view.findViewById(R.id.today_listview_habit_desc);

        habitName.setText(habit.getTitle());
        habitDesc.setText(habit.getReason());

        return view;


    }
}
