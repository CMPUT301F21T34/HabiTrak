package com.cmput301f21t34.habittrak.fragments;

import android.content.Intent;
import android.os.Bundle;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.ViewEditHabit;
import com.cmput301f21t34.habittrak.recycler.HabitRecycler;
import com.cmput301f21t34.habittrak.recycler.TodayHabitRecyclerAdapter;
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


    // Attributes //
    public static String TAG = "All_Habits";
    // These are for the Recycler view
    private RecyclerView habitRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HabitRecycler habitRecycler;
    private ArrayList<Habit> habitsDisplayList;
    private TodayHabitRecyclerAdapter adapter;
    private User mainUser;

    public AllHabitsFragment(User mainUser) {

        habitsDisplayList = new ArrayList<>();
        this.mainUser = mainUser;
        this.adapter = new TodayHabitRecyclerAdapter(habitsDisplayList);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.habi_all_habits_fragment, container, false);

        // Sets up views and manager for recycler view
        habitRecyclerView = view.findViewById(R.id.all_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());


        // set the click listener interface for the adapter
        adapter.setHabitClickListener(new TodayHabitRecyclerAdapter.HabitClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Habit habit = habitsDisplayList.get(position);
                Intent intent = new Intent(getContext(), ViewEditHabit.class);
                intent.putExtra("HABIT", habit);
                intent.putExtra("position", habit.getIndex());
                viewHabitResultLauncher.launch(intent);
            }

            @Override
            public void menuButtonOnClick(View view, int position) {

            }
        });
        // creates a new habitRecycler class with the view and data
        habitRecycler = new HabitRecycler(habitRecyclerView, layoutManager, habitsDisplayList, mainUser.getHabitList());
        habitRecycler.setAdapter(adapter);

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

        Log.d(TAG, "refreshing habit list");
        // Populate today view with Today's habits.
        habitsDisplayList.clear(); // Make sure is clear
        HabitList mainUserHabits = mainUser.getHabitList(); // get HabitsList
        habitsDisplayList.addAll(mainUserHabits);
        // tells the adapter in recycler that the dataset has changed
        adapter.notifyDataSetChanged();

    }

    // activity result launcher for view/edit activity
    ActivityResultLauncher<Intent> viewHabitResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {}
    );

}