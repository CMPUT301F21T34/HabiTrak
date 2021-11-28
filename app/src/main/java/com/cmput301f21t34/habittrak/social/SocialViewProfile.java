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
import com.cmput301f21t34.habittrak.recycler.HabitRecyclerAdapter;
import com.cmput301f21t34.habittrak.user.HabitList;
import com.cmput301f21t34.habittrak.user.User;

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

        // set TextView
        username.setText(user.getUsername());
        if (!user.getBiography().equals("")){
            bio.setText(user.getBiography());
        }

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}