package com.cmput301f21t34.habittrak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.cmput301f21t34.habittrak.socialFragments.FollowersFragment;
import com.cmput301f21t34.habittrak.socialFragments.FollowingFragment;
import com.cmput301f21t34.habittrak.socialFragments.RequestsFragment;
import com.cmput301f21t34.habittrak.socialFragments.SearchFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

/**
 * SocialActivity
 *
 * @author Pranav
 *
 * Hold the Social Activity fragments
 *
 * @version 1.0
 * @since 2021-10-27
 */
public class SocialActivity extends AppCompatActivity {

    TabLayout socialTab;
    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        // add back button to toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.social_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // setting views
        socialTab = findViewById(R.id.social_tab_layout);
        viewPager = findViewById(R.id.social_view_pager);

        // setting up the adapter
        FragmentManager fm = getSupportFragmentManager();
        ViewStateAdapter viewStateAdapter = new ViewStateAdapter(fm, getLifecycle());
        viewPager.setAdapter(viewStateAdapter);

        // setting up tabs in tabLayout
        socialTab.addTab(socialTab.newTab().setText("Followers"));
        socialTab.addTab(socialTab.newTab().setText("Following"));
        socialTab.addTab(socialTab.newTab().setText("Requests"));
        socialTab.addTab(socialTab.newTab().setText("Search"));

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

        // setting view pager slider
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                socialTab.selectTab(socialTab.getTabAt(position));
            }
        });

    }

    /**
     * ViewStateAdapter
     *
     * Adapter for the view pager
     * @author Pranav
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
                    return new FollowersFragment();
                case 1:
                    return new FollowingFragment();
                case 2:
                    return new RequestsFragment();
                default:
                    return new SearchFragment();
            }
        }

        @Override
        public int getItemCount() {
            // Hardcoded, use lists
            return 4;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}