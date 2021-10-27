package com.cmput301f21t34.habittrak.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.user.User;

/**
 * A simple {@link Fragment} subclass.
* Fragment for displaying profile
 */
public class ProfileFragment extends Fragment {

    User mainUser;
    Button confirm;
    Button logOut;
    Button delete;
    EditText nameEdit;
    EditText bioEdit;
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

        confirm = view.findViewById(R.id.confirmer);
        logOut = view.findViewById(R.id.logout);
        delete = view.findViewById(R.id.deleter);
        nameEdit = view.findViewById(R.id.editUsername);
        bioEdit = view.findViewById(R.id.editBio);
        emailView = view.findViewById(R.id.userEmail);

        return view;
    }
}