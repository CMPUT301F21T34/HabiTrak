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


import com.cmput301f21t34.habittrak.user.Habit;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * AddHabitActivity
 *
 * activity for adding new habit
 *
 * @author Pranav
 */
public class AddHabitActivity extends AppCompatActivity {

    //TODO: make attribute explicitly private
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
    boolean[] daysOfWeek = new boolean[]{true, true, true, true, true, true, true};;
    int whiteColor = Color.WHITE;
    int tealColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.add_habit_toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_add_habit);

        // Getting Views //
        startDate = findViewById(R.id.add_habit_selected_date);
        datePickerButton = findViewById(R.id.star_date_button);
        habitName = findViewById(R.id.habit_name_edit_text);
        habitReason = findViewById(R.id.habit_reason_edit_text);
        saveButton = findViewById(R.id.save_habit);
        mondayButton = findViewById(R.id.monday_button);
        tuesdayButton = findViewById(R.id.tuesday_button);
        wednesdayButton = findViewById(R.id.wednesday_button);
        thursdayButton = findViewById(R.id.thursday_button);
        fridayButton = findViewById(R.id.friday_button);
        saturdayButton = findViewById(R.id.saturday_button);
        sundayButton = findViewById(R.id.sunday_button);

        // getting color
        // TODO: change the setting for dark mode as well
        tealColor = ContextCompat.getColor(getBaseContext(), R.color.teal_200);


        // setting date
        calendar = Calendar.getInstance();
        String setDateText = "Selected Date is : " + getDate(calendar);
        Log.d("date", setDateText);
        startDate.setText(setDateText);



        // setting up date picker
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis((long) selection);
                String date = "Selected Date is : " + getDate(calendar);
                startDate.setText(date);
            }
        });

        // get result

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = habitName.getText().toString();
                if(!checkField(habitName.getText())){
                    habitName.setError("Input Required");
                }

                //TODO: make reason field optional
                else if (!checkField(habitReason.getText())){
                    habitReason.setError("Input Required");
                }
                else{
                    finishActivityWithResult();
                }


            }
        });


        // button listeners
        mondayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonState(view, mondayButton, 0);
            }
        });

        tuesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonState(view, tuesdayButton, 1);
            }
        });

        wednesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonState(view, wednesdayButton, 2);
            }
        });

        thursdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonState(view, thursdayButton, 3);
            }
        });

        fridayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonState(view, fridayButton, 4);
            }
        });

        saturdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonState(view, saturdayButton, 5);
            }
        });

        sundayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonState(view, sundayButton, 6);
            }
        });
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

    /**
     * finish activity if all fields are filled
     */

    public void finishActivityWithResult(){
        String name = habitName.getText().toString();
        String reason = habitReason.getText().toString();
        Habit newHabit = new Habit(name, reason, calendar, daysOfWeek);
        //Bundle newHabitBundle = new Bundle();
        //newHabitBundle.putParcelable("newHabit", newHabit);
        Intent result = new Intent();
        result.putExtra("newHabit", newHabit);
        setResult(BaseActivity.RESULT_NEW_HABIT, result);
        this.finish();
    }

}