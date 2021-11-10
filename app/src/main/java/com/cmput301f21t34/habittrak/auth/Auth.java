package com.cmput301f21t34.habittrak.auth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

/**
 * Authentication class for interacting with Firebase Auth
 *
 * @author Dakota
 * @version 1.0
 * @see FirebaseAuth
 * @see FirebaseUser
 * @see 'https://firebase.google.com/docs/auth'
 * @since 2021-11-10
 */
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
                            sendSignInEmail(email);

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

    /**
     * Attempts to log in a user by a given email and password
     *
     * @author Dakota
     * @param email String of email to log into
     * @param password String of password corresponding to email
     * @return String of email on success, null on failure
     */
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

                                // sends the email
                                if (sendEmail) { sendSignInEmail(authUser.getEmail()); }

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

    public void resetPassword(String email){

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Email Sent", Toast.LENGTH_SHORT);
                        }
                    }
                });

    }

    /**
     * Attempts to change current logged in user to a new password
     *
     * @author Dakota
     * @param newPassword String new password to change to
     */
    public void changePassword(String newPassword){

        authUser = mAuth.getCurrentUser();

        if (authUser != null) {


            // Updates users password
            authUser.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Password changed
                                Toast.makeText(context, "Password updated", Toast.LENGTH_SHORT);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Catches exceptions
                            if (exception instanceof FirebaseAuthRecentLoginRequiredException) {
                                // Re-auth User
                                if(reAuthEmail()) {
                                    // If success, then change password
                                    changePassword(newPassword);
                                    // else exit
                                }
                            }
                        }

                    });

        }

    }

    public void changeEmail(String newEmail){

        authUser = mAuth.getCurrentUser();

        if (authUser != null){

            // Makes the user email verify before email is changed
            authUser.verifyBeforeUpdateEmail(newEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Email to change to  new email sent
                                Toast.makeText(context, "Email updated", Toast.LENGTH_SHORT);

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Catches exceptions
                    if (exception instanceof FirebaseAuthRecentLoginRequiredException) {
                        // Re-auth User
                        if(reAuthEmail()) {
                            // If success, then change password
                            changeEmail(newEmail);
                            // else exit
                        }
                    }
                }

            });

        }

    }


    private void sendSignInEmail(String email){

        // see: https://firebase.google.com/docs/auth/android/email-link-auth

        authUser = mAuth.getCurrentUser();

        String url = "http://habittrak.firebaseapp.com/finishSignUp?cartId=1000";
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl(url)
                .setHandleCodeInApp(true)
                .setAndroidPackageName("com.cmput301f21t34.habittrak", false, "12")
                .build();

        mAuth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            // success
                        }
                    }
                });

    }

    /**
     * Re-Auths user with email auth provider
     * This is used for secure events when the user hasn't logged in in awhile
     *
     * @author Dakota
     * @return true on success, false on failure
     */
    private boolean reAuthEmail(){

        authUser = mAuth.getCurrentUser();

        // must use final boolean[] as variable is set by inner class
        final boolean[] reAuthed = new boolean[1];

        String[] credentials = getCredentials();

        if (credentials[0] == null){
            // If the user canceled the reAuth Alert
            return false;
        }

        // Sets up AuthCredential for reAuth from received credentials
        AuthCredential authCredentials = EmailAuthProvider
                .getCredential(credentials[0], credentials[1]);

        // Re Auths user
        authUser.reauthenticate(authCredentials)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            // If successful set reAuthed to true
                            reAuthed[0] = true;
                        } else {
                            Toast.makeText(context, "Authentication Failed", Toast.LENGTH_SHORT);
                            // else reAuthed false
                            reAuthed[0] = false;
                        }
                    }
                });

        // returns result
        return reAuthed[0];

    }

    /**
     * signs a user out
     * @author Dakota
     */
    public void signOut(){
        mAuth.signOut();
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

        // Builds alert

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

    /**
     * quickly gets user credentials with an alert dialogue.
     * This is used for reAuthing if a user has been logged in for
     * a long time.
     *
     * @author Dakota
     * @return String[2] where [0] is the email, and [1] is the password.
     *      both will be null if user cancels.
     */
    private String[] getCredentials(){

        final String[] credentials = new String[2];

        // views for user to edit
        final EditText email = new EditText(context);
        final EditText password = new EditText(context);

        // Builds Alert Dialogue

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setTitle("Re-Authentication Required");

        // sets the Edit Texts
        builder
                .setView(email)
                .setView(password);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // sets the text fields to array
                credentials[0] = email.toString();
                credentials[1] = password.toString();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // sets to null on cancel
                credentials[0] = null;
                credentials[1] = null;
            }
        });

        return credentials;

    }


}
