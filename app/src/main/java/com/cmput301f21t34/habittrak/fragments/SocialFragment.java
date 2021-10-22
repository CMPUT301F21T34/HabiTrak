package com.cmput301f21t34.habittrak.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.User;

/**
 * A simple {@link Fragment} subclass.
 * Fragment for social section
 */
public class SocialFragment extends Fragment {

    User mainUser;

    public SocialFragment(User mainUser) {
        this.mainUser = mainUser;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.habi_social_fragment, container, false);

        Log.d("mainUser", "in SocialFragment mainUser: " + mainUser.getUsername());



        return view;
    }
}