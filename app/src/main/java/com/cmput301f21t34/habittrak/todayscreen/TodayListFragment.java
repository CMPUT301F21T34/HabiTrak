package com.cmput301f21t34.habittrak.todayscreen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmput301f21t34.habittrak.Habit;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.TodayHabitList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 */
public class TodayListFragment extends Fragment {

    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitsData;
    public TodayListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.habi_today_fragment, container, false);
        habitList = view.findViewById(R.id.today_listview);

        Calendar date = new GregorianCalendar(2021,1,31);
        Habit habit1 = new Habit("exercise dog", "some desc", date);
        Habit habit2 = new Habit("go for a walk", "some desc 2", date);

        habitsData = new ArrayList<>();
        habitsData.add(habit1); habitsData.add(habit2);

        habitAdapter = new TodayHabitList(getContext(), habitsData);
        habitList.setAdapter(habitAdapter);

        return view;
    }
}