package com.cmput301f21t34.habittrak.social;

import android.os.AsyncTask;
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

import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.recycler.SocialAdapter;
import com.cmput301f21t34.habittrak.user.User;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

//TODO: Add functions for Blocking and Removing Followers

/**
 * Followers Fragment
 *
 * @author Pranav
 * @author Kaaden
 * <p>
 * Fragment for displaying users followers
 * @version 1.0
 * @see SocialAdapter
 * @since 2021-11-1
 */
public class FollowersFragment extends Fragment {
    public static String TAG = "FOLLOWERS_FRAGMENT";
    private DatabaseManager dm = new DatabaseManager();
    // Views
    private ImageButton imageButton;
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
        View view = inflater.inflate(R.layout.habi_followers_fragment, container, false);

        // setting up recycler view
        recyclerView = view.findViewById(R.id.followers_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        loading = view.findViewById(R.id.followers_shimmer_container);

        // setting social adapter
        socialAdapter = new SocialAdapter(mainUser.getFollowerList(), usernames, new SocialAdapter.ClickListener() {
            @Override
            public void menuButtonOnClick(View view, int position) {
                Log.d("Menu", "Clicked " + position);
                showMenu(view, position);
            }

            @Override
            public void mainButtonOnClick(View view, int position) {
                // empty button not used.
            }
        }, false, bios, "null");

        recyclerView.setAdapter(socialAdapter);

        // set list if empty
        if (usernames.isEmpty()) {
            new FollowersAsyncTask().execute();
            loading.startShimmer();
        } else {
            loading.setVisibility(View.GONE);
        }
        return view;
    }

    /**
     * Populates usernames and bios
     *
     * @author Kaaden
     */
    public void populateUsernamesAndBios() {
        mainUser.getFollowerList().forEach(UUID -> {
            usernames.add(dm.getUserName(UUID));
            bios.add(dm.getUserBio(UUID));
        });
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
     * FollowersAsyncTask
     * <p>
     * gets the data in background
     */
    public class FollowersAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            populateUsernamesAndBios();
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
