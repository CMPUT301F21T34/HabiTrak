package com.cmput301f21t34.habittrak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f21t34.habittrak.user.Habit;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

// TODO: calculate best streak and set the text
/**
 * ViewEditHabit.
 *
 *
 * @author Pranav
 *
 * View and Edit selected habit from Base Fragments.
 *
 * @version 1.0
 * @since 2021-11-08
 */
public class ViewEditHabit extends AppCompatActivity implements View.OnClickListener {

    public static int RESULT_CODE = 2000;


    private TextInputEditText habitName;
    private TextInputEditText habitReason;
    private MaterialButton datePickerButton;
    private TextView startDate;
    private TextView visibilityText;
    private Calendar calendar;
    private MaterialButton saveButton;
    private MaterialButton mondayButton;
    private MaterialButton tuesdayButton;
    private MaterialButton wednesdayButton;
    private MaterialButton thursdayButton;
    private MaterialButton fridayButton;
    private MaterialButton saturdayButton;
    private MaterialButton sundayButton;
    private MaterialDatePicker materialDatePicker;
    private SwitchMaterial publicSwitch;
    private TextView bestStreakStart;
    private TextView bestStreakEnd;
    private TextView totalEventCompleted;
    private TextView bestStreakTotal;
    private TextView progressBarText;
    private LinearProgressIndicator progressBar;
    private boolean[] daysOfWeek;
    private final int whiteColor = Color.WHITE;
    private int tealColor;

    // data variables
    private Habit habit;
    private int habitPosition;

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
        this.habit = intent.getParcelableExtra("HABIT");
        this.habitPosition = intent.getIntExtra("position", 0);
        Log.d("VIEW_HABIT", Integer.toString(habitPosition));

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
        publicSwitch = findViewById(R.id.view_public_switch);
        visibilityText = findViewById(R.id.view_habit_visibility_text);
        bestStreakStart = findViewById(R.id.best_streak_start);
        bestStreakEnd = findViewById(R.id.best_streak_end);
        progressBar = findViewById(R.id.view_habit_progress);
        totalEventCompleted = findViewById(R.id.view_total_events);
        bestStreakTotal = findViewById(R.id.best_streak_count);
        progressBarText = findViewById(R.id.view_habit_streak);
        tealColor = ContextCompat.getColor(getBaseContext(), R.color.teal_200); // color for buttons

        // get data from habit
        String name = habit.getTitle();
        String reason = habit.getReason();
        calendar = habit.getStartDate();
        Boolean isPublic = habit.isPublic();
        Log.d("View_Habit", Boolean.toString(isPublic));
        setDaysSelector();  // set days selector values

        // set data
        habitName.setText(name);
        habitReason.setText(reason);
        if (!isPublic){
            publicSwitch.setChecked(false);
            visibilityText.setText("Private");
        }
        // set progress bar
        progressBar.setProgress((habit.getStreak()*100)/30);
        String txt = habit.getStreak() + "/30";
        progressBarText.setText(txt);
        // setting date
        String setDateText = "Selected Date: " + getDate(calendar);
        startDate.setText(setDateText);


        // setting up date picker
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        materialDatePicker = materialDateBuilder.build();

        // setting listeners
        datePickerButton.setOnClickListener(this);
        mondayButton.setOnClickListener(this);
        tuesdayButton.setOnClickListener(this);
        wednesdayButton.setOnClickListener(this);
        thursdayButton.setOnClickListener(this);
        fridayButton.setOnClickListener(this);
        saturdayButton.setOnClickListener(this);
        sundayButton.setOnClickListener(this);
        publicSwitch.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        // material button listener
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis((long) selection);
            String date = "Selected Date is : " + getDate(calendar);
            startDate.setText(date);
        });

        // switch listener
        publicSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                habit.makePublic();
                visibilityText.setText("Public");
            }
            else {
                habit.makePrivate();
                visibilityText.setText("Private");
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.view_start_date_button) {
            materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        } else if (view.getId() == R.id.monday_button) {
            changeButtonState(view, mondayButton, 0);
        } else if (view.getId() == R.id.tuesday_button) {
            changeButtonState(view, tuesdayButton, 1);
        } else if (view.getId() == R.id.wednesday_button) {
            changeButtonState(view, wednesdayButton, 2);
        } else if (view.getId() == R.id.thursday_button) {
            changeButtonState(view, thursdayButton, 3);
        } else if (view.getId() == R.id.friday_button) {
            changeButtonState(view, fridayButton, 4);
        } else if (view.getId() == R.id.saturday_button) {
            changeButtonState(view, saturdayButton, 5);
        } else if (view.getId() == R.id.sunday_button) {
            changeButtonState(view, sundayButton, 6);
        } else if (view.getId() == R.id.view_save_habit) {

            if (checkField(habitName.getText()) && checkField(habitReason.getText())) {
                habit.setTitle(habitName.getText().toString());
                habit.setReason(habitReason.getText().toString());
                habit.setStartDate(calendar);
                habit.getOnDaysObj().setAll(daysOfWeek);
                Toast.makeText(getBaseContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
                Intent result = new Intent();
                result.putExtra("HABIT", habit);
                result.putExtra("position", habitPosition);
                Log.d("VIEW_HABIT", "SENDING RESULT");
                setResult(RESULT_CODE, result);
                this.finish();
            } else {
                Toast.makeText(getBaseContext(), "Empty Text Fields", Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * setDaysSelector.
     *
     * @author Pranav
     *
     * sets the state of the days of week buttons.
     */
    public void setDaysSelector() {
        daysOfWeek = habit.getOnDaysObj().getAll();
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

    /**
     * Change the color of the button and the arraylist for days of week.
     * @param view Button View
     * @param button to change the color
     * @param position which day to change
     * @author Pranav
     */
    public void changeButtonState(View view, MaterialButton button, int position) {
        if(daysOfWeek[position]) {
            button.setBackgroundColor(whiteColor);
            daysOfWeek[position] = false;
        } else {
            button.setBackgroundColor(tealColor);
            daysOfWeek[position] = true;
        }

    }

    /**
     * get the String value from calendar
     * @param calendar
     * @return string value of type Month, Day
     */
    public String getDate(Calendar calendar) {
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        return month + ", " + day;
    }

    /**
     * Check if the fields are  filled or not.
     * @param name
     * @return boolean whether filled or not
     */
    public boolean checkField(Editable name) {
        return name.toString().trim().length() > 0;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}