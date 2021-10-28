package com.cmput301f21t34.habittrak.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301f21t34.habittrak.AddHabitActivity;
import com.cmput301f21t34.habittrak.Habit;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.TodayHabitRecyclerAdapter;
import com.cmput301f21t34.habittrak.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
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

        // Button for adding habit - Dakota
        final FloatingActionButton addHabitButton = view.findViewById(R.id.today_add_habit_button);
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddHabitActivity.class);
                addHabitActivityLauncher.launch(intent);
            }
        });

        refreshHabitList(); // populates habit list

        // setup recycler view
        recyclerView = view.findViewById(R.id.today_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TodayHabitRecyclerAdapter(habitsData);
        recyclerView.setAdapter(adapter);
        Log.d("Habits Size", Integer.toString(habitsData.size()));




        return view;
    }

    /**
     *  Launch add habit activity for result
     */
    ActivityResultLauncher<Intent> addHabitActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                // Result's are not handled here but rather in BaseActivity
                //TODO:
                // implement public function in BaseActivity to launch
                // AddHabitActivity and refresh views
                // so any frag can call AddHabitActivity

            }

    );



    /**
     * refreshHabitList
     *
     * refreshes the habitListView showing habits for today
     *
     * @author Dakota
     */
    public void refreshHabitList() {

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


}