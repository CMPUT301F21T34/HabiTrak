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
import com.cmput301f21t34.habittrak.user.HabitList;
import com.cmput301f21t34.habittrak.user.User;

import java.util.ArrayList;

/**
 * AllHabitsFragment
 *
 * @author Pranav
 * @author Dakota
 *
 * Fragment for displaying all the user's habits
 *
 * @version 1.0
 */
public class AllHabitsFragment extends Fragment {

    // attributes
    private ListView habitList;
    private ArrayAdapter<Habit> habitAdapter;
    private ArrayList<Habit> habitsData = new ArrayList<>();



    User mainUser;

    public AllHabitsFragment(User mainUser) {
        this.mainUser = mainUser;

        Log.d("User", "In All Habits Frag, User: " + mainUser.getUsername());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.habi_all_habits_fragment, container, false);

        habitList = view.findViewById(R.id.all_habits_listview);



        habitsData = new HabitList();


        //connect the array adapter
        habitAdapter = new TodayHabitList(getContext(), habitsData);
        habitList.setAdapter(habitAdapter);

        refreshAllFragment(); // populates habit list

        return view;
    }

    /**
     * refreshAllFragment
     *
     * @author Dakota
     *
     * refresh the habitsdata list to update the data
     */
    public void refreshAllFragment() {

        Log.d("TodayListFragment", "refreshing habit list");
        // Populate today view with Today's habits.

        habitsData.clear(); // Make sure is clear

        HabitList mainUserHabits = mainUser.getHabitList(); // get HabitsList

        habitsData.addAll(mainUserHabits);
        habitAdapter.notifyDataSetChanged();

    }
}