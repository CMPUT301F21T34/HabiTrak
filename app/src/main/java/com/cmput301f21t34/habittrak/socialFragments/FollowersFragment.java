package com.cmput301f21t34.habittrak.socialFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.SocialAdapter;
import com.cmput301f21t34.habittrak.user.User;

import java.util.ArrayList;

//TODO: Add functions for Blocking and Removing Followers

/**
 * Followers Fragment
 *
 * @author Pranav
 * @author Kaaden
 *
 * Fragment for displaying users followers
 *
 * @see SocialAdapter
 * @version 1.0
 * @since 2021-11-1
 */
public class FollowersFragment extends Fragment {

    SocialAdapter socialAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ImageButton imageButton;

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
        User sample1 = new User("Henry");
        User sample2 = new User("Jakob");
        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(sample1);
        userArrayList.add(sample2);

        // setting up recycler view
        recyclerView = view.findViewById(R.id.followers_recycler_view);
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
                // empty button not used.
            }
        }, false, "null");
        recyclerView.setAdapter(socialAdapter);


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
        menu.getMenu().add("Remove");
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
}
