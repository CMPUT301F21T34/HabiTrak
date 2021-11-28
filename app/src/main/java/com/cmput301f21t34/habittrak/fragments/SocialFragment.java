package com.cmput301f21t34.habittrak.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.recycler.SocialAdapter;
import com.cmput301f21t34.habittrak.social.SocialTabFragment;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.tabs.TabLayout;

public class SocialFragment extends Fragment {
    public static final int FOLLOWERS = 0;
    public static final int FOLLOWING = 1;
    public static final int REQUESTS = 2;
    public static final int SEARCH = 3;
    private static final boolean SEARCHABLE = true;
    private static final boolean NOT_SEARCHABLE = false;
    private final User mainUser;
    private final SocialTabFragment followersFragment;
    private final SocialTabFragment followingFragment;
    private final SocialTabFragment requestsFragment;
    private SocialTabFragment searchFragment;
    private TabLayout socialTab;
    private ViewPager2 viewPager;

    public SocialFragment(User mainUser) {
        this.mainUser = mainUser;
        // We do the below ASAP so that we can start database processes ASAP
        this.followersFragment = new SocialTabFragment(this, mainUser,
                SocialTabFragment.FOLLOWERS, SocialAdapter.NONE, NOT_SEARCHABLE);
        this.followingFragment = new SocialTabFragment(this, mainUser,
                SocialTabFragment.FOLLOWINGS, SocialAdapter.UNFOLLOW, NOT_SEARCHABLE);
        this.requestsFragment = new SocialTabFragment(this, mainUser,
                SocialTabFragment.FOLLOWER_REQUESTS, SocialAdapter.ACCEPT, NOT_SEARCHABLE);
        // Initialise searchFragment on separate thread because need to call slow database method
        new SocialAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        // Start fetching data for the lists
        followersFragment.startPopulateList();
        followingFragment.startPopulateList();
        requestsFragment.startPopulateList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.habi_social_fragment, container, false);

        // setting views
        socialTab = view.findViewById(R.id.social_tab_layout);
        viewPager = view.findViewById(R.id.social_view_pager);

        // setting up the adapter
        FragmentManager fm = getChildFragmentManager();
        ViewStateAdapter viewStateAdapter = new ViewStateAdapter(fm, getLifecycle());
        viewPager.setAdapter(viewStateAdapter);

        // setting up tabs in tabLayout
        socialTab.addTab(socialTab.newTab().setText("Followers"));
        socialTab.addTab(socialTab.newTab().setText("Following"));
        socialTab.addTab(socialTab.newTab().setText("Requests"));
        socialTab.addTab(socialTab.newTab().setText("Search"));

        // For tapping between tabs
        socialTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // setting view pager slider. Adds the sliding feature to the fragments
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                socialTab.selectTab(socialTab.getTabAt(position));
            }
        });

        return view;
    }

    /**
     * Adds the specified user entry to the specified tab's list if its UUID is not already present
     *
     * @param tab      int, the tab number, a public attribute of this class
     * @param UUID     String, the UUID of the user
     * @param username String, the user's username
     * @param bio      String, the user's bio
     */
    public void addUserEntry(int tab, String UUID, String username, String bio) {
        switch (tab) {
            case FOLLOWERS:
                followersFragment.addUserEntry(UUID, username, bio);
                break;
            case FOLLOWING:
                followingFragment.addUserEntry(UUID, username, bio);
                break;
            case REQUESTS:
                requestsFragment.addUserEntry(UUID, username, bio);
                break;
            case SEARCH:
                searchFragment.addUserEntry(UUID, username, bio);
                break;
            default:
                throw new RuntimeException("Tab not found");
        }
    }

    /**
     * Removes the entry specified by UUID from the specified tab's list
     *
     * @param tab  int, the tab number, a public attribute of this class
     * @param UUID String, the UUID of the user whose entry to remove
     */
    public void removeUserEntry(int tab, String UUID) {
        switch (tab) {
            case FOLLOWERS:
                followersFragment.removeUserEntry(UUID);
                break;
            case FOLLOWING:
                followingFragment.removeUserEntry(UUID);
                break;
            case REQUESTS:
                requestsFragment.removeUserEntry(UUID);
                break;
            case SEARCH:
                searchFragment.removeUserEntry(UUID);
                break;
            default:
                throw new RuntimeException("Tab not found");
        }
    }

    /**
     * ViewStateAdapter
     * <p>
     * Adapter for the view pager
     *
     * @author Pranav
     * @author Kaaden
     */
    private class ViewStateAdapter extends FragmentStateAdapter {

        public ViewStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case FOLLOWERS:
                    return followersFragment;
                case FOLLOWING:
                    return followingFragment;
                case REQUESTS:
                    return requestsFragment;
                case SEARCH:
                    return searchFragment;
                default:
                    throw new RuntimeException("Tab not found");
            }
        }

        @Override
        public int getItemCount() {
            return socialTab.getTabCount();
        }
    }


    /**
     * Gets the data in background
     */
    private class SocialAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            // Initialise searchFragment here because need to call a database method
            searchFragment = new SocialTabFragment(SocialFragment.this, mainUser,
                    SocialTabFragment.ALL, SocialAdapter.NONE, SEARCHABLE);
            return null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            searchFragment.startPopulateList(); // Start fetching data for the list
        }
    }

}
