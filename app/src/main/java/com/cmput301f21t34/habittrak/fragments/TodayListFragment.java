package com.cmput301f21t34.habittrak.fragments;

import android.content.Intent;
import android.os.Bundle;

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

import com.cmput301f21t34.habittrak.AddHabitActivity;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.R;

import com.cmput301f21t34.habittrak.TodayHabitRecyclerAdapter;

import com.cmput301f21t34.habittrak.TodayHabitList;
import com.cmput301f21t34.habittrak.user.Habit_List;
import com.cmput301f21t34.habittrak.user.User;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;



/**
 * TodayListFragment
 *
 * @author Pranav
 *
 * Fragment for displaying habits for today
 */
public class TodayListFragment extends Fragment {


    final String TAG = "TodayFrag";


    // attributes


    TodayHabitRecyclerAdapter adapter;

    private ListView habitList;
    private ArrayAdapter<Habit> habitAdapter;
    private Habit_List habitsData = new Habit_List();

    User mainUser;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;



    // constructor
    public TodayListFragment(User mainUser) {

        this.mainUser = mainUser;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.habi_today_fragment, container, false);




        Log.d("mainUser", "in TodayListFragment mainUser: " + mainUser.getUsername());


        // code to execute //

        // sample data (Does not show as we are getting habits from main User)
        Calendar date = new GregorianCalendar(2021,1,31);
        Habit habit1 = new Habit("exercise dog", "some desc", date);
            habit1.getOnDaysObj().setTrue(Calendar.MONDAY);
            habit1.getOnDaysObj().setTrue(Calendar.FRIDAY);
        Habit habit2 = new Habit("go for a walk", "some desc 2", date);
            habit2.getOnDaysObj().setAll(new boolean[]{true, true, true, true, true, true, true});


        //habitsData.add(habit1); habitsData.add(habit2);


        // populates habit list

        // setup recycler view
        recyclerView = view.findViewById(R.id.today_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TodayHabitRecyclerAdapter(habitsData);
        recyclerView.setAdapter(adapter);
        Log.d("Habits Size", Integer.toString(habitsData.size()));

        // touch helper for dragging habits
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        /*
        //connect the array adapter
        habitAdapter = new TodayHabitList(getContext(), habitsData);
        recyclerView.setAdapter(habitAdapter);


         */

        refreshTodayFragment(); // populates habit list






        return view;
    }

    /**
     * Touch Helper for dragging habits
     *
     * onMove swaps the position in the adapter
     */
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START
            | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            if (fromPosition < toPosition && toPosition < habitsData.size()) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(habitsData, i, i + 1);
                }
            } else if (toPosition >= 0) {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(habitsData, i, i - 1);
                }
            }
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            Log.d("Position", "From: " + Integer.toString(fromPosition) + " To: " + Integer.toString(toPosition));
            return true;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // nothing function function
        }
    };

    /**
     * refreshHabitList
     *
     * refreshes the habitListView showing habits for today
     *
     * @author Dakota
     */

    public void refreshTodayFragment() {

        Log.d(TAG, "refreshing today frag");

        // Populate today view with Today's habits.

        habitsData.clear();// Make sure is clear

        Habit_List mainUserHabits = mainUser.getHabitList(); // get HabitsList

        Log.d(TAG, "size of mainUserHabits: " + String.valueOf(mainUserHabits.size()));



        // Iterates through all habits
        for (int index = 0; index < mainUserHabits.size(); index++){

            Log.d(TAG, "is on day?: " + String.valueOf(mainUserHabits.get(index).getOnDaysObj().isOnDay()));
            Log.d(TAG, "is habit start: " + String.valueOf(mainUserHabits.get(index).isHabitStart()));

            if (mainUserHabits.get(index).getOnDaysObj().isOnDay() && mainUserHabits.get(index).isHabitStart()){ // If a habit is active today add

                Log.d(TAG, "populating display");

                habitsData.add(mainUserHabits.get(index));
                adapter.notifyDataSetChanged();
                Log.d("Habits Size", Integer.toString(habitsData.size()));
            }
        }

    }



}