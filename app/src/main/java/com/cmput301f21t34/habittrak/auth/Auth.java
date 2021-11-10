package com.cmput301f21t34.habittrak.auth;

import android.app.Activity;
import android.content.Context;
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



}
