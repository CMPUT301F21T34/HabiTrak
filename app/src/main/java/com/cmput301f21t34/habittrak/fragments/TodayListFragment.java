package com.cmput301f21t34.habittrak.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmput301f21t34.habittrak.recycler.HabitRecycler;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.R;

import com.cmput301f21t34.habittrak.TodayHabitList;




import com.cmput301f21t34.habittrak.user.HabitList;

import com.cmput301f21t34.habittrak.user.User;

import java.util.ArrayList;


/**
 * TodayListFragment
 *
 * @author Pranav
 *
 * Fragment for displaying habits for today
 */
public class TodayListFragment extends Fragment {


    // Attributes //

    // These are for the Recycler view
    private RecyclerView habitRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HabitRecycler habitRecycler;
    private ArrayList<Habit> habitsDisplayList;

    private User mainUser;



    // constructor
    public TodayListFragment(User mainUser) {

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
        this.habitRecycler = new HabitRecycler(habitRecyclerView, layoutManager, habitsDisplayList, mainUser.getHabitList(), true);





        return view;
    }

    @Override
    public void onResume() {

        super.onResume();

        // Refreshes Frag
        refreshTodayFragment();

    }


    /**
     * refreshHabitList
     *
     * refreshes the habitListView showing habits for today
     *
     * @author Dakota
     */

    public void refreshTodayFragment() {


        // Populate today view with Today's habits.

        habitsDisplayList.clear();// Make sure is clear

        HabitList mainUserHabits = mainUser.getHabitList(); // get HabitsList


        // Iterates through all habits

        for (int index = 0; index < mainUserHabits.size(); index++){

            // Checks to see if they should be displayed
            if (mainUserHabits.get(index).getOnDaysObj().isOnDay() && mainUserHabits.get(index).isHabitStart()){ // If a habit is active today add


                // ensure index are parallel when populating from established list
                habitsDisplayList.add(mainUserHabits.get(index));
                habitRecycler.notifyDataSetChanged();

            }
        }


    }



}