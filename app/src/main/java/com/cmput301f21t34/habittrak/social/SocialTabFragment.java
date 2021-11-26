package com.cmput301f21t34.habittrak.social;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.fragments.SocialFragment;
import com.cmput301f21t34.habittrak.recycler.SocialAdapter;
import com.cmput301f21t34.habittrak.user.User;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

/**
 * Fragment for displaying a list of users and their info in a tab
 *
 * @see SocialAdapter
 * @see SearchView
 * @author Kaaden
 * @author Pranav
 */
public class SocialTabFragment extends Fragment {
    // Data
    private final User mainUser;
    private final ArrayList<String> UUIDs;
    private final ArrayList<String> usernames = new ArrayList<>();
    private final ArrayList<String> bios = new ArrayList<>();
    // Views
    private ShimmerFrameLayout loading;
    private SearchView searchBox;
    private final boolean searchable;
    private final SocialAdapter socialAdapter;

    public SocialTabFragment(SocialFragment socialRef, User mainUser, ArrayList<String> UUIDs,
                             String defaultButtonText, boolean searchable) {
        this.mainUser = mainUser;
        this.UUIDs = UUIDs;
        this.searchable = searchable;
        socialAdapter = new SocialAdapter(
                socialRef, mainUser, UUIDs, usernames, bios, defaultButtonText);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.habi_social_tab_fragment, container, false);

        // Shimmer effect
        loading = view.findViewById(R.id.social_tab_shimmer_container);
        loading.setVisibility(View.GONE); // Invisible by default

        populateList(); // Begin fetching data for display

        // List display
        RecyclerView recyclerView = view.findViewById(R.id.social_tab_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(socialAdapter);

        // Search bar
        searchBox = view.findViewById(R.id.social_tab_search_box);
        searchBox.setVisibility(View.GONE); // Off until done getting users
        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String s) {
                socialAdapter.getFilter().filter(s);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String s) {
                socialAdapter.getFilter().filter(s);
                return true;
            }
        });

        return view;
    }

    /**
     * Adds the specified user entry to this tab's list if its UUID is not already present
     *
     * @param UUID     String, the UUID of the user
     * @param username String, the user's username
     * @param bio      String, the user's bio
     */
    public void addUserEntry(String UUID, String username, String bio) {
        socialAdapter.addUserEntry(UUID, username, bio);
    }

    /**
     * Removes the entry specified by UUID from this tab's list
     *
     * @param UUID String, the UUID of the user whose entry to remove
     */
    public void removeUserEntry(String UUID) {
        socialAdapter.removeUserEntry(UUID);
    }

    /**
     * Populates usernames and bios to display, except those that are from users that block or are
     * blocked by mainUser
     *
     * @author Kaaden
     */
    public void populateList() {
        // Only populate if empty
        if (usernames.isEmpty()) {
            new SocialTabAsyncTask().execute();
        }
    }

    /**
     * Gets the user data for the list entry in the background
     */
    public class SocialTabAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            loading.setVisibility(View.VISIBLE);   // Appear visuals
            loading.startShimmer();             // Visual effect
        }
        @Override
        protected Void doInBackground(Void... voids) {
            final DatabaseManager dm = new DatabaseManager();
            // Remove unwanted users that might be present
            UUIDs.removeAll(mainUser.getBlockList());
            UUIDs.removeAll(mainUser.getBlockedByList());
            UUIDs.remove(mainUser.getEmail());
            // Save info
            UUIDs.forEach(UUID -> {
                usernames.add(dm.getUserName(UUID));
                bios.add(dm.getUserBio(UUID));
            });
            return null;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            socialAdapter.notifyDataSetChanged();   // Tell display
            loading.stopShimmer();                  // Stop visuals
            loading.setVisibility(View.GONE);       // Disappear visuals
            if (searchable) {
                searchBox.setVisibility(View.VISIBLE);  // Allow searches now
            }
        }
    }

}
