package com.cmput301f21t34.habittrak;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

    private static final int EDIT_HABIT_EVENT = 4000;
    // data variables
    private Habit habit;
    private int habitPosition;
    private HabitEvent selectedEvent;
    // UI variables
    private ListView eventList;
    private ArrayList<HabitEvent> eventDataList;
    private ArrayAdapter<HabitEvent> eventListAdapter;
    private Toolbar toolbar;
    private Button editBtn;
    private Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout
        setContentView(R.layout.activity_view_habit_events);

        // Set back button up
        toolbar = findViewById(R.id.events_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get views
        editBtn = findViewById(R.id.event_editor);
        deleteBtn = findViewById(R.id.event_deleter);

        // get habit data from the intent
        Intent intent = getIntent();
        this.habit = intent.getParcelableExtra("HABIT");
        this.habitPosition = intent.getIntExtra("position", 0);
        Log.d("VIEW_HABIT", Integer.toString(habitPosition));
        String name = this.habit.getTitle();
        String nameFinisher = " Events";

        //Set up our list of events for display
        toolbar.setTitle(name + nameFinisher);
        eventList = findViewById(R.id.event_list);
        eventDataList = habit.getHabitEvents();
        eventListAdapter = new EventList(this, eventDataList);
        eventList.setAdapter(eventListAdapter);

        // setting up the buttons listener
        editBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

        //Create a click listener that will track the last event clcked
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedEvent = (HabitEvent) adapterView.getItemAtPosition(i);
                Log.d("ViewHabitEvents","clicked on a habit event");
                Log.d("ViewHabitEvents",selectedEvent.getComment());
            }
        });
    }

    public void updateList(){
        eventDataList.clear();
        eventDataList.addAll(this.habit.getHabitEvents());
//        Collections.sort(eventDataList,Collections.reverseOrder());
        eventListAdapter.notifyDataSetChanged();
    }

    // Set up activity launcher
    ActivityResultLauncher<Intent> editEventsResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {}
    );

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == EDIT_HABIT_EVENT){
            habit = (Habit) intent.getParcelableExtra("HABIT_SAVE");
            Log.d("EDITHAbit",habit.getTitle());
            selectedEvent = null;
            Log.d("ViewEditHabitEvents","added the habit event and updating list");
            Log.d("EDIT_HA","The size of the habit event list is " + habit.getHabitEvents().size());
            updateList();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //For delete event button press
            case R.id.event_deleter:
                if (selectedEvent != null) {
                    this.habit.removeHabitEvent(selectedEvent);
                    updateList();
                }
                else{
                    Toast.makeText(this, "Have to select a habit event", Toast.LENGTH_LONG).show();
                }
                break;
            //For edit event button press
            case R.id.event_editor:
                // call view edit habit events
                if(selectedEvent != null){

                    this.habit.removeHabitEvent(selectedEvent);
                    Intent intent = new Intent(view.getContext(),ViewEditHabitEvents.class);
                    intent.putExtra("HABIT_EVENT_VIEW", selectedEvent);
                    intent.putExtra("HABIT_VIEW",habit);

                    editEventsResultLauncher.launch(intent);
                    updateList();
                }
                else{
                    Toast.makeText(this, "Have to select a habit event", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
