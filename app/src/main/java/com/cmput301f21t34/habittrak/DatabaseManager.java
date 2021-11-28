package com.cmput301f21t34.habittrak;

import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.cmput301f21t34.habittrak.user.Habit;
import com.cmput301f21t34.habittrak.user.HabitEvent;
import com.cmput301f21t34.habittrak.user.HabitList;
import com.cmput301f21t34.habittrak.user.OnDays;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

/**
 * @author Tauseef Nafee Fattah
 * @author Henry
 * @version 1.0
 * @see User
 */
public class DatabaseManager {
    public static final boolean ADD = false;
    public static final boolean REMOVE = true;
    private final FirebaseFirestore database;

    public DatabaseManager() {
        database = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore getDatabase() {
        return database;
    }

    /**
     * getAllUsers()
     * Gets a list of the UUIDs of all users in the database
     *
     * @return ArrayList<String>, if successful, a list of UUIDs of all users, otherwise an empty
     * list
     * @author Henry
     * @author Kaaden
     */
    public ArrayList<String> getAllUsers() {
        ArrayList<String> users = new ArrayList<>();
        try {
            Task<QuerySnapshot> task = database.collection("users").get();
            while (!task.isComplete()) ; // wait
            // Add each the id of each document (UUID of the user) to users
            task.getResult().forEach(document -> users.add(document.getId()));
        } catch (Exception ignored) {
            Log.d("Getting all users error", "all users", ignored);
        }

        return users;
    }

    /**
     * isUniqueEmail
     * <p>
     * Checks to see if the user with the provided email already exists
     *
     * @param email - Type String; the email that is to be checked id its unique or not
     * @return boolean; returns true if unique otherwise will return false
     * @author Henry
     */
    public boolean isUniqueEmail(String email) {
        final CollectionReference collectionReference = database.collection("users");
        try {
            DocumentReference docref = collectionReference.document(email);
            Task<DocumentSnapshot> task = docref.get();
            while (!task.isComplete()) ;
            DocumentSnapshot document = task.getResult();
            return !document.exists(); // document for email doesn't exist means email unique
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * createNewUser
     * <p>
     * Creates a new user from the data passed into it, and puts it into the database
     *
     * @param email    -Type String the email of the user to be created
     * @param username -Type String the username of the user to be created
     * @return true if it is successful or false if it is not
     * @author Henry
     */
    public boolean createNewUser(String email, String username) {

        String TAG = "Unique";

        if (isUniqueEmail(email)) {
            Log.d(TAG, "Unique");
            final CollectionReference collectionReference =
                    database.collection("users");

            HashMap<String, Object> data = new HashMap<>();
            data.put("Username", username);
            data.put("Biography", "");
            data.put("habitList", new ArrayList<HabitDatabase>());
            data.put("followerList", new ArrayList<String>());
            data.put("followingList", new ArrayList<String>());
            data.put("followReqList", new ArrayList<String>());
            data.put("followRequestedList", new ArrayList<String>());
            data.put("blockList", new ArrayList<String>());
            data.put("blockedByList", new ArrayList<String>());

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
     * <p>
     * Attempts to delete a user from the database
     *
     * @param email -Type String; the email of the user to be deleted
     * @return true if it is successful or false if it is not
     * @author Henry
     */
    public boolean deleteUser(String email) {
        String TAG = "Delete";
        if (!isUniqueEmail(email)) {
            database.collection("users").document(email)
                    .delete()
                    .addOnSuccessListener(
                            aVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
            return true;
        }
        return false;
    }

    /**
     * getUser
     * <p>
     * returns the user with the provided email
     *
     * @param email- Type String; the email of the user to get
     * @return User
     * @author Tauseef
     * @author Henry
     */
    public User getUser(String email) {
        User user;

        String name = "";
        String bio = "";

        HabitList habitList = new HabitList();
        ArrayList<String> followerList = new ArrayList<>();
        ArrayList<String> followingList = new ArrayList<>();
        ArrayList<String> followReqList = new ArrayList<>();
        ArrayList<String> followRequestedList = new ArrayList<>();
        ArrayList<String> blockList = new ArrayList<>();
        ArrayList<String> blockedByList = new ArrayList<>();

        try {
            DocumentReference docref = database.collection("users").document(email);
            Task<DocumentSnapshot> task = docref.get();
            while (!task.isComplete()) ;
            DocumentSnapshot document = task.getResult();

            if (document.getData() != null) {
                Log.d("getData", "not null");
                ArrayList<HashMap<String, Object>> requestedHabitList =
                        (ArrayList<HashMap<String, Object>>) document.get("habitList");

                ArrayList<HabitDatabase> requestedHabitDatabases =
                        toHabitDatabaseList(requestedHabitList);
                habitList = databaseToHabit(requestedHabitDatabases);
                followerList = (ArrayList<String>) document.get("followerList");

                followingList = (ArrayList<String>) document.get("followingList");
                followReqList = (ArrayList<String>) document.get("followReqList");
                followRequestedList = (ArrayList<String>) document.get("followRequestedList");
                blockList = (ArrayList<String>) document.get("blockList");
                blockedByList = (ArrayList<String>) document.get("blockedByList");
                name = (String) document.get("Username");
                bio = (String) document.get("Biography");
            }

            user = new User(name, email, bio, habitList, followerList, followingList, followReqList,
                    followRequestedList, blockList, blockedByList);
            return user;
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * getUserName
     * <p>
     * gets the user name of the provided email
     *
     * @param email -Type String; The email email of the user who's username is to be retrieved
     * @return username (string)
     * @author Tauseef
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
        } catch (Exception ignored) {
            Log.d("Getting name error", "name", ignored);
        }

        return name;
    }

    /**
     * getUserBio
     * <p>
     * gets the user bio of the provided email
     *
     * @param email -Type String; The email email of the user who's bio is to be retrieved
     * @return String containing the bio
     * @author Tauseef
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
        } catch (Exception ignored) {
            Log.d("Getting bio error", "bio", ignored);
        }

        return bio;
    }

    /**
     * toHabitDatabaseList
     * <p>
     * Converts an array of HashMap (data from the database) to an array of HabitDatabase objects
     *
     * @param hashMapList -Type ArrayList<HashMap<String, Object>> the array of HashMap to be
     *                    converted
     * @return ArrayList <HabitDatabase>
     * @author Tauseef
     */
    public ArrayList<HabitDatabase> toHabitDatabaseList(
            ArrayList<HashMap<String, Object>> hashMapList) {
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
     * @param hashmap -Type HashMap<String, Object> the HashMap to be converted
     * @return HabitDatabase
     * @author Tauseef
     */
    public HabitDatabase toHabitDatabase(HashMap<String, Object> hashmap) {
        HabitDatabase habitDatabase = new HabitDatabase();
        habitDatabase.setIndex((int) (long) hashmap.get("index"));
        habitDatabase.setReason((String) hashmap.get("reason"));
        habitDatabase.setTitle((String) hashmap.get("title"));
        habitDatabase.setIsPublic((boolean) hashmap.get("isPublic"));
        habitDatabase.setHabitEvents(toHabitEventList(
                (ArrayList<HashMap<String, Object>>) hashmap.get("habitEvents")));
        habitDatabase.setOnDaysObjFromDB((ArrayList<Boolean>) hashmap.get("onDaysObj"));
        habitDatabase.setStartDate(toCalendar((HashMap<String, Object>) hashmap.get("startDate")));
        if (hashmap.get("currentStreakDate") != null) {
            habitDatabase.setCurrentStreakDate(toCalendar(
                    (HashMap<String, Object>) hashmap.get("currentStreakDate")));
        }
        else {
            habitDatabase.setCurrentStreakDate(null);
        }
        if (hashmap.get("bestStreakDate") != null) {
            habitDatabase.setBestStreakDate(toCalendar(
                    (HashMap<String, Object>) hashmap.get("bestStreakDate")));
        } else {
            habitDatabase.setBestStreakDate(null);
        }
        return habitDatabase;
    }

    /**
     * toHabitEventList
     * Converts an array of HashMap (data from the database) to an array of HabitEvent objects
     *
     * @param hashMapList -Type ArrayList<HashMap<String, Object>> the array of HashMap to be
     *                    converted
     * @return ArrayList<HabitEvent>
     * @author Tauseef
     */
    public ArrayList<HabitEvent> toHabitEventList(ArrayList<HashMap<String, Object>> hashMapList) {
        ArrayList<HabitEvent> habitEventList = new ArrayList<>();

        for (int i = 0; i < hashMapList.size(); i++) {
            HabitEvent habitEvent = toHabitEvent(hashMapList.get(i));
            habitEventList.add(habitEvent);
        }
        return habitEventList;
    }

    /**
     * toHabitEvent
     *
     * Converts HashMap from database to HabitEvent object
     * @param hashmap -Type HashMap<String, Object> the HashMap to be converted
     * @return HabitEvent
     * @author Tauseef
     */
    public HabitEvent toHabitEvent(HashMap<String, Object> hashmap) {
        HabitEvent event = new HabitEvent();
        if (hashmap.get("comment") != null) {
            event.setComment((String) hashmap.get("comment"));
        } else {
            event.setComment(null);
        }

        if (hashmap.get("location") != null) {
            event.setLocation(toLocation((HashMap<String, Object>) hashmap.get("location")));
        } else {
            event.setLocation(null);
        }

        if (hashmap.get("photograph") != null) {
            event.setPhotograph(Uri.parse((String) hashmap.get("photograph")));
        } else {
            event.setPhotograph(null);
        }

        event.setCompletedDate(toCalendar((HashMap<String, Object>) hashmap.get("completedDate")));
        return event;
    }

    /**
     * toLocation
     *
     * Converts HashMap from database to Location object
     * @param hashMap HashMap<String, Object> the HashMap to be converted
     * @return Location, the location from the HashMap
     */
    private Location toLocation(HashMap<String,Object> hashMap) {
        if (hashMap != null) {
            Location loc = new Location((String) hashMap.get("provider"));
            loc.setLatitude((double) (long) hashMap.get("latitude"));
            loc.setLongitude((double) (long) hashMap.get("longitude"));

            return loc;
        } else {
            return null;
        }
    }

    /**
     * toCalendar
     *
     * Converts HashMap from database to Calendar object
     * @param hashmap -Type HashMap<String, Object> the HashMap to be converted
     * @return GregorianCalendar
     * @author Tauseef
     */
    public GregorianCalendar toCalendar(HashMap<String, Object> hashmap) {
        GregorianCalendar returnCalendar = new GregorianCalendar();
        returnCalendar.setLenient((boolean) hashmap.get("lenient"));
        returnCalendar.setFirstDayOfWeek((int) (long) hashmap.get("firstDayOfWeek"));
        returnCalendar.setMinimalDaysInFirstWeek(
                (int) (long) hashmap.get("minimalDaysInFirstWeek"));
        returnCalendar.setTimeInMillis((long) hashmap.get("timeInMillis"));
        returnCalendar.setGregorianChange(((Timestamp) hashmap.get("gregorianChange")).toDate());
        returnCalendar.setTimeZone(getTimezone((HashMap<String, Object>) hashmap.get("timeZone")));
        returnCalendar.setTime(((Timestamp) hashmap.get("time")).toDate());
        return returnCalendar;
    }

    /**
     * getTimezone
     * <p>
     * Converts HashMap from database to TimeZone object
     *
     * @param hashMap -Type HashMap<String, Object> the HashMap to be converted
     * @return TimeZone
     * @author Tauseef
     */
    public TimeZone getTimezone(HashMap<String, Object> hashMap) {
        return TimeZone.getTimeZone((String) hashMap.get("id"));
    }

    /**
     * Updates the username of the user with the given UUID
     *
     * @param UUID String, the UUID of the user to update
     * @param newUserName String, the name to update to
     * @author Kaaden
     */
    public void updateUsername(String UUID, String newUserName) {
        //if (!isUniqueUsername(newUserName)) return; // Don't update if would cause duplicates
        HashMap<String, Object> data = new HashMap<>();
        data.put("Username", newUserName);
        List<String> fieldsToUpdate = new ArrayList<>();
        fieldsToUpdate.add("Username");
        database.collection("users").document(UUID).set(data,
                SetOptions.mergeFields(fieldsToUpdate));
    }

    /**
     * Updates the biography of the user with the given UUID
     *
     * @param UUID String, the UUID of the user to update
     * @param newBio String, the biography to update to
     * @author Kaaden
     */
    public void updateBio(String UUID, String newBio) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("Biography", newBio);
        List<String> fieldsToUpdate = new ArrayList<>();
        fieldsToUpdate.add("Biography");
        database.collection("users").document(UUID).set(
                data, SetOptions.mergeFields(fieldsToUpdate));
    }

    /**
     * Update the habit list of the given user
     * We can add/remove/edit attributes of habitToUpdate
     * @param UUID String, the uuid of the user who's habit list is to be updated
     * @param habitList HabitList, the habit list that is to be updated
     */
    public void updateHabitList(String UUID, HabitList habitList) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("habitList", habitToDatabase(habitList));
            List<String> fieldsToUpdate = new ArrayList<>();
            fieldsToUpdate.add("habitList");
            database.collection("users").document(UUID).set(
                    data, SetOptions.mergeFields(fieldsToUpdate));
    }

    /**
     * updateUUIDList
     * <p>
     * Add or remove member from a user's list.
     *
     * @param listName   String, name of the user's list in the database
     * @param listHaver  String, UUID of the user whose list to update
     * @param listMember String, UUID to add or remove from the list
     * @param remove     boolean, false for add, true for remove
     * @author Kaaden
     * @author Henry
     * @author Tauseef
     */
    private void updateUUIDList(String listName, String listHaver, String listMember,
                                boolean remove) {
        // Get the user, owner of the list to update, from the database
        DocumentReference userRef = database.collection("users").document(listHaver);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Get the list to update from the user
                    ArrayList<String> list = (ArrayList<String>) document.get(listName);
                    if (list != null) {
                        // Check if the relevant member is already stored
                        boolean contains = list.contains(listMember);

                        if (remove) {
                            list.remove(listMember);
                        } else if (!contains) {  // Only add if not already a member
                            list.add(listMember);
                        }

                        // Only send to database if changes made
                        if (remove && contains || !remove && !contains) {
                            List<String> fieldsToUpdate = new ArrayList<>();
                            fieldsToUpdate.add(listName);
                            HashMap<String, Object> data = new HashMap<>();
                            data.put(listName, list);
                            userRef.set(data, SetOptions.mergeFields(fieldsToUpdate));
                        }
                    }
                }
            } else {
                Log.d("Task: " + task, " failed with: ", task.getException());
            }
        });
    }

    /**
     * updateFollow
     * <p>
     * Add or remove a following relationship in the database
     *
     * @param followee String, UUID of user to be followed/unfollowed
     * @param follower String, UUID of the user that is following/unfollowing
     * @param remove   boolean, false for add, true for remove
     * @author Henry
     * @author Kaaden
     */
    public void updateFollow(String followee, String follower, boolean remove) {
        // Update followee's followerList
        updateUUIDList("followerList", followee, follower, remove);
        // Update follower's followingList
        updateUUIDList("followingList", follower, followee, remove);
    }

    /**
     * updateFollowRequest
     * <p>
     * Add or remove a follow-request relationship in the database
     *
     * @param followRequestee String, UUID of the user to be follow-requested/unfollow-requested
     * @param followRequester String, UUID of the user that is follow-requesting/unfollow-requesting
     * @param remove          boolean, false for add, true for remove
     * @author Tauseef
     */
    public void updateFollowRequest(String followRequestee, String followRequester,
                                    boolean remove) {
        // Update followRequestee's followReqList
        updateUUIDList("followRequestedList", followRequestee, followRequester, remove);
        // Update followRequester's followerRequestedList
        updateUUIDList("followReqList", followRequester, followRequestee, remove);
    }

    /**
     * updateBlock
     * <p>
     * Add or remove a block relationship in the database
     *
     * @param blockee String, UUID of the user to be blocked/unblocked
     * @param blocker String, UUID of the user that is blocking/unblocking
     * @param remove  boolean, false for add, true for remove
     * @author Tauseef
     */
    public void updateBlock(String blockee, String blocker, boolean remove) {
        // Update blocker's blockList
        updateUUIDList("blockList", blocker, blockee, remove);
        // Update blockee's blockedByList
        updateUUIDList("blockedByList", blockee, blocker, remove);
    }

    /**
     * habitToDatabase
     * <p>
     * Convert HabitList<Habit> to an ArrayList<HabitDatabase> that is more compatible with the
     * database
     *
     * @param habits- Type HabitList; the Habitlist that has to be converted to HabitDatabase
     *                so that its compatible with the database
     * @return ArrayList<HabitDatabase>; a habit list compatible with the database
     * @author Henry
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
            habitToDatabase.setCurrentStreakDate(primitiveHabit.getCurrentStreakDate());
            habitToDatabase.setBestStreakDate(primitiveHabit.getBestStreakDate());
            habitToDatabase.setIsPublic(primitiveHabit.isPublic());
            habitListToDatabase.add(habitToDatabase);
        }
        return habitListToDatabase;
    }

    /**
     * databaseToHabit
     *
     * @param habitsFromDatabase - Type Arraylist<HabitDatabase>; the arraylist of HabitDatabase
     *                           that is to be converted to HabitList
     * @return HabitList;
     * Returns a HabitList given the habit list (ArrayList<HabitDatabase>) from the database.
     * @author Henry
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
            habit.setCurrentStreakDate(habitFromDatabase.getCurrentStreakDate());
            habit.setBestStreakDate(habitFromDatabase.getBestStreakDate());
            if(habitFromDatabase.getIsPublic()){
                habit.makePublic();
            } else {
                habit.makePrivate();
            }
            ArrayList<Boolean> dbOnDays = habitFromDatabase.getOnDaysObj();
            habit.setOnDaysObj(new OnDays(dbOnDays));
            // After converting from HabitDatabase to Habit, add to list
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
                returnedUri = uriTask.getResult();
            } else {
                Log.d("Uploading image to firebase", "couldn't get download url");

            }
        } else {
            Log.d("Uploading image to firebase", "couldn't upload url");
        }
        return returnedUri;
    }
}
