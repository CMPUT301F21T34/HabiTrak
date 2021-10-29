package com.cmput301f21t34.habittrak.socialFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.SocialAdapter;
import com.cmput301f21t34.habittrak.User;

import java.util.ArrayList;


public class FollowersFragment extends Fragment {

    SocialAdapter socialAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public FollowersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.habi_followers_fragment, container, false);


        // Sample Data
        /*
        User sample1 = new User("hello123");
        User sample2 = new User("another User");
        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(sample1);
        userArrayList.add(sample2); */

        // setting up recycler view
        recyclerView = view.findViewById(R.id.followers_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //socialAdapter = new SocialAdapter(userArrayList);
        recyclerView.setAdapter(socialAdapter);


        return view;
    }
}