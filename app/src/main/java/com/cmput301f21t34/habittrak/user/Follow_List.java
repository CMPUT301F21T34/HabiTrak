package com.cmput301f21t34.habittrak.user;

import java.util.ArrayList;

public class Follow_List {

    private Database_Pointer mainUser;
    private ArrayList<Database_Pointer> followerList = new ArrayList<>();
    private ArrayList<Database_Pointer> followingList = new ArrayList<>();

    // Gets which user this list belongs to
    Follow_List(String mainUserID){
        this.mainUser = new Database_Pointer(mainUserID);
    }


    /**
     * addFollowing
     *
     * follow/request to follow a particular user
     *
     * @author Dakota
     * @param following Database_Pointer Object user that you are trying to follow
     */
    public void addFollowing(Database_Pointer following){

        this.followingList.add(following);

        // TODO: add mainUser to following's followerList
        // TODO: add following to mainUser's followingList
        // Ex.
        // database(following).addFollower(this.mainUser)
        // database(this.mainUser).addFollowing(following)

    }

    /**
     * removeFollowing
     *
     * unfollow a particular user
     *
     * @author Dakota
     * @param following Database_Pointer Object user that you are trying to unfollow
     */
    public boolean removeFollowing(Database_Pointer following){

        boolean removed;

        removed = this.followingList.removeIf(databasePointer -> {

            // Compares the getID() attribute of Database_Pointer object for removal
            if (databasePointer.getEmail() == following.getEmail()){

                // TODO: remove mainUser from following's followerList
                // TODO: remove following from mainUser's followingList
                // Ex.
                // database(following).removeFollower(this.mainUser)
                // database(this.mainUser).removeFollowing(following)

                return true; // remove
            } else {
                return false; // don't remove
            }
        });

        return removed; // If they were removed or not
    }

    /**
     * removeFollower
     *
     * Force a particular follower to unfollow you.
     * Usage example is blocking a user
     *
     * @author Dakota
     * @param follower Database_Pointer Object of follower you want to force to unfollow
     * @return boolean true if removed, false if the user couldn't be found
     */
    public boolean removeFollower(Database_Pointer follower){

        boolean removed;

        removed = this.followerList.removeIf(databasePointer -> {
            if (databasePointer.getEmail() == follower.getEmail()){

                // TODO: remove mainUser from follower's followingList
                // TODO: remove follower from mainUser's followerList
                // Ex.
                // database(follower).removeFollowing(this.mainUser)
                // database(this.mainUser).removeFollower(follower)

                return true; // remove
            } else {
                return false; // don't remove
            }
        });

        return removed;

    }

    /**
     * refresh
     *
     * refreshes a users follow lists with database
     * @author Dakota
     */
    public void refresh(){

        // TODO: refresh this.followingList and this.followerList with database

    }


}
