package com.cmput301f21t34.habittrak;

import androidx.annotation.NonNull;

import com.cmput301f21t34.habittrak.user.Database_Pointer;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

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
    private boolean userExists;
    String username;
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
        ArrayList<Habit> returnHabitList = new ArrayList<Habit>();
        ArrayList<Database_Pointer> returnFollowerList = new ArrayList<Database_Pointer>();
        ArrayList<Database_Pointer> returnFollowingList = new ArrayList<Database_Pointer>();
        ArrayList<Database_Pointer> returnFollowReqList = new ArrayList<Database_Pointer>();
        ArrayList<Database_Pointer> returnFollowRequestedList = new ArrayList<Database_Pointer>();
        ArrayList<Database_Pointer> returnBlockList = new ArrayList<Database_Pointer>();
        ArrayList<Database_Pointer> returnBlockedByList = new ArrayList<Database_Pointer>();
    }

    public boolean validCredentials() {
        final CollectionReference collectionReference = database.collection("users");

        return false;
    }
    /**
     * createNewUser
     * returns 0 if it is successful or 1 if it is not
     *
     */
    public int createNewUser(String user_email,String password) {
        HashMap<String, String> data = new HashMap<>();

        return 0;
    }

    /**
     * Checks to see if the user with the provided email already exists (not sure if we need it but just in case
     * @param user_email
     * @return boolean
     */
    /*
    public boolean ifUserExist(String user_email) {
        userExists = false;
        // better to do it in createUser
        DatabaseReference usersReference = database.getReference("users");
        DatabaseReference userReference = usersReference.child(user_email);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userExists = true;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return userExists;
    }*/

    /**
     * getUser
     * returns the user with the provided email
     * @param user_email
     * @return User
     */
    /*
    public User getUser(String user_email) {

        User user = new User();
        user.setUsername(getUserName(user_email));
        user.setHabitList(getHabitList(user_email));
        user.setFollowerList(getFollowerList(user_email));
        user.setFollowingList(getFollowingList(user_email));
        user.setFollowerReqList(getFollowReqList(user_email));
        user.setBlockList(getBlockList(user_email));
        user.setBlockByList(getBlockedByList(user_email));

        return user;
    }*/

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