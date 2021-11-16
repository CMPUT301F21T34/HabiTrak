package com.cmput301f21t34.habittrak.social;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.SocialAdapter;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

/**
 * SearchFragment
 *
 * @author Pranav
 * @author Kaaden
 * @version 1.0
 * @see SocialAdapter
 * @see SearchView
 * @since 2021-11-01
 */
public class SearchFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SocialAdapter socialAdapter;
    User mainUser;

    public SearchFragment(User mainUser) {
        this.mainUser = mainUser;
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

        // setting up recycler view
        recyclerView = view.findViewById(R.id.search_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        socialAdapter = new SocialAdapter(new ArrayList<>(), new SocialAdapter.ClickListener() {
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
    }

}
