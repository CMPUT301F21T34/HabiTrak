package com.cmput301f21t34.habittrak;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cmput301f21t34.habittrak.recycler.EventList;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitEvent;

import java.util.ArrayList;

/**
 * ViewHabitEvents.
 *
 * @author Aron Rajabi
 *
 * View and delete habit events tied to a habit.
 *
 * @version 1.0
 * @since 2021-11-10
 */
public class ViewHabitEvents extends AppCompatActivity implements View.OnClickListener {

    // data variables
    private Habit habit;
    private int habitPosition;
    private HabitEvent selectedEvent;
    // List variables
    public ListView eventList;
    public ArrayList<HabitEvent> eventDataList;
    public ArrayAdapter<HabitEvent> eventListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout
        setContentView(R.layout.activity_view_habit_events);

        // Set back button up
        Toolbar toolbar = (Toolbar) findViewById(R.id.events_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get habit data from the intent
        Intent intent = getIntent();
        this.habit = intent.getParcelableExtra("HABIT");
        this.habitPosition = intent.getIntExtra("position", 0);
        Log.d("VIEW_HABIT", Integer.toString(habitPosition));

        //Set up our list of events for display
        eventList = findViewById(R.id.event_list);
        eventDataList = habit.getHabitEvents();
        eventListAdapter = new EventList(this, eventDataList);
        eventList.setAdapter(eventListAdapter);

        //Create a click listener that will track the last event clcked
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedEvent = (HabitEvent) adapterView.getItemAtPosition(i);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
