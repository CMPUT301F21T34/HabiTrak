package com.cmput301f21t34.habittrak.auth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class Auth {

    private FirebaseAuth mAuth;
    private Context context;
    FirebaseUser authUser;

    public Auth(Context context){

        this.context = context;
        mAuth = FirebaseAuth.getInstance();

    }


    /**
     * Gets the current logged in user's email. If no user is logged
     * in, returns null.
     *
     * @author Dakota
     * @return String email of logged in user, null if no user is logged in
     */
    public String getLoggedIn(){

        authUser = null; // Makes sure it's clear
        authUser = mAuth.getCurrentUser();

        if (authUser != null){
            return authUser.getEmail();
        }
        return null;

    }

    /**
     * Try's to sign up a particular email and password combination
     * returning the String of the email signed up with on success and
     * null on failure
     *
     * @author Dakota
     * @param email String email to sign up with
     * @param password String password to sign up with
     * @return String email of account created or null on failure
     */
    public String signUp(String email, String password){

        authUser = null;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if ( task.isSuccessful() ) {
                            // Sign Up was successful
                            authUser = mAuth.getCurrentUser();

                        } else {

                            // Sign Up failed
                            authUser = null;
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        if (authUser != null){
            return authUser.getEmail();
        }
        return null;
    }

    public String logIn (String email, String password){

        authUser = null;

        // Make's sure a user isn't already logged in
        String userEmail = getLoggedIn();
        if (userEmail != null){
            return userEmail;
        }

        // Authenticates
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            // User provided valid credentials

                            authUser = mAuth.getCurrentUser();

                            // Check if they are verified
                            boolean verified = authUser.isEmailVerified();

                            if (!verified) {
                                // If not verified refuse them to log in
                                authUser = null;

                                // alert and give option to resend email
                                boolean sendEmail = alertNotVerified();

                                if (sendEmail) { authUser.sendEmailVerification(); }

                            }

                        } else {
                            // User provided invalid credentials

                            authUser = null;
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                        
                    }
                });

        if (authUser != null){
            return authUser.getEmail();
        }
        return null;

    }

    /**
     * Alerts the user that their account email is not verified and asks
     * if they would like a new email to be sent
     *
     * @author Dakota
     * @return boolean true if they would like a new email, false else wise
     */
    private boolean alertNotVerified(){

        // final boolean[] to be set by inner class (builder)
        final boolean[] sendEmail = new boolean[1];

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setMessage("Would you like to resend a verification email?")
                .setTitle("Email not Verified");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                // Send new email
                sendEmail[0] = true;
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Don't send new email
                sendEmail[0] = false;
            }
        });

        return sendEmail[0];

    }


}
