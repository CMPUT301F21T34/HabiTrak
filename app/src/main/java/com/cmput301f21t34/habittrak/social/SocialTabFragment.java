package com.cmput301f21t34.habittrak.social;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.fragments.SocialFragment;
import com.cmput301f21t34.habittrak.recycler.SocialAdapter;
import com.cmput301f21t34.habittrak.user.User;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class SocialTabFragment extends Fragment {
    private final DatabaseManager dm = new DatabaseManager();
    private final SocialFragment socialRef;
    // Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SocialAdapter socialAdapter;
    private ShimmerFrameLayout loading;
    // Data
    private final User mainUser;
    private ArrayList<String> UUIDs;
    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<String> bios = new ArrayList<>();

    public SocialTabFragment(SocialFragment socialRef, User mainUser, ArrayList<String> UUIDs) {
        this.socialRef = socialRef;
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
        View view = inflater.inflate(R.layout.habi_followers_fragment, container, false);

        socialAdapter = new SocialAdapter(
                socialRef, mainUser, UUIDs, usernames, bios, "none");

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
            new SocialAsyncTask().execute();
        }
    }

    /**
     * Gets the data in background
     */
    public class SocialAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
//            loading.setVisibility(View.VISIBLE);   // Appear visuals
//            loading.startShimmer();             // Visual effect
        }

        @Override
        protected Void doInBackground(Void... voids) {
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

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
//            socialAdapter.notifyDataSetChanged();   // Tell display
//            loading.stopShimmer();                  // Stop visuals
//            loading.setVisibility(View.GONE);       // Disappear visuals
        }
    }

}
