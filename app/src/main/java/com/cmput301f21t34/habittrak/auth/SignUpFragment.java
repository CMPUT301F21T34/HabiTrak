package com.cmput301f21t34.habittrak.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.cmput301f21t34.habittrak.BaseActivity;
import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * SignUpFragment
 *
 * @author Pranav
 * <p>
 * Sign Up frament for creating new users
 * @version 1.0
 * @see DatabaseManager
 * @see User
 * TODO: Add authentication
 * @since 2021-11-03
 */
public class SignUpFragment extends Fragment {

    private final String TAG = "SignUpFragment";

    Auth mAuth;

    TextInputLayout emailLayout;
    TextInputEditText emailEditText;
    TextInputLayout usernameLayout;
    TextInputEditText usernameEditText;
    TextInputLayout passwordLayout;
    TextInputEditText passwordEditText;
    MaterialButton signupButton;
    DatabaseManager db = new DatabaseManager();
    User currentUser;

    public SignUpFragment(Auth auth) {
        /* Required empty public constructor */

        this.mAuth = auth;

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
            // Check if all the fields have input and notify the user if they do not.
            emailLayout.setError(null);
            usernameLayout.setError(null);
            passwordLayout.setError(null);

            // check if fields are full
            if (fieldsFull()) {


                // Gets values of edit texts
                String email = emailEditText.getText().toString();
                Log.d("SignUp", "email: " + email);
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();


                // Check that email is unique
                if (false){//!db.isUniqueEmail(email)) { db.isUnique is always false
                    emailLayout.setError("Email Already in Use");
                } else {

                    // This signs up the user and returns the email signed up with
                    String authedEmail = mAuth.signUp(email, password);

                    // authedEmail is null on failure
                    if (authedEmail != null){
                        // Create account in db ONLY on success
                        db.createNewUser(authedEmail,
                                username,
                                "not_required_to_store"); // We should not store password in db

                        Log.d(TAG, "Fields Full and email unique");

                        // Kick user to sign in page

                        toLogin();


                    } else {

                        // Failure

                        Toast.makeText(getContext(), "Sign Up Failed", Toast.LENGTH_SHORT);


                    }




                }
            }

            // if everything correct then start base activity
        });

        return view;
    }

    private void toLogin() {
        LoginFragment loginFragment = new LoginFragment(null, mAuth);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_fragment_container, loginFragment, "loginFrag")
                .commit();
    }

    /**
     * checks if the fields are full or not
     *
     * @author Dakota
     * @return boolean true if they are full, false else wise
     */
    private boolean fieldsFull(){


        boolean fieldsFull = true;
        if (isEmpty(emailEditText)) {
            fieldsFull = false;
            emailLayout.setError("Email Required");
        }
        if (isEmpty(usernameEditText)) {
            fieldsFull = false;
            usernameLayout.setError("Username Required");
        }
        if (isEmpty(passwordEditText)) {
            fieldsFull = false;
            passwordLayout.setError("Password Required");
        }

        return fieldsFull;
    }



    /**
     * isEmpty
     *
     * @param text EditText
     * @return boolean
     * @author Pranav
     * <p>
     * check to see if a EditText is empty
     */
    private boolean isEmpty(TextInputEditText text) {
        return text.getText().toString().equals("");
    }

    /**
     * startHomePage
     *
     * @param view
     * @author Pranav
     * <p>
     * start the base activity after signing up
     */
    public void startHomePage(View view) {
        Intent intent = new Intent(getActivity(), BaseActivity.class);
        intent.putExtra("mainUser", currentUser);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }
}
