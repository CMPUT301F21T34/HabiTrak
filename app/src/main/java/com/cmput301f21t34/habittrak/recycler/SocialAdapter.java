package com.cmput301f21t34.habittrak.recycler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

//TODO: Send menu options in the adapter itself
//TODO: change username to email
/**
 * SocialAdapter
 *
 * @author Pranav
 * @author Kaaden
 * <p>
 * Custom Recycler View Adapter for users on the social page
 * @version 1.0
 * @see RecyclerView
 * @since 2021-11-01
 */
public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.ViewHolder> implements Filterable{
    private ArrayList<String> profiles;   // UUIDS (emails as of 10/11)
    private ArrayList<String> bio;
    private final ArrayList<String> profilesCopy;
    private final ArrayList<String> bioCopy;
    private final ClickListener listener;
    private final boolean buttonVisibility;
    private final String buttonText;

    // class constructor
    public SocialAdapter(ArrayList<String> users, ClickListener listener, boolean visible,
                         ArrayList<String> bio, String buttonText) {
        this.profiles = users;
        this.listener = listener;
        this.buttonVisibility = visible;
        this.buttonText = buttonText;
        this.profilesCopy = users;
        this.bio = bio;
        this.bioCopy = bio;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                // is no input in searchView put the original list back
                if (charString.isEmpty()){
                    profiles = profilesCopy;
                    bio = bioCopy;
                } else {
                    // filter username and bio bases if username contains the characters
                    ArrayList<String> filteredProfileList = new ArrayList<>();
                    ArrayList<String> filteredBioList = new ArrayList<>();
                    for (int i = 0; i < profilesCopy.size(); i++){
                        if (profilesCopy.get(i).toLowerCase().contains(charString)){
                            filteredProfileList.add(profilesCopy.get(i));
                            filteredBioList.add(bioCopy.get(i));
                        }
                    }
                    profiles = filteredProfileList;
                    bio = filteredBioList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = profiles;
                return  filterResults;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                profiles = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public SocialAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.social_page_listview_content, parent, false);
        return new ViewHolder(view);
    }

    // Used to set up each item in the adapter
    @Override
    public void onBindViewHolder(@NonNull SocialAdapter.ViewHolder holder, int position) {
        // Get user info from database
        holder.getUsername().setText(profiles.get(position));
        holder.getUserBio().setText(bio.get(position));

        // Button setup
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

    public interface ClickListener {
        void menuButtonOnClick(View view, int position);

        void mainButtonOnClick(View view, int position);
    }

    /**
     * ViewHolder
     * <p>
     * View Holder of the SocialAdapter
     *
     * @author Pranav
     * @version 1.0
     * @see RecyclerView.ViewHolder
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

        public TextView getUserBio() {
            return userBio;
        }

        public MaterialButton getMainButton() {
            return mainButton;
        }

        public ImageButton getMenuButton() {
            return menuButton;
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
         * <p>
         * set the visibility of the button depending on the usage of the adapter
         *
         * @author Pranav
         */
        public void makeButtonVisible() {
            mainButton.setVisibility(View.VISIBLE);
        }

        /**
         * makeButtonInvisible
         * <p>
         * set the visibility of the button depending on the usage of the adapter
         *
         * @author Pranav
         */
        public void makeButtonInvisible() {
            mainButton.setVisibility(View.INVISIBLE);
        }

        /**
         * setButtonText
         * <p>
         * sets the test of the main button in the recycler view
         *
         * @param text -Type String; The new button text
         */
        public void setButtonText(String text) {
            mainButton.setText(text);
        }

    }
}
