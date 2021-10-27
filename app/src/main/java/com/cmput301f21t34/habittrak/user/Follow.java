package com.cmput301f21t34.habittrak.user;

/**
 * Follow
 *
 * @author Dakota
 *
 * A pointer to a particular user in the database
 *
 * @version 1.0
 * @since 2021-10-27
 * @see User
 */
public class Follow {

    private final String id; // id can only be assigned once
    private boolean isApproved = false;

    Follow (String id){
       this.id = id;
    }

    Follow (String id, boolean approved){
        this.id = id;
        this.isApproved = approved;
    }

    /**
     * getID
     *
     * gets the id of the follow object
     *
     * @author Dakota
     * @return String id of a particular user
     */
    public String getID(){
        return this.id;
    }

    /**
     * isApproved
     *
     * gets is a user is approved or not
     *
     * @author Dakota
     * @return boolean true if user is approved, false if not
     */
    public boolean isApproved(){
        return this.isApproved;
    }

    /**
     * setApproved
     *
     * sets if a user is approved or not
     *
     * @author Dakota
     * @param isApproved true if the user is approved, false if not
     */
    public void setApproved(boolean isApproved){
        this.isApproved = isApproved;
    }


}
