package com.cmput301f21t34.habittrak.recycler;

import android.icu.text.ListFormatter;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.Habit_List;
import com.cmput301f21t34.habittrak.user.User;

import java.util.ArrayList;

public class HabitRecycler {

    private final String TAG = "HabitRecycler";

    private ArrayList<Habit> displayHabits;
    private Habit_List habits;
    private TodayHabitRecyclerAdapter adapter;

    private ItemTouchHelper.SimpleCallback simpleCallback;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;


    public HabitRecycler(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, ArrayList<Habit> displayHabits, Habit_List habits){



        // Sets up two list of habits, the ones to display and the Habit_List of all habits
        this.displayHabits = displayHabits;
        this.habits = habits;

        // Sets up recycler view and its layout manger
        this.recyclerView = recyclerView;
        this.layoutManager = layoutManager;
        recyclerView.setLayoutManager(layoutManager);

        // Sets up our adapter with our displayHabits
        adapter = new TodayHabitRecyclerAdapter(displayHabits);
        recyclerView.setAdapter(adapter);

        // Sets up our simpleCallBack that manages the moving of objects in
        // the recycler view
        this.simpleCallback = simpleCallback();

        // Sets up touch handling with the recycler view
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);




    }



    public ItemTouchHelper.SimpleCallback simpleCallback(){

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

                if (fromPosition < toPosition && toPosition < displayHabits.size()) {
                    for (int i = fromPosition; i < toPosition; i++) {

                        Log.d(TAG, "==swapping==");

                        Habit habit1 = displayHabits.get(i);
                        Habit habit2 = displayHabits.get(i + 1);

                        Log.d(TAG, "habit1 : " + String.valueOf(habit1.getTitle()) + "index in display: " + String.valueOf(i));
                        Log.d(TAG, "habit2 : " + String.valueOf(habit2.getTitle()) + "index in display: " + String.valueOf(i + 1) );


                        Log.d(TAG, "habit1 index before swap: " + String.valueOf(habit1.getIndex()));
                        Log.d(TAG, "habit2 index before swap: " + String.valueOf(habit2.getIndex()));



                        // We swap the habits in the main list rather then the views particular list
                        habits.swap(habit1.getIndex(), habit2.getIndex());

                        // Then we swap them in the display so that the for loop can continue before refreshing displayHabits
                        displayHabits.set(i, habit2);
                        displayHabits.set(i + 1, habit1);

                        Log.d(TAG, "habit1 index after swap: " + String.valueOf(habit1.getIndex()));
                        Log.d(TAG, "habit2 index after swap: " + String.valueOf(habit2.getIndex()));


                        Log.d(TAG, "====end====");

                        //habitsData.swap(i, i + 1);

                    }
                } else if (toPosition >= 0) {
                    for (int i = fromPosition; i > toPosition; i--) {

                        Log.d(TAG, "==swapping==");

                        Habit habit1 = displayHabits.get(i);
                        Habit habit2 = displayHabits.get(i - 1);

                        Log.d(TAG, "habit1 : " + String.valueOf(habit1.getTitle()) + " index in display: " + String.valueOf(i));
                        Log.d(TAG, "habit2 : " + String.valueOf(habit2.getTitle()) + " index in display: " + String.valueOf(i - 1) );

                        Log.d(TAG, "habit1 index before swap: " + String.valueOf(habit1.getIndex()));
                        Log.d(TAG, "habit2 index before swap: " + String.valueOf(habit2.getIndex()));


                        // We swap the habits in the main list rather then the views particular list
                        habits.swap(habit1.getIndex(), habit2.getIndex());

                        // Then we swap them in the display so that the for loop can continue before refreshing displayHabits
                        displayHabits.set(i, habit2);
                        displayHabits.set(i - 1, habit1);


                        Log.d(TAG, "habit1 index after swap: " + String.valueOf(habit1.getIndex()));
                        Log.d(TAG, "habit2 index after swap: " + String.valueOf(habit2.getIndex()));

                        Log.d(TAG, "====end====");
                    }
                }
                Log.d("Habit", "moved");





                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                Log.d("Position", "From: " + Integer.toString(fromPosition) + " To: " + Integer.toString(toPosition));
                return true;
            }

            @Override
            public boolean isLongPressDragEnabled() {
                Log.d(TAG, "long press enabled");
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // nothing function function
            }
        };

        return simpleCallback;

    }

    public void notifyDataSetChanged(){
        this.adapter.notifyDataSetChanged();
    }









}
