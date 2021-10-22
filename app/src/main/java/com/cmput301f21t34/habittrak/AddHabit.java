package com.cmput301f21t34.habittrak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * AddHabit
 *
 * activity for adding new habit
 *
 * @author Pranav
 */
public class AddHabit extends AppCompatActivity {

    TextInputEditText habitName;
    TextInputEditText habitReason;
    MaterialButton datePickerButton;
    TextView startDate;
    Calendar calendar;
    MaterialButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_habit_toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_add_habit);

        // getting views
        startDate = findViewById(R.id.add_habit_selected_date);
        datePickerButton = findViewById(R.id.star_date_button);
        habitName = findViewById(R.id.habit_name_edit_text);
        habitReason = findViewById(R.id.habit_reason_edit_text);
        saveButton = findViewById(R.id.save_habit);

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
                else if (!checkField(habitReason.getText())){
                    habitReason.setError("Input Required");
                }
                else{
                    finishActivityWithResult();
                }


            }
        });




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
     * finish activity if all fiel
     */

    public void finishActivityWithResult(){
        String name = habitName.getText().toString();
        String reason = habitReason.getText().toString();
        //Habit newHabit = new Habit(name, reason, calendar);
        Bundle args = new Bundle();
        //args.putParcelable("habit", newHabit);
        Intent result = new Intent();
        result.putExtra("result", args);
        setResult(Activity.RESULT_OK, result);
        this.finish();
    }

}