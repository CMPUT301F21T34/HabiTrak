package com.cmput301f21t34.habittrak;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


/**
 * LoginFragment
 *
 * @author Pranav
 * @author Henry
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

        // Password validator
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                passwordLayout.setError(null);
                usernameLayout.setError(null);

                if (!userExists(usernameEditText.getText())) {
                    usernameLayout.setError("This email does not exist!");
                }
                else if (!isPasswordValid(passwordEditText.getText(), usernameEditText.getText())) {
                    passwordLayout.setError("Incorrect password!");
                }
                else {
                    passwordLayout.setError(null);
                    usernameLayout.setError(null);
                    User currentUser = getUser(usernameEditText.getText());
                    startHomePage(view, currentUser);
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
    public boolean isPasswordValid(@Nullable Editable password, @Nullable Editable username) {

        DatabaseManager db = new DatabaseManager();

        String pass = password.toString();
        String user = username.toString();

        return db.validCredentials(user, pass);
    }

    /**
     * userExists
     * Checks if the given username exists in the database
     *
     * @param username username from input
     * @return true if there is such a username, false otherwise
     */
    public boolean userExists(@Nullable Editable username) {

        DatabaseManager db = new DatabaseManager();

        String user = username.toString();

        boolean isInDatabase = !db.isUnique(user);
        return isInDatabase;
    }

    /**
     * getUser
     * Returns a User object for the given username
     * @param username username from input
     * @return
     */
    public User getUser(@Nullable Editable username) {

        DatabaseManager db = new DatabaseManager();

        String user = username.toString();
        return db.getUser(user);
    }

    /**
     * startHomePage
     * Start the base activity after logging in
     * @param view
     */
    public void startHomePage(View view, User currentUser){
        Intent intent = new Intent(getActivity(), BaseActivity.class);
        intent.putExtra("user", currentUser);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }
}
