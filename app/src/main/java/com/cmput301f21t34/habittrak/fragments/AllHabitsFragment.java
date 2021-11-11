package com.cmput301f21t34.habittrak.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.recycler.HabitRecycler;
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
 * Fragment for displaying all the user's habits, viewing and editing them when clicked
 *
 * @version 1.0
 */
public class AllHabitsFragment extends Fragment {


    // Attributes //

    // These are for the Recycler view
    private RecyclerView habitRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HabitRecycler habitRecycler;
    private ArrayList<Habit> habitsDisplayList;

    private User mainUser;

    public AllHabitsFragment(User mainUser) {

        habitsDisplayList = new ArrayList<>();
        this.mainUser = mainUser;


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.habi_all_habits_fragment, container, false);

        // Sets up views and manager for recycler view
        habitRecyclerView = view.findViewById(R.id.all_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());


        // Sets up the recycler view with a list of all habits, and
        // an array list for the recycler to use for display - Dakota
        this.habitRecycler = new HabitRecycler(habitRecyclerView, layoutManager, habitsDisplayList, mainUser.getHabitList());


        return view;
    }


    @Override
    public void onResume() {

        super.onResume();

        // Refreshes Frag
        refreshAllFragment();

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

        habitsDisplayList.clear(); // Make sure is clear

        HabitList mainUserHabits = mainUser.getHabitList(); // get HabitsList

        habitsDisplayList.addAll(mainUserHabits);

        // tells the adapter in recycler that the dataset has changed
        habitRecycler.notifyDataSetChanged();

    }
}