package com.cmput301f21t34.habittrak.social;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.user.Habit;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Locale;

public class SocialViewHabit extends AppCompatActivity {

    private Habit habit;
    private MaterialButton mondayButton;
    private MaterialButton tuesdayButton;
    private MaterialButton wednesdayButton;
    private MaterialButton thursdayButton;
    private MaterialButton fridayButton;
    private MaterialButton saturdayButton;
    private MaterialButton sundayButton;
    private final int whiteColor = Color.WHITE;
    private int tealColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_view_habit);

        // set up toolbar
        Toolbar toolbar = findViewById(R.id.social_view_habit_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // get data
        Intent intent = getIntent();
        habit = intent.getParcelableExtra("HABIT");

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

        // set data for views
        name.setText(habit.getTitle());
        reason.setText(habit.getReason());
        date.setText(getDate(habit.getStartDate()));
        setDaysSelector();


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
     * @param calendar date to convert to string
     * @return string value of type Month, Day
     */
    public String getDate(Calendar calendar){
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        return month + ", " + day;
    }

    /**
     * setDaysSelector.
     *
     * @author Pranav
     *
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
     * @author Pranav
     *
     * set the button on/off state
     * @param button MaterialButton for setting state
     * @param state bool value for on/off state
     */
    public void setButtonState(MaterialButton button, Boolean state) {
        if(state){
            button.setBackgroundColor(tealColor);
        }
        else{
            button.setBackgroundColor(whiteColor);
        }
    }
}