package com.cmput301f21t34.habittrak.fragments;

import android.os.Bundle;

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

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.TodayHabitList;
import com.cmput301f21t34.habittrak.recycler.HabitRecycler;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.Habit_List;
import com.cmput301f21t34.habittrak.user.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Fragment for displaying all habits
 */
public class AllHabitsFragment extends Fragment {

    // attributes
    private RecyclerView habitList;
    private RecyclerView.LayoutManager layoutManager;
    private HabitRecycler recycler;
    private ArrayAdapter<Habit> habitAdapter;
    private ArrayList<Habit> habitsData;
    private ItemTouchHelper.SimpleCallback recyclerListCallBack;

    User mainUser;

    public AllHabitsFragment(User mainUser) {

        habitsData = new ArrayList<>();
        this.mainUser = mainUser;


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.habi_all_habits_fragment, container, false);

        // Sets up views and manager for recycler view
        habitList = view.findViewById(R.id.all_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());



        Log.d("mainUser", "in AllHabitsFragment mainUser: " + mainUser.getUsername());




        //connect the array adapter
        //habitAdapter = new TodayHabitList(getContext(), habitsData);
        //habitList.setAdapter(habitAdapter);

        //refreshAllFragment(); // populates habit list

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();

        // Runs the recyclerList
        recyclerList();

        // Refreshes Frag
        refreshAllFragment();

    }

    private void recyclerList(){


        this.recycler = new HabitRecycler(habitList, layoutManager, habitsData, mainUser.getHabitList());
        this.recyclerListCallBack = recycler.simpleCallback();



    }

    public void refreshAllFragment() {

        Log.d("TodayListFragment", "refreshing habit list");
        // Populate today view with Today's habits.

        habitsData.clear(); // Make sure is clear

        ArrayList<Habit> mainUserHabits = mainUser.getHabitList(); // get HabitsList

        habitsData.addAll(mainUserHabits);

        // tells the adapter in recycler that the dataset has changed
        recycler.notifyDataSetChanged();

    }
}