package com.cmput301f21t34.habittrak.social;

import android.os.AsyncTask;
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
import com.cmput301f21t34.habittrak.recycler.SocialAdapter;
import com.cmput301f21t34.habittrak.user.User;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

/**
 * Fragment for displaying users that follow the main user
 *
 * @see SocialAdapter
 * @author Pranav
 * @author Kaaden
 */
public class FollowersFragment extends Fragment {
    public static String TAG = "FOLLOWERS_FRAGMENT";
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

    public FollowersFragment(User mainUser, ArrayList<String> UUIDs) {
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
        View view = inflater.inflate(R.layout.habi_followers_fragment, container, false);
        loading = view.findViewById(R.id.followers_shimmer_container);
        loading.setVisibility(View.GONE); // Invisible by default

        populateList(); // Begin fetching data for display

        // Set up recycler view
        recyclerView = view.findViewById(R.id.followers_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        socialAdapter = new SocialAdapter(mainUser, UUIDs, usernames, new SocialAdapter.ClickListener() {
            @Override
            public void menuButtonOnClick(View view, int position) {
                Log.d("Menu", "Clicked " + position);
                showMenu(view, position);
            }

            @Override
            public void mainButtonOnClick(View view, int position) {
                MaterialButton button = view.findViewById(R.id.social_main_button);
            }
        }, bios, "none");
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
     * Populates usernames and bios to display, except those that are from users that block or are
     * blocked by mainUser
     *
     * @author Kaaden
     */
    public void populateList() {
        // Only populate if empty
        if (usernames.isEmpty()) {
            new FollowersAsyncTask().execute();
        }
    }

    /**
     * Gets the data in background
     */
    public class FollowersAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            loading.setVisibility(View.VISIBLE);   // Appear visuals
            loading.startShimmer();             // Visual effect
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
            socialAdapter.notifyDataSetChanged();   // Tell display
            loading.stopShimmer();                  // Stop visuals
            loading.setVisibility(View.GONE);       // Disappear visuals
        }
    }

}
