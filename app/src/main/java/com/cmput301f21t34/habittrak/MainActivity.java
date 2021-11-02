package com.cmput301f21t34.habittrak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;


import com.cmput301f21t34.habittrak.user.Database_Pointer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import com.cmput301f21t34.habittrak.user.User;

import java.util.ArrayList;

/**
 * MainActivity
 *
 * Starting point of the app
 * TODO: figure out how to save the login state of the user
 */
public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        User mainUser = null;

        if (savedInstanceState != null) {
            // get users credentials
            // validate credentials with database
            // populate mainUser
            // move to main menu
        } else {
            // get user to login
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.login_fragment_container, new LoginFragment(mainUser))
                    .commit();
        }*/
        DatabaseManager db = new DatabaseManager();
        User user = db.getUser("pog@gmail.com");
        //db.createNewUser("okayge@gmail.com","okayge","abc1234&","i am okayge");
        String TAG ="Checkchek";
        db.updateFollower(user,new Database_Pointer("okayge@gmail.com"),false);
        db.updateFollowerReq(user,new Database_Pointer("okayge@gmail.com"),false);
        db.updateBlock(user,new Database_Pointer("okayge@gmail.com"),false);
        Log.d(TAG,user.getUsername());
        Log.d(TAG,user.getFollowingList().get(0).getEmail());
        Log.d(TAG,db.getFollowerList(user.getEmail()).get(0).getEmail());
        Log.d(TAG,db.getFollowingList(user.getEmail()).get(0).getEmail());
        Log.d(TAG,db.getBlockList(user.getEmail()).get(0).getEmail());
        Log.d(TAG,db.getBlockedByList("okayge@gmail.com").get(0).getEmail());
        Log.d(TAG,db.getFollowReqList(user.getEmail()).get(0).getEmail());

        Log.d(TAG,db.getFollowRequestedList("okayge@gmail.com").get(0).getEmail());

        db.updateFollower(user,new Database_Pointer("okayge@gmail.com"),true);
        db.updateFollowerReq(user,new Database_Pointer("okayge@gmail.com"),true);
        db.updateBlock(user,new Database_Pointer("okayge@gmail.com"),true);

    }
}
