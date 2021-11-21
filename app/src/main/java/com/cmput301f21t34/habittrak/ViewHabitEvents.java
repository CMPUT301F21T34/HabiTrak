package com.cmput301f21t34.habittrak;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cmput301f21t34.habittrak.recycler.EventList;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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

    // activity launcher

    ActivityResultLauncher<Intent> viewEditHabitEventActivityLauncher;

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
        // creating edit events activity launcher
        viewEditHabitEventActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK){
                    Log.d("Edit_HabitEvents","In the on actiivty result");
                    HabitEvent habitEvent = (HabitEvent) result.getData().getParcelableExtra("HABIT_EVENT_SAVE");
                    Log.d("Edit_HabitEvents","got the habit event  comment is "+ habitEvent.getComment());
                    habit.addHabitEvent(habitEvent);
                    habit.removeHabitEvent(selectedEvent);

//                  ViewHabitEvents.this.habit.addHabitEvent(habitEvent);
                    Log.d("ViewEditHabitEvents","added the habit event and updating list");
                    updateList();


                }
            }
        });
    }

    public void updateList(){
        eventDataList.clear();
        eventDataList.addAll(this.habit.getHabitEvents());
//        Collections.sort(eventDataList,Collections.reverseOrder());
        eventListAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == EDIT_HABIT_EVENT){
            HabitEvent habitEvent = (HabitEvent) intent.getParcelableExtra("HABIT_EVENT_SAVE");
            Log.d("EDITHAbit",habitEvent.getComment());
            this.habit.addHabitEvent(habitEvent);
            this.habit.removeHabitEvent(selectedEvent);
            //this.habit.addHabitEvent(habitEvent);
            Log.d("ViewEditHabitEvents","added the habit event and updating list");
            updateList();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //For delete event button press
            case R.id.event_deleter:
                this.habit.removeHabitEvent(selectedEvent);
                updateList();
                break;
            //For edit event button press
            case R.id.event_editor:
                // call view edit habit events
                Log.d("ViewHabitEvents","inside the event_editor");
                Log.d("ViewHabitEvents", " the comment is "+selectedEvent.getComment());
                Intent intent = new Intent(view.getContext(),ViewEditHabitEvents.class);
                intent.putExtra("HABIT_EVENT_VIEW", selectedEvent);
              //  this.habit.removeHabitEvent(selectedEvent);
                Log.d("ViewHabitEvents","Entering view edit habit event activity launcher");
               // viewEditHabitEventActivityLauncher.launch(intent);
                startActivityForResult(intent,EDIT_HABIT_EVENT);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
