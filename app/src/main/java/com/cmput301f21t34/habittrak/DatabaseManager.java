package com.cmput301f21t34.habittrak;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmput301f21t34.habittrak.user.Database_Pointer;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author Tauseef Nafee Fattah
 * @author Henry
 * @see User
 * @version 1.0
 * Implemented the getter methods only the setters methods will be implemented soon
 */

public class DatabaseManager {
    private FirebaseFirestore database;
    private ChildEventListener childEventListener;
    private boolean isUnique = true;
    private boolean validCredentials = false;
    private User userToReturn;
    String username;
    String password;
    ArrayList<Habit> returnHabitList;
    ArrayList<Database_Pointer> returnFollowerList;
    ArrayList<Database_Pointer> returnFollowingList;
    ArrayList<Database_Pointer> returnFollowReqList;
    ArrayList<Database_Pointer> returnFollowRequestedList;
    ArrayList<Database_Pointer> returnBlockList;
    ArrayList<Database_Pointer> returnBlockedByList;

    public DatabaseManager() {
        database = FirebaseFirestore.getInstance();

        username = "";
        password = "";
        ArrayList<Habit> returnHabitList = new ArrayList<Habit>();
        ArrayList<Database_Pointer> returnFollowerList = new ArrayList<Database_Pointer>();
        ArrayList<Database_Pointer> returnFollowingList = new ArrayList<Database_Pointer>();
        ArrayList<Database_Pointer> returnFollowReqList = new ArrayList<Database_Pointer>();
        ArrayList<Database_Pointer> returnFollowRequestedList = new ArrayList<Database_Pointer>();
        ArrayList<Database_Pointer> returnBlockList = new ArrayList<Database_Pointer>();
        ArrayList<Database_Pointer> returnBlockedByList = new ArrayList<Database_Pointer>();

        //
    }

