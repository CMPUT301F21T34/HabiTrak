package com.cmput301f21t34.habittrak.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.MainActivity;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.Utilities;
import com.cmput301f21t34.habittrak.auth.Auth;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * ProfileFragment
 *
 * @author Aron Rajabi
 *
 * Fragment for displaying profile information
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, Utilities {

    User mainUser;
    MaterialButton confirm;
    MaterialButton logOut;
    MaterialButton delete;
    TextInputEditText nameEdit;
    TextInputEditText bioEdit;
    TextView emailView;

    Auth mAuth;
    DatabaseManager db = new DatabaseManager();

    public ProfileFragment(User mainUser) {
        this.mainUser = mainUser;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.habi_profile_fragment, container, false);

        Log.d("mainUser", "in ProfileFragment mainUser: " + mainUser.getUsername());

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

        mAuth = new Auth(getActivity(), db);

        return view;
    }

    /**
     * onClick
     *
     * Profile Fragment onClickListener
     * Handle the buttons of the profile UI
     * For confirm, change the variables if they have been changed in the edittexts
     *
     * @author Aron Rajabi
     * @param view the view we are currently on
     */
    @Override
    public void onClick(View view) {

        FirebaseAuth fAuth = mAuth.getAuth();
        FirebaseUser fUser = fAuth.getCurrentUser();

        switch (view.getId()){
            //For confirm changes button press
            case R.id.confirmer:
                String oldUsername = mainUser.getUsername();
                String oldBio = mainUser.getBiography();
                String newUsername = nameEdit.getText().toString();
                String newBio = bioEdit.getText().toString();

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
                if (fUser != null){
                    delete(fUser);
                }
                break;
        }
    }

    private void delete(FirebaseUser authUser){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert
                .setMessage("This Cannot be undone!")
                .setTitle("Delete Account?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {

                AlertDialog.Builder confirmation = new AlertDialog.Builder(getActivity());

                confirmation
                        .setMessage("Are you sure?")
                        .setTitle("Confirm");

                confirmation.setPositiveButton("Yes", (dialogInterface1, i) -> {
                    // Delete

                    String email = authUser.getEmail();
                    authUser.delete();
                    db.deleteUser(email);

                    goToMainActivity(getActivity());

                });

                confirmation.setNegativeButton("No", (dialogInterface12, i) -> {
                    // Cancel
                    dialogInterface12.cancel();
                });

                confirmation.show();

            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                // Cancel
                dialogInterface.cancel();

            }
        });
        alert.show();
    }
}