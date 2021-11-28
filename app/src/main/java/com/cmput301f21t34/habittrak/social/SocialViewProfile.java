package com.cmput301f21t34.habittrak.social;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.recycler.HabitRecycler;
import com.cmput301f21t34.habittrak.recycler.SocialAdapter;
import com.cmput301f21t34.habittrak.recycler.HabitRecyclerAdapter;
import com.cmput301f21t34.habittrak.streak.Streak;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitList;
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
    private LinearLayout noDataLayout;
    private HabitRecycler habitRecycler;
    private HabitRecyclerAdapter adapter;
    private HabitList habitList = new HabitList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_view_profile);

        // get data
        Intent intent = getIntent();
        user = intent.getParcelableExtra("USER");

        // add back button to toolbar
        Toolbar toolbar = findViewById(R.id.social_view_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle(user.getUsername() + "'s public habits");

        // set view
        recyclerView = findViewById(R.id.social_profile_recycler);
        username = findViewById(R.id.social_profile_username);
        bio = findViewById(R.id.social_profile_bio);
        noDataLayout = findViewById(R.id.social_no_data_view);

        // set TextView
        username.setText(user.getUsername());
        if (!user.getBiography().equals("")){
            bio.setText(user.getBiography());
        }

        // calculate streaks
        refreshHabitStreak();

        // get public habits
        getPublicHabits();

        // set recycler view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HabitRecyclerAdapter(habitList, false);
        adapter.setHabitClickListener(new HabitRecyclerAdapter.HabitClickListener() {
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
        habitRecycler = new HabitRecycler(recyclerView, layoutManager, user.getHabitList(), user.getHabitList(), true);
        habitRecycler.setAdapter(adapter);

        if (habitList.isEmpty())
            noDataLayout.setVisibility(View.VISIBLE);
    }

    /**
     * start view social habit activity
     * @param view
     * @param position
     */
    public void onHabitClick(View view, int position) {
        // start View Habit Activity
        Intent intent = new Intent(this, SocialViewHabit.class);
        intent.putExtra("HABIT", habitList.get(position));
        intent.putExtra("USERNAME", user.getUsername());
        startActivity(intent);
    }

    /**
     * gets the public habits of the user
     */
    public void getPublicHabits(){
        for (int i = 0; i < user.getHabitList().size(); i++) {
            if (user.getHabitList().get(i).isPublic())
                habitList.add(user.getHabitList().get(i));
        }
    }

    /**
     * refreshes all habit streaks
     *
     * @author Dakota
     */
    private void refreshHabitStreak() {
        // Refreshes all habit streaks //
        ArrayList<Habit> habits = (ArrayList<Habit>) user.getHabitList(); // cast for simple iteration

        for (int index = 0; index < habits.size(); index++){
            Streak streak = new Streak(habits.get(index)); // set a Streak class to modify each habit
            streak.refreshStreak(); // refreshes each streak
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}