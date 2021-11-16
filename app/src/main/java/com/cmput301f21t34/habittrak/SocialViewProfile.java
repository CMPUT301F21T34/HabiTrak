package com.cmput301f21t34.habittrak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.cmput301f21t34.habittrak.recycler.HabitRecycler;
import com.cmput301f21t34.habittrak.recycler.TodayHabitRecyclerAdapter;
import com.cmput301f21t34.habittrak.user.User;
import com.cmput301f21t34.habittrak.user.Habit;

import java.util.ArrayList;

/**
 * SocialViewProfile.
 *
 * @author Pranav
 *
 * View the user's profile with the public habits.
 */
public class SocialViewProfile extends AppCompatActivity {

    private User user;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SocialAdapter socialAdapter;
    private TextView username;
    private TextView bio;
    private HabitRecycler habitRecycler;
    private TodayHabitRecyclerAdapter adapter;
    private ArrayList<Habit> habitList;
    private ArrayList<Habit> displayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_view_profile);

        // add back button to toolbar
        Toolbar toolbar = findViewById(R.id.social_view_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // get data
        Intent intent = getIntent();
        user = intent.getParcelableExtra("USER");

        // set view
        recyclerView = findViewById(R.id.social_profile_recycler);
        username = findViewById(R.id.social_profile_username);
        bio = findViewById(R.id.social_profile_bio);


        // set TextView
        username.setText(user.getUsername());
        bio.setText(user.getBiography());

        // get public habits
        getPublicHabits();

        // set recycler view
        layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TodayHabitRecyclerAdapter(displayList, false);
        adapter.setHabitClickListener(new TodayHabitRecyclerAdapter.HabitClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // setup on item click
            }

            @Override
            public void menuButtonOnClick(View view, int position) {
                // no action here
            }

            @Override
            public void checkBoxOnClick(View view, int position) {
                // no action here
            }
        });
        habitRecycler = new HabitRecycler(recyclerView, layoutManager, displayList, user.getHabitList(), true);
        habitRecycler.setAdapter(adapter);

    }

    /**
     * getPublicHabits
     *
     * sets the displayList to the public habits of the User.
     */
    public void getPublicHabits(){
        habitList = user.getHabitList();
        displayList = new ArrayList<Habit>();
        for (int i = 0; i < habitList.size(); i++){
            if (habitList.get(i).isPublic()){
                displayList.add(habitList.get(i));
            }
        }
    }
}