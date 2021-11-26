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

import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.social.FollowersFragment;
import com.cmput301f21t34.habittrak.social.FollowingFragment;
import com.cmput301f21t34.habittrak.social.RequestsFragment;
import com.cmput301f21t34.habittrak.social.SearchFragment;
import com.cmput301f21t34.habittrak.social.SocialTabFragment;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class SocialFragment extends Fragment {
    public static final int FOLLOWERS = 0;
    public static final int FOLLOWING = 1;
    public static final int REQUESTS = 2;
    public static final int SEARCH = 3;
    private FollowersFragment followersFragment;
    private FollowingFragment followingFragment;
    private RequestsFragment requestsFragment;
    private SearchFragment searchFragment;
    private TabLayout socialTab;
    private final User mainUser;
    private ViewPager2 viewPager;

    public SocialFragment(User mainUser) {
        this.mainUser = mainUser;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.followersFragment = new FollowersFragment(mainUser, mainUser.getFollowerList());
        this.followingFragment = new FollowingFragment(mainUser, mainUser.getFollowingList());
        this.requestsFragment = new RequestsFragment(mainUser, mainUser.getFollowerReqList());
        // Initialise searchFragment on separate thread because need to call slow database method
        new SocialAsyncTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
            searchFragment = new SearchFragment(mainUser, new DatabaseManager().getAllUsers());
            return null;
        }
    }
}
