package com.cmput301f21t34.habittrak;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;


/**
 * SocialAdapter
 *
 * Custom list for users on the social page
 */
public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.ViewHolder>{

    private ArrayList<User> profiles;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView username;
        private final MaterialButton mainButton;
        private final ImageButton menuButton;
        private final TextView userBio;

        public ViewHolder(View view){
            super(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("SocialAdapter", "Element " + getAdapterPosition() + " clicked");
                }
            });
            username  = (TextView) view.findViewById(R.id.username_social_page);
            mainButton = (MaterialButton) view.findViewById(R.id.social_main_button);
            menuButton = (ImageButton) view.findViewById(R.id.social_menu);
            userBio = (TextView) view.findViewById(R.id.social_user_bio);
        }


        public TextView getUsername() {
            return username;
        }

        public MaterialButton getMainButton() {
            return mainButton;
        }

        public ImageButton getMenuButton() {
            return menuButton;
        }

        public TextView getUserBio() {
            return userBio;
        }
    }

    public SocialAdapter(ArrayList<User> users){
        this.profiles = users;
    }

    @NonNull
    @Override
    public SocialAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.social_page_listview_content, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SocialAdapter.ViewHolder holder, int position) {
        // TODO: Update function to get bio
        User user = profiles.get(position);
        holder.getUsername().setText(user.getUsername());
        holder.getUserBio().setText(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }
}