    /**
     * validCredentials
     *
     * checks if given password matches the actual password of an email
     *
     * @author Henry
     * @param email
     * @param password
     * @return
     * returns true if credentials match, false otherwise
     */
    public boolean validCredentials(String email, String password) {

        String TAG = "ValidCredentials";

        final CollectionReference collectionReference = database.collection("users");
        DocumentReference docRef = collectionReference.document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot exists");
                        if (document.getData().get("Password").equals(password)) {
                            validCredentials = true;
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return validCredentials;
    }

    /**
     * Checks to see if the user with the provided email already exists
     *
     * @author Henry
     * @param email
     * @return boolean
     */

    public boolean userUnique(String email) {

        String TAG = "UniqueUser";

        database.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(email)) {
                                    isUnique = false;
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return isUnique;
    }

    /**
     * createNewUser
     *
     * @author Henry
     * returns true if it is successful or false if it is not
     */
    public boolean createNewUser(String user_email, String username, String password) {

        if (userUnique(user_email)) {
            final CollectionReference collectionReference = database.collection("users");

            HashMap<String, Object> data = new HashMap<>();
            data.put("Password", password);
            data.put("Username", username);
            data.put("habitList", new ArrayList<Habit>());
            data.put("followerList", new ArrayList<Database_Pointer>());
            data.put("followingList", new ArrayList<Database_Pointer>());
            data.put("followReqList", new ArrayList<Database_Pointer>());
            data.put("followRequestedList", new ArrayList<Database_Pointer>());
            data.put("blockList", new ArrayList<Database_Pointer>());
            data.put("blockedByList", new ArrayList<Database_Pointer>());

            collectionReference
                    .document(user_email)
                    .set(data);

            return true;
        }

        return false;
    }

    /**
     * getUser
     *
     * @author Tauseef
     * @author Henry
     *
     * returns the user with the provided email
     * @param email
     * @return User
     */
    // TODO: DO NOT USE THIS CLASS. It currently returns a dummy user BEFORE data is read from firestore
    public User getUser(String email) {

        String TAG = "User";
        userToReturn = new User();

        final CollectionReference collectionReference = database.collection("users");

        DocumentReference docRef = collectionReference.document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot exists");
                        userToReturn = document.toObject(User.class);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        Log.d(TAG, userToReturn.getUsername());

        return userToReturn;
    }

    /**
     * getUserName
     * gets the user name of the provided email
     * @param user_email
     * @return username (string)
     */
    /*
    public String getUserName(String user_email) {
        DatabaseReference usersReference = database.getReference("users");
        username = "";
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (ds.child("userInfo").child("email").getValue(String.class).equals(user_email)){
                        username = ds.child("userInfo").child("name").getValue(String.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return username;
    }*/

    /**
     * getHabitList
     * gets the habit list of the provided user email
     * @param user_email
     * @return habitList
     */
    /*
    public ArrayList<Habit> getHabitList(String user_email) {
        DatabaseReference usersReference = database.getReference("users");
        // returns empty list if no habit exists
        returnHabitList.clear();

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren()) {
                    if (ds.child("userInfo").child("email").getValue(String.class).equals(user_email)) {
                        for (DataSnapshot dataSnapshot: ds.child("Habits").getChildren()) {
                            returnHabitList.add(dataSnapshot.getValue(Habit.class));
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return returnHabitList;
    }*/

    /**
     * getFollowerList
     * gets the follower list of the provided email
     * @param user_email
     * @return Follower list
     */
    /*
    public ArrayList<Database_Pointer> getFollowerList(String user_email) {
        DatabaseReference usersReference = database.getReference("users");
        // returns empty list if no habit exists
        returnFollowerList.clear();

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.child("userInfo").child("email").getValue(String.class).equals(user_email)) {
                        for(DataSnapshot dataSnapshot: ds.child("Followers").getChildren()){
                            returnFollowerList.add(new Database_Pointer(dataSnapshot.getValue(String.class)));
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return returnFollowerList;
    }*/

    /**
     * getFollowingList
     * gets the following list of the provided email
     * @param user_email
     * @return Following list
     */
    /*
    public ArrayList<Database_Pointer> getFollowingList(String user_email ){
        DatabaseReference usersReference = database.getReference("users");
        // returns empty list if no habit exists
        returnFollowingList.clear();

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.child("userInfo").child("email").getValue(String.class).equals(user_email)){
                        for(DataSnapshot dataSnapshot: ds.child("Followings").getChildren()){
                            returnFollowingList.add(new Database_Pointer(dataSnapshot.getValue(String.class)));
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return returnFollowingList;
    }*/

    /**
     * getFollowReqList
     * returns followReq list of the provided email (The followReq list contains all the users email
     * the main user(the one with the id) has requested to follow)
     * Example: user 1 has requested to follow user 2 and user 3 then this function will return user 2 and user 3
     * the input is user 1 email
     * *** I can change this function to do the opposite if needed
     * @param user_email
     * @return follow req list
     */
    /*
    public ArrayList<Database_Pointer> getFollowReqList(String user_email) {
        DatabaseReference usersReference = database.getReference("users");
        // returns empty list if no habit exists
        returnFollowReqList.clear();

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.child("userInfo").child("email").getValue(String.class).equals(user_email)) {
                        for(DataSnapshot dataSnapshot: ds.child("FollowReqs").getChildren()){
                            returnFollowReqList.add(new Database_Pointer(dataSnapshot.getValue(String.class)));
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return returnFollowReqList;
    }*/

    /**
     * getFollowRequestedList
     * returns followRequested list of the provided email (The followReq list contains all the users email
     * that have requested to follow the main user(the one with the id)
     * Example: user 2 and user 3 has requested to follow user 1 then this function will return user 2 and user 3
     * the input is user 1 email
     * I can change this function to do the opposite if needed
     * @param user_email
     * @return
     */
    /*
    public ArrayList<Database_Pointer> getFollowRequestedList(String user_email) {
        DatabaseReference usersReference = database.getReference("users");
        // returns empty list if no habit exists
        returnFollowRequestedList.clear();

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.child("userInfo").child("email").getValue(String.class).equals(user_email)) {
                        for(DataSnapshot dataSnapshot: ds.child("FollowRequesteds").getChildren()){
                            returnFollowRequestedList.add(new Database_Pointer(dataSnapshot.getValue(String.class)));
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return returnFollowRequestedList;
    }*/

    /**
     * getBlockList
     * returns the block list of the provided email (The block list contains all the users email
     * that the user have blocked
     * Example: user 2 and user 3 has been blocked by user 1 then this function will return user 2 and user 3
     * the input is user 1 email
     * I can change this function to do the opposite if needed
     * @param user_email
     * @return
     */
    /*
    public ArrayList<Database_Pointer> getBlockList(String user_email) {
        DatabaseReference usersReference = database.getReference("users");
        // returns empty list if no habit exists
        returnBlockList.clear();

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.child("userInfo").child("email").getValue(String.class).equals(user_email)) {
                        for(DataSnapshot dataSnapshot: ds.child("BlockList").getChildren()){
                            returnBlockList.add(new Database_Pointer(dataSnapshot.getValue(String.class)));
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return returnBlockList;
    }*/

    /**
     * getBlockedByList
     * returns followRequested list of the provided email (The followReq list contains all the users email
     * that have requested to follow the main user(the one with the id)
     * Example: user 2 and user 3 has requested to follow user 1 then this function will return user 2 and user 3
     * the input is user 1 email
     * I can change this function to do the opposite if needed
     * @param user_email
     * @return
     */
    /*
    public ArrayList<Database_Pointer> getBlockedByList(String user_email) {
        DatabaseReference usersReference = database.getReference("users");
        // returns empty list if no habit exists
        returnBlockedByList.clear();

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.child("userInfo").child("email").getValue(String.class).equals(user_email)) {
                        for(DataSnapshot dataSnapshot: ds.child("BlockedByList").getChildren()){
                            returnBlockedByList.add(new Database_Pointer(dataSnapshot.getValue(String.class)));
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return returnBlockedByList;
    }*/
    /**
     * setHabitListDb
     * sets the habit list in the database
     * have 2 implementations (one is directly changing entire habit list another is changing habit events individually)
     * @param user_email
     * @param habitList
     * @param givenHabitId
     * @param habitEventList
     */
    /*
    public void setHabitListDb(String user_email, ArrayList<Habit> habitList, String givenHabitId,
                               ArrayList<Habit> habitEventList) {
        DatabaseReference usersReference = database.getReference("users");
    }
    */
}