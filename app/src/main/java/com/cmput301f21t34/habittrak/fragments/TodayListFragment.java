package com.cmput301f21t34.habittrak.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.Toast;

import com.cmput301f21t34.habittrak.ViewEditHabit;
import com.cmput301f21t34.habittrak.recycler.HabitRecycler;
import com.cmput301f21t34.habittrak.recycler.TodayHabitRecyclerAdapter;
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
    public static int RESULT_EDIT_HABIT = 2000;
    // These are for the Recycler view
    private RecyclerView habitRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HabitRecycler habitRecycler;
    private ArrayList<Habit> habitsDisplayList;
    private TodayHabitRecyclerAdapter adapter;
    private User mainUser;



    // constructor
    public TodayListFragment(User mainUser) {

        habitsDisplayList = new ArrayList<>();
        this.mainUser = mainUser;
        adapter = new TodayHabitRecyclerAdapter(habitsDisplayList);

        Log.d("TodayList", "New Frag Created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.habi_all_habits_fragment, container, false);

        Log.d("TodayList", "New View Created");
        // Sets up views and manager for recycler view
        habitRecyclerView = view.findViewById(R.id.all_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());


        // Sets up the recycler view with a list of all habits, and
        // an array list for the recycler to use for display - Dakota
        adapter.setHabitClickListener(new TodayHabitRecyclerAdapter.HabitClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Habit habit = habitsDisplayList.get(position);
                Intent intent = new Intent(getContext(), ViewEditHabit.class);
                intent.putExtra("HABIT", habit);
                intent.putExtra("position", habit.getIndex());
                viewHabitResultLauncher.launch(intent);
            }
        });
        habitRecycler = new HabitRecycler(habitRecyclerView, layoutManager, habitsDisplayList, mainUser.getHabitList(), true);
        habitRecycler.setAdapter(adapter);




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

                habitsDisplayList.add(mainUserHabits.get(index));
                // ensure index are parallel when populating from established list
                adapter.notifyDataSetChanged();


            }
        }


    }

    ActivityResultLauncher<Intent> viewHabitResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {}
    );



}