package com.cmput301f21t34.habittrak.socialFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.SocialAdapter;
import com.cmput301f21t34.habittrak.User;

import java.util.ArrayList;


public class FollowingFragment extends Fragment {
    SocialAdapter socialAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public FollowingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.habi_following_fragment, container, false);

        // Sample Data

        User sample1 = new User("hello123");
        User sample2 = new User("another User");
        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(sample1);
        userArrayList.add(sample2);

        // set up recycler view

        recyclerView = view.findViewById(R.id.following_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        socialAdapter = new SocialAdapter(userArrayList, new SocialAdapter.ClickListener() {
            @Override
            public void menuButtonOnClick(View view, int position) {
                Log.d("Menu", "Clicked " + Integer.toString(position));
                showMenu(view, position);
            }

            @Override
            public void mainButtonOnClick(View view, int position) {
                // empty button not used.
            }
        }, false);
        recyclerView.setAdapter(socialAdapter);

        return view;
    }

    public void showMenu(View view, int userPosition){
        PopupMenu menu = new PopupMenu(getContext(), view);
        menu.getMenuInflater().inflate(R.menu.social_popup_menu, menu.getMenu());
        menu.getMenu().add("Remove");
        menu.getMenu().add("Block");
        menu.show();


        menu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getTitle().equals("Remove")){
                Log.d("MenuItem", "Remove Clicked");
            }
            else{
                Log.d("MenuItem", "Block Clicked");
            }
            return true;
        });
    }

}