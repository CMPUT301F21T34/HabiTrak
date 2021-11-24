package com.cmput301f21t34.habittrak.social;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
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

import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.recycler.SocialAdapter;
import com.cmput301f21t34.habittrak.user.User;
import com.facebook.shimmer.ShimmerFrameLayout;
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
    public static String TAG = "SEARCH_FRAGMENT";
    private DatabaseManager dm = new DatabaseManager();
    // Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SocialAdapter socialAdapter;
    private ShimmerFrameLayout loading;
    // Data
    private User mainUser;
    private ArrayList<String> UUIDs;
    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<String> bios = new ArrayList<>();


    public SearchFragment(User mainUser, ArrayList<String> UUIDs) {
        this.mainUser = mainUser;
        this.UUIDs = UUIDs;
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


        // setting up recycler view
        recyclerView = view.findViewById(R.id.search_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        socialAdapter = new SocialAdapter(UUIDs, usernames, new SocialAdapter.ClickListener() {
            @Override
            public void menuButtonOnClick(View view, int position) {
                Log.d("Menu", "Clicked " + position);
                showMenu(view, position);
            }

            @Override
            public void mainButtonOnClick(View view, int position) {
                ButtonClicked(view, position);
            }
        }, true, bios, "Follow");
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

        if (usernames.isEmpty()) {
            new SearchAsyncTask().execute();
            loading.startShimmer();
        } else {
            loading.setVisibility(View.GONE);
        }

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
     * Populates usernames and bios to display, except those that are from users that block or are
     * blocked by mainUser.
     *
     * @author Kaaden
     */
    public void populateList() {
        UUIDs = dm.getAllUsers();
        UUIDs.removeAll(mainUser.getBlockList());
        UUIDs.removeAll(mainUser.getBlockedByList());
        UUIDs.remove(mainUser.getEmail());
        UUIDs.forEach(UUID -> {
            usernames.add(dm.getUserName(UUID));
            bios.add(dm.getUserBio(UUID));
        });
    }

    /**
     * SearchAsyncTask
     *
     * @author Pranav
     * <p>
     * gets the data in background on another thread.
     */
    @SuppressLint("StaticFieldLeak")
    public class SearchAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            populateList();
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
