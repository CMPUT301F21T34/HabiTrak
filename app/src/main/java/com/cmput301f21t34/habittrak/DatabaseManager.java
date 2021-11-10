package com.cmput301f21t34.habittrak;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cmput301f21t34.habittrak.user.DatabasePointer;
import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitEvent;
import com.cmput301f21t34.habittrak.user.HabitList;
import com.cmput301f21t34.habittrak.user.OnDays;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * @author Tauseef Nafee Fattah
 * @author Henry
 * @see User
 * @version 1.0
 *
 */

public class DatabaseManager {
    private FirebaseFirestore database;

    public DatabaseManager() {
        database = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore getDatabase() {
        return database;
    }

    /**
     * validCredentials
     *
     * checks if given password matches the actual password of an email
     *
     * @author Henry
     * @param email - Type String; the email of the user who's validity is being checked
     * @param password - Type password; The password of the user who's validity is being checked
     * @return Boolean; returns true if credentials match, false otherwise
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
     * Fetches all users (identified by email) from the database
     *
     * @author Henry
     * @return ArrayList<Database_Pointer>
     */
    public ArrayList<DatabasePointer> getAllUsers() {

        ArrayList<DatabasePointer> users = new ArrayList<>();

        try {
            Task<QuerySnapshot> task = database.collection("users").get();

            while (!task.isComplete()) ;

            for (QueryDocumentSnapshot document : task.getResult()) {
                users.add(new DatabasePointer(document.getId().toString()));
            }
        }
        catch (Exception ignored) {}

        return users;
    }

    /**
     * isUniqueEmail
     *
     * Checks to see if the user with the provided email already exists
     *
     * @author Henry
     * @param email - Type String; the email that is to be checked id its unique or not
     * @return boolean; returns true if unique otherwise will return false
     */
    public boolean isUniqueEmail(String email) {

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
     * isUniqueUsername
     *
     * Checks to see if the user with the provided email already exists
     *
     * @author Henry
     * @param username -Type String, the username to check
     * @param email - Type String, the email associated with the email
     * @return boolean
     */
    public boolean isUniqueUsername(String username, String email) {

        boolean isUnique = false;

        final CollectionReference collectionReference = database.collection("users");

        try {
            // Get all usernames
            ArrayList<String> allUserNames = new ArrayList<>();
            Task<QuerySnapshot> task1 = database.collection("users").get();
            while (!task1.isComplete()) ;
            for (QueryDocumentSnapshot document : task1.getResult()) {
                allUserNames.add(document.get("username").toString());
            }

            // Get username for given email, then compare
            DocumentReference docref = collectionReference.document(email);
            Task<DocumentSnapshot> task2 = docref.get();
            while (!task2.isComplete());
            DocumentSnapshot document = task2.getResult();
            String name = document.get("username").toString();
            if (!allUserNames.contains(name)) {
                isUnique = true;
            }
            return isUnique;

        }

        catch (Exception ignored) {}

        return isUnique;
    }

    /**
     * createNewUser
     *
     * Creates a new user from the data passed into it, and puts it into the database
     *
     * @author Henry
     * @param email -Type String the email of the user to be created
     * @param password -Type String the password of the user to be created
     * @param username -Type String the username of the user to be created
     * @return true if it is successful or false if it is not
     */
    public boolean createNewUser(String email, String username, String password) {

        String TAG = "Unique";

        if (isUniqueEmail(email)) {
            Log.d(TAG, "Unique");
            final CollectionReference collectionReference = database.collection("users");

            HashMap<String, Object> data = new HashMap<>();
            data.put("Password", password);
            data.put("Username", username);
            data.put("Biography", "");
            data.put("habitList", new ArrayList<HabitDatabase>());
            data.put("followerList", new ArrayList<DatabasePointer>());
            data.put("followingList", new ArrayList<DatabasePointer>());
            data.put("followReqList", new ArrayList<DatabasePointer>());
            data.put("followRequestedList", new ArrayList<DatabasePointer>());
            data.put("blockList", new ArrayList<DatabasePointer>());
            data.put("blockedByList", new ArrayList<DatabasePointer>());

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
     * Attempts to delete a user from the database
     *
     * @author Henry
     * @param email -Type String; the email of the user to be deleted
     * @return true if it is successful or false if it is not
     */
    public boolean deleteUser(String email) {

        String TAG = "Delete";
        if (!isUniqueEmail(email)) {
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
     * returns the user with the provided email
     *
     * @author Tauseef
     * @author Henry
     * @param email- Type String; the email of the user to get
     * @return User
     */
    public User getUser(String email) {
        User user;

        String name = "";
        String password = "";
        String bio = "";

        HabitList habitList = new HabitList();
        ArrayList<DatabasePointer>followerList = new ArrayList<DatabasePointer>();
        ArrayList<DatabasePointer>followingList = new ArrayList<DatabasePointer>();
        ArrayList<DatabasePointer>followReqList = new ArrayList<DatabasePointer>();
        ArrayList<DatabasePointer>followRequestedList = new ArrayList<DatabasePointer>();
        ArrayList<DatabasePointer>blockList = new ArrayList<DatabasePointer>();
        ArrayList<DatabasePointer>blockedByList = new ArrayList<DatabasePointer>();

        try {
            DocumentReference docref = database.collection("users").document(email);
            Task<DocumentSnapshot> task = docref.get();
            while (!task.isComplete());
            DocumentSnapshot document = task.getResult();

            if (document.getData() != null) {
                Log.d("getData", "not null");

                ArrayList<HashMap<String, Object>> requestedHabitList = (ArrayList<HashMap<String, Object>>) document.get("habitList");
                ArrayList<HabitDatabase> requestedHabitDatabases = toHabitDatabaseList(requestedHabitList);
                habitList = databaseToHabit(requestedHabitDatabases);
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
            user.setHabitList(habitList);
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
     *
     * gets the user name of the provided email
     *
     * @author Tauseef
     * @param email -Type String; The email email of the user who's username is to be retrieved
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
     * getUserBio
     *
     * gets the user bio of the provided email
     *
     * @author Tauseef
     * @param email -Type String; The email email of the user who's bio is to be retrieved
     * @return String containing the bio
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
     * @param email -Type String; The email email of the user who's habit list is to be retrieved
     * @return habitList
     */
    public HabitList getHabitList(String email) {

        HabitList returnHabitList = new HabitList();
        try {
            DocumentReference docref = database.collection("users").document(email);
            Task<DocumentSnapshot> task = docref.get();
            while (!task.isComplete());
            DocumentSnapshot document = task.getResult();
            if(document.getData() != null){
                ArrayList<HashMap<String, Object>> requestedHabitList = (ArrayList<HashMap<String, Object>>) document.get("habitList");
                ArrayList<HabitDatabase> requestedHabitDatabases = toHabitDatabaseList(requestedHabitList);
                returnHabitList = databaseToHabit(requestedHabitDatabases);
            }
        }
        catch (Exception ignored){Log.d("XYZGETTING", "habitlist", ignored);}
        return returnHabitList;
    }

    /**
     * toHabitDatabaseList
     *
     * Converts an array of HashMap (data from the database) to an array of HabitDatabase objects
     *
     * @author Tauseef
     * @param hashMapList -Type ArrayList<HashMap<String,Object>> the array of HashMap to be converted
     * @return ArrayList <HabitDatabase>
     */
    public ArrayList<HabitDatabase> toHabitDatabaseList(ArrayList<HashMap<String,Object>>hashMapList){
        ArrayList<HabitDatabase> habitDatabaseList = new ArrayList<>();
        for (int i = 0; i < hashMapList.size(); i++) {
            HabitDatabase habitDatabase = toHabitDatabase(hashMapList.get(i));
            habitDatabaseList.add(habitDatabase);
        }
        return habitDatabaseList;
    }

    /**
     * toHabitDatabase
     * Converts HashMap from database to HabitDatabase object
     *
     * @author Tauseef
     * @param hashmap -Type HashMap<String,Object> the HashMap to be converted
     * @return HabitDatabase
     */
    public HabitDatabase toHabitDatabase(HashMap<String ,Object> hashmap){
        HabitDatabase habitDatabase = new HabitDatabase();
        habitDatabase.setIndex((int)(long) hashmap.get("index"));
        habitDatabase.setReason((String) hashmap.get("reason"));
        habitDatabase.setTitle((String) hashmap.get("title"));
        habitDatabase.setisPublic((boolean) hashmap.get("isPublic"));
        habitDatabase.setHabitEvents(toHabitEventList((ArrayList<HashMap<String, Object>>) hashmap.get("habitEvents")));
        habitDatabase.setOnDaysObj((ArrayList<Boolean>) hashmap.get("onDaysObj"));
        habitDatabase.setStartDate(toCalendar((HashMap<String, Object>) hashmap.get("startDate")));
        return  habitDatabase;
    }

    /**
     * toHabitEventList
     * Converts an array of HashMap (data from the database) to an array of HabitEvent objects
     *
     * @author Tauseef
     * @param hashMapList -Type ArrayList<HashMap<String,Object>> the array of HashMap to be converted
     * @return ArrayList<Habit_Event>
     */
    public ArrayList<HabitEvent> toHabitEventList(ArrayList<HashMap<String,Object>>hashMapList){
        ArrayList<HabitEvent> habitEventList = new ArrayList<>();
        for (int i = 0; i < hashMapList.size(); i++) {
            HabitEvent habitEvent = toHabitEvent( hashMapList.get(i));
            habitEventList.add(habitEvent);
        }
        return habitEventList;
    }

    /**
     * toHabitEvent
     * Converts HashMap from database to HabitEvent object
     *
     * @author Tauseef
     * @param hashmap -Type HashMap<String,Object> the HashMap to be converted
     * @return Habit_Event
     */
    public HabitEvent toHabitEvent(HashMap<String,Object> hashmap){
        HabitEvent event = new HabitEvent();
        event.setComment((String) hashmap.get("comment"));

        //TODO: Figure out away to store a Location and Photograph
        /*
        event.setLocation((String) hashmap.get("location"));
        event.setPhotograph((String) hashmap.get("photograph"));
         */
        event.setCompletedDate(toCalendar((HashMap<String, Object>) hashmap.get("completedDate")));
        return event;
    }

    /**
     * toCalendar
     * Converts HashMap from database to Calendar object
     *
     * @author Tauseef
     * @param hashmap -Type HashMap<String,Object> the HashMap to be converted
     * @return GregorianCalendar
     */
    public GregorianCalendar toCalendar(HashMap<String,Object> hashmap){
        GregorianCalendar returnCalendar = new GregorianCalendar();
        returnCalendar.setLenient((boolean) hashmap.get("lenient"));
        returnCalendar.setFirstDayOfWeek((int) (long) hashmap.get("firstDayOfWeek"));
        returnCalendar.setMinimalDaysInFirstWeek((int) (long) hashmap.get("minimalDaysInFirstWeek"));
        returnCalendar.setTimeInMillis((long) hashmap.get("timeInMillis"));
        returnCalendar.setGregorianChange(((Timestamp) hashmap.get("gregorianChange")).toDate());
        returnCalendar.setTimeZone(getTimezone((HashMap<String, Object>) hashmap.get("timeZone")));
        returnCalendar.setTime(((Timestamp) hashmap.get("time")).toDate());
        return returnCalendar;
    }

    /**
     * getTimezone
     *
     * Converts HashMap from database to TimeZone object
     *
     * @author Tauseef
     * @param hashMap -Type HashMap<String,Object> the HashMap to be converted
     * @return TimeZone
     */
    public TimeZone getTimezone(HashMap<String,Object> hashMap){
        String timezoneId = (String) hashMap.get("id");
        TimeZone zone = TimeZone.getTimeZone(timezoneId);
        return zone;
    }

    /**
     * getFollowerList
     * gets the follower list of the provided email
     *
     * @author Tauseef
     * @param email -Type String; The email of the user who's followerList is to be retrieved
     * @return Follower list
     */
    public ArrayList<DatabasePointer> getFollowerList(String email) {

        ArrayList<DatabasePointer> returnFollowerList = new ArrayList<DatabasePointer>();

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
     *
     * gets the following list of the provided email
     *
     * @author Tauseef
     * @param email -Type String; The email of the user who's followingList is to be retrieved
     * @return Following list
     */
    public ArrayList<DatabasePointer> getFollowingList(String email ){

        ArrayList<DatabasePointer> returnFollowingList = new ArrayList<DatabasePointer>();

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
     *
     * @author Tauseef
     * @param email -Type String; The email email of the user who's follow req list is to be retrieved
     * @return ArrayList<Database_Pointer> Follow req list
     */
    public ArrayList<DatabasePointer> getFollowReqList(String email) {

        ArrayList<DatabasePointer> returnFollowReqList = new ArrayList<DatabasePointer>();

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
     *
     * @author Tauseef
     * @param email -Type String; The email email of the user who's follow requested list is to be retrieved
     * @return ArrayList<Database_Pointer>
     */
    public ArrayList<DatabasePointer> getFollowRequestedList(String email) {

        ArrayList<DatabasePointer> returnFollowRequestedList = new ArrayList<DatabasePointer>();

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
     *
     * @author Tauseef
     * @param email -Type String; The email email of the user who's block list is to be retrieved
     * @return ArrayList<Database_Pointer> Block List
     */
    public ArrayList<DatabasePointer> getBlockList(String email) {

        ArrayList<DatabasePointer> returnBlockList = new ArrayList<DatabasePointer>();

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
     * returns blockedBylist of the provided email (The BlockedBy list contains all the users email
     * that have blocked the user)
     *
     * @author Tauseef
     * @param email -Type String; The email email of the user who's blockedBy list is to be retrieved
     * @return ArrayList<Database_Pointer> BlockedByList
     */
    public ArrayList<DatabasePointer> getBlockedByList(String email) {

        ArrayList<DatabasePointer> returnBlockedByList = new ArrayList<DatabasePointer>();

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
     *
     * updates the habit, name and password of the user
     *
     * @author Tauseef
     * @param user Type- User, the user who's habit/password/name is to be updated
     */
    public void updateHabitNamePassword(User user){
        final CollectionReference collectionReference = database.collection("users");

        HashMap<String, Object> data = new HashMap<>();
        data.put("Password", user.getPassword());
        data.put("Username", user.getUsername());
        data.put("Biography", user.getBiography());
        data.put("habitList", habitToDatabase(user.getHabitList()));
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
     * updateFollower
     *
     * Updates the follower list of the usr by adding or removing the database_pointer toBeAdded
     * @author Henry
     * @param user Type- User, the user who's follower list is to be updated
     * @param toBeAdded Type- Database_Pointer, the database_pointer to be added or removed
     * @param remove -Type Boolean, Indicates whether its add or remove operation
     */
    public void updateFollower(User user, DatabasePointer toBeAdded, boolean remove) {
        // Update user's followerList
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
                        ArrayList<DatabasePointer> followerList = new ArrayList<>();
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
                        // Updates only if follower (toBeAdded) is not already in the list
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

        // Update toBeAdded's followingList
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
                        ArrayList<DatabasePointer> followingList = new ArrayList<>();
                        ArrayList<HashMap<String, String>> followingListMap = (ArrayList<HashMap<String, String>>) document.get("followingList");
                        followingList = toPointerList((followingListMap));
                        DatabasePointer following = new DatabasePointer(user.getEmail());
                        // Updates only if following (user) is not already in the list
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
     * updateFollowerReq
     *
     * Updates the follower requested list of the user by adding or removing the database_pointer toBeAdded
     *
     * @author Tauseef
     * @param user- Type User; the user who's follower requested list is to be updated
     * @param toBeAdded - Type Database_pointer; the database_pointer to be added or removed
     * @param remove - Type Boolean; Indicates whether the operation is adding or removing
     */
    public void updateFollowerReq(User user, DatabasePointer toBeAdded, boolean remove) {
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
                        ArrayList<DatabasePointer> followerReqList = new ArrayList<>();
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
                        // Updates only if followerReq (toBeAdded) is not already in the list
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

        // Update toBeAdded's followerRequestedList
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
                        ArrayList<DatabasePointer> followerRequestedList = new ArrayList<>();
                        ArrayList<HashMap<String, String>> followerRequestedListMap = (ArrayList<HashMap<String, String>>) document.get("followRequestedList");
                        followerRequestedList = toPointerList((followerRequestedListMap));
                        DatabasePointer followerRequested = new DatabasePointer(user.getEmail());
                        // Updates only if followerRequested (user) is not already in the list
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
     * updateBlock
     *
     * Updates the blocklist of the user by adding or removing the database_pointer toBeAdded
     *
     * @author Tauseef
     * @param user- Type User; the user who's block list is to be updated
     * @param toBeAdded - Type Database_pointer; the database_pointer to be added or removed
     * @param remove - Type Boolean; Indicates whether the operation is adding or removing
     *
     */
    public void updateBlock(User user, DatabasePointer toBeAdded, boolean remove) {
        // Update user's blockList
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
                        ArrayList<DatabasePointer> blockList = new ArrayList<>();
                        ArrayList<HashMap<String, String>> blockListMap = (ArrayList<HashMap<String, String>>) document.get("blockList");
                        blockList = toPointerList(blockListMap);
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
                        // Updates only if block (toBeAdded) is not already in the list
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

        // Update toBeAdded's blockedByList
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
                        ArrayList<DatabasePointer> blockedByList = new ArrayList<>();
                        ArrayList<HashMap<String, String>> blockedByListMap = (ArrayList<HashMap<String, String>>) document.get("blockedByList");
                        blockedByList = toPointerList(blockedByListMap);
                        DatabasePointer blockedBy = new DatabasePointer(user.getEmail());
                        // Updates only if blockedBy (user) is not already in the list
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
     * toPointerList
     *
     * Converts an ArrayList<HashMap<String, String>> listOfMap to an ArrayList<Database_Pointer>
     *
     * @author Henry
     * @param listOfMap - Type; The hashmap from the database that has to be converted to an arraylist of Database_pointer
     *
     * @return ArrayList<Database_Pointer>
     */
    public ArrayList<DatabasePointer> toPointerList(ArrayList<HashMap<String, String>> listOfMap) {
        ArrayList<DatabasePointer> pointerList = new ArrayList<>();
        for (int i = 0; i < listOfMap.size(); i++) {
            String email = listOfMap.get(i).get("email");
            DatabasePointer dp = new DatabasePointer(email);
            pointerList.add(dp);
        }
        return pointerList;
    }

    /**
     * habitToDatabase
     *
     * Convert Habit in Habit_List<Habit> to a new object that is compatitble with the database
     *
     * @author Henry
     * @param habits- Type Habit_List; the Habit_list that has to be converted to HabitDatabase
     *              so that its compatible with the database
     * @return ArrayList<HabitDatabase>; a habit list compatible with the database
     */
    public ArrayList<HabitDatabase> habitToDatabase(HabitList habits) {
        ArrayList<HabitDatabase> habitListToDatabase = new ArrayList<>();
        for (int i = 0; i < habits.size(); i++) {
            Habit primitiveHabit = habits.get(i);
            HabitDatabase habitToDatabase = new HabitDatabase();
            habitToDatabase.setIndex(primitiveHabit.getIndex());
            habitToDatabase.setTitle(primitiveHabit.getTitle());
            habitToDatabase.setReason(primitiveHabit.getReason());
            habitToDatabase.setStartDate(primitiveHabit.getStartDate());
            habitToDatabase.setHabitEvents(primitiveHabit.getHabitEvents());
            habitToDatabase.setOnDaysObj(primitiveHabit.getOnDaysObj());

            habitListToDatabase.add(habitToDatabase);
        }
        return habitListToDatabase;
    }

    /**
     * databaseToHabit
     * @author Henry
     * @param habitsFromDatabase - Type Arraylist<HabitDatabase>; the arraylist of HabitDatabase
     *                           that is to be converted to Habit_List
     *
     * @return Habit_List;
     * Returns a Habit_List given the habitList from Database.
     *
     */
    public HabitList databaseToHabit(ArrayList<HabitDatabase> habitsFromDatabase) {
        HabitList habitList = new HabitList();
        for (int i = 0; i < habitsFromDatabase.size(); i++) {
            HabitDatabase habitFromDatabase = habitsFromDatabase.get(i);
            Habit habit = new Habit();
            habit.setIndex(habitFromDatabase.getIndex());
            habit.setTitle(habitFromDatabase.getTitle());
            habit.setReason(habitFromDatabase.getReason());
            habit.setStartDate(habitFromDatabase.getStartDate());
            habit.setHabitEvents(habitFromDatabase.getHabitEvents());

            ArrayList<Boolean> dbOnDays = habitFromDatabase.getOnDaysObj();

            habit.setOnDaysObj(new OnDays(dbOnDays));
            habitList.add(habit);
        }
        return habitList;
    }
    public Uri uploadImageToFirebase(String name, Uri contentUri, StorageReference mStorageRef) {
        Uri returnedUri = null;
        StorageReference picImage = mStorageRef.child("images/" + name);
        UploadTask task = picImage.putFile(contentUri);
        while (!task.isComplete()) ;
        if (task.isSuccessful()) {
            Task<Uri> uriTask = picImage.getDownloadUrl();
            while (!uriTask.isComplete()) ;
            if (uriTask.isSuccessful()) {
                Uri uriTaskResult = uriTask.getResult();
                returnedUri =  uriTaskResult;
            } else {
                Log.d("Uploading image to firebase", "couldn't get download url");

            }
        } else {
            Log.d("Uploading image to firebase", "couldn't upload url");
        }
        return returnedUri;
    }
}
