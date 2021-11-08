package com.cmput301f21t34.habittrak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;

import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;


/**
 * ViewEditHabit
 *
 *
 * @author Pranav
 *
 * View and Edit selected habit from Base Fragments
 *
 * @version 1.0
 * @since 2021-11-08
 */
public class ViewEditHabit extends AppCompatActivity {

    TextInputEditText habitName;
    TextInputEditText habitReason;
    MaterialButton datePickerButton;
    TextView startDate;
    Calendar calendar;
    MaterialButton saveButton;
    MaterialButton mondayButton;
    MaterialButton tuesdayButton;
    MaterialButton wednesdayButton;
    MaterialButton thursdayButton;
    MaterialButton fridayButton;
    MaterialButton saturdayButton;
    MaterialButton sundayButton;
    boolean[] daysOfWeek;
    int whiteColor = Color.WHITE;
    int tealColor;

    User mainUser;
    Habit habit;
    int habitPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_habit);

        // add back button to toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_habit_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // get data
        Intent intent = getIntent();
        this.mainUser = intent.getParcelableExtra("mainUser");
        this.habitPosition = intent.getIntExtra("position", 0);
        this.habit = mainUser.getHabit(habitPosition);
        // getting views
        habitName = findViewById(R.id.view_habit_name_edit_text);
        habitReason = findViewById(R.id.view_habit_reason_edit_text);
        datePickerButton = findViewById(R.id.view_start_date_button);
        startDate = findViewById(R.id.view_habit_selected_date);
        mondayButton = findViewById(R.id.monday_button);
        tuesdayButton = findViewById(R.id.tuesday_button);
        wednesdayButton = findViewById(R.id.wednesday_button);
        thursdayButton = findViewById(R.id.thursday_button);
        fridayButton = findViewById(R.id.friday_button);
        saturdayButton = findViewById(R.id.saturday_button);
        sundayButton = findViewById(R.id.sunday_button);
        saveButton = findViewById(R.id.view_save_habit);

        tealColor = ContextCompat.getColor(getBaseContext(), R.color.teal_200);

        // set data from habit
        String name = habit.getTitle();
        String reason = habit.getReason();
        Calendar date = habit.getStartDate();
        Boolean isPublic = habit.isPublic();
        setDaysSelector();

        // setting date
        String setDateText = "Selected Date: " + getDate(date);
        startDate.setText(setDateText);

        // setting up date picker
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();


    }

    public void setDaysSelector(){
        daysOfWeek = habit.getOnDaysObj().getAll();
        setButtonState(mondayButton, daysOfWeek[0]);
        setButtonState(tuesdayButton, daysOfWeek[1]);
        setButtonState(wednesdayButton, daysOfWeek[2]);
        setButtonState(thursdayButton, daysOfWeek[3]);
        setButtonState(fridayButton, daysOfWeek[4]);
        setButtonState(saturdayButton, daysOfWeek[5]);
        setButtonState(sundayButton, daysOfWeek[6]);
    }

    public void setButtonState(MaterialButton button, Boolean state){
        if(state){
            button.setBackgroundColor(tealColor);
        }
        else{
            button.setBackgroundColor(whiteColor);
        }
    }

    /**
     * Change the color of the button and the arraylist for days of week
     * @param view Button View
     * @param button to change the color
     * @param position which day to change
     * @author Pranav
     */
    public void changeButtonState(View view, MaterialButton button, int position){
        if(daysOfWeek[position]){
            button.setBackgroundColor(whiteColor);
            daysOfWeek[position] = false;
        }
        else{
            button.setBackgroundColor(tealColor);
            daysOfWeek[position] = true;
        }

    }

    /**
     * get the String value from calendar
     * @param calendar
     * @return string value of type Month, Day
     */
    public String getDate(Calendar calendar){
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        return month + ", " + day;
    }

    /**
     * Check if the fields are  filled or not
     * @param name
     * @return boolean whether filled or not
     */
    public boolean checkField(Editable name){
        Boolean fieldCheck = false;
        String field = name.toString();

        if (field.trim().length() > 0 ){
            fieldCheck = true;
        }

        return fieldCheck;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}