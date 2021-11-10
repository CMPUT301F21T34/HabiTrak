package com.cmput301f21t34.habittrak.socialFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.SocialAdapter;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

/**
 * FollowingFragment
 * <p>
 * Fragment for displaying users the main user is following
 *
 * @author Pranav
 * @author Kaaden
 * @version 1.0
 * @see SocialAdapter
 * @since 2021-11-01
 */
public class FollowingFragment extends Fragment {
    DatabaseManager dm = new DatabaseManager();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SocialAdapter socialAdapter;
    User mainUser;

    public FollowingFragment(User mainUser) {
        this.mainUser = mainUser;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.habi_following_fragment, container, false);

        // set up recycler view
        recyclerView = view.findViewById(R.id.following_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<String> profiles = mainUser.getFollowingList();       // Users that mainUser follows
        socialAdapter = new SocialAdapter(profiles, new SocialAdapter.ClickListener() {
            @Override
            public void menuButtonOnClick(View view, int position) {
                Log.d("Menu", "Clicked " + position);
                showMenu(view, position);
            }

            @Override
            public void mainButtonOnClick(View view, int position) {
                ButtonClicked(view, position);
            }
        }, true, "Unfollow");
        recyclerView.setAdapter(socialAdapter);

        return view;
    }

    /**
     * showMenu
     * <p>
     * listener function for ImageButton in Recycler View
     *
     * @param view
     * @param userPosition position of the clicked menu in the adapter
     * @see SocialAdapter
     */
    public void showMenu(View view, int userPosition) {
        PopupMenu menu = new PopupMenu(getContext(), view);
        menu.getMenuInflater().inflate(R.menu.social_popup_menu, menu.getMenu());
        menu.getMenu().add("Remove");
        menu.getMenu().add("Block");
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
     * <p>
     * listener for the button in Recycler View
     *
     * @param view
     * @param userPosition
     * @see SocialAdapter
     */
    public void ButtonClicked(View view, int userPosition) {
        MaterialButton button = view.findViewById(R.id.social_main_button);
        Log.d("ListButton", "Clicked");
        //TODO: implement this function
    }

}
