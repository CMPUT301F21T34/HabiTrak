package com.cmput301f21t34.habittrak.fragments;

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

import com.cmput301f21t34.habittrak.HabitActivity.ViewEditHabitActivity;
import com.cmput301f21t34.habittrak.event.AddHabitEventActivity;
import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.recycler.HabitRecycler;
import com.cmput301f21t34.habittrak.recycler.HabitRecyclerAdapter;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.R;

import com.cmput301f21t34.habittrak.user.HabitList;

import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.checkbox.MaterialCheckBox;

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
    private HabitRecyclerAdapter adapter;
    private LinearLayout noDataLayout;
    private User mainUser;
    // Database Manager
    private final DatabaseManager dm = new DatabaseManager();

    // constructor
    public TodayListFragment(User mainUser) {
        habitsDisplayList = new ArrayList<>();
        this.mainUser = mainUser;
        adapter = new HabitRecyclerAdapter(habitsDisplayList, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.habi_today_fragment, container, false);

        // Sets up views and manager for recycler view
        habitRecyclerView = view.findViewById(R.id.today_recycler_view);
        noDataLayout = view.findViewById(R.id.today_no_data_view);
        layoutManager = new LinearLayoutManager(getContext());

        // set the click listener interface for the adapter
        adapter.setHabitClickListener(new HabitRecyclerAdapter.HabitClickListener() {
            @Override
            public void onItemClick(View view, int position) {
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
                onCheckBoxClick(view, position);
            }
        });
        // creates a new habitRecycler class with the view and data
        habitRecycler = new HabitRecycler(habitRecyclerView, layoutManager, habitsDisplayList, mainUser.getHabitList(), true);
        habitRecycler.setAdapter(adapter);

        setLayoutVisibility();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refreshes Frag
        refreshTodayFragment();
    }

    public void setLayoutVisibility(){
        if(!(noDataLayout == null)){
            if(habitsDisplayList.isEmpty()){
                noDataLayout.setVisibility(View.VISIBLE);
                habitRecycler.setRecyclerVisibility(false);
            } else {
                noDataLayout.setVisibility(View.GONE);
                habitRecycler.setRecyclerVisibility(true);
            }
        }
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
            if (mainUserHabits.get(index).getOnDaysObj().isOnDay()
                    && mainUserHabits.get(index).isHabitStart()) { // If a habit is active today add
                habitsDisplayList.add(mainUserHabits.get(index));
            }
        }
        setLayoutVisibility();
        adapter.notifyDataSetChanged();
    }
    // activity result launcher for view/edit habit
    ActivityResultLauncher<Intent> viewHabitResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {}
    );
    // activity result launcher for add habit event
    ActivityResultLauncher<Intent> addHabitEventResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {}
    );

    /**
     * showMenu.
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
            mainUser.getHabitList().remove(habit);
            refreshTodayFragment();
            dm.updateHabitList(mainUser.getEmail(), mainUser.getHabitList());
            return true;
        });
    }

    /**
     * onCheckBoxClick.
     *
     * @author Pranav
     * @author Henry
     *
     * listener function for checkbox clicking. Start a add new habit event activity.
     * @param view view of the checkbox
     * @param position position of the clicked button in the adapter
     */
    public void onCheckBoxClick(View view, int position) {
        MaterialCheckBox checkBox = (MaterialCheckBox) view;
        checkBox.setChecked(false);
        Habit habit = habitsDisplayList.get(position);
        Intent intent = new Intent(getContext(), AddHabitEventActivity.class);
        intent.putExtra("HABIT", habit);
        addHabitEventResultLauncher.launch(intent);
    }
}
