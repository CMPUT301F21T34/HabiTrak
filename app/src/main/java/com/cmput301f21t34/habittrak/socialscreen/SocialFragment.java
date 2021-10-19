package com.cmput301f21t34.habittrak.socialscreen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301f21t34.habittrak.R;

/**
 * A simple {@link Fragment} subclass.
 * Fragment for social section
 */
public class SocialFragment extends Fragment {

    public SocialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.habi_social_fragment, container, false);
    }
}