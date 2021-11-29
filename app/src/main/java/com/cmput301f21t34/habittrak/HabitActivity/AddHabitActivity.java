package com.cmput301f21t34.habittrak.HabitActivity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.widget.TextView;


import com.cmput301f21t34.habittrak.BaseActivity;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.user.Habit;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

/**
 * AddHabitActivity
 * <p>
 * activity for adding new habit
 *
 * @author Pranav
 */
public class AddHabitActivity extends AppCompatActivity {

    // views
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
    // data
    private boolean[] daysOfWeek = new boolean[]{true, true, true, true, true, true, true};

    private boolean isPublic = true;
    private int buttonOffColor = Color.WHITE;
    private int tealColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        // change the button of color of weekdays selector in dark mode
        int nightModeCheck = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeCheck == Configuration.UI_MODE_NIGHT_YES) {
            buttonOffColor = Color.BLACK;
        }

        // add back button to toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_habit_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Getting Views //
        startDate = findViewById(R.id.add_habit_selected_date);
        MaterialButton datePickerButton = findViewById(R.id.star_date_button);
        habitName = findViewById(R.id.habit_name_edit_text);
        habitReason = findViewById(R.id.habit_reason_edit_text);
        MaterialButton saveButton = findViewById(R.id.save_habit);
        mondayButton = findViewById(R.id.monday_button);
        tuesdayButton = findViewById(R.id.tuesday_button);
        wednesdayButton = findViewById(R.id.wednesday_button);
        thursdayButton = findViewById(R.id.thursday_button);
        fridayButton = findViewById(R.id.friday_button);
        saturdayButton = findViewById(R.id.saturday_button);
        sundayButton = findViewById(R.id.sunday_button);
        SwitchMaterial publicSwitch = findViewById(R.id.add_public_switch);
        visibilityText = findViewById(R.id.add_habit_visibility_text);

        // getting color
        tealColor = ContextCompat.getColor(getBaseContext(), R.color.teal_200);

        // setting date
        calendar = Calendar.getInstance();
        String setDateText = "Selected Date is : " + getDate(calendar);
        startDate.setText(setDateText);

        // setting up date picker
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        datePickerButton.setOnClickListener(view ->
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER"));

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis((long) selection);
            String date = "Selected Date is : " + getDate(calendar);
            startDate.setText(date);
        });

        // switcher listener for selecting if the habit is public or private
        publicSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                isPublic = true;
                visibilityText.setText("Public");
            } else {
                isPublic = false;
                visibilityText.setText("Private");
            }
        });

        // get result
        saveButton.setOnClickListener(view -> {
            if (!checkField(Objects.requireNonNull(habitName.getText()))) {
                habitName.setError("Input Required");
            } else {
                finishActivityWithResult();
            }
        });

        // button listeners
        mondayButton.setOnClickListener(view -> changeButtonState(mondayButton, 0));

        tuesdayButton.setOnClickListener(view -> changeButtonState(tuesdayButton, 1));

        wednesdayButton.setOnClickListener(view -> changeButtonState(wednesdayButton, 2));

        thursdayButton.setOnClickListener(view -> changeButtonState(thursdayButton, 3));

        fridayButton.setOnClickListener(view -> changeButtonState(fridayButton, 4));

        saturdayButton.setOnClickListener(view -> changeButtonState(saturdayButton, 5));

        sundayButton.setOnClickListener(view -> changeButtonState(sundayButton, 6));
    }

    /**
     * Change the color of the button and arraylist data for days of week
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
     * Check if the fields are  filled or not
     *
     * @param name Editable name to check if empty
     * @return boolean whether filled or not
     */
    public boolean checkField(Editable name) {
        return name.toString().trim().length() > 0;
    }

    /**
     * finish activity if all fields are filled
     *
     * @author Pranav
     * @author Dakota
     */
    public void finishActivityWithResult() {
        String name = Objects.requireNonNull(habitName.getText()).toString();
        String reason = Objects.requireNonNull(habitReason.getText()).toString();

        Habit newHabit = new Habit(name, reason, calendar);
        newHabit.getOnDaysObj().setAll(daysOfWeek);
        // set isPublic of habit
        if (isPublic) {
            newHabit.makePublic();
        } else {
            newHabit.makePrivate();
        }

        Intent result = new Intent();
        result.putExtra("newHabit", newHabit);
        setResult(BaseActivity.RESULT_NEW_HABIT, result);
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
