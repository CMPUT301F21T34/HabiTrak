package com.cmput301f21t34.habittrak.social;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.recycler.HabitRecycler;
import com.cmput301f21t34.habittrak.recycler.SocialAdapter;
import com.cmput301f21t34.habittrak.recycler.TodayHabitRecyclerAdapter;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.User;

import java.util.ArrayList;

/**
 * SocialViewProfile.
 *
 * @author Pranav
 * <p>
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
        habitList.removeIf(habit -> !habit.isPublic());

        // set recycler view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TodayHabitRecyclerAdapter(habitList, false);
        adapter.setHabitClickListener(new TodayHabitRecyclerAdapter.HabitClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onHabitClick(view, position);
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
        habitRecycler = new HabitRecycler(recyclerView, layoutManager, habitList, user.getHabitList(), true);
        habitRecycler.setAdapter(adapter);

    }

    public void onHabitClick(View view, int position) {
        // start View Habit Activity
    }
}