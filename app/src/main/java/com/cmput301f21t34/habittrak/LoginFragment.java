package com.cmput301f21t34.habittrak;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.GregorianCalendar;


/**
 * LoginFragment
 *
 * @author Pranav
 *
 * Login Fragment for the app
 *
 * TODO: update the password validation: current username: <any> password: <any>
 */
public class LoginFragment extends Fragment {

    private User mainUser;

    public LoginFragment(User mainUser){
        this.mainUser = mainUser; // Passes User object
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.habi_login_fragment, container, false);

        // Variables of the UI elements
        TextInputLayout usernameLayout = view.findViewById(R.id.username_text_input);
        TextInputEditText usernameEditText = view.findViewById(R.id.username_edit_text);
        TextInputLayout passwordLayout = view.findViewById(R.id.password_text_input);
        TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);
        MaterialButton loginButton = view.findViewById(R.id.login_button);

        // Test Data //

        Calendar date = Calendar.getInstance();
        Habit habit1 = new Habit("exercise dog", "some desc", date);
        habit1.getOnDaysObj().setTrue(Calendar.MONDAY);
        habit1.getOnDaysObj().setTrue(Calendar.FRIDAY);
        Habit habit2 = new Habit("go for a walk", "some desc 2", date);
        habit2.getOnDaysObj().setAll(new boolean[]{true, true, true, true, true, true, true});

        mainUser.addHabit(habit1); mainUser.addHabit(habit2);

        // End Test Data //

        // Password validator
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPasswordValid(passwordEditText.getText(), usernameEditText.getText())) {
                    passwordLayout.setError("Incorrect password");
                    usernameLayout.setError("Incorrect username");
                }
                else{
                    passwordLayout.setError(null);
                    usernameLayout.setError(null);
                    startHomePage(view);

                }



            }
        });


        return view;
    }

    /**
     * Password validation.
     * TODO: Update the function and implement proper password validation. (Hash Checking needed)
     * TODO: Move login validation, and checking to separate class outside frag - Dakota
     * Current Credentials: user: admin pass: admin
     * @param password password from input
     * @param username username from input
     * @return passwordOk boolean if the password is valid
     */
    public boolean isPasswordValid(@Nullable Editable password, @Nullable Editable username){

        Boolean passwordOk = false;
        String pass = password.toString();
        String user = username.toString();


        if (true) { // logic for allowing loggin in, for sake of testing is always true - Dakota
            passwordOk = true;




        }

        return passwordOk;
    }

    /**
     * startHomePage
     * Start the base activity after logging in
     * @param view
     */
    public void startHomePage(View view){
        Intent intent = new Intent(getActivity(), BaseActivity.class);


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra("mainUser", mainUser);
        startActivity(intent);
        getActivity().finish();
    }
}
