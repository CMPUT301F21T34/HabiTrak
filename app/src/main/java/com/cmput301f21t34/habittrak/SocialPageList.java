package com.cmput301f21t34.habittrak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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

    SocialPageList(Context context, ArrayList<User> profiles) {
        super(context, 0, profiles);
        this.context = context;
        this.profiles = profiles;
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



        return view;
    }
}
