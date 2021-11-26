package com.cmput301f21t34.habittrak;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f21t34.habittrak.recycler.EventAdapter;
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

    public static int RESULT_CODE = 5000;
    private static final int EDIT_HABIT_EVENT = 4000;
    // data variables
    private Habit habit;
    private int habitPosition;
    // UI variables
    private ArrayList<HabitEvent> eventDataList;
    private Toolbar toolbar;
    private Button confirmBtn;
    // recycler view
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private EventAdapter eventAdapter;

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
        confirmBtn = findViewById(R.id.events_confirm_changes);


        // get habit data from the intent
        Intent intent = getIntent();
        habit = intent.getParcelableExtra("HABIT");
        habitPosition = intent.getIntExtra("position", 0);
        eventDataList =  habit.getHabitEvents();
        String name = habit.getTitle();
        String nameFinisher = " Events";

        //Set up our list of events for display
        toolbar.setTitle(name + nameFinisher);


        // set recycler view
        recyclerView = findViewById(R.id.events_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        eventAdapter = new EventAdapter(eventDataList, new EventAdapter.EventClickListener() {
            @Override
            public void menuButtonClick(View view, int position) {
                showMenu(view, position);
            }

            @Override
            public void itemClick(View view, int position) {
                Log.d("Event", "Clicked");
                startViewEventActivity(position);
            }
        });
        recyclerView.setAdapter(eventAdapter);

        // setting up the buttons listener
        confirmBtn.setOnClickListener(this);

    }

    /**
     * create a popup menu when the image button is click in ListView
     * @param view View of the image button
     * @param position position in the listView
     */
    @SuppressLint("NotifyDataSetChanged")
    public void showMenu(View view, int position){
        PopupMenu menu = new PopupMenu(this, view);
        menu.getMenuInflater().inflate(R.menu.social_popup_menu, menu.getMenu());
        menu.getMenu().add("Remove");
        menu.show();

        menu.setOnMenuItemClickListener(menuItem -> {
            HabitEvent event = habit.getHabitEvents().get(position);
            habit.removeHabitEvent(event);
            Toast.makeText(this, "Removed Event", Toast.LENGTH_SHORT).show();
            eventAdapter.notifyDataSetChanged();
            return true;
        });
    }

    /**
     * updateList
     *
     * refreshes the displayed list after a change
     *
     * @author Aron Rajabi
     */
    @SuppressLint("NotifyDataSetChanged")
    public void updateList(HabitEvent event, int position){
        eventDataList.set(position, event);
        eventAdapter.notifyDataSetChanged();
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
            HabitEvent returnedEvent = intent.getParcelableExtra("HABIT_EVENT");
            int eventPosition = intent.getIntExtra("event_position", 0);
            updateList(returnedEvent, eventPosition);
        }
    }

    /**
     * start the view event activity when a event is clicked
     * @param position position of the event in the array list
     */
    public void startViewEventActivity(int position){
        HabitEvent event = habit.getHabitEvents().get(position);
        Intent intent = new Intent(this,ViewEditHabitEvents.class);
        intent.putExtra("HABIT_EVENT_VIEW", event);
        intent.putExtra("HABIT_VIEW",habit);
        intent.putExtra("position", habitPosition);
        intent.putExtra("event_position", position);
        editEventsResultLauncher.launch(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.events_confirm_changes:
                Intent result = new Intent();
                habit.setHabitEvents(eventDataList);
                result.putExtra("HABIT", habit);
                result.putExtra("position", habitPosition);
                setResult(RESULT_CODE, result);
                this.finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
