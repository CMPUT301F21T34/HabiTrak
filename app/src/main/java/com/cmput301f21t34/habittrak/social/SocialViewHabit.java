package com.cmput301f21t34.habittrak.social;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.user.Habit;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * SocialViewHabit class for viewing the habit list of the user that the current user clicks on
 *
 * @author Pranav
 */
public class SocialViewHabit extends AppCompatActivity {

    private final int whiteColor = Color.WHITE;
    private Habit habit;
    private MaterialButton mondayButton;
    private MaterialButton tuesdayButton;
    private MaterialButton wednesdayButton;
    private MaterialButton thursdayButton;
    private MaterialButton fridayButton;
    private MaterialButton saturdayButton;
    private MaterialButton sundayButton;
    private int tealColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_view_habit);

        // get data
        Intent intent = getIntent();
        habit = intent.getParcelableExtra("HABIT");
        String username = intent.getStringExtra("USERNAME");

        // set up toolbar
        Toolbar toolbar = findViewById(R.id.social_view_habit_toolbar);
        toolbar.setTitle(username + "'s habit");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // set views
        TextView name = findViewById(R.id.social_habit_name);
        TextView reason = findViewById(R.id.social_habit_reason);
        TextView date = findViewById(R.id.social_habit_date);
        mondayButton = findViewById(R.id.monday_button);
        tuesdayButton = findViewById(R.id.tuesday_button);
        wednesdayButton = findViewById(R.id.wednesday_button);
        thursdayButton = findViewById(R.id.thursday_button);
        fridayButton = findViewById(R.id.friday_button);
        saturdayButton = findViewById(R.id.saturday_button);
        sundayButton = findViewById(R.id.sunday_button);
        tealColor = ContextCompat.getColor(getBaseContext(), R.color.teal_200);
        TextView totalEventsCompleted = findViewById(R.id.social_view_total_events);
        LinearProgressIndicator progressBar = findViewById(R.id.social_view_habit_progress);
        TextView progressBarText = findViewById(R.id.social_view_habit_streak);
        TextView bestStreakStart = findViewById(R.id.social_streak_start);
        TextView bestStreakEnd = findViewById(R.id.social_best_streak_end);
        TextView bestStreakTotal = findViewById(R.id.social_best_streak_count);

        // set data for views
        name.setText(habit.getTitle());
        reason.setText(habit.getReason());
        date.setText(getDate(habit.getStartDate()));
        setDaysSelector();
        totalEventsCompleted.setText(String.valueOf(habit.getHabitEvents().size()));
        progressBar.setProgress( (habit.getCurrentStreak()*100)/30 );
        String currentStreakText = habit.getCurrentStreak() + "/30";
        progressBarText.setText(currentStreakText);

        SimpleDateFormat streakDateFormat = new SimpleDateFormat("MMM dd, yyyy");

        if (habit.getBestStreakDate() == null || habit.getBestStreakDateEnd() == null) {
            bestStreakStart.setText("N/A");
            bestStreakEnd.setText("N/A");
        } else {
            bestStreakStart.setText(streakDateFormat.format(habit.getBestStreakDate().getTime()));
            bestStreakEnd.setText(streakDateFormat.format(habit.getBestStreakDateEnd().getTime()));
            bestStreakTotal.setText(String.valueOf(habit.getBestStreak()));
        }
        // setting listeners to null
        mondayButton.setOnClickListener(null);
        tuesdayButton.setOnClickListener(null);
        wednesdayButton.setOnClickListener(null);
        thursdayButton.setOnClickListener(null);
        fridayButton.setOnClickListener(null);
        saturdayButton.setOnClickListener(null);
        sundayButton.setOnClickListener(null);
    }

    /**
     * get the String value from calendar
     *
     * @param calendar date to convert to string
     * @return string value of type Month, Day
     */
    public String getDate(Calendar calendar) {
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        return month + ", " + day;
    }

    /**
     * setDaysSelector.
     *
     * @author Pranav
     * <p>
     * sets the state of the days of week buttons.
     */
    public void setDaysSelector() {
        boolean[] daysOfWeek = habit.getOnDaysObj().getAll();
        setButtonState(mondayButton, daysOfWeek[0]);
        setButtonState(tuesdayButton, daysOfWeek[1]);
        setButtonState(wednesdayButton, daysOfWeek[2]);
        setButtonState(thursdayButton, daysOfWeek[3]);
        setButtonState(fridayButton, daysOfWeek[4]);
        setButtonState(saturdayButton, daysOfWeek[5]);
        setButtonState(sundayButton, daysOfWeek[6]);
    }

    /**
     * setButtonState.
     *
     * @param button MaterialButton for setting state
     * @param state  bool value for on/off state
     * @author Pranav
     * <p>
     * set the button on/off state
     */
    public void setButtonState(MaterialButton button, Boolean state) {
        if (state) {
            button.setBackgroundColor(tealColor);
        } else {
            button.setBackgroundColor(whiteColor);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}