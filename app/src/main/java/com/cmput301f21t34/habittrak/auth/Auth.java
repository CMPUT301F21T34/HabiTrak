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
 * @version 2.0
 * @see FirebaseAuth
 * @see FirebaseUser
 * @see 'https://firebase.google.com/docs/auth'
 * @since 2021-11-10
 */
public class Auth {

    private FirebaseAuth mAuth;
    private Context context;


    public Auth(Activity activity){
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
    public AlertDialog.Builder alertNotVerified(FirebaseUser authUser) {

        Log.d("NotVer", "alert called");


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

}
