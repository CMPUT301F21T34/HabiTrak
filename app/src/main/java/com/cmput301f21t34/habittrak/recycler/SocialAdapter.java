package com.cmput301f21t34.habittrak.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

//TODO: Send menu options in the adapter itself
//TODO: change username to email

/**
 * Custom Recycler View Adapter for users on the social page
 *
 * @author Pranav
 * @author Kaaden
 * @see RecyclerView
 */
public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.ViewHolder> implements Filterable {
    // Button values
    public static final String ACCEPT = "Accept";
    public static final String FOLLOW = "Follow";
    public static final String FOLLOW_BACK = "Follow Back";
    public static final String REQUESTED = "Requested";
    public static final String UNFOLLOW = "Unfollow";
    private final User mainUser;
    private final ArrayList<String> usernamesCopy;
    private final ArrayList<String> bioCopy;
    private final ClickListener listener;
    private final String defaultButtonText;
    private ArrayList<String> UUIDs;
    private ArrayList<String> UUIDsCopy;
    private ArrayList<String> usernames;   // UUIDS (emails as of 10/11)
    private ArrayList<String> bio;

    public SocialAdapter(User mainUser, ArrayList<String> UUIDs, ArrayList<String> usernames,
                         ClickListener listener, ArrayList<String> bio, String defaultButtonText) {
        this.mainUser = mainUser;
        this.UUIDs = UUIDs;
        this.UUIDsCopy = UUIDs;
        this.usernames = usernames;
        this.usernamesCopy = usernames;
        this.listener = listener;
        this.bio = bio;
        this.bioCopy = bio;
        this.defaultButtonText = defaultButtonText;
    }

    public ArrayList<String> getUUIDs() {
        return UUIDs;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                // is no input in searchView put the original list back
                if (charString.isEmpty()) {
                    UUIDs = UUIDsCopy;
                    usernames = usernamesCopy;
                    bio = bioCopy;
                } else {
                    // filter username and bio bases if username contains the characters
                    ArrayList<String> filteredUUIDs = new ArrayList<>();
                    ArrayList<String> filteredProfiles = new ArrayList<>();
                    ArrayList<String> filteredBios = new ArrayList<>();
                    for (int i = 0; i < usernamesCopy.size(); i++) {
                        if (usernamesCopy.get(i).toLowerCase().contains(charString)) {
                            filteredUUIDs.add(UUIDsCopy.get(i));
                            filteredProfiles.add(usernamesCopy.get(i));
                            filteredBios.add(bioCopy.get(i));
                        }
                    }
                    UUIDs = filteredUUIDs;
                    usernames = filteredProfiles;
                    bio = filteredBios;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = usernames;
                return filterResults;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                usernames = (ArrayList<String>) filterResults.values;
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
        holder.getUsername().setText(usernames.get(position));
        holder.getUserBio().setText(bio.get(position));

        // Button setup
        holder.listenerRef = this.listener;

        // Set appropriate button text based on tab and relationships of main user with each user
        if (defaultButtonText.equals(ACCEPT) || defaultButtonText.equals(UNFOLLOW)) {
            // Requests and Following tabs
            holder.setButtonText(defaultButtonText);
        } else {
            // Followers and Search tabs
            if (mainUser.getFollowingList().contains(UUIDs.get(position))) {
                // If main user follows a given user
                holder.setButtonText(UNFOLLOW);
            } else {
                // Main user does not follow a given user
                if (mainUser.getFollowingReqList().contains(UUIDs.get(position))) {
                    // If main user has requested to follow a given user
                    holder.setButtonText(REQUESTED);
                } else {
                    // Main user has not requested to follow a given user
                    if (mainUser.getFollowerList().contains(UUIDs.get(position))) {
                        // A given user follows main user
                        holder.setButtonText(FOLLOW_BACK);
                    } else {
                        // A given user does not follow main user
                        holder.setButtonText(FOLLOW);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return usernames.size();
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
