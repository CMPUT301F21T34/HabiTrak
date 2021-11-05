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
public class DatabasePointer implements Parcelable {

    private final String email; // id can only be assigned once

    public DatabasePointer(String email){
        this.email = email;
    }
    public DatabasePointer(User user){
        this.email = user.getEmail();
    }



    protected DatabasePointer(Parcel parcel) {
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

    public static final Creator<DatabasePointer> CREATOR = new Creator<DatabasePointer>() {
        @Override
        public DatabasePointer createFromParcel(Parcel in) {
            return new DatabasePointer(in);
        }

        @Override
        public DatabasePointer[] newArray(int size) {
            return new DatabasePointer[size];
        }
    };

    /**
     * custom hashCode() and equals() for Database_Pointer
     *
     * Reference: https://stackoverflow.com/a/17801750
     * @return
     */
    @Override
    public int hashCode() {
        return email.hashCode();
    }

    // equals Implementation
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DatabasePointer other = (DatabasePointer) obj;
        if (email != other.getEmail())
            return false;
        return true;

    }

    public boolean Equals(DatabasePointer dp){
        return this.getEmail().equals(dp.getEmail());
    }
}
