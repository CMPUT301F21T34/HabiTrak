package com.cmput301f21t34.habittrak.recycler;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.fragments.SocialFragment;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

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
    public static final String NONE = "";
    public static final String REQUESTED = "Requested";
    public static final String UNBLOCK = "Unblock";
    public static final String UNFOLLOW = "Unfollow";

    private final SocialFragment socialRef;
    private final User mainUser;
    private final ArrayList<String> UUIDsCopy;
    private final ArrayList<String> usernamesCopy;
    private final ArrayList<String> biosCopy;
    private final String defaultButtonText;
    private ArrayList<String> UUIDs;
    private ArrayList<String> usernames;
    private ArrayList<String> bios;
    private SocialListener socialListener;

    public interface SocialListener {
        void onItemClick(View view, int position);
    }

    public SocialAdapter(
            SocialFragment socialRef, User mainUser, ArrayList<String> UUIDs,
            ArrayList<String> usernames, ArrayList<String> bios, String defaultButtonText) {
        this.socialRef = socialRef;
        this.mainUser = mainUser;
        this.UUIDs = UUIDs;
        this.UUIDsCopy = UUIDs;
        this.usernames = usernames;
        this.usernamesCopy = usernames;
        this.bios = bios;
        this.biosCopy = bios;
        this.defaultButtonText = defaultButtonText;
    }

    /**
     * Sets the listener that sends main user to the public profile of a user it follows
     *
     * @param socialListener The described listener
     */
    public void setSocialListener(final SocialListener socialListener){
        this.socialListener = socialListener;
    }

    /**
     * Adds the specified user entry to the list if its UUID is not already present
     *
     * @param UUID     String, the UUID of the user
     * @param username String, the user's username
     * @param bio      String, the user's bio
     */
    public void addUserEntry(String UUID, String username, String bio) {
        if (!UUIDs.contains(UUID)) {
            UUIDs.add(UUID);
            usernames.add(username);
            bios.add(bio);
            notifyItemInserted(UUIDs.size() - 1);
        }
    }

    /**
     * Removes the entry specified by UUID
     *
     * @param UUID String, the UUID of the user whose entry to remove
     */
    public void removeUserEntry(String UUID) {
        int position = UUIDs.indexOf(UUID);
        if (position != -1) {
            UUIDs.remove(position);
            usernames.remove(position);
            bios.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public SocialAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Just makes a ViewHolder from the inflated view
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.social_page_listview_content, parent, false));
    }

    // Used to set up each item in the adapter
    @Override
    public void onBindViewHolder(@NonNull SocialAdapter.ViewHolder holder, int position) {
        // Set up the data for the given entry
        holder.UUID = UUIDs.get(position);
        holder.username = usernames.get(position);
        holder.bio = bios.get(position);
        holder.usernameTextView.setText(holder.username);
        holder.bioTextView.setText(holder.bio);

        // Set appropriate button text based on tab and relationships of main user with each user
        if (defaultButtonText.equals(ACCEPT) || defaultButtonText.equals(UNFOLLOW)) {
            // Requests and Following tabs
            holder.mainButton.setText(defaultButtonText);
        } else { // Followers and Search tabs
            if (mainUser.getFollowingList().contains(UUIDs.get(position))) {
                // If main user follows a given user
                holder.mainButton.setText(UNFOLLOW);
            } else { // Main user does not follow a given user
                if (mainUser.getFollowingReqList().contains(UUIDs.get(position))) {
                    // If main user has requested to follow a given user
                    holder.mainButton.setText(REQUESTED);
                } else { // Main user has not requested to follow a given user
                    if (mainUser.getBlockList().contains(UUIDs.get(position))) {
                        // If main user has blocked a given user
                        holder.mainButton.setText(UNBLOCK);
                    } else { // Main user does not block a given user
                        if (mainUser.getFollowerList().contains(UUIDs.get(position))) {
                            // A given user follows main user
                            holder.mainButton.setText(FOLLOW_BACK);
                        } else { // A given user does not follow main user
                            holder.mainButton.setText(FOLLOW);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                // If no input in searchView put the original list back
                if (charString.isEmpty()) {
                    UUIDs = UUIDsCopy;
                    usernames = usernamesCopy;
                    bios = biosCopy;
                } else {
                    // Filter based if username contains the inputted characters
                    ArrayList<String> filteredUUIDs = new ArrayList<>();
                    ArrayList<String> filteredProfiles = new ArrayList<>();
                    ArrayList<String> filteredBios = new ArrayList<>();
                    for (int i = 0; i < usernamesCopy.size(); ++i) {
                        if (usernamesCopy.get(i).toLowerCase().contains(charString)) {
                            filteredUUIDs.add(UUIDsCopy.get(i));
                            filteredProfiles.add(usernamesCopy.get(i));
                            filteredBios.add(biosCopy.get(i));
                        }
                    }
                    UUIDs = filteredUUIDs;
                    usernames = filteredProfiles;
                    bios = filteredBios;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = usernames;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                usernames = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    /**
     * View Holder of the SocialAdapter
     *
     * @author Pranav
     * @author Kaaden
     * @see RecyclerView.ViewHolder
     */
    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final DatabaseManager dm = new DatabaseManager(); // For updating the database
        private final TextView usernameTextView; // Displays this entry's username
        private final TextView bioTextView; // Displays this entry's bio
        private final MaterialButton mainButton; // This big button
        private String UUID; // This entry's user's UUID
        private String username; // This entry's user's username
        private String bio; // This entry's user's biography

        private ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            usernameTextView = view.findViewById(R.id.username_social_page);
            bioTextView = view.findViewById(R.id.social_user_bio);
            mainButton = view.findViewById(R.id.social_main_button);
            ImageButton menuButton = view.findViewById(R.id.social_menu); // The 3 dots
            mainButton.setOnClickListener(this);
            menuButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.social_main_button) { // The big button
                mainButtonOnClick();
            } else if (view.getId() == R.id.social_menu) { // The three dots
                menuButtonOnClick(view);
            } else {
                socialListener.onItemClick(view, getAdapterPosition());
            }
        }

        /**
         * Executes the functionality of the main button on an entry
         */
        private void mainButtonOnClick() {
            switch (mainButton.getText().toString()) {
                case ACCEPT:
                    // Update locally
                    mainUser.addFollower(UUID);
                    mainUser.removeFollowerReq(UUID);
                    removeUserEntry(UUID); // Remove this entry from Requests (this) tab
                    // Update in database
                    dm.updateFollow(mainUser.getEmail(), UUID, DatabaseManager.ADD);
                    dm.updateFollowRequest(mainUser.getEmail(), UUID, DatabaseManager.REMOVE);
                    // Add accepted user to Followers tab
                    socialRef.addUserEntry(SocialFragment.FOLLOWERS, UUID, username, bio);
                    break;
                case FOLLOW:
                case FOLLOW_BACK:
                    // Update locally
                    mainUser.addFollowingReq(UUID);
                    // Update in database
                    dm.updateFollowRequest(UUID, mainUser.getEmail(), DatabaseManager.ADD);
                    // Update display
                    mainButton.setText(REQUESTED);
                    break;
                case REQUESTED:
                    // Update locally
                    mainUser.removeFollowingReq(UUID);
                    // Update in database
                    dm.updateFollowRequest(UUID, mainUser.getEmail(), DatabaseManager.REMOVE);
                    // Update display
                    mainButton.setText(
                            mainUser.getFollowerList().contains(UUID) ? FOLLOW_BACK : FOLLOW);
                    break;
                case UNFOLLOW:
                    // Update locally
                    mainUser.removeFollowing(UUID);
                    // Remove entry from Following tab
                    socialRef.removeUserEntry(SocialFragment.FOLLOWING,  UUID);
                    // Update in database
                    dm.updateFollow(UUID, mainUser.getEmail(), DatabaseManager.REMOVE);
                    break;
                case UNBLOCK:
                    // Update locally
                    mainUser.removeBlock(UUID);
                    // Update in database
                    dm.updateBlock(UUID, mainUser.getEmail(), DatabaseManager.REMOVE);
                    // Update display
                    mainButton.setText(FOLLOW);
                    break;
            }
        }

        /**
         * Executes the functionality of the menu button (3 dots) of an entry
         */
        private void menuButtonOnClick(View view) {
            PopupMenu menu = new PopupMenu(view.getContext(), view);
            menu.getMenuInflater().inflate(R.menu.social_popup_menu, menu.getMenu());
            menu.getMenu().add("Block");
            menu.getMenu().add("Remove");
            menu.show();

            menu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getTitle().toString()) {
                    case "Block":
                        Log.d("MenuItem", "Block Clicked");

                        // Store block relationship
                        mainUser.addBlock(UUID);
                        dm.updateBlock(UUID, mainUser.getEmail(), DatabaseManager.ADD);

                        // Remove following or following request (they are mutually exclusive)
                        if (mainUser.removeFollowing(UUID)) {
                            // Only bother database if relationship exists
                            dm.updateFollow(UUID, mainUser.getEmail(), DatabaseManager.REMOVE);
                            // Also update relevant possible other tabs
                            socialRef.removeUserEntry(SocialFragment.FOLLOWING, UUID);
                        } else if (mainUser.removeFollowingReq(UUID)) {
                            // Only bother database if relationship exists
                            dm.updateFollowRequest(
                                    UUID, mainUser.getEmail(), DatabaseManager.REMOVE);
                        }

                        // Remove follower or follower request (they are mutually exclusive)
                        if (mainUser.removeFollower(UUID)) {
                            // Only bother database if relationship exists
                            dm.updateFollow(mainUser.getEmail(), UUID, DatabaseManager.REMOVE);
                            // Also update relevant possible other tabs
                            socialRef.removeUserEntry(SocialFragment.FOLLOWERS, UUID);
                        } else if (mainUser.removeFollowerReq(UUID)) {
                            // Only bother database if relationship exists
                            dm.updateFollowRequest(
                                    mainUser.getEmail(), UUID, DatabaseManager.REMOVE);
                            // Also update relevant possible other tabs
                            socialRef.removeUserEntry(SocialFragment.REQUESTS, UUID);
                        }

                        // Remove from relevant tabs
                        socialRef.removeUserEntry(SocialFragment.FOLLOWERS, UUID);
                        socialRef.removeUserEntry(SocialFragment.FOLLOWING, UUID);
                        socialRef.removeUserEntry(SocialFragment.REQUESTS, UUID);

                        // Update display
                        mainButton.setText(UNBLOCK);
                        break;
                    case "Remove":
                        Log.d("MenuItem", "Remove Clicked");
                        // Remove follower or follower request, remove from this list now

                        // Remove follower or follower request (they are mutually exclusive)
                        if (mainUser.removeFollower(UUID)) {
                            // Only bother database if relationship exists
                            dm.updateFollow(mainUser.getEmail(), UUID, DatabaseManager.REMOVE);
                            // Also update relevant possible other tabs
                            socialRef.removeUserEntry(SocialFragment.FOLLOWERS, UUID);
                        } else if (mainUser.removeFollowerReq(UUID)) {
                            // Only bother database if relationship exists
                            dm.updateFollowRequest(
                                    mainUser.getEmail(), UUID, DatabaseManager.REMOVE);
                            // Also update relevant possible other tabs
                            socialRef.removeUserEntry(SocialFragment.REQUESTS, UUID);
                        }

                        // Remove from this list now
                        removeUserEntry(UUID);
                        break;
                }
                return true;
            });
        } // menuButtonOnClick
    } // ViewHolder
} // SocialAdapter
