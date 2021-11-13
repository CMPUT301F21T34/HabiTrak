package com.cmput301f21t34.habittrak.auth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.cmput301f21t34.habittrak.DatabaseManager;
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
    private String authEmail;
    private boolean working = false; // If we are waiting on Firebase
    FirebaseUser authUser;

    public Auth(FirebaseUser authUser, Context context){

        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        this.authUser = authUser;

    }

    public FirebaseUser getAuthUser() {
        return authUser;
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    /**
     * Gets the current logged in user's email. If no user is logged
     * in, returns null.
     *
     * @author Dakota
     * @return String email of logged in user, null if no user is logged in
     */
    public String getLoggedIn(){


        if (authUser != null){
            this.authEmail = authUser.getEmail();
        } else {
            this.authEmail = null;
        }
        return authEmail;

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


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if ( task.isSuccessful() ) {

                            // Sign Up was successful
                            sendSignInEmail(email);

                        } else {

                            // Sign Up failed
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("SignUp", "Exception thrown: " + e.toString());
            }
        });

        return getLoggedIn();


    }

    /**
     * Attempts to log in a user by a given email and password
     *
     * @author Dakota
     * @param email String of email to log into
     * @param password String of password corresponding to email
     * @return String of email on success, null on failure
     */
    public FirebaseUser logInCall (String email, String password){

        Log.d("LogIn", "auth called");


        this.working = true;
        // Authenticates

        // Note this does not get added to the top of the stack
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            authUser = mAuth.getCurrentUser();
                            Log.d("LogIn", "Success: " + mAuth.getCurrentUser().getEmail());


                        } else {

                            authUser = null;

                            Log.d("LogIn", "Failure");

                        }
                    }
                });

        Log.d("LogIn", "Exiting logInCall: ");

        return authUser;




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

        // Make's sure a user isn't already logged in
        signOut();

        // The reason we call it this way, is because of stack order.
        // These all must be called in their own methods where returnAuthUser
        // Gets the success of logInCall AFTER logInCall has been called
        logInCall(email, password);


        // Waits until we are no longer working to auth

        return getLoggedIn();
    }

    public boolean isVerified(){
        if (authUser == null) { return false; }
        return authUser.isEmailVerified();
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

        // Should be fine
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

    public String getEmail(){


        if ( authUser != null){
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
    public boolean alertNotVerified(FirebaseUser authUser){

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

        builder.show();

        return true;

    }

    public boolean alertDelete(FirebaseUser authUser, DatabaseManager db) {

        // Check if our authUser is null
        if(authUser == null){
            return false;
        }

        // else continue

        // Build an alert
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert
                .setMessage("This Cannot be undone!")
                .setTitle("Delete Account?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {

                // Confirm once more
                AlertDialog.Builder confirmation = new AlertDialog.Builder(context);

                confirmation
                        .setMessage("Are you sure?")
                        .setTitle("Confirm");

                confirmation.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Delete

                        String email = authUser.getEmail();
                        authUser.delete();
                        db.deleteUser(email);

                    }
                });

                confirmation.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Cancel
                    }
                });

                confirmation.show();

            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                // Cancel

            }
        });

        alert.show();

        // if delete was successful authUser should now be null
        if (authUser == null){
            return true;
        } else {
            return false;
        }

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
