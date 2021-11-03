package com.cmput301f21t34.habittrak.user;

import android.os.Parcel;
import android.os.Parcelable;

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
public class Database_Pointer implements Parcelable {

    private final String email; // id can only be assigned once

    public Database_Pointer(String email){
       this.email = email;
    }
    public Database_Pointer(User user){
        this.email = user.getEmail();
    }


    protected Database_Pointer(Parcel parcel) {
        email = parcel.readString();
    }



    /**
     * getEmail
     *
     * gets the email of the follow object
     *
     * @author Dakota
     * @return String id of a particular user
     */
    public String getEmail(){
        return this.email;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(email);
    }

    public static final Creator<Database_Pointer> CREATOR = new Creator<Database_Pointer>() {
        @Override
        public Database_Pointer createFromParcel(Parcel in) {
            return new Database_Pointer(in);
        }

        @Override
        public Database_Pointer[] newArray(int size) {
            return new Database_Pointer[size];
        }
    };


}
