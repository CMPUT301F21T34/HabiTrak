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

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.event.ViewHabitEvents;
import com.cmput301f21t34.habittrak.recycler.HabitRecycler;
import com.cmput301f21t34.habittrak.recycler.HabitRecyclerAdapter;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitList;
import com.cmput301f21t34.habittrak.user.User;

import java.util.ArrayList;

/**
 * EventsFragment
 *
 * @author Aron Rajabi
 * <p>
 * Fragment for displaying all habits, viewing its completed events when clicked
 * Recycler view code and onclicklisteners taken from Pranav and Dakotas AllHabitsFragment
 * @version 1.0
 */
public class EventsFragment extends Fragment {

    // view
    private HabitRecycler habitRecycler;
    private final HabitRecyclerAdapter adapter;
    private LinearLayout noDataLayout;
    // data
    private final ArrayList<Habit> habitsDisplayList;
    private final User mainUser;

    public EventsFragment(User mainUser) {
        habitsDisplayList = new ArrayList<>();
        this.mainUser = mainUser;
        this.adapter = new HabitRecyclerAdapter(habitsDisplayList, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.habi_all_habits_fragment, container, false);

        // Sets up views and manager for recycler view
        noDataLayout = view.findViewById(R.id.all_events_no_data_view);
        // These are for the Recycler view
        RecyclerView habitRecyclerView = view.findViewById(R.id.all_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());


        // set the click listener interface for the adapter
        adapter.setHabitClickListener(new HabitRecyclerAdapter.HabitClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Habit habit = habitsDisplayList.get(position);
                Intent intent = new Intent(getContext(), ViewHabitEvents.class);
                intent.putExtra("HABIT", habit);
                intent.putExtra("position", habit.getIndex());
                viewEventsResultLauncher.launch(intent);
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
     * refresh the fragment display data on return
     */
    @Override
    public void onResume() {
        super.onResume();
        // Refreshes Frag
        refreshEventFragment();
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
     * refreshEventFragment
     *
     * @author Dakota
     * <p>
     * refresh the habitsdata list to update the recycler view data
     */
    public void refreshEventFragment() {
        // Populate today view with Today's habits.
        habitsDisplayList.clear(); // Make sure is clear
        HabitList mainUserHabits = mainUser.getHabitList(); // get HabitsList
        habitsDisplayList.addAll(mainUserHabits);
        // tells the adapter in recycler that the dataset has changed
        habitRecycler.notifyDataSetChanged();
        setLayoutVisibility();
    }

    // activity result launcher for view/edit events activity
    ActivityResultLauncher<Intent> viewEventsResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
            }
    );

    /**
     * showMenu
     * <p>
     * create a menu when Image Button is clicked
     * used for removing habits
     *
     * @param view     view from the adapter to create the menu
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
            refreshEventFragment();
            return true;
        });
    }
}