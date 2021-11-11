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
 * EventsFragment
 *
 * @author Aron Rajabi
 *
 * Fragment for displaying all habits, viewing its completed events when clicked
 * Recycler view code and onclicklisteners taken from Pranav and Dakotas AllHabitsFragment
 *
 * @version 1.0
 */
public class EventsFragment extends Fragment {

    // Attributes //

    // These are for the Recycler view
    private RecyclerView habitRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HabitRecycler habitRecycler;
    private ArrayList<Habit> habitsDisplayList;

    User mainUser;

    public EventsFragment(User mainUser) {
        habitsDisplayList = new ArrayList<>();
        this.mainUser = mainUser;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.habi_events_fragment, container, false);

        // Sets up views and manager for recycler view
        habitRecyclerView = view.findViewById(R.id.events_recycler_view);
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