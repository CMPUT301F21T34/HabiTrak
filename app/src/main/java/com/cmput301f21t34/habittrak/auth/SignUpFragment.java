package com.cmput301f21t34.habittrak.auth;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.Utilities;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/**
 * SignUpFragment
 *
 * @author Dakota
 * @author Pranav
 * <p>
 * Sign Up frament for creating new users
 * @version 1.0
 * @see DatabaseManager
 * @see User
 * @since 2021-11-03
 */
public class SignUpFragment extends Fragment implements Utilities {

    Auth mAuth;
    // views
    private TextInputLayout emailLayout;
    private TextInputEditText emailEditText;
    private TextInputLayout usernameLayout;
    private TextInputEditText usernameEditText;
    private TextInputLayout passwordLayout;
    private TextInputEditText passwordEditText;
    DatabaseManager db = new DatabaseManager();

    public SignUpFragment(Auth auth) {
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
        // setting up layout
        emailLayout = view.findViewById(R.id.signup_email_text_input);
        emailEditText = view.findViewById(R.id.signup_email_edit_text);
        usernameLayout = view.findViewById(R.id.signup_username_text_input);
        usernameEditText = view.findViewById(R.id.signup_username_edit_text);
        passwordLayout = view.findViewById(R.id.signup_password_text_input);
        passwordEditText = view.findViewById(R.id.signup_password_edit_text);
        MaterialButton signupButton = view.findViewById(R.id.signup_signup_button);

        signupButton.setOnClickListener(view1 -> {
            // Check if all the fields have input and notify the user if they do not.
            emailLayout.setError(null);
            usernameLayout.setError(null);
            passwordLayout.setError(null);

            // check if fields are full
            if (fieldsFull()) {
                // Gets values of edit texts
                String email = Objects.requireNonNull(emailEditText.getText()).toString();
                String username = Objects.requireNonNull(usernameEditText.getText()).toString();
                String password = Objects.requireNonNull(passwordEditText.getText()).toString();

                // This signs up the user and returns the email signed up with
                runSignUp(email, username, password);

            }
        });
        // if everything correct then start base activity
        return view;
    }

    /**
     * function to run the firebase sign using firebase auth
     *
     * @param email    user email
     * @param username user username
     * @param password user password
     */
    private void runSignUp(String email, String username, String password) {
        FirebaseAuth fAuth = mAuth.getAuth();
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign Up was successful
                        FirebaseUser authUser = fAuth.getCurrentUser();

                        assert authUser != null; // Should never fail

                        db.createNewUser(authUser.getEmail(), username);
                        fAuth.getCurrentUser().sendEmailVerification();

                        Toast.makeText(getActivity(), "Success",
                                Toast.LENGTH_SHORT).show();

                        goToLogin(requireActivity());

                    } else {
                        // Sign Up failed
                        Toast.makeText(getActivity(), "Authentication failed",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
            if (e instanceof FirebaseAuthWeakPasswordException) {
                passwordLayout.setError("Must be greater than 6");
            } else if (e instanceof FirebaseAuthEmailException) {
                emailLayout.setError("Invalid Email Format");
            } else {
                emailLayout.setError(e.toString());
                passwordLayout.setError(e.toString());
            }
        });
    }

    /**
     * checks if the fields are full or not
     *
     * @return boolean true if they are full, false else wise
     * @author Dakota
     */
    private boolean fieldsFull() {
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
        return Objects.requireNonNull(text.getText()).toString().equals("");
    }
}
