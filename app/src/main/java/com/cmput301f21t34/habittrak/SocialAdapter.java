package com.cmput301f21t34.habittrak;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

//TODO: Send menu options in the adapter itself

/**
 * SocialAdapter
 *
 * @author Pranav
 * @author Kaaden
 *
 * Custom Recycler View Adapter for users on the social page
 * @version 1.0
 * @since 2021-11-01
 * @see RecyclerView
 */
public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.ViewHolder> {

    private final ArrayList<User> profiles;
    private final ClickListener listener;
    private final boolean buttonVisibility;
    private final String buttonText;

    public interface ClickListener {
        void menuButtonOnClick(View view, int position);

        void mainButtonOnClick(View view, int position);
    }

    /**
     * ViewHolder
     *
     * View Holder of the SocialAdapter
     *
     * @see RecyclerView.ViewHolder
     * @author Pranav
     * @version 1.0
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView username;
        private final MaterialButton mainButton;
        private final ImageButton menuButton;
        private final TextView userBio;
        private ClickListener listenerRef;

        public ViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.username_social_page);
            mainButton = view.findViewById(R.id.social_main_button);
            menuButton = view.findViewById(R.id.social_menu);
            userBio = view.findViewById(R.id.social_user_bio);
            menuButton.setOnClickListener(this);
            mainButton.setOnClickListener(this);
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

        @Override
        public void onClick(View view) {
            if (listenerRef != null) {
                if (view.getId() == R.id.social_main_button) {
                    listenerRef.mainButtonOnClick(view, getAdapterPosition());
                } else if (view.getId() == R.id.social_menu) {
                    listenerRef.menuButtonOnClick(view, getAdapterPosition());
                }
            }
        }

        /**
         * makeButtonVisible
         *
         * set the visibility of the button depending on the usage of the adapter
         *
         * @author Pranav
         */
        public void makeButtonVisible() {
            mainButton.setVisibility(View.VISIBLE);
        }

        /**
         * makeButtonInvisible
         *
         * set the visibility of the button depending on the usage of the adapter
         *
         * @author Pranav
         */
        public void makeButtonInvisible() {
            mainButton.setVisibility(View.INVISIBLE);
        }

        /**
         * setButtonText
         *
         * sets the test of the main button in the recycler view
         * @param text
         */
        public void setButtonText(String text) {
            mainButton.setText(text);
        }

    }


    // class constructor
    public SocialAdapter(ArrayList<User> users, ClickListener listener, boolean visible, String buttonText) {
        this.profiles = users;
        this.listener = listener;
        this.buttonVisibility = visible;
        this.buttonText = buttonText;
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
        holder.listenerRef = this.listener;
        holder.setButtonText(buttonText);

        if (buttonVisibility) {
            holder.makeButtonVisible();
        } else {
            holder.makeButtonInvisible();
        }

    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }


}
