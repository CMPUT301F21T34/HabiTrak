package com.cmput301f21t34.habittrak.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmput301f21t34.habittrak.AddHabit;
import com.cmput301f21t34.habittrak.BaseActivity;
import com.cmput301f21t34.habittrak.Habit;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.TodayHabitList;
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
    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitsData;

    User mainUser;



    // constructor
    public TodayListFragment(User mainUser) {

        this.mainUser = mainUser;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.habi_today_fragment, container, false);
        habitList = view.findViewById(R.id.today_listview);


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
                Intent intent = new Intent(view.getContext(), AddHabit.class);
                addHabitActivityLauncher.launch(intent);
            }
        });

        refreshHabitList(); // populates habit list
        //connect the array adapter
        habitAdapter = new TodayHabitList(getContext(), habitsData);
        habitList.setAdapter(habitAdapter);






        return view;
    }

    /**
     *  Launch add habit activity for result
     */
    ActivityResultLauncher<Intent> addHabitActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == BaseActivity.RESULT_NEW_HABIT) {
                        Habit newHabit = result.getData().getParcelableExtra("newHabit");
                        Log.d("newHabit", "in TodayListFrag newHabit: " + newHabit.getTitle());

                        refreshHabitList(); // refresh habit list


                    }
                }

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

        // Itterates through all habits
        for (int index = 0; index < mainUserHabits.size(); index++){
            if (mainUserHabits.get(index).isOnDay()){ // If a habit is active today add
                habitsData.add(mainUserHabits.get(index));
                habitAdapter.notifyDataSetChanged();
            }
        }

    }


}