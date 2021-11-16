package com.cmput301f21t34.habittrak.fragments;

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
import com.cmput301f21t34.habittrak.social.FollowersFragment;
import com.cmput301f21t34.habittrak.social.FollowingFragment;
import com.cmput301f21t34.habittrak.social.RequestsFragment;
import com.cmput301f21t34.habittrak.social.SearchFragment;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.tabs.TabLayout;

public class SocialFragment extends Fragment {
    FollowersFragment followersFragment;
    FollowingFragment followingFragment;
    RequestsFragment requestsFragment;
    SearchFragment searchFragment;
    TabLayout socialTab;
    User mainUser;
    ViewPager2 viewPager;

    public SocialFragment(User mainUser) {
        this.mainUser = mainUser;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_social, container, false);

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

        // Initialise Fragments
        // TODO maybe move these into base onCreate if doesn't cause issues with things not updating because this is called repeatedly possibly leading to reduced performance
        followersFragment = new FollowersFragment(mainUser);
        followingFragment = new FollowingFragment(mainUser);
        requestsFragment = new RequestsFragment(mainUser);
        searchFragment = new SearchFragment(mainUser);

        // tab listener
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
                case 0:
                    return followersFragment;
                case 1:
                    return followingFragment;
                case 2:
                    return requestsFragment;
                default:
                    return searchFragment;
            }
        }

        @Override
        public int getItemCount() {
            return socialTab.getTabCount();
        }
    }
}
