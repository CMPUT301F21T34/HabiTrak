package com.cmput301f21t34.habittrak.user;

/**
 * Database_Pointer
 *
 * @author Dakota
 *
 * A pointer to a particular user in the database
 *
 * @version 2.0
 * @since 2021-10-27
 * @see User
 */
public class Database_Pointer {

    private final String email; // id can only be assigned once

    public Database_Pointer(String email){
       this.email = email;
    }
    public Database_Pointer(User user){
        this.email = user.getEmail();
    }


    /**
     * getID
     *
     * gets the id of the follow object
     *
     * @author Dakota
     * @return String id of a particular user
     */
    public String getEmail(){
        return this.email;
    }

    /**
     * isApproved
     *
     * gets is a user is approved or not
     *
     * @author Dakota
     * @return boolean true if user is approved, false if not
     */


}
