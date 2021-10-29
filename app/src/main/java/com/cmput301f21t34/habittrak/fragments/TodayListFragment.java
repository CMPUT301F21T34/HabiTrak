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
import android.widget.Toast;

import com.cmput301f21t34.habittrak.AddHabitActivity;
import com.cmput301f21t34.habittrak.Habit;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.TodayHabitRecyclerAdapter;
import com.cmput301f21t34.habittrak.User;
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
    // attributes
    ArrayList<Habit> habitsData;
    TodayHabitRecyclerAdapter adapter;
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

        // sample data
        Calendar date = new GregorianCalendar(2021,1,31);
        Habit habit1 = new Habit("exercise dog", "some desc", date, new boolean[]{true, true, true, true, true, true, true});
        Habit habit2 = new Habit("go for a walk", "some desc 2", date, new boolean[]{true, true, true, true, true, true, true});
        habitsData = new ArrayList<>();
        habitsData.add(habit1); habitsData.add(habit2);


        addHabitList(); // populates habit list

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

            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(habitsData, i, i + 1);
                }
            } else {
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
    public void addHabitList() {

        Log.d("TodayListFragment", "refreshing habit list");
        // Populate today view with Today's habits.

        habitsData.clear(); // Make sure is clear

        ArrayList<Habit> mainUserHabits = mainUser.getHabitList(); // get HabitsList

        // Iterates through all habits
        for (int index = 0; index < mainUserHabits.size(); index++){
            if (mainUserHabits.get(index).isOnDay() && mainUserHabits.get(index).isHabitStart()){ // If a habit is active today add
                habitsData.add(mainUserHabits.get(index));
                adapter.notifyDataSetChanged();
                Log.d("Habits Size", Integer.toString(habitsData.size()));
            }
        }

    }

    public void refreshList(Habit newHabit){
        if (newHabit.isOnDay() && newHabit.isHabitStart()) {
            habitsData.add(newHabit);
            adapter.notifyItemInserted(habitsData.size() - 1);
            Log.d("Habits Size", Integer.toString(habitsData.size()));
        }
    }


}