package com.cmput301f21t34.habittrak.habitsscreen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301f21t34.habittrak.R;

/**
 * A simple {@link Fragment} subclass.
 * Fragment for displaying all habits
 */
public class AllHabitsFragment extends Fragment {

    public AllHabitsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.habi_all_habits_fragment, container, false);
    }
}