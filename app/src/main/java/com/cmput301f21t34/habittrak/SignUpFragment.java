package com.cmput301f21t34.habittrak;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301f21t34.habittrak.user.Habit_List;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * SignUpFragment
 * @author Pranav
 *
 * Sign Up frament for creating new users
 *
 * @version 1.0
 * @since 2021-11-03
 * @see DatabaseManager
 * @see User
 */
public class SignUpFragment extends Fragment {

    TextInputLayout emailLayout;
    TextInputEditText emailEditText;
    TextInputLayout usernameLayout;
    TextInputEditText usernameEditText;
    TextInputLayout passwordLayout;
    TextInputEditText passwordEditText;
    MaterialButton signupButton;
    DatabaseManager db = new DatabaseManager();
    User currentUser = null;


    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.habi_signup_fragment, container, false);

        emailLayout = view.findViewById(R.id.signup_email_text_input);
        emailEditText = view.findViewById(R.id.signup_email_edit_text);
        usernameLayout = view.findViewById(R.id.signup_username_text_input);
        usernameEditText = view.findViewById(R.id.signup_username_edit_text);
        passwordLayout = view.findViewById(R.id.signup_password_text_input);
        passwordEditText = view.findViewById(R.id.signup_password_edit_text);
        signupButton = view.findViewById(R.id.signup_signup_button);


        signupButton.setOnClickListener(view1 -> {
            emailLayout.setError(null);
            usernameLayout.setError(null);
            passwordLayout.setError(null);

            boolean fieldsFull = false;
            // check fields empty
            if(isEmpty(emailEditText)){
                emailLayout.setError("Email Required");
            }
            else if(isEmpty(usernameEditText)){
                usernameLayout.setError("Username Required");
            }
            else if(isEmpty(passwordEditText)){
                passwordLayout.setError("Password Required");
            }
            else{
                fieldsFull = true;
            }
            // check email
            if(fieldsFull) {
                if (!checkEmail(emailEditText.getText())) {
                    emailLayout.setError("Email Already in Use");
                }
                else{
                    Habit_List habit_list = new Habit_List();
                    db.createNewUser(emailEditText.getText().toString(),
                            usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(),
                            "", habit_list);
                    currentUser = db.getUser(emailEditText.getText().toString());
                    startHomePage(view);
                }
            }


            // if everything correct then start base activity
        });

        return view;
    }

    public boolean checkEmail(@NonNull Editable userEmail){
        String email = userEmail.toString();
        boolean isValid = false;
        if (db.isUnique(email)){
            isValid = true;
        }
        return isValid;
    }

    public boolean isEmpty(TextInputEditText text){
        return text.getText().toString().length() == 0;
    }

    public void startHomePage(View view){
        Intent intent = new Intent(getActivity(), BaseActivity.class);
        intent.putExtra("user", currentUser);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }
}