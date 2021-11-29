package com.cmput301f21t34.habittrak.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.HabitActivity.ViewEditHabitActivity;
import com.cmput301f21t34.habittrak.recycler.HabitRecycler;
import com.cmput301f21t34.habittrak.recycler.HabitRecyclerAdapter;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitList;
import com.cmput301f21t34.habittrak.user.User;

import java.util.ArrayList;

/**
 * AllHabitsFragment.
 *
 * @author Pranav
 * @author Dakota
 *
 * Fragment for displaying all the user's habits, viewing and editing them when clicked
 *
 * @version 1.0
 */
public class AllHabitsFragment extends Fragment {

    // views
    private HabitRecycler habitRecycler;
    private final ArrayList<Habit> habitsDisplayList;
    private final HabitRecyclerAdapter adapter;
    private LinearLayout noDataLayout;
    // data
    private final User mainUser;
    private final DatabaseManager dm = new DatabaseManager();

    public AllHabitsFragment(User mainUser) {
        habitsDisplayList = new ArrayList<>();
        this.mainUser = mainUser;
        this.adapter = new HabitRecyclerAdapter(habitsDisplayList, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.habi_all_habits_fragment, container, false);

        // Sets up views and manager for recycler view
        noDataLayout = view.findViewById(R.id.all_habit_no_data_view);
        // These are for the Recycler view
        RecyclerView habitRecyclerView = view.findViewById(R.id.all_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        // Set the click listener interface for the adapter
        adapter.setHabitClickListener(new HabitRecyclerAdapter.HabitClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // start the view/edit habit activity when recycler view row is clicked
                Habit habit = habitsDisplayList.get(position);
                Intent intent = new Intent(getContext(), ViewEditHabitActivity.class);
                intent.putExtra("HABIT", habit);
                intent.putExtra("position", habit.getIndex());
                viewHabitResultLauncher.launch(intent);
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
        setLayoutVisibility();
        return view;
    }

    /**
     * refresh the data list on fragment resume
     */
    @Override
    public void onResume() {
        super.onResume();
        refreshAllFragment();
    }

    /**
     * set the layout visibility depending on the data in the displayList
     */
    public void setLayoutVisibility() {
       // if list is empty, then hide the recycler view and show no data text
        if (!(noDataLayout == null)) {
            if (habitsDisplayList.isEmpty()) {
                noDataLayout.setVisibility(View.VISIBLE);
                habitRecycler.setRecyclerVisibility(false);
            } else {
                noDataLayout.setVisibility(View.GONE);
                habitRecycler.setRecyclerVisibility(true);
            }
        }
    }

    /**
     * refreshAllFragment
     *
     * @author Dakota
     *
     * refresh the habits data list to update the recycler view data
     */
    @SuppressLint("NotifyDataSetChanged")
    public void refreshAllFragment() {
        // Populate today view with Today's habits.
        habitsDisplayList.clear(); // Make sure is clear
        HabitList mainUserHabits = mainUser.getHabitList();
        habitsDisplayList.addAll(mainUserHabits);
        adapter.notifyDataSetChanged(); // must notify completely as what has changed is unknown
        setLayoutVisibility();
    }

    ActivityResultLauncher<Intent> viewHabitResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {}
    );

    /**
     * showMenu
     *
     * create a menu when Image Button is clicked
     * used for removing a habit
     * @param view view from the adapter to create the menu
     * @param position position of habit from adapter
     */
    public void showMenu(View view, int position) {
        // create menu
        PopupMenu menu = new PopupMenu(getContext(), view);
        menu.getMenuInflater().inflate(R.menu.social_popup_menu, menu.getMenu());
        menu.getMenu().add("Remove");
        menu.show();

        // menu listener
        menu.setOnMenuItemClickListener(menuItem -> {
            Habit habit = habitsDisplayList.get(position);
            mainUser.getHabitList().remove(habit);
            refreshAllFragment();
            dm.updateHabitList(mainUser.getEmail(), mainUser.getHabitList());
            return true;
        });
    }
}