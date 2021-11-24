package com.cmput301f21t34.habittrak.social;

import androidx.fragment.app.Fragment;

import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.user.User;

import java.util.ArrayList;

public class SocialTabFragment extends Fragment {
    private DatabaseManager dm = new DatabaseManager();
    // Data
    private User mainUser;
    private ArrayList<String> UUIDs;
    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<String> bios = new ArrayList<>();

    public SocialTabFragment(User mainUser, ArrayList<String> UUIDs) {
        this.mainUser = mainUser;
        this.UUIDs = UUIDs;
    }

    /**
     * Populates usernames and bios
     *
     * @author Kaaden
     */
    public void populateUsernamesAndBios() {
        UUIDs.forEach(UUID -> {
            usernames.add(dm.getUserName(UUID));
            bios.add(dm.getUserBio(UUID));
        });
    }
}
