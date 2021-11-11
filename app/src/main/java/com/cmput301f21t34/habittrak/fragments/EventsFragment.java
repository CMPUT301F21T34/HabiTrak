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
import android.widget.PopupMenu;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.ViewEditHabit;
import com.cmput301f21t34.habittrak.ViewHabitEvents;
import com.cmput301f21t34.habittrak.recycler.HabitRecycler;
import com.cmput301f21t34.habittrak.recycler.TodayHabitRecyclerAdapter;
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
    public static String TAG = "Habit_Events";
    // These are for the Recycler view
    private RecyclerView habitRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HabitRecycler habitRecycler;
    private ArrayList<Habit> habitsDisplayList;
    private TodayHabitRecyclerAdapter adapter;

    private User mainUser;

    public EventsFragment(User mainUser) {
        habitsDisplayList = new ArrayList<>();
        this.mainUser = mainUser;
        this.adapter = new TodayHabitRecyclerAdapter(habitsDisplayList, false);
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
                Intent intent = new Intent(getContext(), ViewHabitEvents.class);
                intent.putExtra("HABIT", habit);
                intent.putExtra("position", habit.getIndex());
                startActivity(intent);
            }

            @Override
            public void menuButtonOnClick(View view, int position) {
                showMenu(view, position);
            }

            @Override
            public void checkBoxOnClick(View view, int position) {
                // empty function as no checkbox in this fragment
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
        refreshEventFragment();

    }

    /**
     * refreshEventFragment
     *
     * @author Dakota
     *
     * refresh the habitsdata list to update the data
     */
    public void refreshEventFragment() {

        Log.d("EventsFragment", "refreshing habit list");
        // Populate today view with Today's habits.

        habitsDisplayList.clear(); // Make sure is clear

        HabitList mainUserHabits = mainUser.getHabitList(); // get HabitsList

        habitsDisplayList.addAll(mainUserHabits);

        // tells the adapter in recycler that the dataset has changed
        habitRecycler.notifyDataSetChanged();

    }

    /**
     * showMenu
     *
     * create a menu when Image Button is clicked
     * @param view view from the adapter to create the menu
     * @param position position of habit from adapter
     */
    public void showMenu(View view, int position) {
        PopupMenu menu = new PopupMenu(getContext(), view);
        menu.getMenuInflater().inflate(R.menu.social_popup_menu, menu.getMenu());
        menu.getMenu().add("Remove");
        menu.show();

        menu.setOnMenuItemClickListener(menuItem -> {
            Habit habit = habitsDisplayList.get(position);
            mainUser.removeHabit(habit);
            refreshEventFragment();
            Log.d(TAG,"Habit Removed");
            return true;
        });
    }
}