package com.cmput301f21t34.habittrak.auth;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.Utilities;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * LoginFragment
 *
 * @author Pranav
 * @author Dakota
 * @author Henry
 * <p>
 * Login Fragment for the app
 */
public class LoginFragment extends Fragment implements Utilities {

    private Auth mAuth;  // This is our Auth Helper Class
    private FirebaseUser authUser;

    // views
    private TextInputLayout usernameLayout;
    private TextInputEditText usernameEditText;
    private TextInputLayout passwordLayout;
    private TextInputEditText passwordEditText;
    private MaterialButton loginButton;
    private MaterialButton signupButton;
    private MaterialButton forgotButton;


    // empty fragment constructor
    public LoginFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.habi_login_fragment, container, false);

        // Variables of the UI elements
        usernameLayout = view.findViewById(R.id.username_text_input);
        usernameEditText = view.findViewById(R.id.username_edit_text);
        passwordLayout = view.findViewById(R.id.password_text_input);
        passwordEditText = view.findViewById(R.id.password_edit_text);
        loginButton = view.findViewById(R.id.login_button);
        signupButton = view.findViewById(R.id.signup_button);
        forgotButton = view.findViewById(R.id.forgot_button);

        mAuth = new Auth(getActivity());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        signupButton.setOnClickListener(view -> toSignUp());

        forgotButton.setOnClickListener(view -> toForgot());

        // Password validator
        loginButton.setOnClickListener(view -> {

            passwordLayout.setError(null);
            usernameLayout.setError(null);

            String email;
            if (usernameEditText.getText() == null) {
                email = "";
            } else {
                email = usernameEditText.getText().toString();
            }

            String password;
            if (passwordEditText.getText() == null) {
                password = "";
            } else {
                password = passwordEditText.getText().toString();
            }

            try {
                runLogin(email, password);
            } catch (Exception e) {
                if (e instanceof IllegalArgumentException) {
                    // Invalid entry
                    usernameLayout.setError("Invalid Entry");
                    passwordLayout.setError("Invalid Entry");
                } else {
                    // Displays any other exceptions
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * run the firebase login setup
     *
     * @param email    user email
     * @param password user password
     */
    private void runLogin(String email, String password) {
        // This is the firebase auth
        // Must be used as they don't get put on top of the stack but are called later
        FirebaseAuth fAuth = mAuth.getAuth();

        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        authUser = fAuth.getCurrentUser();
                        assert authUser != null; // should never fail

                        if (authUser.isEmailVerified()) {
                            goToBaseActivity(getActivity(), null);
                        } else {
                            // Email not Verified //
                            usernameLayout.setError("Email not Verified");
                            passwordLayout.setError(null);
                            mAuth.alertNotVerified(authUser).show();
                        }
                    } else {
                        // Login Failed
                        usernameLayout.setError(null);
                        passwordLayout.setError("Incorrect Password");
                    }
                });
    }

    /**
     * function to goto signUp fragment
     */
    private void toSignUp() {
        SignUpFragment signUpFragment = new SignUpFragment(mAuth);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_fragment_container, signUpFragment, "signupFrag")
                .addToBackStack(null)
                .commit();
    }

    /**
     * function to goto forgot Fragment
     */
    private void toForgot() {
        ForgotFragment forgotFragment = new ForgotFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_fragment_container, forgotFragment, "forgotFrag")
                .addToBackStack(null)
                .commit();
    }
}
