package com.cmput301f21t34.habittrak.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.TodayHabitList;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.Habit_List;
import com.cmput301f21t34.habittrak.user.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Fragment for displaying all habits
 */
public class AllHabitsFragment extends Fragment {

    // attributes
    private ListView habitList;
    private ArrayAdapter<Habit> habitAdapter;
    private Habit_List habitsData;



    User mainUser;

    public AllHabitsFragment(User mainUser) {
        this.mainUser = mainUser;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.habi_all_habits_fragment, container, false);

        habitList = view.findViewById(R.id.all_habits_listview);

        Log.d("mainUser", "in AllHabitsFragment mainUser: " + mainUser.getUsername());


        habitsData = new Habit_List();


        //connect the array adapter
        habitAdapter = new TodayHabitList(getContext(), habitsData);
        habitList.setAdapter(habitAdapter);

        refreshAllFragment(); // populates habit list

        return view;
    }


    public void refreshAllFragment() {

        Log.d("TodayListFragment", "refreshing habit list");
        // Populate today view with Today's habits.

        habitsData.clear(); // Make sure is clear

        ArrayList<Habit> mainUserHabits = mainUser.getHabitList(); // get HabitsList

        habitsData.addAll(mainUserHabits);
        habitAdapter.notifyDataSetChanged();

    }
}