package com.cmput301f21t34.habittrak;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cmput301f21t34.habittrak.user.Database_Pointer;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.Habit_List;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public DatabaseManager() {
        database = FirebaseFirestore.getInstance();
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

        boolean validCredentials = false;

        final CollectionReference collectionReference = database.collection("users");

        try {
            DocumentReference docref = collectionReference.document(email);
            Task<DocumentSnapshot> task = docref.get();
            while (!task.isComplete()) ;
            DocumentSnapshot document = task.getResult();
            if (document.getData() != null) {
                if (document.get("Password").equals(password)) {
                    validCredentials = true;
                }
            }
        }
        catch (Exception ignored) {}

        return validCredentials;
    }

    /**
     * getAllUsers()
     *
     * @author Henry
     * @return
     * Fetches all users (identified by email) from the database
     */
    public ArrayList<Database_Pointer> getAllUsers() {

        ArrayList<Database_Pointer> users = new ArrayList<>();

        try {
            Task<QuerySnapshot> task = database.collection("users").get();

            while (!task.isComplete()) ;

            for (QueryDocumentSnapshot document : task.getResult()) {
                users.add(new Database_Pointer(document.getId().toString()));
            }
        }
        catch (Exception ignored) {}

        return users;
    }

    /**
     * Checks to see if the user with the provided email already exists
     *
     * @author Henry
     * @param email
     * @return boolean
     */
    public boolean isUnique(String email) {

        boolean isUnique = false;

        final CollectionReference collectionReference = database.collection("users");

        try {
            DocumentReference docref = collectionReference.document(email);
            Task<DocumentSnapshot> task = docref.get();
            while (!task.isComplete());
            DocumentSnapshot document = task.getResult();
            if (!document.exists()) {
                isUnique = true;
                return isUnique;
            }
        }

        catch (Exception ignored) {}

        return isUnique;
    }

    /**
     * createNewUser
     *
     * @author Henry
     * returns true if it is successful or false if it is not
     */
    public boolean createNewUser(String email, String username, String password, String biography) {

        String TAG = "Unique";

        if (isUnique(email)) {
            Log.d(TAG, "Unique");
            final CollectionReference collectionReference = database.collection("users");

            HashMap<String, Object> data = new HashMap<>();
            data.put("Password", password);
            data.put("Username", username);
            data.put("Biography", biography);
            data.put("habitList", new ArrayList<Habit>());
            data.put("followerList", new ArrayList<Database_Pointer>());
            data.put("followingList", new ArrayList<Database_Pointer>());
            data.put("followReqList", new ArrayList<Database_Pointer>());
            data.put("followRequestedList", new ArrayList<Database_Pointer>());
            data.put("blockList", new ArrayList<Database_Pointer>());
            data.put("blockedByList", new ArrayList<Database_Pointer>());

            collectionReference
                    .document(email)
                    .set(data);

            return true;
        }
        Log.d(TAG, "Not unique");
        return false;
    }

    /**
     * deleteUser
     *
     * @author Henry
     * returns true if it is successful or false if it is not
     */
    public boolean deleteUser(String email) {

        String TAG = "Delete";
        if (!isUnique(email)) {
            database.collection("users").document(email)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });
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
    public User getUser(String email) {
        User user;

        String name = "";
        String password = "";
        String bio = "";
        ArrayList<Habit> habitList = new ArrayList<Habit>();
        ArrayList<Database_Pointer>followerList = new ArrayList<Database_Pointer>();
        ArrayList<Database_Pointer>followingList = new ArrayList<Database_Pointer>();
        ArrayList<Database_Pointer>followReqList = new ArrayList<Database_Pointer>();
        ArrayList<Database_Pointer>followRequestedList = new ArrayList<Database_Pointer>();
        ArrayList<Database_Pointer>blockList = new ArrayList<Database_Pointer>();
        ArrayList<Database_Pointer>blockedByList = new ArrayList<Database_Pointer>();

        try {
            DocumentReference docref = database.collection("users").document(email);
            Task<DocumentSnapshot> task = docref.get();
            while (!task.isComplete());
            DocumentSnapshot document = task.getResult();

            if (document.getData() != null) {
                Log.d("getData", "not null");
                // ArrayList<Map<Integer, Habit>> habitListMap = (ArrayList<Map<Integer, Habit>>) document.get("habitList");
                ArrayList<HashMap<String, String>> followerListMap = (ArrayList<HashMap<String, String>>) document.get("followerList");
                followerList = toPointerList(followerListMap);

                ArrayList<HashMap<String, String>> followingListMap = (ArrayList<HashMap<String, String>>) document.get("followingList");
                followingList = toPointerList(followingListMap);

                ArrayList<HashMap<String, String>> followReqListMap = (ArrayList<HashMap<String, String>>) document.get("followReqList");
                followReqList = toPointerList((followReqListMap));

                ArrayList<HashMap<String, String>> followRequestedListMap = (ArrayList<HashMap<String, String>>) document.get("followRequestedList");
                followRequestedList = toPointerList((followRequestedListMap));

                ArrayList<HashMap<String, String>> blockListMap = (ArrayList<HashMap<String, String>>) document.get("blockList");
                blockList = toPointerList((blockListMap));

                ArrayList<HashMap<String, String>> blockedByListMap = (ArrayList<HashMap<String, String>>) document.get("blockedByList");
                blockedByList = toPointerList((blockedByListMap));

                name = (String) document.get("Username");
                password = (String) document.get("Password");
                bio = (String) document.get("Biography");
            }

            user = new User(email);

            user.setPassword(password);
            user.setUsername(name);
            user.setHabitList((Habit_List) habitList);
            user.setFollowerList(followerList);
            user.setFollowingList(followingList);
            user.setBlockList(blockList);
            user.setBlockedByList(blockedByList);
            user.setFollowerReqList(followReqList);
            user.setFollowerRequestedList(followRequestedList);
            return user;
        }
        catch (Exception ignored) {}
        user = new User();
        return user;
    }

    /**
     * getUserName
     * gets the user name of the provided email
     *
     * @author Tauseef
     * @param email
     * @return username (string)
     */
    public String getUserName(String email) {

        String name = "";

        try {
            DocumentReference docref = database.collection("users").document(email);
            Task<DocumentSnapshot> task = docref.get();
            while (!task.isComplete()) ;
            DocumentSnapshot document = task.getResult();

            if (document.getData() != null) {
                name = (String) document.get("Username");
            }
        }
        catch (Exception ignored){}
        return name;
    }

    /**
     * getUserName
     * gets the user bio of the provided email
     *
     * @author Tauseef
     * @param email
     * @return
     */
    public String getUserBio(String email) {

        String bio = "";

        try {
            DocumentReference docref = database.collection("users").document(email);
            Task<DocumentSnapshot> task = docref.get();
            while (!task.isComplete()) ;
            DocumentSnapshot document = task.getResult();

            if (document.getData() != null) {
                bio = (String) document.get("Biography");
            }
        }
        catch (Exception ignored){}
        return bio;
    }

    /**
     * getHabitList
     * gets the habit list of the provided user email
     *
     * @author Tauseef
     * @param email
     * @return habitList
     */

    public ArrayList<Habit> getHabitList(String email) {

        ArrayList<Habit> returnHabitList = new ArrayList<Habit>();

        try {
            DocumentReference docref = database.collection("users").document(email);
            Task<DocumentSnapshot> task = docref.get();
            while (!task.isComplete()) ;
            DocumentSnapshot document = task.getResult();

            if (document.getData() != null) {
                returnHabitList = (ArrayList<Habit>) document.get("habitList");
            }
        }
        catch (Exception ignored){}
        return returnHabitList;
    }

    /**
     * getFollowerList
     * gets the follower list of the provided email
     *
     * @author Tauseef
     * @param email
     * @return Follower list
     */

    public ArrayList<Database_Pointer> getFollowerList(String email) {

        ArrayList<Database_Pointer> returnFollowerList = new ArrayList<Database_Pointer>();

        try {
            DocumentReference docref = database.collection("users").document(email);
            Task<DocumentSnapshot> task = docref.get();
            while (!task.isComplete()) ;
            DocumentSnapshot document = task.getResult();

            if (document.getData() != null) {
                ArrayList<HashMap<String, String>> followerListMap = (ArrayList<HashMap<String, String>>) document.get("followerList");
                returnFollowerList = toPointerList(followerListMap);
            }
        }
        catch (Exception ignored){}
        return returnFollowerList;
    }

    /**
     * getFollowingList
     * gets the following list of the provided email
     *
     * @author Tauseef
     * @param email
     * @return Following list
     */

    public ArrayList<Database_Pointer> getFollowingList(String email ){

        ArrayList<Database_Pointer> returnFollowingList = new ArrayList<Database_Pointer>();

        try {
            DocumentReference docref = database.collection("users").document(email);
            Task<DocumentSnapshot> task = docref.get();
            while (!task.isComplete()) ;
            DocumentSnapshot document = task.getResult();

            if (document.getData() != null) {
                ArrayList<HashMap<String, String>> followingListMap = (ArrayList<HashMap<String, String>>) document.get("followingList");
                returnFollowingList = toPointerList(followingListMap);
            }
        }
        catch (Exception ignored){}
        return returnFollowingList;
    }

    /**
     * getFollowReqList
     * returns followReq list of the provided email (The followReq list contains all the users email
     * the main user(the one with the id) has requested to follow)
     * Example: user 1 has requested to follow user 2 and user 3 then this function will return user 2 and user 3
     * the input is user 1 email
     *
     * @author Tauseef
     * @param email
     * @return follow req list
     */

    public ArrayList<Database_Pointer> getFollowReqList(String email) {

        ArrayList<Database_Pointer> returnFollowReqList = new ArrayList<Database_Pointer>();

        try {
            DocumentReference docref = database.collection("users").document(email);
            Task<DocumentSnapshot> task = docref.get();
            while (!task.isComplete()) ;
            DocumentSnapshot document = task.getResult();

            if (document.getData() != null) {
                ArrayList<HashMap<String, String>> followReqListMap = (ArrayList<HashMap<String, String>>) document.get("followReqList");
                returnFollowReqList = toPointerList((followReqListMap));
            }
        }
        catch (Exception ignored){}
        return returnFollowReqList;
    }


    /**
     * getFollowRequestedList
     * returns followRequested list of the provided email (The followReq list contains all the users email
     * that have requested to follow the main user(the one with the id)
     * Example: user 2 and user 3 has requested to follow user 1 then this function will return user 2 and user 3
     * the input is user 1 email
     *
     * @author Tauseef
     * @param email
     * @return
     */

    public ArrayList<Database_Pointer> getFollowRequestedList(String email) {

        ArrayList<Database_Pointer> returnFollowRequestedList = new ArrayList<Database_Pointer>();

        try {
            DocumentReference docref = database.collection("users").document(email);
            Task<DocumentSnapshot> task = docref.get();
            while (!task.isComplete()) ;
            DocumentSnapshot document = task.getResult();

            if (document.getData() != null) {
                ArrayList<HashMap<String, String>> followRequestedListMap = (ArrayList<HashMap<String, String>>) document.get("followRequestedList");
                returnFollowRequestedList = toPointerList((followRequestedListMap));
            }
        }
        catch (Exception ignored){}
        return returnFollowRequestedList;
    }


    /**
     * getBlockList
     * returns the block list of the provided email (The block list contains all the users email
     * that the user have blocked
     * Example: user 2 and user 3 has been blocked by user 1 then this function will return user 2 and user 3
     * the input is user 1 email
     *
     * @author Tauseef
     * @param email
     * @return
     */

    public ArrayList<Database_Pointer> getBlockList(String email) {

        ArrayList<Database_Pointer> returnBlockList = new ArrayList<Database_Pointer>();

        try {
            DocumentReference docref = database.collection("users").document(email);
            Task<DocumentSnapshot> task = docref.get();
            while (!task.isComplete()) ;
            DocumentSnapshot document = task.getResult();

            if (document.getData() != null) {
                ArrayList<HashMap<String, String>> blockListMap = (ArrayList<HashMap<String, String>>) document.get("blockList");
                returnBlockList = toPointerList((blockListMap));
            }
        }
        catch (Exception ignored){}
        return returnBlockList;
    }

    /**
     * getBlockedByList
     * returns followRequested list of the provided email (The followReq list contains all the users email
     * that have requested to follow the main user(the one with the id)
     * Example: user 2 and user 3 has requested to follow user 1 then this function will return user 2 and user 3
     * the input is user 1 email
     *
     * @author Tauseef
     * @param email
     * @return
     */

    public ArrayList<Database_Pointer> getBlockedByList(String email) {

        ArrayList<Database_Pointer> returnBlockedByList = new ArrayList<Database_Pointer>();

        try {
            DocumentReference docref = database.collection("users").document(email);
            Task<DocumentSnapshot> task = docref.get();
            while (!task.isComplete()) ;
            DocumentSnapshot document = task.getResult();

            if (document.getData() != null) {
                ArrayList<HashMap<String, String>> blockedByListMap = (ArrayList<HashMap<String, String>>) document.get("blockedByList");
                returnBlockedByList = toPointerList((blockedByListMap));
            }
        }
        catch (Exception ignored){}
        return returnBlockedByList;
    }

    /**
     * updateHabitNamePassword
     * @author Tauseef
     * @param user
     * updates the habit name and password
     */
    public void updateHabitNamePassword(User user){
        final CollectionReference collectionReference = database.collection("users");

        HashMap<String, Object> data = new HashMap<>();
        data.put("Password", user.getPassword());
        data.put("Username", user.getUsername());
        data.put("Biography", user.getBiography());
        data.put("habitList", user.getHabitList());
        data.put("followerList", user.getFollowerList());
        data.put("followingList", user.getFollowingList());
        data.put("followReqList", user.getFollowerReqList());
        data.put("followRequestedList", user.getFollowerRequestedList());
        data.put("blockList", user.getBlockList());
        data.put("blockedByList", user.getBlockedByList());

        collectionReference
                .document(user.getEmail())
                .set(data);
    }

    /**
     * addFollower
     * email is now a follower of user (so add email to the follower list of user and add user to the following list of email2)
     * @author Henry
     * @param user
     * @param toBeAdded
     */
    public void updateFollower(User user, Database_Pointer toBeAdded,boolean remove) {
        // Update user

        DocumentReference userRef = database.collection("users").document(user.getEmail());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String TAG = "contain checker";
                        int index = -1;
                        boolean contains = false;
                        ArrayList<Database_Pointer> followerList = new ArrayList<>();
                        ArrayList<HashMap<String, String>> followerListMap = (ArrayList<HashMap<String, String>>) document.get("followerList");
                        Log.d(TAG,Integer.toString(followerListMap.size()));
                        followerList = toPointerList((followerListMap));
                        for(int i = 0; i <followerList.size(); i++){
                            if (followerList.get(i).equals(toBeAdded)){
                                index = i;
                                contains = true;
                                Log.d(TAG,"from equals");
                            }
                            else if (followerList.get(i).Equals(toBeAdded)){
                                index = i;
                                contains = true;
                                Log.d(TAG,"not from equals");
                            }
                            Log.d(TAG,"inside for loop");
                        }
                        // Updates only if follower is not already in the list
                        if (!contains && !remove) {
                            Log.d("Contains", "does not");
                            List<String> fieldsToUpdate = new ArrayList<>();
                            fieldsToUpdate.add("followerList");

                            followerList.add(toBeAdded);

                            HashMap<String, Object> data = new HashMap<>();
                            data.put("followerList", followerList);
                            userRef.set(data, SetOptions.mergeFields(fieldsToUpdate));
                        }
                        else if (contains && remove){
                            Log.d("Contains", "does not");
                            List<String> fieldsToUpdate = new ArrayList<>();
                            fieldsToUpdate.add("followerList");

                            followerList.remove(index);

                            HashMap<String, Object> data = new HashMap<>();
                            data.put("followerList", followerList);
                            userRef.set(data, SetOptions.mergeFields(fieldsToUpdate));

                        }
                    }
                } else {
                    Log.d("Task", "get failed with ", task.getException());
                }
            }
        });

        // Update toBeAdded
        DocumentReference addedRef = database.collection("users").document(toBeAdded.getEmail());
        addedRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String TAG = "contain checker";
                        int index = -1;
                        boolean contains = false;
                        ArrayList<Database_Pointer> followingList = new ArrayList<>();
                        ArrayList<HashMap<String, String>> followingListMap = (ArrayList<HashMap<String, String>>) document.get("followingList");
                        followingList = toPointerList((followingListMap));
                        Database_Pointer following = new Database_Pointer(user.getEmail());
                        // Updates only if following is not already in the list
                        for(int i = 0; i < followingList.size(); i++){
                            if (followingList.get(i).equals(following)){
                                index = i;
                                contains = true;
                                Log.d(TAG,"from equals");
                            }
                            else if (followingList.get(i).Equals(following)){
                                index = i;
                                contains = true;
                                Log.d(TAG,"not from equals");
                            }
                        }
                        if (!contains && !remove){
                            Log.d("Contains", "does not");
                            List<String> fieldsToUpdate = new ArrayList<>();
                            fieldsToUpdate.add("followingList");

                            followingList.add(following);

                            HashMap<String, Object> data = new HashMap<>();
                            data.put("followingList", followingList);
                            addedRef.set(data, SetOptions.mergeFields(fieldsToUpdate));
                        }
                        else if (contains && remove){
                            Log.d("Contains", "does not");
                            List<String> fieldsToUpdate = new ArrayList<>();
                            fieldsToUpdate.add("followingList");

                            followingList.remove(index);


                            HashMap<String, Object> data = new HashMap<>();
                            data.put("followingList", followingList);
                            addedRef.set(data, SetOptions.mergeFields(fieldsToUpdate));
                        }
                    }
                } else {
                    Log.d("Task", "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * addfollow req
     * email1 asked to follow email2 (so add email2 to the followerReq list of email1 and add email1 to the followerRequested list of email2)
     * followerRequested list means the people that asked to follow the user
     * followerReq list means the people that the user requested to follow
     * @author Tauseef
     * @param user
     * @param toBeAdded
     */
    public void updateFollowerReq(User user, Database_Pointer toBeAdded, boolean remove) {
        // Update user

        DocumentReference userRef = database.collection("users").document(user.getEmail());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String TAG = "contain checker";
                        int index = -1;
                        boolean contains = false;
                        ArrayList<Database_Pointer> followerReqList = new ArrayList<>();
                        ArrayList<HashMap<String, String>> followerReqListMap = (ArrayList<HashMap<String, String>>) document.get("followReqList");
                        followerReqList = toPointerList((followerReqListMap));
                        for(int i = 0; i < followerReqList.size(); i++){
                            if (followerReqList.get(i).equals(toBeAdded)){
                                index = i;
                                contains = true;
                                Log.d(TAG,"from equals");
                            }
                            else if (followerReqList.get(i).Equals(toBeAdded)){
                                index = i;
                                contains = true;
                                Log.d(TAG,"not from equals");
                            }
                        }
                        // Updates only if follower is not already in the list
                        if (!contains && !remove) {
                            Log.d("Contains", "does not");
                            List<String> fieldsToUpdate = new ArrayList<>();
                            fieldsToUpdate.add("followReqList");

                            followerReqList.add(toBeAdded);

                            HashMap<String, Object> data = new HashMap<>();
                            data.put("followReqList", followerReqList);
                            userRef.set(data, SetOptions.mergeFields(fieldsToUpdate));
                        }
                        else if (contains && remove){
                            Log.d("Contains", "does not");
                            List<String> fieldsToUpdate = new ArrayList<>();
                            fieldsToUpdate.add("followReqList");

                            followerReqList.remove(index);

                            HashMap<String, Object> data = new HashMap<>();
                            data.put("followReqList", followerReqList);
                            userRef.set(data, SetOptions.mergeFields(fieldsToUpdate));
                        }
                    }
                } else {
                    Log.d("Task", "get failed with ", task.getException());
                }
            }
        });

        // Update toBeAdded
        DocumentReference addedRef = database.collection("users").document(toBeAdded.getEmail());
        addedRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        int index = -1;
                        String TAG = "contain checker";
                        boolean contains = false;
                        ArrayList<Database_Pointer> followerRequestedList = new ArrayList<>();
                        ArrayList<HashMap<String, String>> followerRequestedListMap = (ArrayList<HashMap<String, String>>) document.get("followRequestedList");
                        followerRequestedList = toPointerList((followerRequestedListMap));
                        Database_Pointer followerRequested = new Database_Pointer(user.getEmail());
                        // Updates only if following is not already in the list
                        for(int i = 0; i < followerRequestedList.size(); i++){
                            if (followerRequestedList.get(i).equals(followerRequested)){
                                index = i;
                                contains = true;
                                Log.d(TAG,"from equals");
                            }
                            else if (followerRequestedList.get(i).Equals(followerRequested)){
                                index = i;
                                contains = true;
                                Log.d(TAG,"not from equals");
                            }
                        }
                        if (!contains && !remove){
                            Log.d("Contains", "does not");
                            List<String> fieldsToUpdate = new ArrayList<>();
                            fieldsToUpdate.add("followRequestedList");

                            followerRequestedList.add(followerRequested);

                            HashMap<String, Object> data = new HashMap<>();
                            data.put("followRequestedList", followerRequestedList);
                            addedRef.set(data, SetOptions.mergeFields(fieldsToUpdate));
                        }
                        else if (contains && remove){
                            Log.d("Contains", "does not");
                            List<String> fieldsToUpdate = new ArrayList<>();
                            fieldsToUpdate.add("followRequestedList");

                            followerRequestedList.remove(index);

                            HashMap<String, Object> data = new HashMap<>();
                            data.put("followRequestedList", followerRequestedList);
                            addedRef.set(data, SetOptions.mergeFields(fieldsToUpdate));
                        }
                    }
                } else {
                    Log.d("Task", "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * addtoblocklist
     * email2 is now blocked by email1 (so add email2 to the block list of email1 and add email1 to the blockedby list of email2)
     * @author Tauseef
     * @param user
     * @param toBeAdded
     */
    public void updateBlock(User user, Database_Pointer toBeAdded, boolean remove) {
        // Update user

        DocumentReference userRef = database.collection("users").document(user.getEmail());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        int index = -1;
                        String TAG = "contain checker";
                        boolean contains = false;
                        ArrayList<Database_Pointer> blockList = new ArrayList<>();
                        ArrayList<HashMap<String, String>> blockListMap = (ArrayList<HashMap<String, String>>) document.get("blockList");
                        blockList = toPointerList((blockListMap));
                        for(int i = 0; i < blockList.size(); i++){
                            if (blockList.get(i).equals(toBeAdded)){
                                contains = true;
                                index = i;
                                Log.d(TAG,"from equals");
                            }
                            else if (blockList.get(i).Equals(toBeAdded)){
                                contains = true;
                                index = i;
                                Log.d(TAG,"not from equals");
                            }
                        }
                        // Updates only if follower is not already in the list
                        if (!contains && !remove) {
                            Log.d("Contains", "does not");
                            List<String> fieldsToUpdate = new ArrayList<>();
                            fieldsToUpdate.add("blockList");

                            blockList.add(toBeAdded);

                            HashMap<String, Object> data = new HashMap<>();
                            data.put("blockList", blockList);
                            userRef.set(data, SetOptions.mergeFields(fieldsToUpdate));
                        }
                        else if (contains && remove){
                            Log.d("Contains", "does not");
                            List<String> fieldsToUpdate = new ArrayList<>();
                            fieldsToUpdate.add("blockList");

                            blockList.remove(index);

                            HashMap<String, Object> data = new HashMap<>();
                            data.put("blockList", blockList);
                            userRef.set(data, SetOptions.mergeFields(fieldsToUpdate));
                        }
                    }
                } else {
                    Log.d("Task", "get failed with ", task.getException());
                }
            }
        });

        // Update toBeAdded
        DocumentReference addedRef = database.collection("users").document(toBeAdded.getEmail());
        addedRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String TAG = "contain checker";
                        int index = -1;
                        boolean contains = false;
                        ArrayList<Database_Pointer> blockedByList = new ArrayList<>();
                        ArrayList<HashMap<String, String>> blockedByListMap = (ArrayList<HashMap<String, String>>) document.get("blockedByList");
                        blockedByList = toPointerList((blockedByListMap));
                        Database_Pointer blockedBy = new Database_Pointer(user.getEmail());
                        // Updates only if following is not already in the list
                        for(int i = 0; i < blockedByList.size();i++){
                            if (blockedByList.get(i).equals(blockedBy)){
                                contains = true;
                                index = i;
                                Log.d(TAG,"from equals");
                            }
                            else if (blockedByList.get(i).Equals(blockedBy)){
                                contains = true;
                                index = i;
                                Log.d(TAG,"not from equals");
                            }
                        }
                        if (!contains && !remove){
                            Log.d("Contains", "does not");
                            List<String> fieldsToUpdate = new ArrayList<>();
                            fieldsToUpdate.add("blockedByList");

                            blockedByList.add(blockedBy);

                            HashMap<String, Object> data = new HashMap<>();
                            data.put("blockedByList", blockedByList);
                            addedRef.set(data, SetOptions.mergeFields(fieldsToUpdate));
                        }
                        else if (contains && remove){
                            Log.d("Contains", "does not");
                            List<String> fieldsToUpdate = new ArrayList<>();
                            fieldsToUpdate.add("blockedByList");

                            blockedByList.remove(index);

                            HashMap<String, Object> data = new HashMap<>();
                            data.put("blockedByList", blockedByList);
                            addedRef.set(data, SetOptions.mergeFields(fieldsToUpdate));
                        }
                    }
                } else {
                    Log.d("Task", "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * Converts an ArrayList<HashMap<String, String>> listOfMap to an ArrayList<Database_Pointer>
     * @author henry
     * @param listOfMap
     * @return
     */
    public ArrayList<Database_Pointer> toPointerList(ArrayList<HashMap<String, String>> listOfMap) {
        ArrayList<Database_Pointer> pointerList = new ArrayList<>();
        for (int i = 0; i < listOfMap.size(); i++) {
            String email = listOfMap.get(i).get("email");
            Database_Pointer dp = new Database_Pointer(email);
            pointerList.add(dp);
        }
        return pointerList;
    }
}