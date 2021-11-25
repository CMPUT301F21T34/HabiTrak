package com.cmput301f21t34.habittrak.auth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.MainActivity;
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
import java.util.concurrent.TimeUnit;

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
    private DatabaseManager db;


    public Auth(Activity activity, DatabaseManager db){

        this.context = activity;
        this.db = db;
        mAuth = FirebaseAuth.getInstance();


    }


    public FirebaseAuth getAuth() {
        return mAuth;
    }










    /**
     * Attempts to change current logged in user to a new password
     *
     * @author Dakota
     * @param authUser Firebase user to change password
     * @param newPassword String new password to change to
     */
    public void changePassword(FirebaseUser authUser, String newPassword){



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
                                reAuthEmail(authUser); // else exit
                                }
                            }
                        });



        }

    }

    /**
     * changes users email
     *
     * TODO Make work
     * @author Dakota
     * @param authUser Firebase User to change
     * @param newEmail String new email
     */
    public void changeEmail(FirebaseUser authUser, String newEmail){

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
                        reAuthEmail(authUser);
                    }
                }

            });

        }

    }





    /**
     * Re-Auths user with email auth provider
     * This is used for secure events when the user hasn't logged in in awhile
     *
     * @author Dakota
     * @return true on success, false on failure
     */
    private boolean reAuthEmail(FirebaseUser authUser){

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
    public AlertDialog.Builder alertNotVerified(FirebaseUser authUser){

        Log.d("NotVer", "alert called");

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
                authUser.sendEmailVerification();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                // Don't send new email

            }
        });


        return builder;

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
