package com.cmput301f21t34.habittrak.social;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    private LinearLayout noDataView;
    private RecyclerView recyclerView;
    // Other
    private final SocialAdapter socialAdapter;
    private final boolean searchable;
    private final SocialTabAsyncTask fetchData = new SocialTabAsyncTask();

    public SocialTabFragment(SocialFragment socialRef, User mainUser, ArrayList<String> UUIDs,
                             String defaultButtonText, boolean searchable) {
        this.mainUser = mainUser;
        this.UUIDs = UUIDs;
        this.searchable = searchable;
        socialAdapter = new SocialAdapter(
                socialRef, mainUser, UUIDs, usernames, bios, defaultButtonText);
        socialAdapter.setSocialListener(this::onRowClick);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Views
        View view = inflater.inflate(
                R.layout.habi_social_tab_fragment, container, false);
        // No data display
        noDataView = view.findViewById(R.id.social_no_data_view);
        // List display
        recyclerView = view.findViewById(R.id.social_tab_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(socialAdapter);
        // Shimmer effect
        loading = view.findViewById(R.id.social_tab_shimmer_container);
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

        // Invisible by default
        noDataView.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        searchBox.setVisibility(View.GONE);

        // Set display based on if still fetching data or not
        if (fetchData.getStatus() != AsyncTask.Status.FINISHED) {
            // Loading Visuals
            loading.setVisibility(View.VISIBLE);
            loading.startShimmer();
        } else {
            displayViews();
        }

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
            fetchData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // run parallel tasks
        }
    }

    /**
     * Start view profile activity when a row is clicked in the recycler view
     * @param view viewHolder of the recycler
     * @param position position in the List
     */
    public void onRowClick(View view, int position){
        Log.d("Social", "Row Clicked " + position);
        DatabaseManager dm = new DatabaseManager();
        String UUID = UUIDs.get(position);
        Log.d("Social", UUID);
        // Display user profile if main user is following a given user
        if (mainUser.getFollowingList().contains(UUID)) {
            Intent intent = new Intent(getContext(), SocialViewProfile.class);
            intent.putExtra("USER", dm.getUser(UUID));
            startActivity(intent);
        }
    }

    /**
     * UI updates for when the data fetching is done
     */
    public void displayViews() {
            if (UUIDs.isEmpty()) {
                // Don't cause null references if fragment view isn't created yet
                if (noDataView != null) {
                    noDataView.setVisibility(View.VISIBLE);
                }
            } else {
                if (loading != null) {
                    loading.stopShimmer();                  // Stop visuals
                    loading.setVisibility(View.GONE);       // Disappear visuals
                }
                if (recyclerView != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                }
                if (searchBox != null && searchable) {
                    searchBox.setVisibility(View.VISIBLE);  // Allow searches now
                }
            }
    }


    /**
     * Gets the user data for the list entry in the background
     */
    public class SocialTabAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            final DatabaseManager dm = new DatabaseManager();
            // Remove unwanted users that might be present
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
            socialAdapter.notifyDataSetChanged();   // Tell list manager
            displayViews();
        }
    }
}
