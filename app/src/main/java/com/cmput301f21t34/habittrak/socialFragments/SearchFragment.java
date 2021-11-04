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
import android.widget.SearchView;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.SocialAdapter;

import com.cmput301f21t34.habittrak.user.User;


import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

/**
 * SearchFragment
 *
 *
 * @author Pranav
 * @author Kaaden
 *
 *
 * @see SocialAdapter
 * @see SearchView
 * @version 1.0
 * @since 2021-11-01
 *
 */
public class SearchFragment extends Fragment {

    SocialAdapter socialAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.habi_search_fragment, container, false);

        // Sample Data
        User sample1 = new User("hello123");
        User sample2 = new User("another User");
        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(sample1);
        userArrayList.add(sample2);

        // setting up recycler view
        recyclerView = view.findViewById(R.id.search_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        socialAdapter = new SocialAdapter(userArrayList, new SocialAdapter.ClickListener() {
            @Override
            public void menuButtonOnClick(View view, int position) {
                Log.d("Menu", "Clicked " + position);
                showMenu(view, position);
            }

            @Override
            public void mainButtonOnClick(View view, int position) {
                ButtonClicked(view, position);
            }
        }, true, "Follow");
        recyclerView.setAdapter(socialAdapter);

        // TODO search stuff
        SearchView searchBox = view.findViewById(R.id.social_search_box);

        return view;
    }

    /**
     * showMenu
     *
     * listener function for ImageButton in Recycler View
     *
     * @see SocialAdapter
     * @param view
     * @param userPosition position of the clicked menu in the adapter
     */
    public void showMenu(View view, int userPosition) {
        PopupMenu menu = new PopupMenu(getContext(), view);
        menu.getMenuInflater().inflate(R.menu.social_popup_menu, menu.getMenu());
        menu.getMenu().add("Block");
        Log.d("image Button", "menu button clicked");
        menu.show();

        menu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getTitle().equals("Remove")) {
                Log.d("MenuItem", "Remove Clicked");
            } else {
                Log.d("MenuItem", "Block Clicked");
            }
            return true;
        });
    }

    /**
     * ButtonClicked
     *
     * listener for the button in Recycler View
     *
     * @see SocialAdapter
     * @param view
     * @param userPosition
     */
    public void ButtonClicked(View view, int userPosition) {
        MaterialButton button = view.findViewById(R.id.social_main_button);
        Log.d("ListButton", "Clicked");
    }

}
