package com.cmput301f21t34.habittrak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


/**
 * SocialPageList
 *
 * Custom list for users on the social page
 */
public class SocialPageList extends ArrayAdapter<User> {

    private ArrayList<User> profiles;
    private Context context;
    private User mainUser;

    SocialPageList(Context context, ArrayList<User> profiles, User mainUser) {
        super(context, 0, profiles);
        this.context = context;
        this.profiles = profiles;
        this.mainUser = mainUser;
    }

    /**
     * Set the view of the list
     * @param position position of the User in the list
     * @param convertView view to be returned
     * @param parent ViewGroup of the list
     * @return view
     */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.social_page_listview_content, parent, false);
        }

        User profile = profiles.get(position);

        TextView username = view.findViewById(R.id.username_social_page);
        Button followButton = view.findViewById(R.id.follow_button_social_page);

        username.setText(profile.getUsername());
        followButton.setText(mainUser.getFollowingList().contains(profile) ? "Unfollow" : (mainUser.getFollowerList().contains(profile) ? "Follow Back" : "Follow"));

        return view;
    }
}
