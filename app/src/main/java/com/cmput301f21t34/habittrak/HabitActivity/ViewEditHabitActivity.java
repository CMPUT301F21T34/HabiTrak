package com.cmput301f21t34.habittrak.HabitActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f21t34.habittrak.BaseActivity;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.user.Habit;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

/**
 * ViewEditHabit
 *
 * @author Pranav
 * <p>
 * View and Edit selected habit from Base Fragments.
 * @version 1.0
 * @since 2021-11-08
 */
public class ViewEditHabitActivity extends AppCompatActivity implements View.OnClickListener {

    public static int RESULT_CODE = BaseActivity.RESULT_EDIT_HABIT;

    // Views //
    private TextInputEditText habitName;
    private TextInputEditText habitReason;
    private TextView startDate;
    private TextView visibilityText;
    private Calendar calendar;
    private MaterialButton mondayButton;
    private MaterialButton tuesdayButton;
    private MaterialButton wednesdayButton;
    private MaterialButton thursdayButton;
    private MaterialButton fridayButton;
    private MaterialButton saturdayButton;
    private MaterialButton sundayButton;
    private MaterialDatePicker materialDatePicker;

    // Other Variables //
    private boolean[] daysOfWeek;
    private int buttonOffColor = Color.WHITE;
    private int tealColor;

    // Data Variables //
    private Habit habit;
    private int habitPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_habit);

        // check if dark mode and change the button off color
        int nightModeCheck = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeCheck == Configuration.UI_MODE_NIGHT_YES) {
            buttonOffColor = Color.BLACK;
        }

        // add back button to toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_habit_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // get habit data from intent
        Intent intent = getIntent();
        this.habit = intent.getParcelableExtra("HABIT");
        this.habitPosition = intent.getIntExtra("position", 0);

        // getting views
        habitName = findViewById(R.id.view_habit_name_edit_text);
        habitReason = findViewById(R.id.view_habit_reason_edit_text);
        MaterialButton datePickerButton = findViewById(R.id.view_start_date_button);
        startDate = findViewById(R.id.view_habit_selected_date);
        mondayButton = findViewById(R.id.monday_button);
        tuesdayButton = findViewById(R.id.tuesday_button);
        wednesdayButton = findViewById(R.id.wednesday_button);
        thursdayButton = findViewById(R.id.thursday_button);
        fridayButton = findViewById(R.id.friday_button);
        saturdayButton = findViewById(R.id.saturday_button);
        sundayButton = findViewById(R.id.sunday_button);
        MaterialButton saveButton = findViewById(R.id.view_save_habit);
        SwitchMaterial publicSwitch = findViewById(R.id.view_public_switch);
        visibilityText = findViewById(R.id.view_habit_visibility_text);
        TextView bestStreakStart = findViewById(R.id.best_streak_start);
        TextView bestStreakEnd = findViewById(R.id.best_streak_end);
        LinearProgressIndicator progressBar = findViewById(R.id.view_habit_progress);
        TextView totalEventCompleted = findViewById(R.id.view_total_events);
        TextView bestStreakTotal = findViewById(R.id.best_streak_count);
        TextView progressBarText = findViewById(R.id.view_habit_streak);
        tealColor = ContextCompat.getColor(getBaseContext(), R.color.teal_200); // color for buttons

        // get data from habit
        String name = habit.getTitle();
        String reason = habit.getReason();
        calendar = habit.getStartDate();
        boolean isPublic = habit.isPublic();
        setDaysSelector();  // set days selector values

        // set name and reason
        habitName.setText(name);
        habitReason.setText(reason);
        if (!isPublic) {
            publicSwitch.setChecked(false);
            visibilityText.setText(getResources().getString((R.string.habit_title_private)));
        }

        // set progress bar
        progressBar.setProgress((habit.getCurrentStreak() * 100) / 30);
        String currentStreakText = habit.getCurrentStreak() + "/30";
        progressBarText.setText(currentStreakText);

        SimpleDateFormat streakDateFormat = new SimpleDateFormat("MMM dd, yyyy");

        // set streak values
        if (habit.getBestStreakDate() == null || habit.getBestStreakDateEnd() == null) {
            bestStreakStart.setText("N/A");
            bestStreakEnd.setText("N/A");
        } else {
            bestStreakStart.setText(streakDateFormat.format(habit.getBestStreakDate().getTime()));
            bestStreakEnd.setText(streakDateFormat.format(habit.getBestStreakDateEnd().getTime()));
            bestStreakTotal.setText(String.valueOf(habit.getBestStreak()));
        }
        totalEventCompleted.setText(String.valueOf(habit.getHabitEvents().size()));

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

        // switch listener to make habit public or private
        publicSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                habit.makePublic();
                visibilityText.setText(getResources().getString(R.string.habit_title_public));
            } else {
                habit.makePrivate();
                visibilityText.setText(getResources().getString(R.string.habit_title_private));
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.view_start_date_button) {
            materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        } else if (view.getId() == R.id.monday_button) {
            changeButtonState(mondayButton, 0);

        } else if (view.getId() == R.id.tuesday_button) {
            changeButtonState(tuesdayButton, 1);

        } else if (view.getId() == R.id.wednesday_button) {
            changeButtonState(wednesdayButton, 2);

        } else if (view.getId() == R.id.thursday_button) {
            changeButtonState(thursdayButton, 3);

        } else if (view.getId() == R.id.friday_button) {
            changeButtonState(fridayButton, 4);

        } else if (view.getId() == R.id.saturday_button) {
            changeButtonState(saturdayButton, 5);

        } else if (view.getId() == R.id.sunday_button) {
            changeButtonState(sundayButton, 6);

        } else if (view.getId() == R.id.view_save_habit) {
            if (
                    checkField(Objects.requireNonNull(habitName.getText()))
                            &&
                            checkField(Objects.requireNonNull(habitReason.getText()))) {

                habit.setTitle(habitName.getText().toString());
                habit.setReason(habitReason.getText().toString());
                habit.setStartDate(calendar);
                habit.getOnDaysObj().setAll(daysOfWeek);
                Toast.makeText(getBaseContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
                Intent result = new Intent();
                result.putExtra("HABIT", habit);
                result.putExtra("position", habitPosition);
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
     * <p>
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
            button.setBackgroundColor(buttonOffColor);
        }
    }

    /**
     * changeButtonState
     * <p>
     * Change the color of the button and the arraylist for days of week.
     *
     * @param button   to change the color
     * @param position which day to change
     * @author Pranav
     */
    public void changeButtonState(MaterialButton button, int position) {
        if (daysOfWeek[position]) {
            button.setBackgroundColor(buttonOffColor);
            daysOfWeek[position] = false;
        } else {
            button.setBackgroundColor(tealColor);
            daysOfWeek[position] = true;
        }

    }

    /**
     * getDate
     * <p>
     * get the String value from calendar
     *
     * @param calendar
     * @return string value of type Month, Day
     */
    public String getDate(Calendar calendar) {
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        return month + ", " + day;
    }

    /**
     * checkField
     * <p>
     * Check if the fields are  filled or not.
     *
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
