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
    private DatabaseManager dm = new DatabaseManager();
    private ImageButton imageButton;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SocialAdapter socialAdapter;
    private ArrayList<String> followersList = new ArrayList<>();
    private ArrayList<String> bioList = new ArrayList<>();
    private User mainUser;

    public FollowersFragment(User mainUser) {
        this.mainUser = mainUser;
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

        // setting social adapter
        socialAdapter = new SocialAdapter(followersList, new SocialAdapter.ClickListener() {
            @Override
            public void menuButtonOnClick(View view, int position) {
                Log.d("Menu", "Clicked " + position);
                showMenu(view, position);
            }

            @Override
            public void mainButtonOnClick(View view, int position) {
                // empty button not used.
            }
        }, false, bioList,"null");

        recyclerView.setAdapter(socialAdapter);

        // set list if empty
        if (followersList.isEmpty()){
            new FollowersAsyncTask().execute();
        }
        return view;
    }

    /**
     * getUserList
     *
     * @author Pranav
     *
     * get the followers username and bio
     */
    public void getUserList(){
        ArrayList<String> followersEmail = mainUser.getFollowerList();
        for (String user: followersEmail){
            followersList.add(dm.getUserName(user));
            bioList.add(dm.getUserBio(user));
        }
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

    public class FollowersAsyncTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            getUserList();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            socialAdapter.notifyDataSetChanged();
        }
    }

}
