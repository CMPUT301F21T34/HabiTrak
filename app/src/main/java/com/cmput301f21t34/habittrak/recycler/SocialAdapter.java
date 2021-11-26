package com.cmput301f21t34.habittrak.recycler;

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
    public static final String REQUESTED = "Requested";
    public static final String UNFOLLOW = "Unfollow";
    private final User mainUser;
    private final ArrayList<String> usernamesCopy;
    private final ArrayList<String> biosCopy;
    private final ClickListener listener;
    private final String defaultButtonText;
    private final ArrayList<String> UUIDsCopy;
    private ArrayList<String> UUIDs;
    private ArrayList<String> usernames;   // UUIDS (emails as of 10/11)
    private ArrayList<String> bios;

    public SocialAdapter(User mainUser, ArrayList<String> UUIDs, ArrayList<String> usernames,
                         ClickListener listener, ArrayList<String> bios, String defaultButtonText) {
        this.mainUser = mainUser;
        this.UUIDs = UUIDs;
        this.usernames = usernames;
        this.bios = bios;
        this.UUIDsCopy = UUIDs;
        this.usernamesCopy = usernames;
        this.biosCopy = bios;
        this.listener = listener;
        this.defaultButtonText = defaultButtonText;
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
        UUIDs.remove(position);
        usernames.remove(position);
        bios.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public SocialAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.social_page_listview_content, parent, false);
        return new ViewHolder(mainUser, view);
    }

    // Used to set up each item in the adapter
    @Override
    public void onBindViewHolder(@NonNull SocialAdapter.ViewHolder holder, int position) {
        holder.setUUID(UUIDs.get(position));
        holder.getUsername().setText(usernames.get(position));
        holder.getUserBio().setText(bios.get(position));

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
                    bios = biosCopy;
                } else {
                    // filter username and bios bases if username contains the characters
                    ArrayList<String> filteredUUIDs = new ArrayList<>();
                    ArrayList<String> filteredProfiles = new ArrayList<>();
                    ArrayList<String> filteredBios = new ArrayList<>();
                    for (int i = 0; i < usernamesCopy.size(); i++) {
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

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                usernames = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final User mainUser;
        private final DatabaseManager dm = new DatabaseManager();
        private final TextView username;
        private final MaterialButton mainButton;
        private final ImageButton menuButton;
        private final TextView userBio;
        private String UUID;
        private ClickListener listenerRef;

        public ViewHolder(User mainUser, View view) {
            super(view);
            this.mainUser = mainUser;
            username = view.findViewById(R.id.username_social_page);
            mainButton = view.findViewById(R.id.social_main_button);
            menuButton = view.findViewById(R.id.social_menu);
            userBio = view.findViewById(R.id.social_user_bio);
            menuButton.setOnClickListener(this);
            mainButton.setOnClickListener(this);
        }

        public void setUUID(String UUID) {
            this.UUID = UUID;
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
            int position = getAdapterPosition();
            //if (listenerRef != null) {
            //listenerRef.mainButtonOnClick(view, getAdapterPosition());

            if (view.getId() == R.id.social_main_button) { // The big button
                MaterialButton button = view.findViewById(R.id.social_main_button);
                String buttonType = button.getText().toString();
                String mainUUID = mainUser.getEmail();

                switch (buttonType) {
                    case ACCEPT:
                        // Update locally
                        mainUser.addFollower(UUID);
                        mainUser.removeFollowerReq(UUID);
//                      UUIDs.remove(UUID);
//                      usernames.remove(position);
//                      bios.remove(position);
                        removeUserEntry(UUID);
                        // Update in database
//                      dm.updateFollow(mainUUID, UUID, false);
//                      dm.updateFollowRequest(mainUUID, UUID, true);

                        // Update display(s)
                        notifyItemRemoved(position);
                        break;
                    case FOLLOW:
                    case FOLLOW_BACK:
                        // Update locally
                        mainUser.addFollowingReq(UUID);

                        // Update in database
                        //     dm.updateFollowRequest(UUID, mainUser.getEmail(), false);

                        // Update display(s)
                        button.setText(REQUESTED);

                        break;
                    case REQUESTED:
                        // Update locally
                        mainUser.removeFollowingReq(UUID);

                        // Update in database
//                      dm.updateFollowRequest(UUID, mainUUID, true);

                        // Update display(s)
                        button.setText(mainUser.getFollowerList().contains(UUID) ? FOLLOW_BACK : FOLLOW);
                        break;
                    case UNFOLLOW:
                        // Update locally
                        mainUser.removeFollowing(UUID);
                        usernames.remove(position);
                        bios.remove(position);

                        // Update in database
//                    dm.updateFollow(UUID, mainUUID, true);

                        // Update display(s)
                        notifyItemRemoved(position);
                        break;
                }


            } else if (view.getId() == R.id.social_menu) { // The three dots
                //listenerRef.menuButtonOnClick(view, getAdapterPosition());
                PopupMenu menu = new PopupMenu(view.getContext(), view);
                menu.getMenuInflater().inflate(R.menu.social_popup_menu, menu.getMenu());
                menu.getMenu().add("Block");
                menu.getMenu().add("Remove");
                menu.show();

                menu.setOnMenuItemClickListener(menuItem -> {
                    if (menuItem.getTitle().equals("Block")) {
                        Log.d("MenuItem", "Block Clicked");


                    } else if (menuItem.getTitle().equals("Remove")) {
                        Log.d("MenuItem", "Remove Clicked");

                    }
                    return true;
                });
            }
            //}
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
