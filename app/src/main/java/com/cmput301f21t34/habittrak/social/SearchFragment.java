package com.cmput301f21t34.habittrak.social;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.recycler.SocialAdapter;
import com.cmput301f21t34.habittrak.user.User;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

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
    public static String TAG = "SEARCH_FRAGMENT";
    private DatabaseManager dm = new DatabaseManager();
    // Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SocialAdapter socialAdapter;
    private ShimmerFrameLayout loading;
    // Data
    private ArrayList<String> displayList = new ArrayList<>();
    private ArrayList<String> bioList = new ArrayList<>();
    private User mainUser;



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
        SearchView searchBox = view.findViewById(R.id.social_search_box);
        loading = view.findViewById(R.id.shimmer_container);
        loading.startShimmer();

        // setting up recycler view
        recyclerView = view.findViewById(R.id.search_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        socialAdapter = new SocialAdapter(displayList, new SocialAdapter.ClickListener() {
            @Override
            public void menuButtonOnClick(View view, int position) {
                Log.d("Menu", "Clicked " + position);
                showMenu(view, position);
            }

            @Override
            public void mainButtonOnClick(View view, int position) {
                ButtonClicked(view, position);
            }
        }, true, bioList, "Follow");
        recyclerView.setAdapter(socialAdapter);


        // search box listener
        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                socialAdapter.getFilter().filter(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                socialAdapter.getFilter().filter(s);
                return true;
            }
        });

        new SearchAsyncTask().execute();
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
            if (menuItem.getTitle().equals("Block")) {
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

    /**
     * getAllUsers.
     *
     * @author Pranav
     *
     * gets all users and removes the blocked and blocked by from the display list.
     */
    public void getAllUsers(){
        ArrayList<String> users = dm.getAllUsers();
        ArrayList<String> blockedUsers = mainUser.getBlockList();
        ArrayList<String> blockedBy = mainUser.getBlockedByList();

        for (String user: users){
            if(!blockedBy.contains(user) && !blockedUsers.contains(user)){
                displayList.add(dm.getUserName(user));
                bioList.add(dm.getUserBio(user));
                Log.d(TAG, user);
            }
        }

    }

    /**
     * SearchAsyncTask
     *
     * @author Pranav
     *
     * gets the data in background on another thread.
     */
    @SuppressLint("StaticFieldLeak")
    public class SearchAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            getAllUsers();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            socialAdapter.notifyDataSetChanged();
            loading.stopShimmer();
            loading.setVisibility(View.GONE);
        }
    }
}
