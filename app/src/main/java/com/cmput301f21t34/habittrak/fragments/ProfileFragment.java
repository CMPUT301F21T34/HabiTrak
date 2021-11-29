package com.cmput301f21t34.habittrak.fragments;

import android.app.AlertDialog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.Utilities;
import com.cmput301f21t34.habittrak.auth.Auth;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/**
 * ProfileFragment
 *
 * @author Aron Rajabi
 * <p>
 * Fragment for displaying user information like email, bio and username
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, Utilities {

    private User mainUser;
    // views
    private MaterialButton confirm;
    private MaterialButton logOut;
    private MaterialButton delete;
    private TextInputEditText nameEdit;
    private TextInputEditText bioEdit;
    private TextView emailView;

    // database and auth
    private Auth mAuth;
    private DatabaseManager db = new DatabaseManager();

    public ProfileFragment(User mainUser) {
        this.mainUser = mainUser;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.habi_profile_fragment, container, false);

        //Link views in the UI to the variables
        confirm = view.findViewById(R.id.confirmer);
        logOut = view.findViewById(R.id.logout);
        delete = view.findViewById(R.id.deleter);
        nameEdit = view.findViewById(R.id.editUsername);
        bioEdit = view.findViewById(R.id.editBio);
        emailView = view.findViewById(R.id.userEmail);
        confirm.setOnClickListener(this);
        logOut.setOnClickListener(this);
        delete.setOnClickListener(this);

        //Set the text to the information of the current user
        nameEdit.setText(mainUser.getUsername());
        emailView.setText(mainUser.getEmail());
        bioEdit.setText(mainUser.getBiography());

        mAuth = new Auth(getActivity());

        return view;
    }

    /**
     * onClick
     * <p>
     * Profile Fragment onClickListener
     * Handle the buttons of the profile UI
     * For confirm, change the variables if they have been changed in the edittexts
     *
     * @param view the view we are currently on
     * @author Aron Rajabi
     */
    @Override
    public void onClick(View view) {

        FirebaseAuth fAuth = mAuth.getAuth();
        FirebaseUser fUser = fAuth.getCurrentUser();

        switch (view.getId()) {
            //For confirm changes button press
            case R.id.confirmer:
                String oldUsername = mainUser.getUsername();
                String oldBio = mainUser.getBiography();
                String newUsername = Objects.requireNonNull(nameEdit.getText()).toString();
                String newBio = Objects.requireNonNull(bioEdit.getText()).toString();

                if (fUser != null) { // Only update if we have a user
                    // If stored and written variables differ, update variables
                    if (!oldUsername.equals(newUsername)) {
                        db.updateUsername(fUser.getEmail(), newUsername);
                        mainUser.setUsername(newUsername);
                    }
                    if (!oldBio.equals(newBio)) {
                        db.updateBio(fUser.getEmail(), newBio);
                        mainUser.setBiography(newBio);
                    }
                }
                break;

            case R.id.logout:
                // sign out
                mAuth.signOut();

                // Send user back to main activity
                goToMainActivity(getActivity());

                break;
            case R.id.deleter:
                // run alert to delete
                if (fUser != null) {
                    delete(fUser);
                }
                break;
        }
    }

    /**
     * function to delete the user from the database.
     * It sets up a dialog builder to ask for deletion conformation
     *
     * @param authUser firebase User to delete
     */
    private void delete(FirebaseUser authUser) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert
                .setMessage("This Cannot be undone!")
                .setTitle("Delete Account?");

        alert.setPositiveButton("Yes", (dialogInterface, id) -> {

            AlertDialog.Builder confirmation = new AlertDialog.Builder(getActivity());

            confirmation
                    .setMessage("Are you sure?")
                    .setTitle("Confirm");

            confirmation.setPositiveButton("Yes", (dialogInterface1, i) -> {
                // Delete
                try {
                    String email = authUser.getEmail();
                    authUser.delete();
                    db.deleteUser(email);

                    goToMainActivity(getActivity());
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            confirmation.setNegativeButton("No", (dialogInterface12, i) -> {
                // Cancel
                dialogInterface12.cancel();
            });

            confirmation.show();
        });

        alert.setNegativeButton("No", (dialogInterface, id) -> {
            // Cancel
            dialogInterface.cancel();

        });
        alert.show();
    }
}
