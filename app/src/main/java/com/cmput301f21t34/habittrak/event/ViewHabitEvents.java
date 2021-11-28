package com.cmput301f21t34.habittrak.event;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.recycler.EventAdapter;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitEvent;

import java.util.ArrayList;

/**
 * ViewHabitEvents.
 *
 * @author Aron Rajabi
 * @author Pranav
 *
 * View and delete habit events tied to a habit.
 *
 * @version 2.0
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
    private LinearLayout noDataLayout;
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
        noDataLayout = findViewById(R.id.events_no_data_view);

        // get habit data from the intent
        Intent intent = getIntent();
        habit = intent.getParcelableExtra("HABIT");
        habitPosition = intent.getIntExtra("position", 0);
        eventDataList =  habit.getHabitEvents();

        //Set up our list of events for display
        toolbar.setTitle(habit.getTitle() + "'s events");


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
        setNoDataLayout();

    }

    /**
     * setNoDataLayout
     *
     * sets the visibility to gone of the noDataLayout if habits events list is empty
     */
    public void setNoDataLayout(){
        if (noDataLayout != null) {
            if (eventDataList.isEmpty())
                noDataLayout.setVisibility(View.VISIBLE);
            else noDataLayout.setVisibility(View.GONE);
        }
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
            Toast.makeText(this, "Event Removed", Toast.LENGTH_SHORT).show();
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
        setNoDataLayout();
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
        intent.putExtra("HABIT_TITLE",habit.getTitle());
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
