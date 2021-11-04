package com.cmput301f21t34.habittrak.fragments;

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

import com.cmput301f21t34.habittrak.MainActivity;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * ProfileFragment
 *
 * @author Aron Rajabi
 *
 * Fragment for displaying profile information
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{

    User mainUser;
    Button confirm;
    Button logOut;
    Button delete;
    TextInputEditText nameEdit;
    TextInputEditText bioEdit;
    TextView emailView;

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

        return view;
    }

    /**
     * onClick
     *
     * Profile Fragment onClickListener
     * Handle the buttons of the profile UI
     * For confirm, change the variables if they have been changed in the edittexts
     * TODO:delete account and log out functionality
     *
     * @author Aron Rajabi
     * @param view the view we are currently on
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //For confirm changes button press
            case R.id.confirmer:
                String oldUsername = mainUser.getUsername();
                String oldBio = mainUser.getBiography();
                String newUsername = nameEdit.getText().toString();
                String newBio = bioEdit.getText().toString();

                // If stored and written variables differ, update variables
                if (!oldUsername.equals(newUsername)){
                    mainUser.setUsername(newUsername);
                }
                if (!oldBio.equals(newBio)){
                    mainUser.setBiography(newBio);
                }
                break;
            case R.id.logout:
                // Logout button pressed //
                // TODO: make sure database it updated
                // TODO: make sure to remove any offline stored credientals

                // Send user back to main activity
                startMainActivity();






                break;
            case R.id.deleter:

                // TODO: require reauth
                // TODO: remove from database
                // TODO: remove from account manager if needed

                // Send user back to main activity
                startMainActivity();

                break;
        }
    }

    /**
     * startMainActivity
     *
     * sends the user back to the main activity through an intent
     *
     * @author Dakota
     */
    private void startMainActivity(){
        // Send user back to main activity
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        mainUser = null; // Possibly redundant
        getActivity().finish();
    }
}