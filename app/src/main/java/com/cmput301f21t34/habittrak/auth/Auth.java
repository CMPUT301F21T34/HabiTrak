package com.cmput301f21t34.habittrak.auth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Authentication class for interacting with Firebase Auth
 *
 * @author Dakota
 * @version 2.0
 * @see FirebaseAuth
 * @see FirebaseUser
 * @see 'https://firebase.google.com/docs/auth'
 * @since 2021-11-10
 */
public class Auth {

    private final FirebaseAuth mAuth;
    private final Context context;


    public Auth(Activity activity) {
        this.context = activity;
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }


    /**
     * signs a user out
     *
     * @author Dakota
     */
    public void signOut() {
        mAuth.signOut();
    }

    /**
     * Alerts the user that their account email is not verified and asks
     * if they would like a new email to be sent
     *
     * @return boolean true if they would like a new email, false else wise
     * @author Dakota
     */
    public AlertDialog.Builder alertNotVerified(FirebaseUser authUser) {

        // Builds alert
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setMessage("Would you like to resend a verification email?")
                .setTitle("Email not Verified");

        builder.setPositiveButton("Yes", (dialogInterface, id) -> {
            // Send new email
            authUser.sendEmailVerification();
        });
        builder.setNegativeButton("No", (dialogInterface, id) -> {
            // Don't send new email
        });
        return builder;
    }

}
